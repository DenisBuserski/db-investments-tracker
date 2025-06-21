package com.investments.tracker.unit.controller;

public class CashTransactionController {
//        depositResponse = DepositResponse.builder()
//                .date(date)
//                .amount(BigDecimal.valueOf(1000))
//                .currency(EUR)
//                .description("TEST DESCRIPTION")
//                .build();


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
