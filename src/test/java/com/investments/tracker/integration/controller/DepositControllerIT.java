package com.investments.tracker.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class DepositControllerIT {
    @Value("${deposit.insert.url}")
    private String depositInsertUrl;

    private static final String DEPOSIT_REQUEST_JSON = "src/test/resources/json/deposit-request.json";
    private static final String BALANCE_RESPONSE_JSON = "src/test/resources/json/balance-response.json";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test the insertion from the DepositController")
    void testCallingInsertDepositMethod() throws Exception {
        String requestBody = Files.readString(Paths.get(DEPOSIT_REQUEST_JSON));
        String expectedResponse = Files.readString(Paths.get(BALANCE_RESPONSE_JSON));

        String actualResponse = mockMvc
                .perform(post(depositInsertUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONAssert.assertEquals(expectedResponse, actualResponse, false);
    }
}
