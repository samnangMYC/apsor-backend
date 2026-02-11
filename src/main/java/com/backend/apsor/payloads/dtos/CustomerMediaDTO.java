package com.backend.apsor.payloads.dtos;

import com.backend.apsor.enums.MediaPurpose;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerMediaDTO {

    private Long customerMediaId;

    private MediaAssetDTO media;

    private MediaPurpose purpose;

    private int sortOrder;

}
