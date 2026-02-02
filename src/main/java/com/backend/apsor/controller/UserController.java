package com.backend.apsor.controller;

import com.backend.apsor.payloads.dtos.UserDTO;
import com.backend.apsor.payloads.requests.UpdateMeReq;
import com.backend.apsor.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Self-Management", description = "Endpoints for authenticated users to manage their own account details.")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // -----------------------
    // Me (any logged-in user)
    // -----------------------
    @Operation(summary = "Get current user's details",
            description = "Retrieves the details of the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User details retrieved successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/me")
    public ResponseEntity<UserDTO> me(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(userService.getMe(jwt));
    }

    @Operation(summary = "Update current user's details",
            description = "Partially updates the details of the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User details updated successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/me")
    public ResponseEntity<UserDTO> updateMe(@AuthenticationPrincipal Jwt jwt,
                                            @Valid @RequestBody UpdateMeReq req) {
        return ResponseEntity.ok(userService.updateMe(jwt, req));
    }


}
