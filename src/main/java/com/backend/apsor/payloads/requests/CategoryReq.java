package com.backend.apsor.payloads.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryReq {
    @NotBlank
    @Size(min = 2, max = 120)
    private String name;

    @Size(min = 2, max = 140)
    private String slug;

    @Size(max = 500)
    private String description;

    @NotNull
    private Integer sortOrder;

}
