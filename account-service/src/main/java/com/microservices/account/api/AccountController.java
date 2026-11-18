package com.microservices.account.api;

import com.microservices.account.application.AccountService;
import com.microservices.account.domain.Account;
import com.microservices.account.domain.AccountNotFoundException;
import com.microservices.account.domain.InsufficientFundsException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AccountDtos.AccountResponse> create(
            @Valid @RequestBody AccountDtos.CreateAccountRequest request) {
        Account account = service.createAccount(
                request.accountNumber(), request.ownerName(),
                request.openingBalance(), request.currency().toUpperCase());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(account));
    }

    @GetMapping("/{id}")
    public AccountDtos.AccountResponse get(@PathVariable UUID id) {
        return toResponse(service.getAccount(id));
    }

    @PostMapping("/{id}/deposit")
    public AccountDtos.AccountResponse deposit(
            @PathVariable UUID id, @Valid @RequestBody AccountDtos.AmountRequest request) {
        return toResponse(service.deposit(id, request.amount()));
    }

    @PostMapping("/{id}/withdraw")
    public AccountDtos.AccountResponse withdraw(
            @PathVariable UUID id, @Valid @RequestBody AccountDtos.AmountRequest request) {
        return toResponse(service.withdraw(id, request.amount()));
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@Valid @RequestBody AccountDtos.TransferRequest request) {
        service.transfer(request.fromAccountId(), request.toAccountId(), request.amount());
        return ResponseEntity.ok().build();
    }

    private AccountDtos.AccountResponse toResponse(Account account) {
        return new AccountDtos.AccountResponse(
                account.getId(), account.getAccountNumber(), account.getOwnerName(),
                account.getBalance(), account.getCurrency());
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(AccountNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<Map<String, String>> handleInsufficientFunds(InsufficientFundsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(Map.of("error", exception.getMessage()));
    }
}
