package com.example.transactionstarter.controller;

import com.example.transactionstarter.dto.CreateTransactionRequest;
import com.example.transactionstarter.dto.TransactionResponse;
import com.example.transactionstarter.dto.UpdateStatusRequest;
import com.example.transactionstarter.service.TransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transactions")
    public ResponseEntity<TransactionResponse> create(@Valid @RequestBody CreateTransactionRequest request) {
        TransactionResponse response = transactionService.create(request);
        return ResponseEntity.created(URI.create("/api/transactions/" + response.transactionId())).body(response);
    }

    @GetMapping("/transactions/{transactionId}")
    public TransactionResponse get(
            @PathVariable @NotBlank @Size(max = 50) String transactionId) {
        return transactionService.getById(transactionId);
    }

    @PatchMapping("/transactions/{transactionId}/status")
    public TransactionResponse updateStatus(
            @PathVariable @NotBlank @Size(max = 50) String transactionId,
            @Valid @RequestBody UpdateStatusRequest request) {
        return transactionService.updateStatus(transactionId, request.transactionStatus());
    }

    @GetMapping("/customers/{customerId}/transactions")
    public List<TransactionResponse> getCustomerTransactions(
            @PathVariable @NotBlank @Size(max = 50) String customerId) {
        return transactionService.getByCustomerId(customerId);
    }
}
