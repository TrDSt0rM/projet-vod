package com.services;

import com.dtos.PaymentRequestDto;
import com.dtos.PaymentResponseDto;

public interface PaymentService {
    PaymentResponseDto processPayment(PaymentRequestDto request);
}