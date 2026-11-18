package com.microservices.account.application;

import com.microservices.account.domain.Account;
import com.microservices.account.domain.AccountNotFoundException;
import com.microservices.account.infrastructure.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AccountService {
    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Account createAccount(String accountNumber, String ownerName,
                                 BigDecimal openingBalance, String currency) {
        if (openingBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Opening balance cannot be negative");
        }
        return repository.save(new Account(accountNumber, ownerName, openingBalance, currency));
    }

    @Transactional(readOnly = true)
    public Account getAccount(UUID id) {
        return repository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
    }

    @Transactional
    public Account deposit(UUID id, BigDecimal amount) {
        validateAmount(amount);
        Account account = getAccount(id);
        account.deposit(amount);
        return account;
    }

    @Transactional
    public Account withdraw(UUID id, BigDecimal amount) {
        validateAmount(amount);
        Account account = getAccount(id);
        account.withdraw(amount);
        return account;
    }

    @Transactional
    public void transfer(UUID fromId, UUID toId, BigDecimal amount) {
        validateAmount(amount);
        Account source = getAccount(fromId);
        Account destination = getAccount(toId);
        if (!source.getCurrency().equalsIgnoreCase(destination.getCurrency())) {
            throw new IllegalArgumentException("Accounts must use the same currency");
        }
        source.withdraw(amount);
        destination.deposit(amount);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }
}
