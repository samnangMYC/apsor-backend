package com.backend.apsor.payloads.dtos;

import com.backend.apsor.entities.MediaAsset;
import com.backend.apsor.enums.MediaPurpose;
import com.backend.apsor.enums.Status;
import lombok.Data;

@Data
public class ServiceMediaDTO {
    private Long serviceMediaId;
    private MediaPurpose purpose;
    private Integer sortOrder;
    private MediaAssetDTO media;
    private Status status;
}
