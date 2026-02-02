package com.backend.apsor.controller;

import com.backend.apsor.entities.ApiError;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
public class ApiErrorController implements ErrorController {

    @RequestMapping("${server.error.path:/error}")
    public ResponseEntity<ApiError> handleError(HttpServletRequest request) {

        Object statusObj = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int status = statusObj != null ? Integer.parseInt(statusObj.toString()) : 500;

        String path = String.valueOf(request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));
        Object msgObj = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        String message = msgObj != null ? msgObj.toString() : "Unexpected error";

        String error = HttpStatus.valueOf(status).getReasonPhrase();
        String code = switch (status) {
            case 404 -> "ROUTE_NOT_FOUND";
            case 405 -> "METHOD_NOT_ALLOWED";
            case 400 -> "BAD_REQUEST";
            default -> "INTERNAL_ERROR";
        };

        ApiError body = new ApiError(
                System.currentTimeMillis(),
                status,
                error,
                code,
                path,
                message
        );

        return ResponseEntity.status(status).body(body);
    }
}
