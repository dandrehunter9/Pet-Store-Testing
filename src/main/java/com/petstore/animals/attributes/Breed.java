package com.petstore.animals.attributes;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Breed is an enum for Dogs, Cats, Birds, and Snakes that can be found in the pet store
 */
public enum Breed {
    POODLE, MALTESE, GOLDEN_DOODLE, GREY_HOUND, GERMAN_SHEPHERD, //DOGS
    SIAMESE, MAINE, BIRMAN, RAGDOLL, BURMESE, SPHYNX, //CATS
    PARROT, FINCHE, LOVEBIRD, HAWK, COCKATIEL, BALTIMORE_ORIOLE, //Birds
    BALL_PYTHON, BURMESE_PYTHON, COPPERHEAD, GRASS, MILK, CORN, CORAL, //SNAKES
    UNKNOWN;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Breed fromText(String text) {
        for (Breed breed : Breed.values()) {
            if (breed.name().equalsIgnoreCase(text)) {
                return breed;
            }
        }
        throw new IllegalArgumentException();
    }

    public String toString()
    {
        return name();
    }
}
