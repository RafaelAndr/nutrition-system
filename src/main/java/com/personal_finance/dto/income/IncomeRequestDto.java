package com.personal_finance.dto.income;

import com.personal_finance.entity.enums.IncomeCategory;

import java.math.BigDecimal;
import java.time.LocalDate;

public record IncomeRequestDto(
        IncomeCategory incomeCategory,
        BigDecimal value,
        LocalDate receiptDate
) {
}
