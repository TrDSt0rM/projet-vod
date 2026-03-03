package com.dtos;

import lombok.Data;

@Data
public class PaymentRequestDto {
    private String cardNumber;
    private String cardHolder;
    private String expirationDate;
    private String cvc;
    private Double amount;
}