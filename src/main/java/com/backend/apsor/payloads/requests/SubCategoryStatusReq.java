package com.backend.apsor.payloads.requests;

import com.backend.apsor.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryStatusReq {
    @NotNull(message = "Status is require")
    private Status status;
}
