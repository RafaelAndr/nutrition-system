package com.personal_finance.mapper;

import com.personal_finance.dto.response.AccountResponseDto;
import com.personal_finance.dto.resquest.AccountRequestDto;
import com.personal_finance.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account toEntity(AccountRequestDto accountRequestDto);

    AccountResponseDto toDto(Account account);
}
