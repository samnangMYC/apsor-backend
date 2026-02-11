package com.backend.apsor.payloads.dtos;

import com.backend.apsor.enums.MediaPurpose;
import com.backend.apsor.enums.MediaType;
import com.backend.apsor.enums.MediaVisibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MediaAssetDTO {
    private Long id;

    private MediaType mediaType;
    private MediaVisibility visibility;

    private String bucket;
    private String objectKey;

    private String contentType;
    private Long sizeBytes;
    private String etag;
    private Instant createdAt;
    private Instant updatedAt;
}
