package com.investments.tracker.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.investments.tracker.controller.DepositController;
import com.investments.tracker.dto.response.BalanceResponse;
import com.investments.tracker.dto.request.DepositRequest;
import com.investments.tracker.service.DepositService;
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
import java.util.Collections;
import java.util.List;

import static com.investments.tracker.enums.Currency.EUR;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DepositController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class DepositControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Works, but is deprecated
    private DepositService depositService;

    private DepositRequest depositRequest;
//    private DepositResponse depositResponse;
    private BalanceResponse balanceResponse;
    private final LocalDate date = LocalDate.of(2025, 1, 1);
    private final String POST_ENDPOINT = "/api/v1/deposits/in";
//    private final String GET_FROM_TO_ENDPOINT = "/api/v1/deposits/get/from/{fromDate}/to/{toDate}";
//    private final String GET_ALL_DEPOSITS_ENDPOINT = "/api/v1/deposits/get/all";
//    private final String GET_TOTAL_DEPOSITS_ENDPOINT = "/api/v1/deposits/get/total/amount";

    @BeforeEach
    void setUp() {
        depositRequest = DepositRequest.builder()
                .date(date)
                .amount(BigDecimal.valueOf(1000))
                .currency(EUR)
                .description("TEST DESCRIPTION")
                .build();

//        depositResponse = DepositResponse.builder()
//                .date(date)
//                .amount(BigDecimal.valueOf(1000))
//                .currency(EUR)
//                .description("TEST DESCRIPTION")
//                .build();

        balanceResponse = BalanceResponse.builder()
                .date(date)
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

//    @Test
//    @DisplayName("Test should return all deposits from [date] to [date] when we have deposits")
//    public void testGetAllDepositsFromToNotEmpty() throws Exception {
//        when(depositService.getAllDepositsFromTo(any(), any())).thenReturn(List.of(depositResponse));
//
//        mockMvc.perform(get(GET_FROM_TO_ENDPOINT, date, date)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(1));
//
//        verify(depositService, times(1)).getAllDepositsFromTo(date, date);
//    }

//    @Test
//    @DisplayName("Test should return all deposits from [date] to [date] when we don't have deposits")
//    public void testGetAllDepositsFromToEmpty() throws Exception {
//        when(depositService.getAllDepositsFromTo(any(), any())).thenReturn(Collections.emptyList());
//
//        mockMvc.perform(get(GET_FROM_TO_ENDPOINT, date, date)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(0));
//
//        verify(depositService, times(1)).getAllDepositsFromTo(date, date);
//    }

//    @Test
//    @DisplayName("Test should return all deposits when we have deposits")
//    public void testGetAllDepositsNotEmpty() throws Exception {
//        when(depositService.getAllDepositsFromTo(any(), any())).thenReturn(List.of(depositResponse));
//
//        mockMvc.perform(get(GET_ALL_DEPOSITS_ENDPOINT)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(1));
//
//        verify(depositService, times(1)).getAllDepositsFromTo(any(), any());
//    }

//    @Test
//    @DisplayName("Test should return all deposits from when we don't have deposits")
//    public void testGetAllDepositsEmpty() throws Exception {
//        when(depositService.getAllDepositsFromTo(any(), any())).thenReturn(Collections.emptyList());
//
//        mockMvc.perform(get(GET_ALL_DEPOSITS_ENDPOINT)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(0));
//
//        verify(depositService, times(1)).getAllDepositsFromTo(any(), any());
//    }


//    @Test
//    @DisplayName("Test should return total amount of all deposits when we have deposits")
//    public void testGetTotalDepositsAmountNotEmpty() throws Exception {
//        when(depositService.getTotalDepositsAmount()).thenReturn(BigDecimal.valueOf(1000));
//
//        mockMvc.perform(get(GET_TOTAL_DEPOSITS_ENDPOINT)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().string("1000"));
//
//        verify(depositService, times(1)).getTotalDepositsAmount();
//    }

//    @Test
//    @DisplayName("Test should return total amount of all deposits when we don't have deposits")
//    public void testGetTotalDepositsAmountEmpty() throws Exception {
//        when(depositService.getTotalDepositsAmount()).thenReturn(BigDecimal.ZERO);
//
//        mockMvc.perform(get(GET_TOTAL_DEPOSITS_ENDPOINT)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().string("0"));
//
//        verify(depositService, times(1)).getTotalDepositsAmount();
//    }

}


