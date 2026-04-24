package com.personal_finance.service;

import com.personal_finance.dto.income.IncomeRequestDto;
import com.personal_finance.dto.income.IncomeResponseDto;
import com.personal_finance.entity.Account;
import com.personal_finance.entity.Income;
import com.personal_finance.entity.Users;
import com.personal_finance.mapper.IncomeMapper;
import com.personal_finance.repository.IncomeRepository;
import com.personal_finance.security.SecurityService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final IncomeMapper incomeMapper;
    private final AccountService accountService;
    private final SecurityService securityService;

    private Users getLoggedUser(){
        return securityService.getUserLoggedIn();
    }

    public IncomeResponseDto save(IncomeRequestDto incomeRequestDto){
        Users userLoggedIn = getLoggedUser();

        log.info("Starting expense creation for userId={}", userLoggedIn.getId());

        Income income = incomeMapper.toEntity(incomeRequestDto);
        income.setUser(userLoggedIn);

        if (incomeRequestDto.accountId() != null) {
            log.info("Fetching accountId={} for userId={}", incomeRequestDto.accountId(), userLoggedIn.getId());
            Account account = accountService.searchById(incomeRequestDto.accountId());
            account.validateOwnership(userLoggedIn.getId());

            income.setAccount(account);
        }

        Income savedIncome = incomeRepository.save(income);

        log.info("Income created successfully with id={} for userId={}",
                savedIncome.getId(), userLoggedIn.getId());

        return incomeMapper.toDto(savedIncome);
    }

    public IncomeResponseDto getIncome(UUID id){
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Income not found"));

        return incomeMapper.toDto(income);
    }

    public List<IncomeResponseDto> getAllUserIncome(){
        Users userLogged = getLoggedUser();

        List<Income> userIncomes = incomeRepository.findByUser(userLogged);

        return userIncomes.stream().map(incomeMapper::toDto).toList();
    }

    public List<IncomeResponseDto> getAllAccountIncome(UUID accountId){
        Users userLogged = getLoggedUser();

        Account account = accountService.searchById(accountId);

        account.validateOwnership(userLogged.getId());

        List<Income> accountIncomes = incomeRepository.findByAccount(account);

        return accountIncomes.stream().map(incomeMapper::toDto).toList();
    }
}
