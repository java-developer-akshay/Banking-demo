package com.banking.app.service;

import com.banking.app.entity.Account;
import com.banking.app.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public Account createAccount(Account account) {
        return repository.save(account);
    }

    public Account getAccount(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public List<Account> getAllAccounts() {
        return repository.findAll();
    }

    public Account deposit(Long id, double amount) {
        Account acc = getAccount(id);
        acc.setBalance(acc.getBalance() + amount);
        return repository.save(acc);
    }

    public Account withdraw(Long id, double amount) {
        Account acc = getAccount(id);

        if (acc.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        acc.setBalance(acc.getBalance() - amount);
        return repository.save(acc);
    }

    public void deleteAccount(Long id) {
        repository.deleteById(id);
    }
}