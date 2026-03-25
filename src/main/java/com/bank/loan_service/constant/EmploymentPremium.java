package com.bank.loan_service.constant;

public enum EmploymentPremium {
    SALARIED (0.0f),
    SELF_EMPLOYED(1.0f);

    private final float premium;
    EmploymentPremium(float premium) {
        this.premium = premium;
    }

    public float getPremium() {
        return premium;
    }
}
