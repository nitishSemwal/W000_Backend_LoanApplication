package com.bank.loan_service.controller;

import com.bank.loan_service.dto.LoanApplicationRequest;
import com.bank.loan_service.dto.LoanApplicationResponse;
import com.bank.loan_service.service.LoanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/applications")
    public ResponseEntity<LoanApplicationResponse> processLoanApplication(@RequestBody LoanApplicationRequest loanApplicationRequest) {
        log.info("loan application request : {}", loanApplicationRequest);
        LoanApplicationResponse response = loanService.processLoanApplication(loanApplicationRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

