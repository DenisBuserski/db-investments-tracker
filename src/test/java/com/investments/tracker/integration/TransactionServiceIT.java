package com.investments.tracker.integration;

import com.investments.tracker.model.Portfolio;
import com.investments.tracker.repository.PortfolioRepository;
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

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class TransactionServiceIT {
    private static final String DEPOSIT_REQUEST_JSON = "src/test/resources/json/deposit-request.json";
    private static final String TRANSACTION_REQUEST_JSON = "src/test/resources/json/transaction-request.json";
    private static final String TRANSACTION_TWO_REQUEST_JSON = "src/test/resources/json/transaction-2-request.json";
    private static final String BALANCE_AFTER_TRANSACTION_JSON = "src/test/resources/json/balance-after-transaction.json";
    private static final String BALANCE_AFTER_SECOND_TRANSACTION_JSON = "src/test/resources/json/balance-after-second-transaction.json";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Value("${application.test.deposit-insert-url}")
    private String depositInsertUrl;

    @Value("${application.test.transaction-insert-url}")
    private String transactionInsertUrl;

    @Test
    @DisplayName("Test insertion of successful transaction")
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
        String actualResponseAfterTransaction = mockMvc
                .perform(post(transactionInsertUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestTransactionBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponseAfterTransaction = Files.readString(Paths.get(BALANCE_AFTER_TRANSACTION_JSON));
        JSONAssert.assertEquals(expectedResponseAfterTransaction, actualResponseAfterTransaction, false);
    }

    @Test
    @DisplayName("Test insertion of 2 successful transactions")
    void testInsertionOfTwoSuccessfulTransactions() throws Exception {
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
        mockMvc
                .perform(post(transactionInsertUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestTransactionBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String requestTransaction2Body = Files.readString(Paths.get(TRANSACTION_TWO_REQUEST_JSON));
        String actualResponseAfterSecondTransaction = mockMvc
                .perform(post(transactionInsertUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestTransaction2Body))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponseAfterSecondTransaction = Files.readString(Paths.get(BALANCE_AFTER_SECOND_TRANSACTION_JSON));
        JSONAssert.assertEquals(expectedResponseAfterSecondTransaction, actualResponseAfterSecondTransaction, false);

        Portfolio portfolio = portfolioRepository.findAll().getFirst();
        assertEquals(15, portfolio.getQuantity());
        assertEquals(BigDecimal.valueOf(180.68), portfolio.getInvestedMoney());
        assertEquals(BigDecimal.valueOf(12.0453), portfolio.getAveragePrice());
    }
}
