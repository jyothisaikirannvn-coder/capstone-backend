package com.example.capstone.dto;

public class StockResponse {
    private String stockId;
    private String stockSymbol;
    private String companyName;
    private double currentPrice;
    private int quantity;
    private double totalValue;
    private String createdAt;
    private String updatedAt;

    // Constructor
    public StockResponse(String stockId, String stockSymbol, String companyName, 
                        double currentPrice, int quantity, double totalValue,
                        String createdAt, String updatedAt) {
        this.stockId = stockId;
        this.stockSymbol = stockSymbol;
        this.companyName = companyName;
        this.currentPrice = currentPrice;
        this.quantity = quantity;
        this.totalValue = totalValue;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Default constructor
    public StockResponse() {}

    // Getters only
    public String getStockId() { return stockId; }
    public String getStockSymbol() { return stockSymbol; }
    public String getCompanyName() { return companyName; }
    public double getCurrentPrice() { return currentPrice; }
    public int getQuantity() { return quantity; }
    public double getTotalValue() { return totalValue; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
}