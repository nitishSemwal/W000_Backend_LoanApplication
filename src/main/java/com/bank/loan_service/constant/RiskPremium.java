package com.bank.loan_service.constant;

public enum RiskPremium {
    LOW (0.0f),
    MEDIUM(1.5f),
    HIGH(3.0f);

    private final float premium;
    RiskPremium(float premium) {
        this.premium = premium;
    }

    public float getPremium() {
        return premium;
    }
}
