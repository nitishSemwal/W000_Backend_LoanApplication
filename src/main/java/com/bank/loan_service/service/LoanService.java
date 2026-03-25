package com.bank.loan_service.service;

import com.bank.loan_service.dto.LoanApplicationRequest;
import com.bank.loan_service.dto.LoanApplicationResponse;

public interface LoanService {
    public LoanApplicationResponse processLoanApplication(LoanApplicationRequest request);
}
