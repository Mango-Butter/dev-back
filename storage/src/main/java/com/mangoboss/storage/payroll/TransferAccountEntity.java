package com.mangoboss.storage.payroll;

import com.mangoboss.storage.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "transfer_account")
public class TransferAccountEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transfer_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BankCode bankCode;

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String accountHolder;

    @Column(nullable = false)
    private String finAccount;

    @Builder
    private TransferAccountEntity(final BankCode bankCode, final String accountHolder,
                                  final String accountNumber, final String finAccount) {
        this.accountHolder = accountHolder;
        this.bankCode = bankCode;
        this.accountNumber = accountNumber;
        this.finAccount = finAccount;
    }

    public static TransferAccountEntity create(final BankCode bankCode, final String accountHolder,
                                               final String accountNumber, final String finAccount) {
        return TransferAccountEntity.builder()
                .bankCode(bankCode)
                .accountHolder(accountHolder)
                .accountNumber(accountNumber)
                .finAccount(finAccount)
                .build();
    }

}
