package org.example.eventprocessor;

public record Event(
        Integer id,
        Integer timestamp,
        String payload,
        Integer checksum
) {
}