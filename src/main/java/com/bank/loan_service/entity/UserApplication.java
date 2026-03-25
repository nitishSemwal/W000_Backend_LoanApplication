package com.bank.loan_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserApplication {
    @Id
    private String applicationId;
    private String applicantName;
    private int age;
    private int monthlyIncome;
    private int loanAmount;
    private String applicationStatus;
}
