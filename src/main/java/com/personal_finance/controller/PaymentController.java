package com.personal_finance.controller;

import com.personal_finance.dto.payment.PaymentResponseDto;
import com.personal_finance.entity.Payment;
import com.personal_finance.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("{id}")
    public ResponseEntity<PaymentResponseDto> getPayment(@PathVariable UUID id){
        return ResponseEntity.ok(paymentService.getPayment(id));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponseDto>> getAllPayments(){
        return ResponseEntity.ok(paymentService.getAllPayments());
    }
}
