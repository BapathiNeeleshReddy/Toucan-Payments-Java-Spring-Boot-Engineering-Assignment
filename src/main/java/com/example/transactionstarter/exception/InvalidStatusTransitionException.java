package com.example.transactionstarter.exception;

import com.example.transactionstarter.entity.TransactionStatus;

public class InvalidStatusTransitionException extends RuntimeException {
    public InvalidStatusTransitionException(TransactionStatus current, TransactionStatus requested) {
        super("Invalid status transition from " + current + " to " + requested);
    }
}
