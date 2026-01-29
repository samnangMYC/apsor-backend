package com.backend.apsor.payloads.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {
    private Instant timestamp;
    private int status;
    private String code;
    private String message;
    private String path;
    private String traceId;
    private List<FieldViolation> violations;

    @Getter
    @Builder
    public static class FieldViolation {
        private String field;
        private String message;
    }
}
