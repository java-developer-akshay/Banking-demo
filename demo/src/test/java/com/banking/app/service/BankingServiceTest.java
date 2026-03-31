package com.banking.app.service;

import com.banking.app.entity.Account;
import com.banking.app.model.BankStrategy;
import com.banking.app.repository.AccountRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BankingServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private BanksListService banksList;

    @Mock
    private BankStrategy bankStrategy;

    @InjectMocks
    private BankingService bankingService;

    private Account account;

    @BeforeEach
    void setup() {
        account = new Account();
        account.setAccountNumber("123");
        account.setBankName("HDFC");
        account.setBalance(1000.0);
    }

    @Test
    void testCreateAccount() {
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account saved = bankingService.createAccount(account);

        assertNotNull(saved);
        verify(accountRepository, times(1)).save(account);
    }


    @Test
    void testDeposit() {
        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(account));
        when(banksList.getBank("HDFC")).thenReturn(bankStrategy);

        bankingService.deposit("123", 500.0);

        verify(bankStrategy).deposit(account, 500.0);
        verify(accountRepository).save(account);
    }

    @Test
    void testWithdrawSuccess() {
        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(account));
        when(banksList.getBank("HDFC")).thenReturn(bankStrategy);

        bankingService.withdraw("123", 200.0);

        verify(bankStrategy).withdraw(account, 200.0);
        verify(accountRepository).save(account);
    }

    @Test
    void testWithdrawFailure() {
        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(account));
        when(banksList.getBank("HDFC")).thenReturn(bankStrategy);

        doThrow(new RuntimeException("Insufficient balance"))
                .when(bankStrategy).withdraw(account, 2000.0);

        assertThrows(RuntimeException.class, () ->
                bankingService.withdraw("123", 2000.0));
    }

    @Test
    void testTransferSuccess() {

        Account toAccount = new Account();
        toAccount.setAccountNumber("456");
        toAccount.setBankName("SBI");
        toAccount.setBalance(500.0);

        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(account));
        when(accountRepository.findByAccountNumber("456")).thenReturn(Optional.of(toAccount));

        when(banksList.getBank("HDFC")).thenReturn(bankStrategy);
        when(banksList.getBank("SBI")).thenReturn(bankStrategy);

        bankingService.transfer("123", "456", 100.0);

        verify(bankStrategy, times(1)).deposit(any(), anyDouble());
        verify(bankStrategy, times(1)).withdraw(any(), anyDouble());
    }

    @Test
    void testTransferFailureRollback() {

        Account toAccount = new Account();
        toAccount.setAccountNumber("456");
        toAccount.setBankName("SBI");
        toAccount.setBalance(500.0);

        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(account));
        when(accountRepository.findByAccountNumber("456")).thenReturn(Optional.of(toAccount));

        when(banksList.getBank("HDFC")).thenReturn(bankStrategy);
        when(banksList.getBank("SBI")).thenReturn(bankStrategy);


        doThrow(new RuntimeException("Failure"))
                .when(bankStrategy).deposit(toAccount, 100.0);

        assertThrows(RuntimeException.class, () ->
                bankingService.transfer("123", "456", 100.0));

        verify(bankStrategy).deposit(account, 100.0);
    }
}