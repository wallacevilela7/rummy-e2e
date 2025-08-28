package tech.buildrun.rummye2e.dto;

import java.time.LocalDateTime;

public record BookResponseDto(
        Long id,
        Long roomId,
        LocalDateTime startTime,
        LocalDateTime endTime) {
}
