package com.personal_finance.dto.payment;

import com.personal_finance.entity.enums.PaymentMethod;

import java.util.UUID;

public record PaymentRequestDto(
        UUID accountId,
        PaymentMethod paymentMethod
) {
}
