package com.petstore;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;
import com.petstore.animals.attributes.Breed;
import com.petstore.animals.attributes.Gender;
import com.petstore.animals.attributes.PetType;
import com.petstore.animals.attributes.Skin;
import com.petstoreservices.exceptions.UpdatePetException;
import jakarta.persistence.Entity;

import java.math.BigDecimal;


/**
 * Pet class is a type really for Domestic Animals
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class PetEntity extends AbstractPet
{
    @SerializedName("cost")
    private BigDecimal cost;


    @SerializedName("petId")
    protected int petId; //id will be populated if animal type is PET

    public int getLegs() {
        return legs;
    }

    public void setLegs(int legs) {
        this.legs = legs;
    }

    @SerializedName("legs")
    protected int legs;
    /**
     * Constructor
     * @param petType The {@link PetType}
     * @param cost The cost of the pet
     * @param gender The {@link Gender} of the cat
     */
    public PetEntity(PetType petType, BigDecimal cost, Gender gender, Breed breed)
    {

        this(petType, cost, gender, 0, breed);
     }

    /**
     * Constructor
     * @param petType The {@link PetType}
     * @param cost The cost of the pet
     * @param gender The {@link Gender} of the cat
     * @param petStoreId The pet store id
     */
    public PetEntity(PetType petType, BigDecimal cost, Gender gender, int petStoreId, Breed breed)
    {
        super();
        this.gender = gender;
        this.petType = petType;
        this.setCost(cost);
        this.petId=petStoreId;
        super.animalType = AnimalType.DOMESTIC;
        super.breed = breed;
    }
    public PetEntity(PetType petType, BigDecimal cost, Gender gender, Skin skinType, AnimalType animalType, Breed breed)
    {
        super();
        this.gender = gender;
        this.petType = petType;
        this.setCost(cost);
        super.skinType = skinType;
        super.animalType = animalType;
        super.breed = breed;
    }
    public PetEntity(PetType petType, BigDecimal cost, Gender gender, Skin skinType, AnimalType animalType,
                     Breed breed, int petId, int legs)
    {
        super();
        this.gender = gender;
        this.petType = petType;
        this.setCost(cost);
        this.petId = petId;
        this.legs = legs;
        super.skinType = skinType;
        super.animalType = animalType;
        super.breed = breed;
    }
    public PetEntity(PetType petType, int petId)
    {
        super();
        this.petId = petId;
        this.petType = petType;
    }
    public PetEntity() {

    }
    public PetEntity compareAndUpdate(PetEntity dataStorePetEntity) throws UpdatePetException
    {
        //pet type this was already set to existing item if unknown
        this.petId = dataStorePetEntity.getPetId();
        //check the cost
        if(this.cost != dataStorePetEntity.cost)
        {
            //check to see if there was nothing updated for cost, if so keep the datastore pet info for cost
            if(this.cost == null && dataStorePetEntity.getCost()!=null)
            {
                this.cost = dataStorePetEntity.getCost();
            }
            else if((this.cost.compareTo(BigDecimal.valueOf(0.00)) ==0 )&&
                    (dataStorePetEntity.getCost().compareTo(BigDecimal.valueOf(0.00)) >0))
            {
                System.out.println("Pet No  new cost change!");
                this.cost = dataStorePetEntity.getCost();
            }
            System.out.println("Pet updated with new cost[" + this.cost + "]");
        }
        //check the gender
        if(super.gender.equals(Gender.UNKNOWN) &&
                ! dataStorePetEntity.getAnimalType().equals(AnimalType.UNKNOWN))
        {
            super.gender =  dataStorePetEntity.getGender();
        }
        else if(! this.gender.equals(dataStorePetEntity.getGender()))
        {
            System.out.println("Pet updated with new gender[" + this.gender +"]");
        }
        //check the breed
        if(super.breed == null && dataStorePetEntity.getBreed() !=null)
        {
            super.breed = dataStorePetEntity.getBreed();
        }
        else if(! super.breed.equals(dataStorePetEntity.getBreed()))
        {
            System.out.println("Pet updated with new breed[" + super.breed + "]");
        }
       //set the animal type
        if(super.animalType.equals(AnimalType.UNKNOWN) &&
                ! dataStorePetEntity.getAnimalType().equals(AnimalType.UNKNOWN))
        {
            super.animalType =  dataStorePetEntity.getAnimalType();
        }
        else if(! super.animalType.equals(dataStorePetEntity.getAnimalType()))
        {
            // if(dataStorePetEntity.getPetType() )
            throw new UpdatePetException("The Pet Store only support Animal Type[Domestic] No animals of Type[" +
                    dataStorePetEntity.getAnimalType() + "]");
        }
        //set the legs
        if(this.legs != dataStorePetEntity.getLegs())
        {
            System.out.println("Pet updated with new legs[" + this.legs + "]");
        }
        if(this.hasLegs != dataStorePetEntity.hasLegs)
        {
            System.out.println("Updating hasLegs value to[" + this.hasLegs + "]");
            if(!this.hasLegs)
            {
                if(this.legs >0)
                {
                    System.out.println("Updating legs to zero as an animal that doesn't have legs can't have any!");
                    this.legs = 0;
                }

            }
        } else
        {
            // a pet cannot have 0 legs and hasLegs==true - check the datastore for legs
            if(this.hasLegs && this.legs == 0)
            {
                this.legs = dataStorePetEntity.getLegs(); //set the legs to the datastore entity in this instance
            }
        }
        //set the skin type
        if(super.skinType.equals(Skin.UNKNOWN) &&
                ! dataStorePetEntity.getSkinType().equals(Skin.UNKNOWN))
        {
            super.skinType =  dataStorePetEntity.getSkinType();
        }
        else if(!super.skinType.equals(dataStorePetEntity.getSkinType()))
        {
            System.out.println("Pet updated with new skin[" + super.skinType +"]");
        }
        return this;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Gender getGender() {
        return this.gender;
    }


    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public Skin getSkinType() {
        return super.skinType;
    }
    @Override
    public void setSkinType(Skin skin) {
       super.skinType = skin;
    }
    @Override
    public Breed getBreed(){return super.breed;}

    public void setBreed(Breed breed){
        super.breed = breed;

    }

    @Override
    public PetType getPetType() {
        return this.petType;
    }

    @Override
    public void setPetType(PetType petType) {
        this.petType = petType;
    }

    public BigDecimal getCost()
    {
        return this.cost;
    }


    public void setPetId(int storeId)
    {
       this.petId = storeId;
    }

    public int getPetId()
    {
        return this.petId;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost.setScale(2); //set the precision to 2
    }

    public AnimalType getAnimalType(){
        return super.animalType;
    }


    public String toString()
    {
        if(this.petId == 0)
        {
            return "The type of pet is " + this.petType + "!\n" +
                    "The "+ this.petType + " gender is " + this.gender + "!\n" +
                    "The "+ this.petType + " # of legs is " + this.legs + "!\n" +
                    "The "+ this.petType + " cost is $" + this.cost + "!\n";

        }
        else {
            return "The type of pet is " + this.petType + "!\n" +
                    "The "+ this.petType + " pet store id is " + this.petId + "!\n" +
                    "The "+ this.petType + " gender is " + this.gender + "!\n" +
                    "The "+ this.petType + " cost is $" + this.cost + "!\n";
                    /*"The "+ this.petType+ "is " + super.animalType + "!\n" +
                    "The "+ this.petType+ "breed is "+ super.breed + "!";*/

        }

    }

}
