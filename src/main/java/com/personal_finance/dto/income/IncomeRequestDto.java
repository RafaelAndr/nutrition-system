package com.personal_finance.dto.income;

import com.personal_finance.entity.enums.IncomeCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record IncomeRequestDto(

        UUID accountId,

        @NotNull(message = "Income category is required")
        IncomeCategory incomeCategory,

        @NotNull(message = "Value is required")
        @DecimalMin(value = "0.01", message = "Value must be greater than zero")
        @Digits(integer = 10, fraction = 2, message = "Value must have up to 10 integer digits and 2 decimal places")
        BigDecimal value,

        @NotNull(message = "Receipt date is required")
        @PastOrPresent(message = "Receipt date cannot be in the future")
        LocalDate receiptDate

) {
}
