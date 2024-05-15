package com.petstore;


import com.google.gson.annotations.SerializedName;
import com.petstore.animals.attributes.Breed;
import com.petstore.animals.attributes.Gender;
import com.petstore.animals.attributes.PetType;
import com.petstore.animals.attributes.Skin;


/**
 * Common attributes of a pet
 */
public abstract class  AbstractPet implements PetImpl {

    @SerializedName(value="animalType")
    protected AnimalType animalType = AnimalType.UNKNOWN;
    @SerializedName(value="petType")
    protected PetType petType = PetType.UNKNOWN;
    @SerializedName(value="skinType")
    protected Skin skinType = Skin.UNKNOWN;
    @SerializedName(value="gender")
    protected Gender gender = Gender.UNKNOWN;

    @SerializedName(value="breed")
    protected Breed breed;

    protected boolean hasLegs = true;
    protected boolean isMammal = false;

    /**
     * Get the pet's gender
     * @return {@link Gender}
     */
    public abstract Gender getGender();

    public abstract Skin getSkinType();
    public abstract void setSkinType(Skin skin);

    public abstract Breed getBreed();

    public abstract PetType getPetType();

    public abstract void setPetType(PetType petType);

    /**
     * Is the pet allergy friendly determined by skin type
     * @param skin type of skin the pet has
     * @return A message that tells if the pet is hypoallergenic
     */
    public String petHypoallergenic(Skin skin)
    {
        String petHypoallergenicStmt;
        switch(skin)
        {
            case FUR:
            case FEATHERS:
                petHypoallergenicStmt = "The pet is not hyperallergetic!";
                break;
            case HAIR:
            case SCALES:
                petHypoallergenicStmt = "The pet is hyperallergetic!";
                break;
            default:
                petHypoallergenicStmt = "The pet skin is UNKNOWN at this time, so cannot determine if pet is " +
                        "hypoallergetic!";
                break;
        }
        return petHypoallergenicStmt;
    }


}
