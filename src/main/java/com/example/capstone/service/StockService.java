package com.example.capstone.service;

import com.example.capstone.dto.StockListResponse;
import com.example.capstone.dto.StockRequest;
import com.example.capstone.dto.StockResponse;
import com.example.capstone.model.UserStock;
import com.example.capstone.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public StockResponse createStock(StockRequest request, String userId) {
        String stockId = UUID.randomUUID().toString();
        UserStock stock = new UserStock(
            userId, stockId, request.getStockSymbol(), 
            request.getCompanyName(), request.getCurrentPrice(), 
            request.getQuantity()
        );
        stockRepository.save(stock);
        return mapToResponse(stock);
    }

    public StockResponse updateStock(String stockId, StockRequest request, String userId) {
        // ✅ FIXED: Check existence first
        if (!stockRepository.findByCompositeKey(userId, stockId).isPresent()) {
            throw new RuntimeException("Stock not found for user");
        }
        
        UserStock existing = new UserStock();
        existing.setUserId(userId);
        existing.setStockId(stockId);
        existing.setStockSymbol(request.getStockSymbol());
        existing.setCompanyName(request.getCompanyName());
        existing.setCurrentPrice(request.getCurrentPrice());
        existing.setQuantity(request.getQuantity());
        existing.setCreatedAt(java.time.Instant.now().toString()); // Will be updated in DB
        existing.setUpdatedAt(java.time.Instant.now().toString());
        
        stockRepository.update(existing);  // Uses PutItem (overwrite)
        return mapToResponse(existing);
    }

    public StockListResponse getAllStocks(String userId) {
        List<UserStock> stocks = stockRepository.findAllByUserId(userId);
        List<StockResponse> responses = stocks.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
        return new StockListResponse(responses);
    }

    public StockResponse getStock(String stockId, String userId) {
        // ✅ FIXED: Safe Optional handling
        UserStock stock = stockRepository.findByCompositeKey(userId, stockId)
            .orElseThrow(() -> new RuntimeException("Stock not found"));
        return mapToResponse(stock);
    }

    public void deleteStock(String stockId, String userId) {
        // ✅ FIXED: Safe deletion - no Optional.map crash
        if (!stockRepository.findByCompositeKey(userId, stockId).isPresent()) {
            throw new RuntimeException("Stock not found for this user");
        }
        stockRepository.deleteByCompositeKey(userId, stockId);
    }

    private StockResponse mapToResponse(UserStock stock) {
        return new StockResponse(
            stock.getStockId(),
            stock.getStockSymbol(),
            stock.getCompanyName(),
            stock.getCurrentPrice(),
            stock.getQuantity(),
            stock.getTotalValue(),
            stock.getCreatedAt(),
            stock.getUpdatedAt()
        );
    }
}