package com.backend.apsor.entities;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.storage.s3")
public record StorageProps(
        String endpoint,
        String accessKey,
        String secretKey,
        String region,
        String bucket,
        String publicBaseUrl
) {}
