package com.example.aws_demo.s3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class S3Service {
    private static final Logger LOG = LoggerFactory.getLogger(S3Service.class);

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Autowired
    private S3Connection connection;

    public void createBucket(String bucketName) {

        if (bucketExists(bucketName))
            return;

        CreateBucketRequest createReq = CreateBucketRequest.builder()
                .bucket(bucketName)
                .build();

        CreateBucketResponse bucket = connection.getS3client().createBucket(createReq);
        LOG.debug("Bucket Created: {}", bucket);
    }

    public List<String> bucketNames() {
        return connection.getS3client()
                .listBuckets()
                .buckets().stream()
                .map(Bucket::name)
                .collect(toList());
    }

    private boolean bucketExists(String bucketName) {
        boolean bucketAlreadyExists = this.bucketNames()
                .contains(bucketName);

        if(bucketAlreadyExists) {
            LOG.info("Bucket name is not available."
                    + " Try again with a different Bucket name.");
            return true;
        }

        return false;
    }

    public void uploadOnBucket(String key, String fileName) {

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        PutObjectResponse putObjectResponse = connection.getS3client()
                .putObject(request, Path.of(new File(fileName).toURI()));
        LOG.debug("File Uploaded: {}", putObjectResponse);

    }

    public byte[] downloadFromBucket(String key) {
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        ResponseBytes<GetObjectResponse> responseResponseBytes = connection
                .getS3client().getObjectAsBytes(objectRequest);

        return responseResponseBytes.asByteArray();
    }
}
