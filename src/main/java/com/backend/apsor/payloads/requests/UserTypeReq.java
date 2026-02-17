package com.backend.apsor.payloads.requests;

import com.backend.apsor.enums.UserType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserTypeReq {

    @NotNull(message = "User type is required")
    private UserType userType;
}
