package services;

import com.petstore.AnimalType;
import com.petstore.PetEntity;
import com.petstore.animals.CatEntity;
import com.petstore.animals.DogEntity;
import com.petstore.animals.attributes.Breed;
import com.petstore.animals.attributes.Gender;
import com.petstore.animals.attributes.PetType;
import com.petstore.animals.attributes.Skin;
import com.petstore.exceptions.DuplicatePetStoreRecordException;
import com.petstore.exceptions.PetNotFoundSaleException;
import com.petstoreservices.exceptions.PetDataStoreException;
import com.petstoreservices.repository.PetRepository;
import com.petstoreservices.service.PetInventoryService;;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.petstore.animals.attributes.Skin.FUR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class ArgumentMatcherTests
{
    @InjectMocks //inject the pet repository into the pet service
    private PetInventoryService petService;

    @Mock //Mock the petRepository
    private PetRepository petRepository;

    private List<PetEntity> myPets;
    @BeforeEach
    public void init() throws PetDataStoreException
    {
        //create the datastore to be used for testing
        myPets = new ArrayList<PetEntity>(Arrays.asList(
                new DogEntity(AnimalType.DOMESTIC, FUR, Gender.MALE, Breed.MALTESE,
                        new BigDecimal("750.00"), 3),
                new DogEntity(AnimalType.DOMESTIC, FUR, Gender.MALE, Breed.POODLE,
                        new BigDecimal("650.00"), 1),
                new DogEntity(AnimalType.DOMESTIC, FUR, Gender.FEMALE, Breed.GREY_HOUND,
                        new BigDecimal("750.00"), 4),
                new CatEntity(AnimalType.DOMESTIC, Skin.HAIR, Gender.MALE, Breed.BURMESE,
                        new BigDecimal("65.00"),1)
        ));

    }


    @TestFactory
    @DisplayName("Argument Captor Test")
    public Stream<DynamicNode> argumentCaptorTest() throws PetDataStoreException,
            DuplicatePetStoreRecordException, PetNotFoundSaleException {
        //found pet items from list in constructor
        PetEntity pet = new DogEntity(AnimalType.DOMESTIC, FUR, Gender.FEMALE, Breed.GREY_HOUND,
                new BigDecimal("750.00"), 4);
        //declare the argument captors
        ArgumentCaptor petEntityCaptor = ArgumentCaptor.forClass(PetType.class);
        ArgumentCaptor acInteger = ArgumentCaptor.forClass(Integer.class);
        //mock the repository method and provide a results
        Mockito.doReturn(pet).when(petRepository).findPetByPetTypeAndPetId(PetType.DOG, 4);
        //execute the service
        PetEntity foundEntity = petService.getPetByIdAndType(PetType.DOG, 4);
        //verify the petRepository has executed the query and capture the input params
        verify(petRepository).findPetByPetTypeAndPetId((PetType) petEntityCaptor.capture(), (Integer) acInteger.capture());
        //generate additional test results
        List<DynamicNode> testResults = new ArrayList<DynamicNode>();

        List<DynamicTest> inventoryTests = Arrays.asList(
                DynamicTest.dynamicTest("Pet item with Dog id 4",
                        ()-> assertTrue(pet.getPetId()==foundEntity.getPetId())),
                DynamicTest.dynamicTest("Pet item with PetType<Dog>",
                        ()-> assertTrue(pet.getPetType()==foundEntity.getPetType())),
                DynamicTest.dynamicTest("Pet item with Breed<GreyHound>",
                        ()-> assertTrue(pet.getBreed() == foundEntity.getBreed())));
        DynamicContainer petResponseContainer = DynamicContainer.dynamicContainer(
                "Pet Entity Service Tests[" +pet.getPetId() +"]", inventoryTests);
        //verify the arguments capture are what was provided
        List<DynamicTest> argCaptureTests = Arrays.asList(
                DynamicTest.dynamicTest("Pet item with Dog id 4",
                        ()-> assertTrue(pet.getPetId()==Integer.valueOf(acInteger.getValue().toString()))),
                DynamicTest.dynamicTest("Pet item with PetType<Dog>",
                        ()-> assertEquals("DOG", petEntityCaptor.getValue().toString())));

        DynamicContainer argumentTestContainer = DynamicContainer.dynamicContainer(
                "Pet Entity findPetByPetTypeAndPetId Captor Tests[" +pet.getPetId() +"]", argCaptureTests);
        testResults.add(argumentTestContainer);
        testResults.add(petResponseContainer);

        return testResults.stream();
    }
    @TestFactory
    @DisplayName("Argument Captor MultiTest")
    public Stream<DynamicNode> argumentCaptorMultiTest() throws PetDataStoreException,
            DuplicatePetStoreRecordException, PetNotFoundSaleException {
        //found pet items from list in constructor
        PetEntity pet1 = new DogEntity(AnimalType.DOMESTIC, FUR, Gender.FEMALE, Breed.GREY_HOUND,
                new BigDecimal("750.00"), 4);
        PetEntity pet2 =  new CatEntity(AnimalType.DOMESTIC, Skin.HAIR, Gender.MALE, Breed.BURMESE,
                new BigDecimal("65.00"),1);
        //declare the argument captors
        ArgumentCaptor petEntityCaptor = ArgumentCaptor.forClass(PetType.class);
        ArgumentCaptor acInteger = ArgumentCaptor.forClass(Integer.class);

        //mock the repository method and provide a results
        Mockito.doReturn(pet1).when(petRepository).findPetByPetTypeAndPetId(PetType.DOG, 4);
        //execute the service
        PetEntity foundDogEntity = petService.getPetByIdAndType(PetType.DOG, 4);

        //mock the repository method and provide a results
        Mockito.doReturn(pet2).when(petRepository).findPetByPetTypeAndPetId(PetType.CAT, 1);
        //execute the service
        PetEntity foundCatEntity = petService.getPetByIdAndType(PetType.CAT, 1);

        //verify the petRepository has executed the query and capture the input params, why 2 times?
        verify(petRepository, times(2)).findPetByPetTypeAndPetId((PetType)
                petEntityCaptor.capture(), (Integer) acInteger.capture());
        //generate additional test results
        List<DynamicNode> testResults = new ArrayList<DynamicNode>();
        List<DynamicTest> inventoryDogTests = Arrays.asList(
                DynamicTest.dynamicTest("Pet item with Dog id 4",
                        ()-> assertTrue(pet1.getPetId()==foundDogEntity.getPetId())),
                DynamicTest.dynamicTest("Pet item with PetType<Dog>",
                        ()-> assertTrue(pet1.getPetType()==foundDogEntity.getPetType())),
                DynamicTest.dynamicTest("Pet item with Breed<GreyHound>",
                        ()-> assertTrue(pet1.getBreed() == foundDogEntity.getBreed())));
        DynamicContainer petResponseContainer = DynamicContainer.dynamicContainer(
                "Pet Entity Service Tests[" +pet1.getPetId() +"][DOG]", inventoryDogTests);

        List<DynamicTest> inventoryCatTests = Arrays.asList(
                DynamicTest.dynamicTest("Pet item with CAT id 1",
                        ()-> assertEquals(pet2.getPetId(), foundCatEntity.getPetId())),
                DynamicTest.dynamicTest("Pet item with PetType<CAT>",
                        ()-> assertTrue(pet2.getPetType()==foundCatEntity.getPetType())),
                DynamicTest.dynamicTest("Pet item with Breed<BURMESE>",
                        ()-> assertTrue(pet2.getBreed() == foundCatEntity.getBreed())));
        DynamicContainer catResponseContainer = DynamicContainer.dynamicContainer(
                "Pet Entity Service Tests[" +pet1.getPetId() +"][CAT]", inventoryCatTests);

        //verify the arguments capture are what was provided
        List<DynamicTest> argCaptureTests = Arrays.asList(
                DynamicTest.dynamicTest("Pet item with Dog id 4 and Cat id 1",
                        ()-> assertEquals("[" + pet1.getPetId() +  ", " + pet2.getPetId() + "]",
                                acInteger.getAllValues().toString())),
                DynamicTest.dynamicTest("Pet item with PetType<Dog,CAT>",
                        ()-> assertEquals("[" + pet1.getPetType() +  ", " + pet2.getPetType() + "]",
                                petEntityCaptor.getAllValues().toString())));

        DynamicContainer argumentTestContainer = DynamicContainer.dynamicContainer(
                "Pet Entity findPetByPetTypeAndPetId Captor Tests[" +pet1.getPetId() +"]", argCaptureTests);
        testResults.add(argumentTestContainer);
        testResults.add(petResponseContainer);
        testResults.add(catResponseContainer);
        return testResults.stream();
    }
}
