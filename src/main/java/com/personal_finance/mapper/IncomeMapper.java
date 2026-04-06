package com.personal_finance.mapper;

import com.personal_finance.dto.income.IncomeRequestDto;
import com.personal_finance.dto.income.IncomeResponseDto;
import com.personal_finance.entity.Income;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IncomeMapper {
    Income toEntity(IncomeRequestDto incomeRequestDto);

    IncomeResponseDto toDto(Income income);
}
