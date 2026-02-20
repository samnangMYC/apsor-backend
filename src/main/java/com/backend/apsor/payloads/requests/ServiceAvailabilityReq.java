package com.backend.apsor.payloads.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceAvailabilityReq {

    private Long serviceId;

    @NotNull
    @Min(1) @Max(127)
    private Integer openDaysMask;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime;

    @NotNull
    @Min(5) @Max(1440)
    private Integer slotDurationMinutes;

    @NotNull
    @Min(1)
    private Integer capacityPerSlot;


}
