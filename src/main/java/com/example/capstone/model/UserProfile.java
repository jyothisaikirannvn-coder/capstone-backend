package com.example.capstone.model;

public class UserProfile {

    private String userId;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phone;
    private String experience;
    private String riskAppetite;
    private String investmentGoal;
    private String createdAt;
    private String investorSince;

    // Default constructor (needed for DynamoDB mapping)
    public UserProfile() {}

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }

    public String getRiskAppetite() { return riskAppetite; }
    public void setRiskAppetite(String riskAppetite) { this.riskAppetite = riskAppetite; }

    public String getInvestmentGoal() { return investmentGoal; }
    public void setInvestmentGoal(String investmentGoal) { this.investmentGoal = investmentGoal; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getInvestorSince() { return investorSince; }
    public void setInvestorSince(String investorSince) { this.investorSince = investorSince; }
}