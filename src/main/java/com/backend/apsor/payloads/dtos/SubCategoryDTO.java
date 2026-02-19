package com.backend.apsor.payloads.dtos;

import com.backend.apsor.enums.Status;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.NamedEntityGraph;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@NamedEntityGraph
@AllArgsConstructor
@Builder
@Jacksonized
@JsonPropertyOrder({
        "id",
        "categoryId",
        "name",
        "description",
        "sortOrder",
        "status"
})
public class SubCategoryDTO {

    private Long id;

    private Long categoryId;

    private String name;

    private String description;

    private Integer sortOrder;

    private Status status;
}
