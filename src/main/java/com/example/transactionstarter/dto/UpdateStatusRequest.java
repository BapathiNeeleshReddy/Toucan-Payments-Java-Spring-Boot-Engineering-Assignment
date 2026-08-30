package com.example.transactionstarter.dto;

import com.example.transactionstarter.entity.TransactionStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(
        @NotNull(message = "Transaction status is required")
        TransactionStatus transactionStatus
) {
}
