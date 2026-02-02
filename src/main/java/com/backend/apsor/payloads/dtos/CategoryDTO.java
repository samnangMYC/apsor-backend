package com.backend.apsor.payloads.dtos;

import com.backend.apsor.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private Integer sortOrder;
    private Status status;
    private Instant createdAt;
    private Instant updatedAt;
}
