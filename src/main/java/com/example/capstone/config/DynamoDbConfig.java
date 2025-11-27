package com.example.capstone.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Configuration
public class DynamoDbConfig {

    @Value("${aws.region:us-east-1}")
    private String region;

    @Value("${aws.dynamodb.endpoint:#{null}}")
    private String endpoint;

    @Value("${aws.access-key-id:#{null}}")
    private String accessKey;

    @Value("${aws.secret-access-key:#{null}}")
    private String secretKey;

    @Bean
    public DynamoDbClient dynamoDbClient() {
        var builder = DynamoDbClient.builder()
                .region(Region.of(region));

        // ✅ PRIORITY 1: Use explicit access keys if provided (Render env vars)
        if (accessKey != null && !accessKey.isBlank() && secretKey != null && !secretKey.isBlank()) {
            System.out.println("✅ Using explicit AWS credentials");
            builder.credentialsProvider(
                    StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey.trim(), secretKey.trim()))
            );
        } 
        // ✅ PRIORITY 2: Fallback to DefaultCredentialsProvider (IAM roles)
        else {
            System.out.println("✅ Using DefaultCredentialsProvider (IAM roles)");
            builder.credentialsProvider(DefaultCredentialsProvider.create());
        }

        // ✅ Local/Override endpoint support
        if (endpoint != null && !endpoint.isBlank()) {
            builder.endpointOverride(URI.create(endpoint.trim()));
            System.out.println("🔧 DynamoDB endpoint override: " + endpoint.trim());
        } else {
            System.out.println("🌐 Using AWS DynamoDB: " + region);
        }

        DynamoDbClient client = builder.build();
        System.out.println("✅ DynamoDB Client initialized successfully");
        return client;
    }
}