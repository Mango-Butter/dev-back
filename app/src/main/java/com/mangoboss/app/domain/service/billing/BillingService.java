package com.mangoboss.app.domain.service.billing;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.common.util.JsonConverter;
import com.mangoboss.app.dto.subscription.response.BillingCustomerKeyResponse;
import com.mangoboss.app.external.tosspayment.TossPaymentClient;
import com.mangoboss.storage.billing.BillingEntity;
import com.mangoboss.app.domain.repository.BillingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BillingService {

    private final TossPaymentClient tossPaymentClient;
    private final BillingRepository billingRepository;

    @Transactional
    public BillingCustomerKeyResponse getOrCreateCustomerKey(Long userId) {
        Optional<BillingEntity> existing = billingRepository.findByBossId(userId);
        if (existing.isPresent()) {
            return BillingCustomerKeyResponse.of(existing.get().getCustomerKey());
        }

        String customerKey = generateCustomerKey(userId);

        BillingEntity billing = BillingEntity.createPending(userId, customerKey);
        billingRepository.save(billing);

        return BillingCustomerKeyResponse.of(customerKey);
    }

    @Transactional
    public void issueBillingKey(Long bossId, String customerKey, String authKey) {
        Map<String, Object> billingKeyResponse = tossPaymentClient.issueBillingKey(customerKey, authKey);
        String billingKey = (String) billingKeyResponse.get("billingKey");
        Map<String, Object> card = (Map<String, Object>) billingKeyResponse.get("card");
        String cardDataJson = JsonConverter.toJson(card);

        BillingEntity billing = billingRepository.findByBossId(bossId)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.BILLING_NOT_FOUND));

        billing.registerBillingKey(billingKey, cardDataJson);

        log.info("BillingKey 발급 및 저장 완료: billingKey={}", billingKey);
    }

    @Transactional
    public void deleteBillingKey(Long bossId) {
        BillingEntity billing = billingRepository.findByBossId(bossId)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.BILLING_NOT_FOUND));

        billing.deleteBillingKey();
    }

    private String generateCustomerKey(Long bossId) {
        return "boss-" + bossId + "-" + UUID.randomUUID();
    }
}
