package com.banking.app.controller;

import com.banking.app.entity.Account;
import com.banking.app.service.BankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/bank")
@RequiredArgsConstructor
public class BankingController {

    private final BankingService service;

    @PostMapping("/create")
    public Account create(@RequestBody Account acc) {
        return service.createAccount(acc);
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam String acc, @RequestParam Double amt) {
        service.deposit(acc, amt);
        return "Deposited";
    }

    @PostMapping("/transfer")
    public String transfer(String from, String to, Double amt) {
        service.transfer(from, to, amt);
        return "Transfer Done";
    }
}