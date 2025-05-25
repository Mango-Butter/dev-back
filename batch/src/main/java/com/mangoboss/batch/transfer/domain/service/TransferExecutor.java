package com.mangoboss.batch.transfer.domain.service;

import com.mangoboss.batch.common.exception.CustomErrorInfo;
import com.mangoboss.batch.transfer.external.nhdevelopers.NhDevelopersClient;
import com.mangoboss.batch.transfer.external.nhdevelopers.dto.response.DrawingTransferResponse;
import com.mangoboss.batch.transfer.external.nhdevelopers.dto.response.InquireTransactionHistoryResponse;
import com.mangoboss.batch.transfer.external.nhdevelopers.dto.response.ReceivedTransferResponse;
import com.mangoboss.storage.payroll.BankCode;
import com.mangoboss.storage.payroll.PayrollAmount;
import com.mangoboss.storage.payroll.PayrollEntity;
import com.mangoboss.storage.payroll.TransferState;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransferExecutor {
    private static final String WITHDRAWN_PREFIX = "MangoBoss";
    private static final String TRANSFERRED_PREFIX = "transferred_";
    private static final String TMS_DSNC = "A";
    private static final String LNSQ = "DESC";
    private static final String DMCNT = "20";

    @PersistenceContext
    private final EntityManager entityManager;

    private final NhDevelopersClient nhDevelopersClient;
    private final Clock clock;

    @Value("${external.nh.mangoboss-account}")
    private String mangobossAccount;

    @Value("${external.nh.mangoboss-bankcode}")
    private BankCode mangobossBankcode;

    @Transactional
    @Retryable(
            retryFor = {WebClientRequestException.class, TimeoutException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 3000)
    )
    public void drawingTransferWithRetry(final PayrollEntity payroll) {
        if (!isBeforeWithdrawing(payroll.getTransferState())) {
            return;
        }
        try {
            markWithdrawn(payroll);
            PayrollAmount amount = payroll.getPayrollAmount();
            DrawingTransferResponse response = nhDevelopersClient.drawingTransfer(
                    payroll.getFinAccount(),
                    amount.getNetAmount().toString(),
                    WITHDRAWN_PREFIX + payroll.getId()
            );
            markCompleted(payroll);
        } catch (Exception e) {
            if (isTransactionRecorded(
                    WITHDRAWN_PREFIX + payroll.getId(),
                    payroll.getWithdrawalBankcode(),
                    payroll.getWithdrawalAccount()
            )) {
                markCompleted(payroll);
                return;
            }
            markFailed(payroll);
        }
    }

    @Transactional
    @Retryable(
            retryFor = {WebClientRequestException.class, TimeoutException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 3000)
    )
    public void receivedTransferWithRetry(final PayrollEntity payroll) {
        if (!isBeforeReceivedTransfer(payroll.getTransferState())) {
            return;
        }
        try {
            markTransferred(payroll);
            PayrollAmount amount = payroll.getPayrollAmount();
            ReceivedTransferResponse response = nhDevelopersClient.receivedTransfer(
                    payroll.getDepositBankCode().getCode(),
                    payroll.getDepositAccount(),
                    amount.getNetAmount().toString(),
                    TRANSFERRED_PREFIX + payroll.getId(),
                    payroll.getStoreName()
            );
            markCompleted(payroll);

        } catch (Exception e) {
            if (isTransactionRecorded(
                    TRANSFERRED_PREFIX + payroll.getId(),
                    mangobossBankcode,
                    mangobossAccount
            )) {
                markCompleted(payroll);
                return;
            }
            markFailed(payroll);
        }
    }

    @Recover
    public void recover(final Exception e, final PayrollEntity payroll) {
        log.warn("[{}] payrollId = {}", CustomErrorInfo.FAILURE_RETRY.getMessage(), payroll.getId(), e);
        markFailed(payroll);
    }

    public Boolean isTransactionRecorded(final String bnprCntn, final BankCode bankCode, final String account) {
        String insymd = LocalDate.now(clock).minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String ineymd = LocalDate.now(clock).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        InquireTransactionHistoryResponse response = nhDevelopersClient.inquireTransaction(
                bankCode.getCode(), account, insymd, ineymd, TMS_DSNC, LNSQ, DMCNT
        );
        return response.REC().stream()
                .anyMatch(item -> item.BnprCntn().equals(bnprCntn));
    }

    private Boolean isBeforeWithdrawing(final TransferState state) {
        return state.equals(TransferState.PENDING)
                || state.equals(TransferState.FAILED_WITHDRAW);
    }

    private Boolean isBeforeReceivedTransfer(final TransferState state) {
        return state.equals(TransferState.COMPLETED_WITHDRAWN)
                || state.equals(TransferState.FAILED_TRANSFERRED);
    }

    private void markWithdrawn(final PayrollEntity payroll) {
        payroll.markWithdrawn();
        entityManager.flush();
    }

    private void markTransferred(final PayrollEntity payroll) {
        payroll.markTransferred();
        entityManager.flush();
    }

    private void markCompleted(final PayrollEntity payroll) {
        payroll.markCompleted(LocalDateTime.now(clock));
        entityManager.flush();
    }

    private void markFailed(final PayrollEntity payroll) {
        payroll.markFailed();
        entityManager.flush();
    }
}
