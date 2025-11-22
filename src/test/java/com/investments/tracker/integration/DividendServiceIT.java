package com.investments.tracker.integration;

import com.investments.tracker.model.Dividend;
import com.investments.tracker.repository.DividendRepository;
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
class DividendServiceIT {
    private static final String DEPOSIT_REQUEST_JSON = "src/test/resources/json/deposit-request.json";
    private static final String TRANSACTION_REQUEST_JSON = "src/test/resources/json/transaction-request.json";
    private static final String DIVIDEND_REQUEST_JSON = "src/test/resources/json/dividend-request.json";
    private static final String BALANCE_AFTER_DIVIDEND_RESPONSE_JSON = "src/test/resources/json/balance-after-dividend-response.json";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DividendRepository dividendRepository;

    @Value("${application.test.deposit-insert-url}")
    private String depositInsertUrl;

    @Value("${application.test.transaction-insert-url}")
    private String transactionInsertUrl;

    @Value("${application.test.dividend-insert-url}")
    private String dividendInsertUrl;

    @Test
    @DisplayName("Test insertion of successful dividend")
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
        mockMvc
                .perform(post(transactionInsertUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestTransactionBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String requestDividendBody = Files.readString(Paths.get(DIVIDEND_REQUEST_JSON));
        String actualResponseAfterDividend = mockMvc
                .perform(post(dividendInsertUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestDividendBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponseAfterDividend = Files.readString(Paths.get(BALANCE_AFTER_DIVIDEND_RESPONSE_JSON));
        JSONAssert.assertEquals(expectedResponseAfterDividend, actualResponseAfterDividend, false);

        Dividend dividend = dividendRepository.findAll().getFirst();
        assertEquals(BigDecimal.valueOf(1.50), dividend.getTotalAmountAfterTaxAndConversion());
        assertEquals(BigDecimal.valueOf(1.66), dividend.getTotalAmountAfterTaxBeforeConversion());
        assertEquals(BigDecimal.valueOf(2.71), dividend.getTotalBaseAmount());
        assertEquals(BigDecimal.valueOf(0.71), dividend.getTotalBaseTaxAmount());
        assertEquals(BigDecimal.valueOf(0.2633), dividend.getAmountPerShare());
        assertEquals(BigDecimal.valueOf(0.0789), dividend.getTaxAmountPerShare());
    }
}
