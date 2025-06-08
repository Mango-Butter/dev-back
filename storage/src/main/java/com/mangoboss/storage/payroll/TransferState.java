package com.mangoboss.storage.payroll;

public enum TransferState {
    PENDING,
    REQUESTED_WITHDRAWN,
    REQUESTED_TRANSFERRED,
    COMPLETED_WITHDRAWN,
    COMPLETED_TRANSFERRED,
    FAILED_WITHDRAW,
    FAILED_TRANSFERRED,
    UNKNOWN;

    public TransferState markFailed() {
        if (this.equals(REQUESTED_WITHDRAWN)) {
            return FAILED_WITHDRAW;
        }
        if (this.equals(REQUESTED_TRANSFERRED)) {
            return FAILED_TRANSFERRED;
        }
        return UNKNOWN;
    }

    public TransferState markCompleted() {
        if (this.equals(REQUESTED_WITHDRAWN) || this.equals(FAILED_WITHDRAW)) {
            return COMPLETED_WITHDRAWN;
        }
        if (this.equals(REQUESTED_TRANSFERRED) || this.equals(FAILED_TRANSFERRED)) {
            return COMPLETED_TRANSFERRED;
        }
        return UNKNOWN;
    }
}
