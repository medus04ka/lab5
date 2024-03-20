package ru.ifmo.se.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * The type Coordinates.
 */
@Data
@Builder
@AllArgsConstructor
public class Coordinates {
    @NotNull
    @Min(-534)
    private Double x;
    private double y;
}



