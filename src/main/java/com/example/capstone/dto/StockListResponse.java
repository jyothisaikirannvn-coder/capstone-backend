package com.example.capstone.dto;

import java.util.List;

public class StockListResponse {
    private List<StockResponse> stocks;
    private double totalPortfolioValue;
    private int totalStocks;

    // Constructor
    public StockListResponse(List<StockResponse> stocks) {
        this.stocks = stocks;
        this.totalPortfolioValue = stocks.stream()
                .mapToDouble(StockResponse::getTotalValue)
                .sum();
        this.totalStocks = stocks != null ? stocks.size() : 0;
    }

    // Default constructor for JSON deserialization
    public StockListResponse() {}

    // Getters only (Response DTO)
    public List<StockResponse> getStocks() { 
        return stocks; 
    }

    public double getTotalPortfolioValue() { 
        return totalPortfolioValue; 
    }

    public int getTotalStocks() { 
        return totalStocks; 
    }
}