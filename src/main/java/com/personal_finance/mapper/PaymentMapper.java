package com.personal_finance.mapper;

import com.personal_finance.dto.payment.PaymentRequestDto;
import com.personal_finance.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    Payment toEntity(PaymentRequestDto paymentExpenseRequestDto);

}
