package com.petstore;


import com.petstore.animals.attributes.Breed;
import com.petstore.animals.attributes.PetType;

/**
 *
 */
public interface PetImpl {

    Breed breed = Breed.UNKNOWN;
    PetType petType = PetType.UNKNOWN;
    /**
     * Get the pet breed type
     * @return {@link Breed}
     */
    Breed getBreed();

    PetType getPetType();
    void setPetType(PetType petType);
}
