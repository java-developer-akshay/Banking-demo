package com.banking.app.model;

import com.banking.app.entity.Account;

public interface BankStrategy {
    String getBankName();
    void deposit(Account account, Double amount);
    void withdraw(Account account, Double amount);
}