package com.backend.apsor.payloads.dtos;

import com.backend.apsor.enums.MediaPurpose;
import com.backend.apsor.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryMediaDTO {
    private Long categoryMediaId;
    private MediaPurpose mediaPurpose;
    private Integer sortOrder;
    private MediaAssetDTO media;
    private Status status;
}
