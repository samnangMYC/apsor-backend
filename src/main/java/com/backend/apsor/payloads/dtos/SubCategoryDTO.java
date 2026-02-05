package com.backend.apsor.payloads.dtos;

import com.backend.apsor.enums.Status;
import jakarta.persistence.NamedEntityGraph;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@NamedEntityGraph
@AllArgsConstructor
@Builder
public class SubCategoryDTO {

    private Long id;

    private Long categoryId;

    private String name;

    private String description;

    private Integer sortOrder;

    private Status status;
}
