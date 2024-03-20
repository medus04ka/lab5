package ru.ifmo.se.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * The type Car.
 */
@Data
@Builder
@AllArgsConstructor
public class Car {
    private String name;
}
