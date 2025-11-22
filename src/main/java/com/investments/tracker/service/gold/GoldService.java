package com.investments.tracker.service.gold;

import com.investments.tracker.controller.preciousmetals.GoldBuyRequest;
import com.investments.tracker.model.PreciousMetals;
import com.investments.tracker.repository.PreciousMetalsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.investments.tracker.enums.PreciousMetalType.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoldService {
    private final PreciousMetalsRepository preciousMetalsRepository;

    @Transactional
    public void insertGoldTransaction(GoldBuyRequest goldBuyRequest) {
        BigDecimal pricePerGramEUR = goldBuyRequest.getPriceEUR().divide(BigDecimal.valueOf(goldBuyRequest.getSizeInGrams()));

        PreciousMetals gold = PreciousMetals.builder()
                .type(GOLD)
                .sellerName(goldBuyRequest.getSellerName())
                .productName(goldBuyRequest.getProductName())
                .url(goldBuyRequest.getUrl())
                .transactionDate(goldBuyRequest.getTransactionDate())
                .sizeInGrams(goldBuyRequest.getSizeInGrams())
                .priceBGN(goldBuyRequest.getPriceBGN())
                .priceEUR(goldBuyRequest.getPriceEUR())
                .pricePerGramEUR(pricePerGramEUR)
                .pricePerGramOnDateEUR(goldBuyRequest.getPricePerGramOnDateEUR())
                .difference(pricePerGramEUR.subtract(goldBuyRequest.getPricePerGramOnDateEUR()))
                .build();
        preciousMetalsRepository.save(gold);
        log.info("Gold transaction successfully inserted");
    }
}
