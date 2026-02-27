package com.backend.apsor.payloads.dtos;

import com.backend.apsor.enums.Status;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.NamedEntityGraph;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

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

    private Map<String, String> name;

    private Map<String, String> description;

    private String slug;

    private Integer sortOrder;

    private Status status;
}
