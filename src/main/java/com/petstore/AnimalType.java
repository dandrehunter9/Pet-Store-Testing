package com.petstore;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AnimalType {
    WILD, //Animal in the wild which is not a pet sold in a pet store nor treated as a farm animal
    DOMESTIC, //Pet Store
    FARM,  //Farm animal related
    UNKNOWN;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static AnimalType fromText(String text) {
        for (AnimalType animalType : AnimalType.values()) {
            if (animalType.name().equalsIgnoreCase(text)) {
                return animalType;
            }
        }
        throw new IllegalArgumentException();
    }

    public String toString()
    {
        return name();
    }
}
