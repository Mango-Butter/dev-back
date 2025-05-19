package com.mangoboss.batch.external.nhdevelopers.dto.reqeust;

import lombok.Getter;

@Getter
public enum ApiName {
    DRAWING_TRANSFER("DrawingTransfer"),
    RECEIVED_TRANSFER_ACCOUNT_NUMBER("ReceivedTransferAccountNumber"),
    INQUIRE_TRANSACTION_HISTORY("InquireTransactionHistory");

    private final String name;

    ApiName(final String name) {
        this.name = name;
    }
}
