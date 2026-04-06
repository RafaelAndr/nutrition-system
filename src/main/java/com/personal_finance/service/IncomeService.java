package com.personal_finance.service;

import com.personal_finance.dto.income.IncomeRequestDto;
import com.personal_finance.dto.income.IncomeResponseDto;
import com.personal_finance.entity.Income;
import com.personal_finance.mapper.IncomeMapper;
import com.personal_finance.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final IncomeMapper incomeMapper;

    public IncomeResponseDto save(IncomeRequestDto incomeRequestDto){
        Income income = incomeMapper.toEntity(incomeRequestDto);
        return incomeMapper.toDto(incomeRepository.save(income));
    }
}
