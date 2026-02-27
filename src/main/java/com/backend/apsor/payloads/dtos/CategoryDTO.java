package com.backend.apsor.payloads.dtos;

import com.backend.apsor.enums.Status;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;
import java.util.Map;

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
    private Map<String, String> name;
    private Map<String, String> description;
    private String slug;
    private String imageUrl;
    private Integer sortOrder;
    private Status status;
    private Instant createdAt;
    private Instant updatedAt;
}
