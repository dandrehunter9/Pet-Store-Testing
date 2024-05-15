package com.petstoreservices.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.petstore.PetEntity;
import com.petstore.animals.attributes.PetType;
import com.petstore.exceptions.DuplicatePetStoreRecordException;
import com.petstore.exceptions.PetNotFoundSaleException;
import com.petstore.exceptions.PetTypeNotSupportedException;
import com.petstoreservices.exceptions.*;
import com.petstoreservices.service.PetInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



/**
 * The Pet inventory controller is where the definition of the different services occur
 */
@RestController
public class PetInventoryController
{
    @Autowired
    private PetInventoryService inventoryService;

   public PetInventoryController()
    {

    }


    //===================  GET SERVICES  ========================================================

    /**
     * Get Request for inventory
     * <br>url: <a href="http://localhost:8080/inventory">http://localhost:8080/inventory</a>
     * @return - the inventory body in json format
     */
    @RequestMapping(value = "/inventory",
            method = RequestMethod.GET
    )
    public ResponseEntity<?> getInventory()
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try{

            return new ResponseEntity<>(gson.toJson(this.inventoryService.getInventory()),HttpStatus.OK);
        }catch( PetDataStoreException e)
        {
            return new ResponseEntity<>("Issue with Reading Json file", HttpStatus.NO_CONTENT);
        }

    }

    /**
     * GetRequest for a specific pet type and id
     * <br>url: <a href="http://localhost:8080/inventory/search?petType=DOG&petId=2">http://localhost:8080/inventory/search?petType=DOG&petId=2</a>
     * @return - Http status and response body
     */
    @RequestMapping(value = "/inventory/search",
                    params={"petType", "petId"},
                    method = RequestMethod.GET)
    public ResponseEntity<?> findPetByIdAndPetType(@RequestParam ("petType") PetType petType,
                                                   @RequestParam("petId") int petId)
    {
        String responseBody;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try{
            responseBody = gson.toJson(
                       this.inventoryService.getPetByIdAndType(petType, petId));
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }catch(DuplicatePetStoreRecordException | PetNotFoundSaleException e)
        {
            responseBody = e.getMessage();
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        }catch(PetDataStoreException e)
        {
            return new ResponseEntity<>("Issue with Reading Json file", HttpStatus.NO_CONTENT);
        }
    }

    /**
     * GetRequest for a specific pet type
     * <br>url: <a href="http://localhost:8080/inventory/search/DOG">http://localhost:8080/inventory/search/DOG</a>
     * @return - Http status and response body
     */
    @RequestMapping("/inventory/search/{petType}")
    public ResponseEntity<?> findPetByPetType(@PathVariable ("petType") PetType petType)
    {
        String responseBody;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try{
            responseBody = gson.toJson(
                    this.inventoryService.getPetsByPetType(petType));
            System.out.println("Exception NOT  caught");
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }catch(PetNotFoundSaleException e)
        {
            System.out.println("Exception caught");
            responseBody = "The petType [" + petType + "] is not supported!";
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        }catch( PetDataStoreException f)
        {
            f.printStackTrace();
            return new ResponseEntity<>("Issue with Reading Json file", HttpStatus.NO_CONTENT);
        }
    }

    //===================  POST SERVICES  ========================================================

    /**
     * Post a request to add a pet item
     * <br>url: <a href="http://localhost:8080/inventory/petType/DOG">http://localhost:8080/inventory/petType/DOG</a>
     * <pre>
     * requestbody={
     *     "cost":1050.00,
     *     "gender":"MALE",
     *     "breed":"MALTESE",
     *     "skinType":"FUR",
     *     "animalType":"DOMESTIC"
     * }
     * </pre>
     *
     * @param petType - pet type the request is for to add inventory
     * @param petItem - the request body
     * @return Http status and response body
     */
    @RequestMapping(method= RequestMethod.POST, value="inventory/petType/{petType}")
    public ResponseEntity<?> addInventory(@PathVariable ("petType") PetType petType,
                                          @RequestBody PetEntity petItem)
    {
        String responseBody;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try{
            responseBody = gson.toJson(this.inventoryService.addInventory(petType, petItem));
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }catch(PetInventoryFileNotCreatedException e)
        {
            responseBody = e.getMessage();
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        }catch( PetDataStoreException f)
        {
            f.printStackTrace();
            return new ResponseEntity<>("Issue with Reading Json file", HttpStatus.NO_CONTENT);
        }

    }

    /**
     * Update the inventory of the petstore.
     * <br>url: <a href="http://localhost:8080/inventory/update?petType=DOG&petId=4">http://localhost:8080/inventory/update?petType=DOG&petId=4</a>
     * <pre>
     * requestbody={
     *      "cost":1050.00
     * }
     * </pre>
     *
     * @param petType - pet type the inventory is being updated
     * @param petId - pet id to be updated
     * @param petItem - the request body
     * @return - HTTP status code and response body
     */
    @RequestMapping(value="inventory/update",
                    params = {"petType", "petId"},
                    method= RequestMethod.PUT)
    public ResponseEntity<?> updateInventory(@RequestParam ("petType") PetType petType,
                                             @RequestParam ("petId") int petId,
                                             @RequestBody PetEntity petItem)
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String responseBody;
        try{
            responseBody = gson.toJson(
                    this.inventoryService.updateInventoryByPetIdAndPetType(petType, petId, petItem));
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }catch(PetTypeNotSupportedException | PetStoreAnimalTypeException | DuplicatePetStoreRecordException |
               UpdatePetException e)
        {
            responseBody = e.getMessage();
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        }catch( PetDataStoreException | PetInventoryFileNotCreatedException f)
        {
            f.printStackTrace();
            return new ResponseEntity<>("Issue with Reading Json file", HttpStatus.NO_CONTENT);
        }

    }

    //===================  DELETE SERVICES  ========================================================

    /**
     * Remove Item from pet store inventory
     * <br>url: <a href="http://localhost:8080/inventory/petType/DOG/petId/4">http://localhost:8080/inventory/petType/DOG/petId/4</a>
     *
     * @param petType - the pet type
     * @param petId - unique id
     * @return - response code and json representation of item that was deleted
     */
    @DeleteMapping(value="inventory/petType/{petType}/petId/{petId}")
    public ResponseEntity<?> removeInventoryByIDAndPetType(@PathVariable ("petType") PetType petType,
                                                           @PathVariable ("petId") int petId)
    {
        String responseBody;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try{
            responseBody = gson.toJson(
                    this.inventoryService.removeInventoryByIDAndPetType(petType, petId));
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }catch( DuplicatePetStoreRecordException | PetNotFoundSaleException
                | PetInventoryFileNotCreatedException e)
        {
            responseBody = e.getMessage();
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        }catch( PetDataStoreException f)
        {
            f.printStackTrace();
            return new ResponseEntity<>("Issue with Reading Json file", HttpStatus.NO_CONTENT);
        }
    }
}
