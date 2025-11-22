package com.investments.tracker.integration;

import com.investments.tracker.enums.PreciousMetalType;
import com.investments.tracker.model.PreciousMetal;
import com.investments.tracker.repository.PreciousMetalsRepository;
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

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.investments.tracker.enums.PreciousMetalType.GOLD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class GoldTransactionCreationIT {
    private static final String GOLD_BUY_TRANSACTION_REQUEST_JSON = "src/test/resources/json/gold-buy-transaction-request.json";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PreciousMetalsRepository preciousMetalsRepository;

    @Value("${application.test.gold-insert-url}")
    private String goldInsertUrl;

    @Test
    @DisplayName("Test create successful gold buy transaction")
    void testCreateSuccessfulGoldBuyTransaction() throws Exception {
        String requestBody = Files.readString(Paths.get(GOLD_BUY_TRANSACTION_REQUEST_JSON));

        mockMvc.perform(post(goldInsertUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        PreciousMetal gold = preciousMetalsRepository.findAll().get(0);

        assertEquals("Top Gold", gold.getSellerName());
        assertEquals(GOLD, gold.getType());
        assertEquals(5, gold.getSizeInGrams());
        assertEquals(BigDecimal.valueOf(500.55), gold.getPriceEUR());
        assertEquals(BigDecimal.valueOf(100.11), gold.getPricePerGramEUR());
        assertEquals(BigDecimal.valueOf(91.94), gold.getPricePerGramOnDateEUR());
        assertEquals(BigDecimal.valueOf(8.17), gold.getDifference());
    }
}
