package ru.ifmo.se.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HumanBeing implements Comparable<HumanBeing> {
    @Min(0)
    private long id;
    @NotBlank
    private String name;
    @NotNull
    private Coordinates coordinates;
    @NotNull
    private java.time.LocalDate creationDate;
    @NotNull
    private Boolean realHero;
    private boolean hasToothpick;
    @Max(659)
    private double impactSpeed;
    private WeaponType weaponType;
    @NotNull
    private Mood mood;
    private Car car;

    public HumanBeing(String name, Coordinates coordinates, Boolean realHero, boolean hasToothpick, double impactSpeed, WeaponType weaponType, Mood mood, Car car) {
        this.name = name;
        this.coordinates = coordinates;
        this.realHero = realHero;
        this.hasToothpick = hasToothpick;
        this.impactSpeed = impactSpeed;
        this.weaponType = weaponType;
        this.mood = mood;
        this.car = car;
    }

    @Override
    public int compareTo(HumanBeing o) {
        return Double.compare(this.impactSpeed, o.getImpactSpeed());
    }
}
