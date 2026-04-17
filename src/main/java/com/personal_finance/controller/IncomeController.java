package com.personal_finance.controller;

import com.personal_finance.dto.income.IncomeRequestDto;
import com.personal_finance.dto.income.IncomeResponseDto;
import com.personal_finance.service.IncomeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/incomes")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<IncomeResponseDto> createIncome(@RequestBody @Valid IncomeRequestDto incomeRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(incomeService.save(incomeRequestDto));
    }

    @GetMapping("{id}")
    public ResponseEntity<IncomeResponseDto> getIncome(@PathVariable UUID id){
        return ResponseEntity.ok(incomeService.getIncome(id));
    }

    @GetMapping("/user")
    public ResponseEntity<List<IncomeResponseDto>> getUserIncomes(){
        return ResponseEntity.ok(incomeService.getAllUserIncome());
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<List<IncomeResponseDto>> getAccountIncomes(@PathVariable UUID accountId){
        return ResponseEntity.ok(incomeService.getAllAccountIncome(accountId));
    }
}
