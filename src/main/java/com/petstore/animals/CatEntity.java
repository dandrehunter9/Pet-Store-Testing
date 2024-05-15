package com.petstore.animals;

import com.google.gson.annotations.SerializedName;
import com.petstore.AnimalType;
import com.petstore.PetEntity;
import com.petstore.animals.attributes.Breed;
import com.petstore.animals.attributes.Gender;
import com.petstore.animals.attributes.PetType;
import com.petstore.animals.attributes.Skin;
import jakarta.persistence.Entity;


import java.math.BigDecimal;

/**
 * Cat attributes
 */
@Entity
public class CatEntity extends PetEntity {

    /* Properties */



    @SerializedName(value="speaks")
    private String speaks;
    /**
     * Constructor
     * @param animalType {@link AnimalType} that defines if it domesticated or wild cat
     * @param skinType The {@link Skin} of the cat
     * @param gender The {@link Gender} of the cat
     * @param breed The type of cat {@link Breed}
     */
    public CatEntity(AnimalType animalType, Skin skinType, Gender gender, Breed breed)
    {
        this(animalType, skinType, gender, breed, new BigDecimal(0));
    }

    /**
     * Constructor
     * @param animalType {@link AnimalType} that defines if it domesticated or wild cat
     * @param skinType The {@link Skin} of the cat
     * @param gender The {@link Gender} of the cat
     * @param breed The type of cat {@link Breed}
     * @param cost The cost of the cat
     */
    public CatEntity(AnimalType animalType, Skin skinType, Gender gender, Breed breed, BigDecimal cost)
    {
        this(animalType, skinType, gender, breed, cost, 0);
    }

    /**
     * Constructor
     * @param animalType {@link AnimalType} that defines if it domesticated or wild cat
     * @param skinType The {@link Skin} of the cat
     * @param gender The {@link Gender} of the cat
     * @param breed The type of cat {@link Breed}
     * @param cost The cost of the cat
     * @param petStoreId The pet store id
     */
    public CatEntity(AnimalType animalType, Skin skinType, Gender gender, Breed breed, BigDecimal cost, int petStoreId)
    {
        super(PetType.CAT, cost, gender, petStoreId, breed);
        super.skinType = skinType;
        super.animalType = animalType;
        super.legs= 4;
        this.speaks = this.speaks();
    }

    /**
     * Is the cat allergy friendly determined by skin type
     * @return A message that tells if the cat is hypoallergenic
     */
    public String catHypoallergenic() {
        return super.petHypoallergenic(this.skinType).replaceAll("pet", "cat");
    }

    /**
     * Dependent if the cat is domestic, wild, or neither what can the say
     * @return what cats would speak
     */
    public String speaks() {

        switch (this.animalType) {
            case DOMESTIC:
                speaks = "The cat goes prr! prr!";
                break;
            case WILD:
                speaks = "The cat goes hiss! hiss!";
                break;
            default:
                speaks = "The cat goes " + super.getPetType().speak + "! " + super.getPetType().speak + "!";
                break;
        }
        return speaks;
    }

    private String numberOfLegs() {
        return "Cats have " + super.getLegs() + " legs!";
    }

    public String typeOfPet() {
        return "The type of pet is " + petType + "!";
    }

    @Override
    public String toString() {
        return super.toString() +
                "The cat is " + this.animalType + "!\n" +
                "The cat breed is " + this.getBreed() + "!\n" +
                this.catHypoallergenic() + "!\n" +
                this.speaks() + "\n" +
                this.numberOfLegs();
    }
}