# Loan Application Backend Service – Development Notes

## Overall Approach

The goal of this project was to design a clean, maintainable backend service that evaluates loan applications based on business rules and generates a single loan offer if eligible.

I followed a layered architecture approach to separate responsibilities:

Controller → handles REST requests and responses  
Service → contains business logic and orchestration  
DTO → request/response contracts  
Repository → persistence of loan decisions  
Util → financial calculations and reusable logic  
Constants → business rule values

The service processes a loan application in the following sequence:

1. Validate input data
2. Classify risk band based on credit score
3. Calculate final interest rate
4. Calculate EMI using the standard EMI formula
5. Apply eligibility rules
6. Generate offer if eligible
7. Store decision for audit
8. Return structured response

The design focuses on readability, separation of concerns, and business rule clarity.

---

## Key Design Decisions

### 1. Layered Architecture
I used a layered architecture to ensure clear separation of concerns and maintainability. Business logic is isolated in the service layer while the controller remains thin.

### 2. DTO-based API contract
DTOs were used instead of exposing persistence entities directly. This helps maintain API stability even if internal models change.

### 3. Builder Pattern for Responses
Lombok builder pattern was used to construct responses to improve readability and avoid complex constructors.

### 4. BigDecimal for financial calculations
All financial calculations (EMI, interest, total payable) use BigDecimal instead of float/double to maintain financial precision and avoid rounding errors.

### 5. Centralized business constants
Business limits like credit score thresholds, interest rates, and EMI limits were placed in constants to avoid magic numbers and improve maintainability.

### 6. Utility class for EMI calculation
EMI calculation was separated into a utility class to keep financial calculations reusable and independent from service orchestration.

### 7. Meaningful rejection reasons
Loan rejection reasons are explicitly captured and returned to improve transparency and auditability.

---

## Trade-offs Considered

### 1. Single service vs multiple services
I chose to keep business logic in one service class instead of splitting into multiple smaller services to keep the implementation simple and aligned with assignment scope. In a production system, I would separate eligibility, risk, and interest logic into dedicated services.

### 2. H2 vs production database
A simple in-memory database approach was preferred instead of configuring a production-grade database because persistence was only required for audit demonstration.

### 3. Minimal validation framework usage
Basic validation was implemented instead of introducing complex validation frameworks to keep the solution lightweight.

---

## Assumptions Made

The following assumptions were made based on the assignment:

• Interest rates are fixed and not dynamically configurable  
• Only one loan offer is generated per request  
• Loan tenure is fixed as provided in the request  
• Credit score provided is assumed correct (no external verification)
• No authentication or authorization required
• No prepayment or foreclosure logic considered

---

## Improvements With More Time

If more time were available, the following improvements would be implemented:

### Architecture Improvements
• Split business logic into RiskService, EligibilityService, InterestService  
• Introduce mapper layer for DTO conversion  
• Add strategy pattern for risk calculation

### Technical Improvements
• Add comprehensive unit test coverage
• Add integration tests
• Add request validation annotations
• Add structured logging with correlation IDs
• Add Swagger/OpenAPI documentation
• Containerize using Docker

### Business Improvements
• Configurable interest rates via configuration
• External credit score validation API
• Dynamic risk models
• Rule engine for eligibility evaluation

### Production Readiness
• Add global exception handling improvements
• Add monitoring hooks
• Add audit history tracking
• Add request tracing
• Add retry mechanisms

### Performance Improvements
• Add caching for risk classification rules
• Optimize EMI calculations
---

## Summary

The solution focuses on clean code practices, business rule clarity, and maintainable structure rather than over-engineering. The design aims to demonstrate backend development fundamentals including:

• Clean architecture
• Financial calculation correctness
• Business rule implementation
• Code readability
• Testability

With additional time, the system could be extended toward a more production-ready microservice architecture.