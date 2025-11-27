package com.example.capstone.model;

public class UserStock {
    private String userId;
    private String stockId;
    private String stockSymbol;
    private String companyName;
    private double currentPrice;
    private int quantity;
    private double totalValue;
    private String createdAt;
    private String updatedAt;

    // Default constructor
    public UserStock() {}

    // Constructor for new stock
    public UserStock(String userId, String stockId, String stockSymbol, String companyName, 
                    double currentPrice, int quantity) {
        this.userId = userId;
        this.stockId = stockId;
        this.stockSymbol = stockSymbol;
        this.companyName = companyName;
        this.currentPrice = currentPrice;
        this.quantity = quantity;
        this.totalValue = currentPrice * quantity;
        this.createdAt = this.updatedAt = getCurrentTimestamp();
    }

    private String getCurrentTimestamp() {
        return java.time.Instant.now().toString();
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getStockId() { return stockId; }
    public void setStockId(String stockId) { this.stockId = stockId; }

    public String getStockSymbol() { return stockSymbol; }
    public void setStockSymbol(String stockSymbol) { this.stockSymbol = stockSymbol; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(double currentPrice) { 
        this.currentPrice = currentPrice; 
        this.totalValue = currentPrice * quantity;
        this.updatedAt = getCurrentTimestamp();
    }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { 
        this.quantity = quantity; 
        this.totalValue = currentPrice * quantity;
        this.updatedAt = getCurrentTimestamp();
    }

    public double getTotalValue() { return totalValue; }
    public void setTotalValue(double totalValue) { this.totalValue = totalValue; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}