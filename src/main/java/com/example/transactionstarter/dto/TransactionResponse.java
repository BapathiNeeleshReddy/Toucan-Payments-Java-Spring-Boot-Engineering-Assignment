package com.example.transactionstarter.dto;

import com.example.transactionstarter.entity.Currency;
import com.example.transactionstarter.entity.Transaction;
import com.example.transactionstarter.entity.TransactionStatus;
import com.example.transactionstarter.entity.TransactionType;

import java.math.BigDecimal;

public record TransactionResponse(
        String transactionId,
        String customerId,
        BigDecimal amount,
        Currency currency,
        TransactionType transactionType,
        TransactionStatus transactionStatus
) {
    public static TransactionResponse from(Transaction transaction) {
        return new TransactionResponse(
                transaction.getTransactionId(),
                transaction.getCustomerId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getTransactionType(),
                transaction.getTransactionStatus()
        );
    }
}
