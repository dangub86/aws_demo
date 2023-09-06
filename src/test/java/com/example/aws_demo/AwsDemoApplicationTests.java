package com.example.aws_demo;

import com.example.aws_demo.s3.S3Connection;
import com.example.aws_demo.s3.S3Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.*;
import java.util.List;

@SpringBootTest
class AwsDemoApplicationTests {

    @Autowired
    private S3Connection conn;
    @Autowired
    private S3Service s3Service;

    @Test
    void contextLoads() {
    }

    @Test
    void getAwsCredentials() {
        AwsCredentials connectionCredentials = conn.getConnectionCredentials();
        System.out.println("Conn: " + connectionCredentials);
    }

    @Test
    void getS3Client() {
        S3Client s3client = conn.getS3client();
        System.out.println("S3 Client: " + s3client);
    }

    @Test
    void createBucket() {
        s3Service.createBucket("test-bucket-aaaa-74694");
    }

    @Test
    void listBuckets() {
        List<String> bucketNames =
                s3Service.bucketNames();
        System.out.println("Bucket Names: " + bucketNames);
    }

    @Test
    void uploadOnBucket() {
        s3Service.uploadOnBucket(
                "key-a1",
                "src/main/resources/fileToUpload.txt"
        );
    }

    @Test
    void downloadFromBucket() {
        byte[] bytes = s3Service.downloadFromBucket("key-a1");

        File fileDownloaded = new File("src/main/resources/fileDownloaded.txt");
        try(OutputStream os = new FileOutputStream(fileDownloaded)) {
            os.write(bytes);
            System.out.println("Successfully obtained bytes from an S3 object");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
