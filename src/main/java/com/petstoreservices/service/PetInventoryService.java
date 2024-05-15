package com.petstoreservices.service;


import com.petstore.PetEntity;

import com.petstore.animals.attributes.PetType;
import com.petstore.exceptions.DuplicatePetStoreRecordException;
import com.petstore.exceptions.PetNotFoundSaleException;
import com.petstore.exceptions.PetTypeNotSupportedException;
import com.petstoreservices.exceptions.*;
import com.petstoreservices.repository.PetRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Pet Inventory Service.  This service currently supports
 * Adding a single pet, removing a single pet, and retrieving the list/
 * The service also keeps up inventory data in json file
 */
@Service
public class PetInventoryService {

    @Autowired
    private PetRepository petRepo;

    public PetInventoryService()
    {

//        petRepo = new PetRepository();
    }

    /**
     *  Retrieve the pet store Inventory of pets
     * @return list of {@link PetEntity} that represent the inventory
     * @throws PetDataStoreException - Issue with file format, reading the file, or file is not present
     */
    public List<PetEntity> getInventory() throws PetDataStoreException {
        List<PetEntity> inventoryList = this.petRepo.getPetInventory();
        return inventoryList;
    }

    /**
     * Identify and return pet by it's type and unique id
     * @param petType - type of pet searching for
     * @param petId - unique id per pet type
     * @return - found pet
     * @throws PetNotFoundSaleException - pet does not exist in inventory
     * @throws DuplicatePetStoreRecordException - duplicate record found
     * @throws PetDataStoreException - Issue with file format, reading the file, or file is not present
     */
    public PetEntity getPetByIdAndType(PetType petType, int petId) throws PetNotFoundSaleException,
            DuplicatePetStoreRecordException, PetDataStoreException {
        return this.petRepo.findPetByPetTypeAndPetId(petType, petId);
    }
    /**
     * Add a new item to the inventory list
     * @param petType - type of pet to be added
     * @param newPetRestItem - the Rest Request body representation
     * @return - return the Pet
     * @throws PetInventoryFileNotCreatedException - pet inventory file could not be created
     * @throws PetDataStoreException - Issue with file format, reading the file, or file is not present
     */
    public PetEntity addInventory(PetType petType, PetEntity newPetRestItem)
            throws PetInventoryFileNotCreatedException, PetDataStoreException
    {
        List<PetEntity> sortedPets = this.petRepo.getPetInventory().stream()
                .filter(p -> p.getPetType().equals(petType))
                .sorted(Comparator.comparingInt(PetEntity::getPetId))
                .collect(Collectors.toList());
        newPetRestItem.setPetType(petType);
        return  this.petRepo.createPetEntity(newPetRestItem,sortedPets);
    }

    /**
     * Remove a pet item inventory item from list
     * @param petType - Need the PetType to filter the store
     * @param petId - each pet id is unique per pet type
     * @return - Return the Pet found
     * @throws DuplicatePetStoreRecordException - Duplicate record exists
     * @throws PetNotFoundSaleException - Pet search return nothing
     * @throws PetInventoryFileNotCreatedException - file not created
     * @throws PetDataStoreException - Issue with file format, reading the file, or file is not present
     */
    public PetEntity removeInventoryByIDAndPetType(PetType petType, int petId)
            throws DuplicatePetStoreRecordException, PetNotFoundSaleException,
            PetInventoryFileNotCreatedException, PetDataStoreException
    {
        PetEntity removeItem = this.petRepo.removeEntity(this.petRepo.findPetByPetTypeAndPetId(petType, petId));
        return removeItem;
    }



    /**
     * Search petsForSale list for matches to PetType
     * @param petType - the type of pet
     * @return - all the pets by pet type found
     * @throws PetNotFoundSaleException - Pet type not found in inventory
     * @throws PetDataStoreException - Issue with file format, reading the file, or file is not present
     */
    public List<PetEntity> getPetsByPetType(PetType petType) throws PetNotFoundSaleException,  PetDataStoreException {
        List<PetEntity> sortedPets = this.petRepo.getPetInventory().stream()
                .filter(p -> p.getPetType().equals(petType))
                .sorted(Comparator.comparingInt(p->p.getPetId()))
                .collect(Collectors.toList());
        if(sortedPets.isEmpty())
        {
            throw new PetNotFoundSaleException("0 results found for search criteria petType[" + petType
                    +"] Please try again!!");
        }
        return sortedPets;
    }

    /**
     * Update the inventory by Pet id and Type.  Update the data file
     * @param petType - the type of pet
     * @param petId - the unique id of the pet
     * @param petItemUpdate - The RestItem representation of Pet -
     * @return - the pet item updated
     * @throws DuplicatePetStoreRecordException - There is more than 1 pet store record for that pet type with same id
     * @throws PetTypeNotSupportedException - Pet Type is not supported so cannot update the inventory
     * @throws UpdatePetException - the body is not what is expected
     * @throws PetDataStoreException - Issue with file format, reading the file, or file is not present
     */
    public PetEntity updateInventoryByPetIdAndPetType(PetType petType, int petId, PetEntity petItemUpdate)
            throws  DuplicatePetStoreRecordException, PetTypeNotSupportedException, UpdatePetException,
            PetStoreAnimalTypeException, PetInventoryFileNotCreatedException, PetDataStoreException {
        PetEntity updatedPetItem = null;
        try{
            updatedPetItem = this.petRepo.findPetByPetTypeAndPetId(petType, petId);
            this.petRepo.removeEntity(updatedPetItem);
            updatedPetItem = this.petRepo.updatePetEntity(petItemUpdate, updatedPetItem);

        }catch(PetNotFoundSaleException e)
        {
            System.out.println("Item Not Found so adding with new petstore id");// should use a logger
            updatedPetItem = this.addInventory(petType, petItemUpdate);
        }

        return updatedPetItem;
    }
}
