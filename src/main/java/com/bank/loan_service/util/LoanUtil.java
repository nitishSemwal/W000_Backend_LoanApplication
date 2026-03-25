package com.bank.loan_service.util;

import com.bank.loan_service.dto.LoanApplicationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.bank.loan_service.constant.LoanConstants.*;

@Slf4j
@Component
public class LoanUtil {

    public List<String> checkEligibility(LoanApplicationRequest request) {
        List<String> rejectionList = new ArrayList<>();

        if (request.getApplicant().getCreditScore() < 600) {
            rejectionList.add(CREDIT_SCORE_INVALID);
        }

        if ((request.getApplicant().getAge() + (request.getLoan().getTenureMonths())/12) > 65) {
            rejectionList.add(AGE_TENURE_EXCEEDED);
        }

        if (calculateEmi(request, INTEREST_RATE).compareTo(BigDecimal.valueOf(request.getApplicant().getMonthlyIncome() * 0.6).setScale(2, RoundingMode.HALF_UP)) > 0) {
            rejectionList.add(EMI_EXCEEDED_LIMIT);
        }

        return rejectionList;
    }

    public BigDecimal calculateEmi(LoanApplicationRequest request, float interestRate) {

        BigDecimal principal = BigDecimal.valueOf(request.getLoan().getAmount());
        BigDecimal annualInterest = BigDecimal.valueOf(interestRate);
        int months = request.getLoan().getTenureMonths();

        MathContext mc = new MathContext(10, RoundingMode.HALF_UP);

        BigDecimal monthlyInterestRate = annualInterest.divide(BigDecimal.valueOf(12), mc).divide(BigDecimal.valueOf(100), mc);

        BigDecimal power = BigDecimal.ONE.add(monthlyInterestRate).pow(months, mc);
        BigDecimal numerator = principal.multiply(monthlyInterestRate, mc).multiply(power, mc);
        BigDecimal denominator = power.subtract(BigDecimal.valueOf(1));

        BigDecimal emi = numerator.divide(denominator, 2, RoundingMode.HALF_UP);
        log.info("The emi calculated is : {}", emi);
        return emi;
    }

    public String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
