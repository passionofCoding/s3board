package com.example.s3ex.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Uploader {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;
    @Value("${cloud.aws.region.static}")
    private String region;

    private S3Client s3Client;

    @PostConstruct
    public void initializeS3() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    public String uploadFile(String dirName, java.io.InputStream inputStream, String originalFileName) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String uploadFileName = dirName + "/" + uuid + "_" + originalFileName;

        File tempFile = convert(inputStream, originalFileName);
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(uploadFileName)
                        .build(),
                Paths.get(tempFile.getAbsolutePath()));
        tempFile.delete();
        return s3Client.utilities().getUrl(builder -> builder.bucket(bucket).key(uploadFileName)).toString();
    }

    private File convert(java.io.InputStream inputStream, String fileName) throws IOException {
        File file = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(inputStream.readAllBytes());
        }
        return file;
    }
}