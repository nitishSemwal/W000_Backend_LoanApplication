package com.bank.loan_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoanApplicationRequest {
    @NotNull
    @Valid
    private ApplicantRequest applicant;
    @NotNull
    @Valid
    private LoanRequest loan;
}
