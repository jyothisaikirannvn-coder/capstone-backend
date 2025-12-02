// src/main/java/com/example/capstone/controller/ProfileController.java
package com.example.capstone.controller;

import com.example.capstone.model.UserProfile;
import com.example.capstone.model.UserStock;
import com.example.capstone.repository.StockRepository;
import com.example.capstone.repository.UserProfileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ProfileController {

    private final UserProfileRepository userProfileRepository;
    private final StockRepository stockRepository;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    public ProfileController(UserProfileRepository userProfileRepository,
                             StockRepository stockRepository) {
        this.userProfileRepository = userProfileRepository;
        this.stockRepository = stockRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(Authentication auth) {
        String email = (String) auth.getPrincipal(); // ← This is email from JWT

        UserProfile profile = userProfileRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User profile not found"));

        // Now get stocks using real userId
        List<UserStock> stocks = stockRepository.findAllByUserId(profile.getUserId());

        List<Map<String, String>> recentActivity = stocks.stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(5)
                .map(stock -> Map.of(
                        "action", "Bought " + stock.getQuantity() + " shares of " + stock.getStockSymbol(),
                        "date", formatDate(stock.getCreatedAt())
                ))
                .toList();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("fullName", profile.getFirstName() + " " + profile.getLastName());
        response.put("username", profile.getUsername());
        response.put("email", profile.getEmail());
        response.put("phone", profile.getPhone());
        response.put("experience", profile.getExperience());
        response.put("riskAppetite", profile.getRiskAppetite());
        response.put("investmentGoal", profile.getInvestmentGoal());
        response.put("investorSince", profile.getInvestorSince());
        response.put("recentActivity", recentActivity);

        return ResponseEntity.ok(response);
    }

    private String formatDate(String isoDate) {
        try {
            return Instant.parse(isoDate)
                    .atZone(ZoneId.of("UTC"))
                    .format(DATE_FMT);
        } catch (Exception e) {
            return "Recently";
        }
    }
}