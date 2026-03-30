package com.personal_finance.dto.resquest;

public record AccountRequestDto(
        String bankName,
        String balance
) {
}
