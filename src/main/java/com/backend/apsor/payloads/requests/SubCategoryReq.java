package com.backend.apsor.payloads.requests;

import com.backend.apsor.enums.Status;
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
public class SubCategoryReq {

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotEmpty(message = "name is required")
    private Map<String, @NotBlank(message = "name value cannot be blank") String> name;

    private Map<String, @NotBlank(message = "description value cannot be blank") String> description;

    @NotNull(message = "sortOrder is required")
    @Min(value = 0, message = "sortOrder must be at least 0")
    private Integer sortOrder;


}
