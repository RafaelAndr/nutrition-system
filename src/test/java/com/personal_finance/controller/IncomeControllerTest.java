package com.personal_finance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal_finance.dto.income.IncomeRequestDto;
import com.personal_finance.dto.income.IncomeResponseDto;
import com.personal_finance.entity.enums.IncomeCategory;
import com.personal_finance.mapper.ExpenseMapper;
import com.personal_finance.mapper.IncomeMapper;
import com.personal_finance.security.JwtService;
import com.personal_finance.service.IncomeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IncomeController.class)
@AutoConfigureMockMvc(addFilters = false)
class IncomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private IncomeService incomeService;

    @Test
    void shouldCreateIncome() throws Exception {

        UUID id = UUID.randomUUID();

        IncomeRequestDto request = new IncomeRequestDto(
                id,
                IncomeCategory.INVESTMENT,
                BigDecimal.valueOf(5000),
                LocalDate.now()
        );

        IncomeResponseDto response = new IncomeResponseDto(
                id,
                IncomeCategory.INVESTMENT,
                BigDecimal.valueOf(5000),
                LocalDate.now()
        );

        when(incomeService.save(any(IncomeRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/incomes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.incomeCategory").value(IncomeCategory.INVESTMENT.name()))
                .andExpect(jsonPath("$.value").value(5000));

        verify(incomeService).save(any(IncomeRequestDto.class));
    }

    @Test
    void shouldReturnIncomeById() throws Exception {

        UUID id = UUID.randomUUID();

        IncomeResponseDto response = new IncomeResponseDto(
                id,
                IncomeCategory.INVESTMENT,
                BigDecimal.valueOf(1200),
                LocalDate.now()
        );

        when(incomeService.getIncome(id)).thenReturn(response);

        mockMvc.perform(get("/incomes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.incomeCategory").value("INVESTMENT"))
                .andExpect(jsonPath("$.value").value(1200));

        verify(incomeService).getIncome(id);
    }

    @Test
    void shouldReturnUserIncomes() throws Exception {

        List<IncomeResponseDto> incomes = List.of(
                new IncomeResponseDto(
                        UUID.randomUUID(),
                        IncomeCategory.SALARY,
                        BigDecimal.valueOf(5000),
                        LocalDate.now()
                )
        );

        when(incomeService.getAllUserIncome()).thenReturn(incomes);

        mockMvc.perform(get("/incomes/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].incomeCategory").value("SALARY"));

        verify(incomeService).getAllUserIncome();
    }

    @Test
    void shouldReturnAccountIncomes() throws Exception {

        UUID accountId = UUID.randomUUID();

        List<IncomeResponseDto> incomes = List.of(
                new IncomeResponseDto(
                        UUID.randomUUID(),
                        IncomeCategory.INVESTMENT,
                        BigDecimal.valueOf(800),
                        LocalDate.now()
                )
        );

        when(incomeService.getAllAccountIncome(accountId))
                .thenReturn(incomes);

        mockMvc.perform(get("/incomes/accounts/{accountId}", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].incomeCategory").value("INVESTMENT"));

        verify(incomeService).getAllAccountIncome(eq(accountId));
    }
}
