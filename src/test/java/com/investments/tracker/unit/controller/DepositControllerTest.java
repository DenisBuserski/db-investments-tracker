package com.investments.tracker.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.investments.tracker.controller.deposit.DepositController;
import com.investments.tracker.controller.response.BalanceResponse;
import com.investments.tracker.controller.deposit.DepositRequest;
import com.investments.tracker.service.deposit.DepositService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.investments.tracker.enums.Currency.EUR;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DepositController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@Tag("unit")
public class DepositControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Works, but is deprecated
    private DepositService depositService;

    private DepositRequest depositRequest;
    private BalanceResponse balanceResponse;
    private final LocalDate DATE = LocalDate.of(2025, 1, 1);
    private final String POST_ENDPOINT = "/api/v1/deposits/in";

    @BeforeEach
    void setUp() {
        depositRequest = DepositRequest.builder()
                .date(DATE)
                .amount(BigDecimal.valueOf(1000))
                .currency(EUR)
                .description("TEST DESCRIPTION")
                .build();

        balanceResponse = BalanceResponse.builder()
                .date(DATE)
                .balance(BigDecimal.valueOf(1000))
                .totalInvestments(BigDecimal.ZERO)
                .totalDeposits(BigDecimal.valueOf(1000))
                .totalWithdrawals(BigDecimal.ZERO)
                .totalDividends(BigDecimal.ZERO)
                .totalFees(BigDecimal.ZERO)
                .lastPortfolioValue(BigDecimal.ZERO)
                .build();
    }

    @Test
    @DisplayName("Test should create a successful deposit")
    public void testInsertSuccessfulDeposit() throws Exception {
        when(depositService.insertDeposit(any(DepositRequest.class))).thenReturn(balanceResponse);

        mockMvc.perform(post(POST_ENDPOINT)
                        .content(objectMapper.writeValueAsString(depositRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.date").value("2025-01-01"))
                .andExpect(jsonPath("$.balance").value(BigDecimal.valueOf(1000)))
                .andExpect(jsonPath("$.totalInvestments").value(BigDecimal.ZERO))
                .andExpect(jsonPath("$.totalDeposits").value(BigDecimal.valueOf(1000)))
                .andExpect(jsonPath("$.totalWithdrawals").value(BigDecimal.ZERO))
                .andExpect(jsonPath("$.totalDividends").value(BigDecimal.ZERO))
                .andExpect(jsonPath("$.totalFees").value(BigDecimal.ZERO))
                .andExpect(jsonPath("$.lastPortfolioValue").value(BigDecimal.ZERO));

        verify(depositService, times(1)).insertDeposit(any(DepositRequest.class));
    }

}


