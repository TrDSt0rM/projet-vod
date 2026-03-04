package com.services.impl;

import com.dtos.PaymentRequestDto;
import com.dtos.PaymentResponseDto;
import com.services.PaymentService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Override
    public PaymentResponseDto processPayment(PaymentRequestDto request) {
        PaymentResponseDto response = new PaymentResponseDto();

        if (request.getCardNumber() == null || request.getCardNumber().isEmpty()) {
            response.setSuccess(false);
            response.setMessage("Invalid card number");
            return response;
        }

        if (request.getCvc() == null || request.getCvc().isEmpty()) {
            response.setSuccess(false);
            response.setMessage("Invalid CVC");
            return response;
        }

        if (request.getAmount() == null) {
            response.setSuccess(false);
            response.setMessage("Invalid amount");
            return response;
        }

        response.setSuccess(true);
        response.setTransactionId(UUID.randomUUID().toString());
        response.setMessage("Payment accepted");
        return response;
    }
}