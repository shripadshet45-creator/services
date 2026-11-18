package com.microservices.account.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountDtos {
    public record CreateAccountRequest(
            @NotBlank String accountNumber,
            @NotBlank String ownerName,
            @NotNull BigDecimal openingBalance,
            @NotBlank String currency) {}

    public record AmountRequest(@NotNull BigDecimal amount) {}

    public record TransferRequest(
            @NotNull UUID fromAccountId,
            @NotNull UUID toAccountId,
            @NotNull BigDecimal amount) {}

    public record AccountResponse(
            UUID id,
            String accountNumber,
            String ownerName,
            BigDecimal balance,
            String currency) {}
}
