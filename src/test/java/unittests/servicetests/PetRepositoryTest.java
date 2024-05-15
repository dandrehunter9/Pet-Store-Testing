package unittests.servicetests;


import com.petstore.AnimalType;

import com.petstore.PetEntity;
import com.petstore.animals.CatEntity;
import com.petstore.animals.DogEntity;
import com.petstore.animals.attributes.Breed;
import com.petstore.animals.attributes.Gender;
import com.petstore.animals.attributes.PetType;
import com.petstore.animals.attributes.Skin;
import com.petstore.exceptions.PetNotFoundSaleException;

import com.petstoreservices.exceptions.PetDataStoreException;
import com.petstoreservices.exceptions.PetInventoryFileNotCreatedException;
import com.petstoreservices.repository.PetRepository;
import org.junit.jupiter.api.*;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

/**
 * Pet Repository tests
 */
public class PetRepositoryTest
{
    private static PetRepository petRepository;

    private List<PetEntity> petInventoryList;
    @BeforeEach
    public void loadThePetStoreInventory() throws PetInventoryFileNotCreatedException {
        petRepository = new PetRepository();

        petInventoryList = new ArrayList<PetEntity>(Arrays.asList(
                new DogEntity(AnimalType.DOMESTIC, Skin.FUR, Gender.MALE, Breed.MALTESE, new BigDecimal("750.00"), 3),
                new DogEntity(AnimalType.DOMESTIC, Skin.FUR, Gender.MALE, Breed.POODLE, new BigDecimal("650.00"), 1),
                new CatEntity(AnimalType.DOMESTIC, Skin.HAIR, Gender.MALE, Breed.BURMESE, new BigDecimal("65.00"),1),
                new DogEntity(AnimalType.DOMESTIC, Skin.HAIR, Gender.MALE, Breed.GERMAN_SHEPHERD,new BigDecimal("50.00"),
                        2),
                new CatEntity(AnimalType.DOMESTIC, Skin.UNKNOWN, Gender.FEMALE, Breed.SPHYNX,new BigDecimal("100.00"),
                        2)));
        petRepository.convertListToJsonFlatFile(petInventoryList);
    }
    /* ======================================GET REQUESTS VALIDATION     ======================================*/
    @Test
    @DisplayName("Inventory Count Test")
    public void validateInventory() throws PetDataStoreException {

        assertEquals(5, petRepository.getPetInventory().size(),"Inventory counts are off!");
    }

    /*==================================  Remove item tests ======================================================= */
    @Test
    @DisplayName("Sale of Poodle Remove Item Test")
    public void poodleSoldTest() throws PetDataStoreException, PetInventoryFileNotCreatedException,
            PetNotFoundSaleException {
        int inventorySize = petRepository.getPetInventory().size() - 1;
        PetEntity poodle = new DogEntity(AnimalType.DOMESTIC, Skin.FUR, Gender.MALE, Breed.POODLE,
                new BigDecimal("650.00"), 1);

        PetEntity itemReturned = petRepository.removeEntity(poodle);

        assertEquals(inventorySize, petRepository.getPetInventory().size(),
                "Expected inventory does not match actual");
    }



    @Test
    @DisplayName("Sale of Sphynx Remove Item Test")
    public void sphynxSoldTest() throws PetDataStoreException, PetInventoryFileNotCreatedException,
            PetNotFoundSaleException {
        int inventorySize = petRepository.getPetInventory().size() - 1;

        PetEntity sphynx = new CatEntity(AnimalType.DOMESTIC, Skin.UNKNOWN, Gender.FEMALE, Breed.SPHYNX,
                new BigDecimal("100.00"),2);

        PetEntity itemReturned = petRepository.removeEntity(sphynx);

        // Validation
        assertEquals(inventorySize, petRepository.getPetInventory().size(),
                "Expected inventory does not match actual");
        assertEquals(sphynx.getPetId(), itemReturned.getPetId(), "The cat items are identical");
    }

    /**
     * Limitations to test factory as it does not instantiate before all
     * @return list of {@link DynamicNode} that contains the test results
     * @throws PetDataStoreException if there is issue with pet records
     * @throws PetInventoryFileNotCreatedException if the datasource (represented in json file) is not found
     */
    @TestFactory
    @DisplayName("Sale of Sphynx Remove Item Test2")
    public Stream<DynamicNode> sphynxSoldTest2() throws PetDataStoreException, PetInventoryFileNotCreatedException,
            PetNotFoundSaleException {
        int inventorySize = petRepository.getPetInventory().size() - 1;

        PetEntity sphynx = new CatEntity(AnimalType.DOMESTIC, Skin.UNKNOWN, Gender.FEMALE, Breed.SPHYNX,
                new BigDecimal("100.00"),2);

        PetEntity itemReturned = petRepository.removeEntity(sphynx);

        // Validation
        List<DynamicNode> nodes = new ArrayList<>();
        List<DynamicTest> dynamicTests = Arrays.asList(
                dynamicTest("Inventory Check Size Test ", () -> assertEquals(inventorySize,
                        petRepository.getPetInventory().size())),
                dynamicTest("Cat pet id match ", () -> assertEquals(sphynx.getPetId(),
                        itemReturned.getPetId())),
                dynamicTest("The cat breed match ", () -> assertEquals(sphynx.getBreed(),
                        itemReturned.getBreed())),
                dynamicTest("Cat cost match ", () -> assertEquals(sphynx.getCost(),
                        itemReturned.getCost()))
                );
        nodes.add(dynamicContainer("Cat Removal of Pet id 2 Test", dynamicTests));//dynamicNode("", dynamicContainers);

        return nodes.stream();
    }

    // Validation
    @Test
    @DisplayName("Pet not found exception test")
    public void petNotFoundTest()
    {
        PetEntity greyHound = new DogEntity(AnimalType.DOMESTIC, Skin.UNKNOWN, Gender.FEMALE, Breed.GREY_HOUND,
                new BigDecimal("667.00"),1002);
        String expectedMessage = "The Pet is not part of the pet store!!";
        Exception exception = assertThrows(PetNotFoundSaleException.class,
                                            () -> {
                                                    petRepository.removeEntity(greyHound);
                                                  }
                                          );
        assertEquals(expectedMessage, exception.getMessage(), "Item was unexpectedly found in inventory!");
    }

    /* =================================== Add Pet Entity Tests =====================================================*/
    @TestFactory
    @DisplayName("Add Item Test")
    public Stream<DynamicNode> addPetEntityItem() throws PetDataStoreException, PetInventoryFileNotCreatedException
             {
        int inventorySize = petRepository.getPetInventory().size() +1;

        PetEntity greyHound = new DogEntity(AnimalType.DOMESTIC, Skin.UNKNOWN, Gender.FEMALE, Breed.GREY_HOUND,
                new BigDecimal("667.00"),2);

        PetEntity itemReturned = petRepository.createPetEntity(greyHound, petInventoryList);
        List<DynamicNode> nodes = new ArrayList<>();
        List<PetEntity> entityList =  petRepository.getPetInventory();

        PetEntity tmpItem = petRepository.getPetInventory().stream()
                .filter(p -> p.getPetType().equals(PetType.DOG) && p.getPetId()== itemReturned.getPetId())
                .findFirst()
                .orElse(null);
        List<DynamicTest> dynamicTests = Arrays.asList(
                dynamicTest("Inventory Check Size Test ", () -> assertEquals(inventorySize,
                        petRepository.getPetInventory().size())),
                dynamicTest("The dog objects matches item in list by id and type ", () ->
                        assertNotNull( tmpItem)));
        nodes.add(dynamicContainer("Added DogItem with id of 2", dynamicTests));//dynamicNode("", dynamicContainers);

        return nodes.stream();

    }

    // ************************  WHAT OTHER TESTS COULD I ADD HERE?  ******************************************
}
