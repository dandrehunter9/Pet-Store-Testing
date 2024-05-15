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
@Entity
public class DogEntity extends PetEntity {

    /* Properties */

    @SerializedName(value="speaks")
    private String speaks;

    /**
     * Constructor
     * @param animalType {@link AnimalType} that defines if it domesticated or wild dog
     * @param skinType The {@link Skin} of the dog
     * @param gender The {@link Gender} of the dog
     * @param breed The type of dog {@link Breed}
     */
    public DogEntity(AnimalType animalType, Skin skinType, Gender gender, Breed breed)
    {
        this(animalType, skinType, gender, breed, new BigDecimal(0));
    }

    /**
     * Constructor
     * @param animalType {@link AnimalType} that defines if it domesticated or wild dog
     * @param skinType The {@link Skin} of the dog
     * @param gender The {@link Gender} of the dog
     * @param breed The type of dog {@link Breed}
     * @param cost The cost of the dog
     */
    public DogEntity(AnimalType animalType, Skin skinType, Gender gender, Breed breed, BigDecimal cost)
    {
        this(animalType, skinType, gender, breed, cost, 0);
    }


    /**
     * Constructor
     * @param animalType {@link AnimalType} that defines if it domesticated or wild dog
     * @param skinType The {@link Skin} of the dog
     * @param gender The {@link Gender} of the dog
     * @param breed The type of dog {@link Breed}
     * @param cost The cost of the dog
     * @param petStoreId  The pet store id
     */
    public DogEntity(AnimalType animalType, Skin skinType, Gender gender, Breed breed, BigDecimal cost, int petStoreId)
    {
        super(PetType.DOG, cost, gender, petStoreId, breed);
        super.skinType = skinType;
        super.animalType = animalType;
        super.legs = 4;
        this.speaks = this.speaks();
    }

    /**
     * Is the dog allergy friendly determined by skin type
     * @return A message that tells if the cat is hypoallergenic
     */
    public String dogHypoallergenic()
    {
        return super.petHypoallergenic(this.skinType).replaceAll("pet", "dog");
    }

    /**
     * What does the dog say depends on {@link AnimalType} - Domestic, Wild, Farm, or Unknown
     * @return what dogs would speak
     */
    public String speaks()
    {
        switch(animalType){
            case DOMESTIC:
                speaks = "The dog goes woof! woof!";
                break;
            case WILD:
                speaks = "The dog goes grr! grr!";
                break;
            default:
                speaks = "The dog goes " + super.getPetType().speak + "! " + super.getPetType().speak + "!";
        }
       return speaks;
    }

    private String numberOfLegs()
    {
        return "Dogs have " + super.legs + " legs!";
    }

    @Override
    public String toString()
    {
        return super.toString() +
               "The dog is " +  super.animalType + "!\n" +
               "The dogs breed is " + this.getBreed() + "!\n" +
               this.dogHypoallergenic() + "!\n" +
               this.speaks() + "\n" +
               this.numberOfLegs();
    }


}
