package com.bank.loan_service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ApplicantRequest {
    @NotNull
    private String name;

    @Min(21)
    @Max(60)
    private int age;

    @Min(0)
    private int monthlyIncome;

    @NotNull
    private String employmentType;

    @Min(300)
    @Max(900)
    private int creditScore;
}
