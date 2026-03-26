package com.bank.loan_service.service;

import com.bank.loan_service.dto.*;
import com.bank.loan_service.repository.UserApplicationRepository;
import com.bank.loan_service.util.LoanUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanServiceImplTest {

    private LoanUtil loanUtil;
    private UserApplicationRepository repository;
    private LoanServiceImpl loanService;

    @BeforeEach
    void setup(){

        loanUtil = mock(LoanUtil.class);
        repository = mock(UserApplicationRepository.class);
        loanService =
                new LoanServiceImpl(loanUtil, repository);
    }

    private LoanApplicationRequest createValidRequest(){
        ApplicantRequest applicantRequest = new ApplicantRequest("Raj", 30, 75000, "SALARIED", 720);
        LoanRequest loanRequest = new LoanRequest(500000, 36, "PERSONAL");

        return new LoanApplicationRequest(applicantRequest, loanRequest);
    }

    @Test
    void shouldApproveValidLoanApplication(){
        LoanApplicationRequest request = createValidRequest();

        when(loanUtil.checkEligibility(request)).thenReturn(new ArrayList<>());
        when(loanUtil.calculateEmi(any(), anyFloat())).thenReturn(new BigDecimal("16000"));
        LoanApplicationResponse response = loanService.processLoanApplication(request);

        assertEquals("APPROVED", response.getStatus());
        assertNotNull(response.getOffer());
    }

    @Test
    void shouldRejectWhenEligibilityFails(){
        LoanApplicationRequest request = createValidRequest();

        List<String> rejection = new ArrayList<>();
        rejection.add("LOW_CREDIT_SCORE");

        when(loanUtil.checkEligibility(request)).thenReturn(rejection);
        LoanApplicationResponse response = loanService.processLoanApplication(request);

        assertEquals("REJECTED", response.getStatus());
        assertNotNull(response.getRejectionReasons());
    }

    @Test
    void shouldRejectWhenEmiExceedsLimit(){
        LoanApplicationRequest request = createValidRequest();

        when(loanUtil.checkEligibility(request)).thenReturn(new ArrayList<>());
        when(loanUtil.calculateEmi(any(), anyFloat())).thenReturn(new BigDecimal("50000"));

        LoanApplicationResponse response = loanService.processLoanApplication(request);
        assertEquals("REJECTED", response.getStatus());
    }

    @Test
    void shouldCalculateOfferWhenApproved(){
        LoanApplicationRequest request = createValidRequest();

        when(loanUtil.generateUUID()).thenReturn("123");
        when(loanUtil.checkEligibility(request)).thenReturn(new ArrayList<>());
        when(loanUtil.calculateEmi(any(), anyFloat())).thenReturn(new BigDecimal("16000"));

        LoanApplicationResponse response = loanService.processLoanApplication(request);

        OfferResponse offer = response.getOffer();

        assertNotNull(offer);
        assertEquals(36, offer.getTenureMonths());
    }

}