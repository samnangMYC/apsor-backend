package com.backend.apsor.payloads.requests;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubCategoryUpdateReq {

    @NotEmpty(message = "name is required")
    private Map<String, @NotBlank(message = "name value cannot be blank") String> name;

    private Map<String, @NotBlank(message = "description value cannot be blank") String> description;

    @NotNull(message = "Sort order is required")
    @Min(value = 0, message = "Sort order must be 0 or greater")
    private Integer sortOrder;
}
