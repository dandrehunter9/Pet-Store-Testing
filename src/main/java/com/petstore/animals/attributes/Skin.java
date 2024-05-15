package com.petstore.animals.attributes;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * The different types of skin
 */
public enum Skin {

    FUR,

    HAIR,

    FEATHERS,

    SCALES,

    UNKNOWN;
    private String skinType;

    public String skinType() {
        return this.skinType;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Skin fromText(String text) {
        for (Skin skin : Skin.values()) {
            if (skin.name().equalsIgnoreCase(text)) {
                return skin;
            }
        }
        throw new IllegalArgumentException();
    }

    public String toString()
    {
        return name();
    }
}
