package com.backend.apsor.payloads.requests;

import com.backend.apsor.enums.UserStatus;
import com.backend.apsor.enums.UserType;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateUserReq extends UpdateMeReq {
    private UserType userType;
    private UserStatus status;

}