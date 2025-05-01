package com.investments.tracker.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class DepositIntegrationTest {
    String BASE_URL = "/api/v1/deposits";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testDeposit() throws Exception {
        mockMvc.perform(get(BASE_URL)).andExpect(status().isOk());
    }
}
