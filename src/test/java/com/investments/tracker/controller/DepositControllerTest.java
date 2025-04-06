package com.investments.tracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.deposit.DepositRequestDTO;
import com.investments.tracker.service.DepositService;
import com.investments.tracker.service.impl.DepositServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.investments.tracker.model.enums.Currency.EUR;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@ExtendWith(SpringExtension.class)
//@WebMvcTest(controllers = DepositController.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DepositControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DepositService depositService;

//    @Mock
//    private DepositService depositService;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    DepositRequestDTO depositRequestDTO;

    BalanceResponseDTO balanceResponseDTO;

//    @TestConfiguration
//    static class TestConfig {
//        @Bean
//        public DepositService depositService() {
//            return mock(DepositService.class); // Manually providing a mock
//        }
//    }



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
    public void testInsertSuccessfulDeposit() throws Exception {
        // when(depositService.insertDeposit(any(DepositRequestDTO.class))).thenReturn(balanceResponseDTO);
        when(depositService.insertDeposit(depositRequestDTO)).thenReturn(balanceResponseDTO);

        mockMvc.perform(post("/deposit/in")
                        .content(objectMapper.writeValueAsString(depositRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
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
}


