package com.mangoboss.app.common.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@Getter
@RequiredArgsConstructor
public class S3Config {

    @Value("${cloud.aws.s3.public-base-url}")
    private String publicBaseUrl;

    @Value("${cloud.aws.s3.kms-key-id}")
    private String kmsKeyId;

    @Getter
    @Value("${cloud.aws.s3.private-bucket}")
    private String privateBucket;

    @Getter
    @Value("${cloud.aws.s3.public-bucket}")
    private String publicBucket;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                )
                .build();
    }
}