package tech.buildrun.rummye2e.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record BookRequestDto(
        Long roomId,
        @JsonFormat(shape = JsonFormat.Shape.STRING) LocalDateTime startTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING) LocalDateTime endTime) {
}
