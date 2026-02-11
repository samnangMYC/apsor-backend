package com.backend.apsor.configs;

import com.backend.apsor.entities.StorageProps;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
public class S3Config {

    @Bean
    S3Client s3(StorageProps p) {
        var creds = AwsBasicCredentials.create(p.accessKey(), p.secretKey());
        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(creds))
                .region(Region.of(p.region()))
                .endpointOverride(URI.create(p.endpoint()))
                .forcePathStyle(true)
                .build();
    }

    @Bean
    S3Presigner presigner(StorageProps p) {
        var creds = AwsBasicCredentials.create(p.accessKey(), p.secretKey());
        return S3Presigner.builder()
                .credentialsProvider(StaticCredentialsProvider.create(creds))
                .region(Region.of(p.region()))
                .endpointOverride(URI.create(p.endpoint()))
                .build();
    }

}
