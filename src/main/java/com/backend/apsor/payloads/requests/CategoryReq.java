package com.backend.apsor.payloads.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryReq {

    @NotEmpty(message = "name is required")
    private Map<String, String> name;

    // optional: if null -> auto-generate from name
    @Size(max = 140)
    private String slug;

    private Map<String, String> description;

    private Integer sortOrder;

}
