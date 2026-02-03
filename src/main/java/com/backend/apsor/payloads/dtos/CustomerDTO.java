package com.backend.apsor.payloads.dtos;

import com.backend.apsor.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    private Long id;
    private Long userId;
    private LocalDate dob;
    private Gender gender;
    private String preferredLanguage;
    private String bio;
    private Boolean onboardingCompleted;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
}
