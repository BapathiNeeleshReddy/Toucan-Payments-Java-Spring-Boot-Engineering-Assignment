package com.example.transactionstarter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @Column(name = "transaction_id", nullable = false, updatable = false, length = 50)
    private String transactionId;

    @Column(name = "customer_id", nullable = false, length = 50)
    private String customerId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 20)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status", nullable = false, length = 20)
    private TransactionStatus transactionStatus;

    protected Transaction() {
    }

    public Transaction(String transactionId, String customerId, BigDecimal amount,
                       Currency currency, TransactionType transactionType,
                       TransactionStatus transactionStatus) {
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.amount = amount;
        this.currency = currency;
        this.transactionType = transactionType;
        this.transactionStatus = transactionStatus;
    }

    public String getTransactionId() { return transactionId; }
    public String getCustomerId() { return customerId; }
    public BigDecimal getAmount() { return amount; }
    public Currency getCurrency() { return currency; }
    public TransactionType getTransactionType() { return transactionType; }
    public TransactionStatus getTransactionStatus() { return transactionStatus; }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
}
