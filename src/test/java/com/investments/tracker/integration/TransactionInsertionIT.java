package com.investments.tracker.integration;

import com.investments.tracker.model.Portfolio;
import com.investments.tracker.model.Transaction;
import com.investments.tracker.repository.PortfolioRepository;
import com.investments.tracker.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class TransactionInsertionIT {
    private static final String DEPOSIT_REQUEST_JSON = "src/test/resources/json/deposit-request.json";
    private static final String TRANSACTION_REQUEST_JSON = "src/test/resources/json/transaction-request.json";
    private static final String BALANCE_AFTER_TRANSACTION_JSON = "src/test/resources/json/balance-after-transaction.json";

    @Autowired
    private MockMvc mockMvc;

    @Value("${deposit.insert.url}")
    private String depositInsertUrl;

    @Value("${transaction.insert.url}")
    private String transactionInsertUrl;

    @Test
    @DisplayName("Tsst insertion of successful transaction")
    void testInsertSuccessfulTransaction() throws Exception {
        String requestDepositBody = Files.readString(Paths.get(DEPOSIT_REQUEST_JSON));
        mockMvc
                .perform(post(depositInsertUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestDepositBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String requestTransactionBody = Files.readString(Paths.get(TRANSACTION_REQUEST_JSON));
        String expectedResponseAfterTransaction = Files.readString(Paths.get(BALANCE_AFTER_TRANSACTION_JSON));
        String actualResponseAfterTransaction = mockMvc
                .perform(post(transactionInsertUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestTransactionBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONAssert.assertEquals(expectedResponseAfterTransaction, actualResponseAfterTransaction, false);
    }
}
