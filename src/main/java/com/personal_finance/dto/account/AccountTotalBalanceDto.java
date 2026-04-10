package com.personal_finance.dto.account;

import java.math.BigDecimal;

public record AccountTotalBalanceDto(
        BigDecimal totalBalance
) {
}
