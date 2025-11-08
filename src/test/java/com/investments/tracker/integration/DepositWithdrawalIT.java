package com.investments.tracker.integration;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class DepositWithdrawalIT {
    private static final String DEPOSIT_REQUEST_JSON = "src/test/resources/json/deposit-request.json";
    private static final String BALANCE_AFTER_DEPOSIT_ESPONSE_JSON = "src/test/resources/json/balance-after-deposit-response.json";
    private static final String WITHDRAWAL_REQUEST_JSON = "src/test/resources/json/withdrawal-request.json";
    private static final String BALANCE_AFTER_WITHDRAWAL_RESPONSE_JSON = "src/test/resources/json/balance-after-withdrawal-response.json";

    @Autowired
    private MockMvc mockMvc;

    @Value("${deposit.insert.url}")
    private String depositInsertUrl;

    @Value("${withdrawal.out.url}")
    private String withdrawalOutUrl;

    @Test
    @DisplayName("Test successful insertion of a deposit and withdrawal")
    void testSuccessfulInsertionOfADepositAndWithdrawal() throws Exception {
        String requestDepositBody = Files.readString(Paths.get(DEPOSIT_REQUEST_JSON));
        String expectedResponseAfterDeposit = Files.readString(Paths.get(BALANCE_AFTER_DEPOSIT_ESPONSE_JSON));

        String actualResponseAfterDeposit = mockMvc
                .perform(post(depositInsertUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestDepositBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONAssert.assertEquals(expectedResponseAfterDeposit, actualResponseAfterDeposit, false);


        String requestWithdrawalBody = Files.readString(Paths.get(WITHDRAWAL_REQUEST_JSON));
        String expectedResponseAfterWithdrawal = Files.readString(Paths.get(BALANCE_AFTER_WITHDRAWAL_RESPONSE_JSON));

        String actualResponseAfterWithdrawal =  mockMvc
                .perform(post(withdrawalOutUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestWithdrawalBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONAssert.assertEquals(expectedResponseAfterWithdrawal, actualResponseAfterWithdrawal, false);

    }
}
