package com.petstore.animals.attributes;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Pet Gender
 */
public enum Gender {
    MALE, FEMALE, UNKNOWN;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Gender fromText(String text) {
        for (Gender gender : Gender.values()) {
            if (gender.name().equalsIgnoreCase(text)) {
                return gender;
            }
        }
        throw new IllegalArgumentException();
    }

    public String toString()
    {
        return name();
    }
}
