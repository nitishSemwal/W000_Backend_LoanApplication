package com.bank.loan_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LoanApplicationResponse {
    private String applicationId;
    private String status;
    private String riskBand;
    private OfferResponse offer;
    private List<String> rejectionReasons;
}
