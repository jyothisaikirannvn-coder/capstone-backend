// src/main/java/com/example/capstone/repository/UserProfileRepository.java
package com.example.capstone.repository;

import com.example.capstone.model.UserProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserProfileRepository {

    private final DynamoDbClient dynamoDbClient;
    private final String tableName;
    private static final DateTimeFormatter MONTH_YEAR = DateTimeFormatter.ofPattern("MMM yyyy");

    public UserProfileRepository(DynamoDbClient dynamoDbClient,
            @Value("${REGISTER_USERS_TABLE_NAME}") String tableName) {
    		this.dynamoDbClient = dynamoDbClient;
    		this.tableName = tableName;
}

    // This method name must be findByEmail (not findByUserId)
    public Optional<UserProfile> findByEmail(String email) {
        QueryRequest request = QueryRequest.builder()
                .tableName(tableName)
                .indexName("EmailIndex")  // Your existing GSI
                .keyConditionExpression("email = :emailVal")
                .expressionAttributeValues(Map.of(":emailVal", AttributeValue.fromS(email)))
                .limit(1)
                .build();

        QueryResponse response = dynamoDbClient.query(request);
        if (response.items().isEmpty()) {
            return Optional.empty();
        }

        Map<String, AttributeValue> item = response.items().get(0);
        UserProfile profile = new UserProfile();

        profile.setUserId(get(item, "userId"));
        profile.setFirstName(get(item, "firstName"));
        profile.setLastName(get(item, "lastName"));
        profile.setUsername(get(item, "username"));
        profile.setEmail(get(item, "email"));
        profile.setPhone(get(item, "phone"));
        profile.setExperience(get(item, "experience"));
        profile.setRiskAppetite(get(item, "riskAppetite"));
        profile.setInvestmentGoal(get(item, "investmentGoal"));
        profile.setCreatedAt(get(item, "createdAt"));

        // Format investorSince
        String createdAt = profile.getCreatedAt();
        if (createdAt != null && !createdAt.isBlank()) {
            try {
                Instant instant = Instant.parse(createdAt);
                profile.setInvestorSince(MONTH_YEAR.format(instant.atZone(ZoneId.of("UTC"))));
            } catch (Exception e) {
                profile.setInvestorSince("Unknown");
            }
        } else {
            profile.setInvestorSince("Unknown");
        }

        return Optional.of(profile);
    }

    private String get(Map<String, AttributeValue> item, String key) {
        AttributeValue val = item.get(key);
        return val != null && val.s() != null ? val.s() : "";
    }
}