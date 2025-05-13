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
    private Long id;

    @Column(nullable = false)
    private BankCode bankCode;

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String accountHolder;

    @Builder
    public TransferAccountEntity(final BankCode bankCode, final String accountHolder, final String accountNumber) {
        this.accountHolder = accountHolder;
        this.bankCode = bankCode;
        this.accountNumber = accountNumber;
    }

    public static TransferAccountEntity create(final BankCode bankCode, final String accountHolder, final String accountNumber) {
        return TransferAccountEntity.builder()
                .bankCode(bankCode)
                .accountHolder(accountHolder)
                .accountNumber(accountNumber)
                .build();
    }

}
