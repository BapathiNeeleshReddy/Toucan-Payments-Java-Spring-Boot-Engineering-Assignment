package com.example.transactionstarter;

import com.example.transactionstarter.entity.Currency;
import com.example.transactionstarter.entity.TransactionType;
import com.example.transactionstarter.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void cleanDatabase() {
        transactionRepository.deleteAll();
    }

    @Test
    void shouldCreateTransactionSuccessfully() throws Exception {
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionJson("TX100", "CUST1", "1250.00")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId", is("TX100")))
                .andExpect(jsonPath("$.customerId", is("CUST1")))
                .andExpect(jsonPath("$.transactionStatus", is("PENDING")));
    }

    @Test
    void shouldRejectInvalidTransaction() throws Exception {
        String invalidJson = """
                {
                  "transactionId": "TX101",
                  "customerId": "CUST1",
                  "amount": 0,
                  "currency": "INR",
                  "transactionType": "PAYMENT",
                  "transactionStatus": "PENDING"
                }
                """;

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectDuplicateTransactionId() throws Exception {
        String json = transactionJson("TX102", "CUST1", "100.00");

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("Transaction ID already exists: TX102")));
    }

    @Test
    void shouldReturnNotFoundForMissingTransaction() throws Exception {
        mockMvc.perform(get("/api/transactions/DOES-NOT-EXIST"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Transaction not found: DOES-NOT-EXIST")));
    }

    @Test
    void shouldUpdatePendingTransactionToCompleted() throws Exception {
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionJson("TX103", "CUST2", "250.00")))
                .andExpect(status().isCreated());

        mockMvc.perform(patch("/api/transactions/TX103/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"transactionStatus\":\"COMPLETED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionStatus", is("COMPLETED")));
    }

    @Test
    void shouldReturnAllTransactionsForCustomer() throws Exception {
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionJson("TX104", "CUST3", "100.00")))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionJson("TX105", "CUST3", "200.00")))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/customers/CUST3/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].customerId", is("CUST3")))
                .andExpect(jsonPath("$[1].customerId", is("CUST3")));
    }

    private String transactionJson(String transactionId, String customerId, String amount) {
        return """
                {
                  "transactionId": "%s",
                  "customerId": "%s",
                  "amount": %s,
                  "currency": "%s",
                  "transactionType": "%s",
                  "transactionStatus": "PENDING"
                }
                """.formatted(transactionId, customerId, amount, Currency.INR, TransactionType.PAYMENT);
    }
}
