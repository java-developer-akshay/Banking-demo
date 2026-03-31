package com.banking.app.model;

import com.banking.app.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class Bank1Strategy implements BankStrategy {

    @Override
    public String getBankName() {
        return "This is Bank 1";
    }

    @Override
    public void deposit(Account account, Double amount) {
        account.setBalance(account.getBalance() + amount);
    }

    @Override
    public void withdraw(Account account, Double amount) {
        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }
        account.setBalance(account.getBalance() - amount);
    }
}

