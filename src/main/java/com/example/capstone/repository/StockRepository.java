package com.example.capstone.repository;

import com.example.capstone.model.UserStock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class StockRepository {
    private final DynamoDbClient dynamoDbClient;
    private final String tableName;

    public StockRepository(DynamoDbClient dynamoDbClient,
                          @Value("${aws.dynamodb.user-stocks-table-name}") String tableName) {
        this.dynamoDbClient = dynamoDbClient;
        this.tableName = tableName;
    }

    public void save(UserStock stock) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("userId", AttributeValue.fromS(stock.getUserId()));
        item.put("stockId", AttributeValue.fromS(stock.getStockId()));
        item.put("stockSymbol", AttributeValue.fromS(stock.getStockSymbol()));
        item.put("companyName", AttributeValue.fromS(stock.getCompanyName()));
        item.put("currentPrice", AttributeValue.fromN(String.valueOf(stock.getCurrentPrice())));
        item.put("quantity", AttributeValue.fromN(String.valueOf(stock.getQuantity())));
        item.put("totalValue", AttributeValue.fromN(String.valueOf(stock.getTotalValue())));
        item.put("createdAt", AttributeValue.fromS(stock.getCreatedAt()));
        item.put("updatedAt", AttributeValue.fromS(stock.getUpdatedAt()));

        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build();
        dynamoDbClient.putItem(request);
    }

    public Optional<UserStock> findByCompositeKey(String userId, String stockId) {
        GetItemRequest request = GetItemRequest.builder()
                .tableName(tableName)
                .key(Map.of(
                    "userId", AttributeValue.fromS(userId),
                    "stockId", AttributeValue.fromS(stockId)
                ))
                .build();

        GetItemResponse response = dynamoDbClient.getItem(request);
        if (!response.hasItem()) {
            return Optional.empty();
        }

        Map<String, AttributeValue> item = response.item();
        return Optional.of(mapToUserStock(item));
    }

    public List<UserStock> findAllByUserId(String userId) {
        QueryRequest request = QueryRequest.builder()
                .tableName(tableName)
                .keyConditionExpression("userId = :userId")
                .expressionAttributeValues(Map.of(":userId", AttributeValue.fromS(userId)))
                .build();

        QueryResponse response = dynamoDbClient.query(request);
        List<UserStock> stocks = new ArrayList<>();
        for (Map<String, AttributeValue> item : response.items()) {
            stocks.add(mapToUserStock(item));
        }
        return stocks;
    }

    public void deleteByCompositeKey(String userId, String stockId) {
        DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(Map.of(
                    "userId", AttributeValue.fromS(userId),
                    "stockId", AttributeValue.fromS(stockId)
                ))
                .build();
        dynamoDbClient.deleteItem(request);
    }

    public void update(UserStock stock) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("userId", AttributeValue.fromS(stock.getUserId()));
        item.put("stockId", AttributeValue.fromS(stock.getStockId()));
        item.put("stockSymbol", AttributeValue.fromS(stock.getStockSymbol()));
        item.put("companyName", AttributeValue.fromS(stock.getCompanyName()));
        item.put("currentPrice", AttributeValue.fromN(String.valueOf(stock.getCurrentPrice())));
        item.put("quantity", AttributeValue.fromN(String.valueOf(stock.getQuantity())));
        item.put("totalValue", AttributeValue.fromN(String.valueOf(stock.getTotalValue())));
        item.put("updatedAt", AttributeValue.fromS(stock.getUpdatedAt()));

        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build();
        dynamoDbClient.putItem(request);
    }

    private UserStock mapToUserStock(Map<String, AttributeValue> item) {
        UserStock stock = new UserStock();
        stock.setUserId(getStringOrDefault(item, "userId"));
        stock.setStockId(getStringOrDefault(item, "stockId"));
        stock.setStockSymbol(getStringOrDefault(item, "stockSymbol"));
        stock.setCompanyName(getStringOrDefault(item, "companyName"));
        
        stock.setCurrentPrice(getDoubleOrDefault(item, "currentPrice", 0.0));
        stock.setQuantity(getIntOrDefault(item, "quantity", 0));
        stock.setTotalValue(getDoubleOrDefault(item, "totalValue", 0.0));
        stock.setCreatedAt(getStringOrDefault(item, "createdAt"));
        stock.setUpdatedAt(getStringOrDefault(item, "updatedAt"));
        
        return stock;
    }

    private String getStringOrDefault(Map<String, AttributeValue> item, String key) {
        AttributeValue value = item.get(key);
        return value != null && value.s() != null ? value.s() : "";
    }

    private double getDoubleOrDefault(Map<String, AttributeValue> item, String key, double defaultValue) {
        AttributeValue value = item.get(key);
        if (value != null && value.n() != null) {
            try {
                return Double.parseDouble(value.n());
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    private int getIntOrDefault(Map<String, AttributeValue> item, String key, int defaultValue) {
        AttributeValue value = item.get(key);
        if (value != null && value.n() != null) {
            try {
                return Integer.parseInt(value.n());
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
}