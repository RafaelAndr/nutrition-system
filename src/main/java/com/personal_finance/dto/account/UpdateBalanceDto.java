package com.personal_finance.dto.account;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateBalanceDto(
        @NotNull
        @Positive
        BigDecimal amount
) {
}
