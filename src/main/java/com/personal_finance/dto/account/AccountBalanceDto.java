package com.personal_finance.dto.account;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountBalanceDto(
        UUID id,
        String name,
        BigDecimal balance,
        String bankName
) {
}
