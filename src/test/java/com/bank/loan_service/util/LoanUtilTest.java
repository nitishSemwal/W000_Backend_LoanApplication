package com.bank.loan_service.util;

import com.bank.loan_service.dto.ApplicantRequest;
import com.bank.loan_service.dto.LoanApplicationRequest;
import com.bank.loan_service.dto.LoanRequest;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoanUtilTest {

    private LoanUtil loanUtil = new LoanUtil();

    private LoanApplicationRequest createValidRequest(){
        ApplicantRequest applicantRequest = new ApplicantRequest("Raj", 30, 75000, "SALARIED", 720);
        LoanRequest loanRequest = new LoanRequest(500000, 36, "PERSONAL");

        return new LoanApplicationRequest(applicantRequest, loanRequest);
    }

    @Test
    void shouldCalculateCorrectEmi(){
        BigDecimal emi =
                loanUtil.calculateEmi(
                        createValidRequest(),13.5f);

        assertEquals(new BigDecimal("16967.64"), emi);

    }

}