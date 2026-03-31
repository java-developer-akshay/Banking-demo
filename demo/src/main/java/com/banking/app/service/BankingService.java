package com.banking.app.service;

import com.banking.app.entity.Account;
import com.banking.app.model.BankStrategy;
import com.banking.app.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankingService {

    private final AccountRepository accountRepository;
    private final BanksListService banksListServiceList;

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public void deposit(String accountNumber, Double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow();

        BankStrategy strategy = banksListServiceList.getBank(account.getBankName());
        strategy.deposit(account, amount);

        accountRepository.save(account);
    }

    public void withdraw(String accountNumber, Double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow();

        BankStrategy strategy = banksListServiceList.getBank(account.getBankName());
        strategy.withdraw(account, amount);

        accountRepository.save(account);
    }

    @Transactional
    public void transfer(String from, String to, Double amount) {

        Account fromAcc = accountRepository.findByAccountNumber(from).orElseThrow();
        Account toAcc = accountRepository.findByAccountNumber(to).orElseThrow();

        try {
            withdraw(from, amount);
            deposit(to, amount);
            System.out.println("Transfer done from" + fromAcc + " to: "+ toAcc);

        } catch (Exception e) {
            deposit(from, amount);
            throw new RuntimeException("Transfer Failed, rolled back");
        }
    }
}