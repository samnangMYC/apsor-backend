package com.backend.apsor.payloads.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceAvailabilityDTO {
    private Long id;

    private Long serviceId;

    private String timezone;

    private Integer openDaysMask;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime;

    private Integer slotDurationMinutes;

    private Integer capacityPerSlot;

    private Boolean isDefault;

    private String status; // "ACTIVE" / "INACTIVE"

    private Instant createdAt;

    private Instant updatedAt;
}
