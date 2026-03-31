package com.banking.app.service;

import com.banking.app.model.BankStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BanksListService {

    private final Map<String, BankStrategy> strategyMap = new HashMap<>();

    public BanksListService(List<BankStrategy> strategies) {
        for (BankStrategy strategy : strategies) {
            strategyMap.put(strategy.getBankName(), strategy);
        }
    }

    public BankStrategy getBank(String bankName) {
        return strategyMap.get(bankName);
    }
}
