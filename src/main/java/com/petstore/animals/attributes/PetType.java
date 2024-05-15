package com.petstore.animals.attributes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The different types of PETS that are available
 */

public enum PetType  {
    @JsonProperty("BIRD") BIRD("BIRD", "Bark"),
    @JsonProperty("CAT") CAT("CAT", "Meow"),
    @JsonProperty("DOG") DOG("DOG", "Bark"),
    @JsonProperty("SNAKE") SNAKE("CAT", "Psss"),
    @JsonProperty("UNKNOWN") UNKNOWN("UNKNOWN", "Bark");


    public final String speak;
    public final String name;
    PetType(String name, String speak) {
        this.name = name;
        this.speak = speak;
    }
    /*public PetType getPetTypeByString(String name)
    {
        return PetType.valueOf()
    }*/

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PetType fromText(String text) {
        for (PetType petType : PetType.values()) {
            if (petType.getName().equalsIgnoreCase(text)) {
                return petType;
            }
        }
        throw new IllegalArgumentException();
    }

    public String getName()
    {
        return this.name;
    }

    public String toString()
    {
        return this.name;
    }
}
