package com.backend.apsor.service.validation;

import com.backend.apsor.enums.ApiErrorCode;
import com.backend.apsor.enums.Status;
import com.backend.apsor.exceptions.ApiException;
import org.springframework.stereotype.Component;

@Component
public class StatusValidator {
    public void requireActive(Status status, ApiErrorCode code, String entityName, Object id) {
        if (status != Status.ACTIVE) {
            throw ApiException.conflict(code, "%s is not active: %s", entityName, id);
        }
    }
}
