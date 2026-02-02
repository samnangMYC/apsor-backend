package com.backend.apsor.entities;

public record ApiError(
        long timestamp,
        int status,
        String error,
        String code,
        String path,
        String message
) {
}
