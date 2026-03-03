package com.dtos;

import lombok.Data;

@Data
public class PaymentResponseDto {
    private boolean success;
    private String transactionId;
    private String message;
}