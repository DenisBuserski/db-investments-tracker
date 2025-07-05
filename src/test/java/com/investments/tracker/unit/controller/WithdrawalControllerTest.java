package com.investments.tracker.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.investments.tracker.controller.WithdrawalController;
import com.investments.tracker.controller.request.WithdrawalRequest;
import com.investments.tracker.controller.response.BalanceResponse;
import com.investments.tracker.service.withdrawal.WithdrawalService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WithdrawalController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@Tag("unit")
public class WithdrawalControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Works, but is deprecated
    private WithdrawalService withdrawalService;

    private WithdrawalRequest withdrawalRequest;
    private BalanceResponse balanceResponse;
    private final LocalDate DATE = LocalDate.of(2025, 1, 1);
    private final String POST_ENDPOINT = "/api/v1/withdrawals/in";

    @BeforeEach
    void setUp() {
        withdrawalRequest = WithdrawalRequest.builder()
                .date(DATE)
                .amount(BigDecimal.valueOf(1000))
                .currency(EUR)
                .description("TEST DESCRIPTION")
                .build();

        balanceResponse = BalanceResponse.builder()
                .date(DATE)
                .balance(BigDecimal.ZERO)
                .totalInvestments(BigDecimal.ZERO)
                .totalDeposits(BigDecimal.ZERO)
                .totalWithdrawals(BigDecimal.valueOf(1000))
                .totalDividends(BigDecimal.ZERO)
                .totalFees(BigDecimal.ZERO)
                .lastPortfolioValue(BigDecimal.ZERO)
                .build();
    }

    @Test
    @DisplayName("Test should create a successful withdrawal")
    public void testInsertSuccessfulWithdrawal() throws Exception {
        when(withdrawalService.insertWithdraw(any(WithdrawalRequest.class))).thenReturn(balanceResponse);

        mockMvc.perform(post(POST_ENDPOINT)
                        .content(objectMapper.writeValueAsString(withdrawalRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.date").value("2025-01-01"))
                .andExpect(jsonPath("$.balance").value(BigDecimal.ZERO))
                .andExpect(jsonPath("$.totalInvestments").value(BigDecimal.ZERO))
                .andExpect(jsonPath("$.totalDeposits").value(BigDecimal.ZERO))
                .andExpect(jsonPath("$.totalWithdrawals").value(BigDecimal.valueOf(1000)))
                .andExpect(jsonPath("$.totalDividends").value(BigDecimal.ZERO))
                .andExpect(jsonPath("$.totalFees").value(BigDecimal.ZERO))
                .andExpect(jsonPath("$.lastPortfolioValue").value(BigDecimal.ZERO));

        verify(withdrawalService, times(1)).insertWithdraw(any(WithdrawalRequest.class));
    }
}
