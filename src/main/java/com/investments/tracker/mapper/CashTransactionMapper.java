package com.investments.tracker.mapper;

import com.investments.tracker.model.CashTransaction;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CashTransactionMapper {
    public <T> CashTransaction createCashtransaction(T dto, Function<T, CashTransaction> mapper) {
        return mapper.apply(dto);
    }
}
