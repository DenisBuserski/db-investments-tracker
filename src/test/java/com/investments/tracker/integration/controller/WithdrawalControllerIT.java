package com.investments.tracker.integration.controller;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class WithdrawalControllerIT {
    @Value("${withdrawal.out.url}")
    private String withdrawalOutUrl;

    private static final String WITHDRAWAL_REQUEST_JSON = "src/test/resources/json/withdrawal-request.json";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test the insertion from the WithdrawalController")
    void testCallingInsertWithdrawalMethod() throws Exception {
        String requestBody = Files.readString(Paths.get(WITHDRAWAL_REQUEST_JSON));

        mockMvc.perform(post(withdrawalOutUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }


}
