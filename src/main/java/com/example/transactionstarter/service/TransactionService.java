package com.example.transactionstarter.service;

import com.example.transactionstarter.dto.CreateTransactionRequest;
import com.example.transactionstarter.dto.TransactionResponse;
import com.example.transactionstarter.entity.Transaction;
import com.example.transactionstarter.entity.TransactionStatus;
import com.example.transactionstarter.exception.DuplicateTransactionException;
import com.example.transactionstarter.exception.InvalidStatusTransitionException;
import com.example.transactionstarter.exception.TransactionNotFoundException;
import com.example.transactionstarter.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public TransactionResponse create(CreateTransactionRequest request) {
        String transactionId = request.transactionId().trim();
        String customerId = request.customerId().trim();

        if (transactionRepository.existsById(transactionId)) {
            throw new DuplicateTransactionException(transactionId);
        }

        if (request.transactionStatus() != TransactionStatus.PENDING) {
            throw new IllegalArgumentException("New transactions must start with PENDING status");
        }

        Transaction transaction = new Transaction(
                transactionId,
                customerId,
                request.amount(),
                request.currency(),
                request.transactionType(),
                request.transactionStatus()
        );

        return TransactionResponse.from(transactionRepository.save(transaction));
    }

    @Transactional(readOnly = true)
    public TransactionResponse getById(String transactionId) {
        return transactionRepository.findById(transactionId)
                .map(TransactionResponse::from)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));
    }

    @Transactional
    public TransactionResponse updateStatus(String transactionId, TransactionStatus requestedStatus) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));

        TransactionStatus currentStatus = transaction.getTransactionStatus();
        if (currentStatus != TransactionStatus.PENDING ||
                (requestedStatus != TransactionStatus.COMPLETED && requestedStatus != TransactionStatus.FAILED)) {
            throw new InvalidStatusTransitionException(currentStatus, requestedStatus);
        }

        transaction.setTransactionStatus(requestedStatus);
        return TransactionResponse.from(transactionRepository.save(transaction));
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getByCustomerId(String customerId) {
        return transactionRepository.findByCustomerIdOrderByTransactionIdAsc(customerId.trim())
                .stream()
                .map(TransactionResponse::from)
                .toList();
    }
}
