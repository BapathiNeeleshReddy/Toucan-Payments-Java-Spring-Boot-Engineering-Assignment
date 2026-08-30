package com.example.transactionstarter.dto;

import com.example.transactionstarter.entity.Currency;
import com.example.transactionstarter.entity.TransactionStatus;
import com.example.transactionstarter.entity.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateTransactionRequest(
        @NotBlank(message = "Transaction ID is required")
        @Size(max = 50, message = "Transaction ID must be at most 50 characters")
        String transactionId,

        @NotBlank(message = "Customer ID is required")
        @Size(max = 50, message = "Customer ID must be at most 50 characters")
        String customerId,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
        BigDecimal amount,

        @NotNull(message = "Currency is required")
        Currency currency,

        @NotNull(message = "Transaction type is required")
        TransactionType transactionType,

        @NotNull(message = "Transaction status is required")
        TransactionStatus transactionStatus
) {
}
