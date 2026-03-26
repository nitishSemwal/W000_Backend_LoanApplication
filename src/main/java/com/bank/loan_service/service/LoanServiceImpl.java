package com.bank.loan_service.service;

import com.bank.loan_service.constant.EmploymentPremium;
import com.bank.loan_service.constant.RiskPremium;
import com.bank.loan_service.dto.LoanApplicationRequest;
import com.bank.loan_service.dto.LoanApplicationResponse;
import com.bank.loan_service.dto.OfferResponse;
import com.bank.loan_service.entity.UserApplication;
import com.bank.loan_service.repository.UserApplicationRepository;
import com.bank.loan_service.util.LoanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static com.bank.loan_service.constant.LoanConstants.*;

@Service
@Slf4j
public class LoanServiceImpl implements LoanService{

    private final LoanUtil loanUtil;
    private final UserApplicationRepository userApplicationRepository;

    public LoanServiceImpl(LoanUtil loanUtil, UserApplicationRepository userApplicationRepository) {
        this.loanUtil = loanUtil;
        this.userApplicationRepository = userApplicationRepository;
    }

    @Override
    public LoanApplicationResponse processLoanApplication(LoanApplicationRequest request) {
        log.info("EXEC_BEGIN::LOAN_SERVICE");

        LoanApplicationResponse response;
        String status;

        String applicationId = loanUtil.generateUUID();

        List<String> rejectionList = loanUtil.checkEligibility(request);

        if (rejectionList.size() == 0) {
            log.info("rejection list empty. Calculating final EMI");
            RiskPremium riskPremium = calculateRiskBand(request.getApplicant().getCreditScore());
            float interestRate = finalInterestRate(riskPremium, request.getApplicant().getEmploymentType(), request.getLoan().getAmount());
            BigDecimal emi = loanUtil.calculateEmi(request, interestRate);

            if ((emi).compareTo(BigDecimal.valueOf(request.getApplicant().getMonthlyIncome()).multiply(BigDecimal.valueOf(0.5))) > 0) {
                status = REJECTED;
                rejectionList.add(EMI_LIMIT_EXCEEDED);
                log.info("Loan rejected - {}", rejectionList);
            } else {
                status = APPROVED;
                log.info("Loan approved");

            }
            response = createLoanResponse(status, applicationId, interestRate, riskPremium.name(), emi, rejectionList, request);
        }  else {
            log.info("Loan rejected - {}", rejectionList);
            status = REJECTED;
            response = createLoanResponse(status, applicationId, 0.0f, null, null,  rejectionList, request);
        }

        return response;
    }

    private LoanApplicationResponse createLoanResponse(String status, String applicationId, float interestRate, String riskBand,
                                                       BigDecimal emi, List<String> rejectionList, LoanApplicationRequest request) {
        LoanApplicationResponse response;
        if (status.equals(APPROVED)) {
            BigDecimal totalPayable = emi.multiply(BigDecimal.valueOf(request.getLoan().getTenureMonths()));
            OfferResponse offer = OfferResponse.builder().interestRate(interestRate).tenureMonths(request.getLoan().getTenureMonths()).emi(emi).totalPayable(totalPayable).build();
            response = LoanApplicationResponse.builder().applicationId(applicationId).status(APPROVED).riskBand(riskBand).offer(offer).build();
        } else {
            response = LoanApplicationResponse.builder().applicationId(applicationId).status(status).rejectionReasons(rejectionList).build();
        }

        saveUserInfo(applicationId, request, status);

        return response;
    }


    private void saveUserInfo(String applicationId, LoanApplicationRequest request, String status) {
        UserApplication userApplication = new UserApplication(applicationId, request.getApplicant().getName(),
                request.getApplicant().getAge(), request.getApplicant().getMonthlyIncome(), request.getLoan().getAmount(), status);
        userApplicationRepository.save(userApplication);
        log.info("Application status saved in database");
    }

    private RiskPremium calculateRiskBand(int creditScore) {
        if (creditScore > 750) {
            return RiskPremium.LOW;
        } else if (creditScore >= 650) {
            return RiskPremium.MEDIUM;
        } else {
            return RiskPremium.HIGH;
        }
    }


    private float finalInterestRate(RiskPremium riskPremium, String employmentType, int loanSize) {
        EmploymentPremium employmentPremium = EmploymentPremium.valueOf(employmentType.toUpperCase());
        float loanPremium = 0;

        if (loanSize > 1000000) {
            loanPremium = 0.5f;
        }

        float finalRate = INTEREST_RATE + riskPremium.getPremium() + employmentPremium.getPremium() + loanPremium;
        return finalRate;
    }
}
