package com.example.capstone.controller;

import com.example.capstone.dto.StockListResponse;
import com.example.capstone.dto.*;  // ✅ Add this
import com.example.capstone.dto.StockRequest;
import com.example.capstone.dto.StockResponse;
import com.example.capstone.service.StockService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stocks")
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping
    public ResponseEntity<StockResponse> createStock(
            @Valid @RequestBody StockRequest request,
            @RequestHeader("X-User-Id") String userId,
            Authentication authentication) {
        StockResponse response = stockService.createStock(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<StockListResponse> getAllStocks(
            @RequestHeader("X-User-Id") String userId,
            Authentication authentication) {
        StockListResponse response = stockService.getAllStocks(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{stockId}")
    public ResponseEntity<StockResponse> getStock(
            @PathVariable String stockId,
            @RequestHeader("X-User-Id") String userId,
            Authentication authentication) {
        StockResponse response = stockService.getStock(stockId, userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{stockId}")
    public ResponseEntity<StockResponse> updateStock(
            @PathVariable String stockId,
            @Valid @RequestBody StockRequest request,
            @RequestHeader("X-User-Id") String userId,
            Authentication authentication) {
        StockResponse response = stockService.updateStock(stockId, request, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{stockId}")
    public ResponseEntity<Void> deleteStock(
            @PathVariable String stockId,
            @RequestHeader("X-User-Id") String userId,
            Authentication authentication) {
        stockService.deleteStock(stockId, userId);
        return ResponseEntity.noContent().build();
    }
}