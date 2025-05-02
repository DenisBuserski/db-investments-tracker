package com.investments.tracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.deposit.DepositRequestDTO;
import com.investments.tracker.service.DepositServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import static com.investments.tracker.model.enums.Currency.EUR;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DepositController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class DepositControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean // Works, but is deprecated
    private DepositServiceImpl depositService;

    @Autowired
    private ObjectMapper objectMapper;

    // private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    DepositRequestDTO depositRequestDTO;

    BalanceResponseDTO balanceResponseDTO;


    @BeforeEach
    void setUp() {
        depositRequestDTO = DepositRequestDTO.builder()
                .date(LocalDate.of(2025, 3, 16))
                .amount(BigDecimal.valueOf(1000))
                .currency(EUR)
                .description("TEST DESCRIPTION")
                .build();

        balanceResponseDTO = BalanceResponseDTO.builder()
                .date(LocalDate.of(2025, 3, 16))
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
        when(depositService.insertDeposit(any(DepositRequestDTO.class))).thenReturn(balanceResponseDTO);

        mockMvc.perform(post("/deposit/in")
                        .content(objectMapper.writeValueAsString(depositRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.date").value("2025-03-16"))
                .andExpect(jsonPath("$.balance").value(BigDecimal.valueOf(1000)))
                .andExpect(jsonPath("$.totalInvestments").value(BigDecimal.ZERO))
                .andExpect(jsonPath("$.totalDeposits").value(BigDecimal.valueOf(1000)))
                .andExpect(jsonPath("$.totalWithdrawals").value(BigDecimal.ZERO))
                .andExpect(jsonPath("$.totalDividends").value(BigDecimal.ZERO))
                .andExpect(jsonPath("$.totalFees").value(BigDecimal.ZERO))
                .andExpect(jsonPath("$.lastPortfolioValue").value(BigDecimal.ZERO));

        verify(depositService, times(1)).insertDeposit(any(DepositRequestDTO.class));
    }

    // Unit test

}


