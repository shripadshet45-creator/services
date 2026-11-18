package com.microservices.account.application;

import com.microservices.account.domain.Account;
import com.microservices.account.infrastructure.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AccountServiceTest {
    @Autowired
    private AccountService service;

    @Autowired
    private AccountRepository repository;

    @Test
    void depositsAndTransfersMoney() {
        Account source = service.createAccount("ACC-TEST-1", "Alice",
                new BigDecimal("100.00"), "USD");
        Account destination = service.createAccount("ACC-TEST-2", "Bob",
                new BigDecimal("50.00"), "USD");

        service.transfer(source.getId(), destination.getId(), new BigDecimal("25.00"));

        assertThat(service.getAccount(source.getId()).getBalance())
                .isEqualByComparingTo("75.00");
        assertThat(service.getAccount(destination.getId()).getBalance())
                .isEqualByComparingTo("75.00");
    }
}
