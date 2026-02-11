package com.backend.apsor.payloads.dtos;

import com.backend.apsor.entities.MediaAsset;
import com.backend.apsor.enums.MediaPurpose;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderMediaDTO {

    private Long providerMediaId;

    private MediaPurpose purpose;
    private int sortOrder;

    private MediaAsset media;
}