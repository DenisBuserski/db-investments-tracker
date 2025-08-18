package com.investments.tracker.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test the insertion from the DepositController")
    void testCallingInsertDepositMethod() throws Exception {
        String requestBody = Files.readString(Paths.get(DEPOSIT_REQUEST_JSON));

        mockMvc.perform(post(depositInsertUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }
}
