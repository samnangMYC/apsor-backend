package com.backend.apsor.payloads.dtos;

import com.backend.apsor.enums.Status;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;

@Data
@Builder
@Jacksonized
@JsonPropertyOrder({
        "id",
        "name",
        "slug",
        "description",
        "sortOrder",
        "status",
        "createdAt",
        "updatedAt"
})
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
