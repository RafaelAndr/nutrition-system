package com.personal_finance.dto.payment;

import com.personal_finance.entity.Account;
import com.personal_finance.entity.Expense;
import com.personal_finance.entity.Users;
import com.personal_finance.entity.enums.PaymentMethod;

import java.util.UUID;

public record PaymentResponseDto(
        UUID id,
        PaymentMethod paymentMethod,
        UUID expenseId,
        UUID userId,
        UUID accountId
) {
}
