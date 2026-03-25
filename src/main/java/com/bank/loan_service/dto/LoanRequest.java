package com.bank.loan_service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class LoanRequest {
    @Min(10000)
    @Max(5000000)
    private int amount;

    @Min(6)
    @Max(360)
    private int tenureMonths;

    private String purpose;

}
