package com.petstoreservices.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.petstore.PetEntity;

import com.petstore.PetStoreReader;


import com.petstore.animals.attributes.PetType;
import com.petstore.exceptions.DuplicatePetStoreRecordException;
import com.petstore.exceptions.PetNotFoundSaleException;
import com.petstore.exceptions.PetTypeNotSupportedException;
import com.petstoreservices.exceptions.PetDataStoreException;
import com.petstoreservices.exceptions.PetInventoryFileNotCreatedException;

import com.petstoreservices.exceptions.UpdatePetException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.io.FileWriter;
import java.io.IOException;

import java.io.Writer;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Pet Repository initializes the pet inventory list and handles the conversion of the pet store list to json file
 */
@Repository
public class PetRepository {



    /**
     * Initialize the pet store object
     */
    public PetRepository()   {

    }

    /**
     * Used for post requests
     * @param petEntity pet info
     * @param sortedPets pet list which is sorted
     * @return {@link PetEntity}
     * @throws PetInventoryFileNotCreatedException file not created
     * @throws PetDataStoreException - Issue with file format, reading the file, or file is not present
     *
     */
    public PetEntity createPetEntity(PetEntity petEntity, List<PetEntity> sortedPets) throws
            PetInventoryFileNotCreatedException, PetDataStoreException {
        petEntity.setPetId((sortedPets.get(sortedPets.size() - 1)).getPetId() + 1);
        List<PetEntity> inventoryList = this.getPetInventory();
        inventoryList.add(petEntity);
        this.convertListToJsonFlatFile(inventoryList);
        return petEntity;
    }
    /**
     * Used for put requests
     * @param petEntity Existing pet item in the datastore
     * @param updatedPetItem updates for the updated pet item
     * @return {@link PetEntity}
     * @throws PetTypeNotSupportedException pet type not supported by pet store
     * @throws UpdatePetException issues updating the pet type
     * @throws PetDataStoreException issue with retrieving the datastore
     * @throws PetInventoryFileNotCreatedException issue with creating new datastore with updated pet item

     */
    public PetEntity updatePetEntity(@NotNull PetEntity petEntity, PetEntity updatedPetItem) throws
            PetTypeNotSupportedException, UpdatePetException, PetDataStoreException,
            PetInventoryFileNotCreatedException {
        PetEntity newPetItem = new PetEntity();
        boolean isPetSupported = true;
        if(petEntity.getPetType().equals(PetType.UNKNOWN))
        {
            petEntity.setPetType(updatedPetItem.getPetType()); //pet type is not required body field but is part of request
        }
        switch (petEntity.getPetType())
        {
            case CAT, DOG -> {
                try{
                    newPetItem = petEntity.compareAndUpdate(updatedPetItem);
                }catch(UpdatePetException upe)
                {
                    upe.printStackTrace();
                    throw upe;
                }
                isPetSupported = true;
            }
            case BIRD, SNAKE -> {
                isPetSupported = false;
            }
            default -> {
                isPetSupported = false;
            }
        }
        if (isPetSupported)
        {
            List<PetEntity> petInventoryItems = this.getPetInventory();//get new inventory list
            petInventoryItems.add(newPetItem);
            this.convertListToJsonFlatFile(petInventoryItems);
            return newPetItem;
        } else {
            throw new PetTypeNotSupportedException("The pet store is not equipped to support an animal of type[" +
                    petEntity.getPetType() + "]");
        }
    }


    /**
     * Remove the pet from the inventory
     * @param petEntity - Pet entity to be removed from the list
     * @return Pet item removed from the inventory list
     * @throws PetNotFoundSaleException - Pet does not exist in inventory list
     * @throws PetInventoryFileNotCreatedException - file could not be created
     * @throws PetDataStoreException - Issue with file format, reading the file, or file is not present
     */
    public PetEntity removeEntity(PetEntity petEntity) throws
            PetNotFoundSaleException, PetInventoryFileNotCreatedException, PetDataStoreException
    {
        PetStoreReader psReader = new PetStoreReader();

        List<PetEntity> currentPetList = psReader.readJsonFromFile();

        List<PetEntity> otherPets = currentPetList.stream()
                .filter(p -> (p.getPetType() != petEntity.getPetType()))
                .collect(Collectors.toList());

        List<PetEntity> newPetList =  currentPetList.stream()
                .filter(p -> ((p.getPetType() == petEntity.getPetType())
                        && (p.getPetId() != petEntity.getPetId())))
                .collect(Collectors.toList());

        List<PetEntity> foundPetItems = currentPetList.stream()
                .filter(p -> ((p.getPetType() == petEntity.getPetType())
                        && (p.getPetId() == petEntity.getPetId())))
                .collect(Collectors.toList());
        newPetList.addAll(otherPets);

        if (foundPetItems.isEmpty())
        {
            throw new PetNotFoundSaleException("The Pet is not part of the pet store!!");
        }else
        {
            this.convertListToJsonFlatFile(newPetList);
            return foundPetItems.get(0);
        }
    }
    /**
     * Retrieve the pet inventory file and return it in a form of list<PetEntity>
     * @return - Remaining pet entity list
     * @throws PetDataStoreException - Issue with file format, reading the file, or file is not present
     */
    public List<PetEntity> getPetInventory() throws PetDataStoreException {

        PetStoreReader psReader = new PetStoreReader();
        return psReader.readJsonFromFile();
    }
    /**
     * Search list for a pet type and pet id
     * @param petType - Need the PetType to filter the store
     * @param petId - each pet id is unique per pet type
     * @return - Return the Pet found
     * @throws PetNotFoundSaleException - Pet is not found
     * @throws DuplicatePetStoreRecordException - Duplicate record found in the store
     * @throws PetDataStoreException - Issue with file format, reading the file, or file is not present
     */
    public PetEntity findPetByPetTypeAndPetId(PetType petType, int petId) throws PetNotFoundSaleException,
            DuplicatePetStoreRecordException, PetDataStoreException {
            List<PetEntity> filteredPets =  this.getPetInventory().stream()
               .filter(p -> p.getPetType().equals(petType))
               .filter(id -> id.getPetId()==petId)
               .collect(Collectors.toList());

        if(filteredPets.isEmpty())
        {
            throw new PetNotFoundSaleException("0 results found for search criteria for pet id[" + petId + "] " +
                    "petType[" + petType +"] Please try again!!");
        }
        else if(filteredPets.size() >1)
        {
            throw new DuplicatePetStoreRecordException("Should only of retrieved one item from the pet store and " +
                    "found duplicate records in the results.  This list size was[" + filteredPets.size() +
                    "] for pet id[" + petId + "] petType[" + petType +"]");
        }
        else{
            return filteredPets.get(0);
        }
    }
    /**
     * Convert the pet inventory list to json flat file
     * @ throws PetInventoryFileNotCreatedException - Issue converting the PetEntity List to json format
     */
    public void convertListToJsonFlatFile(List<PetEntity> newList) throws PetInventoryFileNotCreatedException
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            Writer writer = new FileWriter("./datastore/application/petstore.json");    //this would normally be a config
            gson.toJson(newList, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new PetInventoryFileNotCreatedException("Issue writing the file");
        }
    }


}
