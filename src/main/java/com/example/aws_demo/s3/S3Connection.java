package com.example.aws_demo.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

@Configuration
public class S3Connection {

    private final String key;
    private final String secret;

    public S3Connection(
            @Value("${aws.connection.key}") String key,
            @Value("${aws.connection.secret}") String secret
    ) {
        this.key = key;
        this.secret = secret;
    }

    public AwsCredentials getConnectionCredentials() {
        return AwsBasicCredentials.create(key, secret);
    }

    public S3Client getS3client() {

        return S3Client.builder()
                .credentialsProvider(this::getConnectionCredentials)
                .region(Region.EU_WEST_2)
                .build();

    }

}
