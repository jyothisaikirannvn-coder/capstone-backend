package com.example.capstone.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StockRequest {
    @NotBlank(message = "Stock symbol is required")
    private String stockSymbol;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotNull(message = "Current price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double currentPrice;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    // Default constructor
    public StockRequest() {}

    // Getters and Setters
    public String getStockSymbol() { return stockSymbol; }
    public void setStockSymbol(String stockSymbol) { this.stockSymbol = stockSymbol; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public Double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(Double currentPrice) { this.currentPrice = currentPrice; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}