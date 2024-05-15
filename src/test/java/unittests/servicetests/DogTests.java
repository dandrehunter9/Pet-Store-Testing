package unittests.servicetests;


import com.petstore.AnimalType;

import com.petstore.animals.attributes.Breed;
import com.petstore.animals.attributes.Gender;
import com.petstore.animals.attributes.PetType;
import com.petstore.animals.attributes.Skin;
import org.junit.jupiter.api.*;
import com.petstore.animals.DogEntity;


import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DogTests {
    private static DogEntity actualDog;
    @BeforeAll
    public static void createAnimals()
    {
        //Arrange
        actualDog = new DogEntity(AnimalType.DOMESTIC, Skin.FUR, Gender.UNKNOWN, Breed.UNKNOWN);
    }
    @Test
    @Order(1)
    @DisplayName("PetType Tests")
    public void petTypeTests()
    {
        //act and assert the value
        assertEquals(PetType.DOG, actualDog.getPetType(), "Pet Type Expected[" + PetType.DOG
                + "] Actual[" + actualDog.getPetType() + "]");
    }

    @Test
    @Order(1)
    @DisplayName("Animal Test Type Tests Domestic")
    public void animalTypeTests()
    {
        //act and assert
        assertEquals(AnimalType.DOMESTIC, actualDog.getAnimalType(), "Animal Type Expected[" + AnimalType.DOMESTIC
            + "] Actual[" + actualDog.getAnimalType() + "]");
    }

    @Test
    @Order(1)
    @DisplayName("Dog Speak Woof Tests")
    public void dogGoesWoffTest()
    {
        assertEquals("The dog goes woof! woof!", actualDog.speaks(), "I was expecting wuff");
    }

    @Test
    @Order(1)
    @DisplayName("Dog Fur is it Hyperallergetic")
    public void dogHyperAllergeticTests()
    {
        assertEquals("The dog is not hyperallergetic!", actualDog.dogHypoallergenic(),
                "The dog is not hyperallergetic!");
    }

    @Test
    @Order(1)
    @DisplayName("Dog has legs Test")
    public void legTests()
    {
        Assertions.assertNotNull(actualDog.getLegs());
    }

    @Test
    @Order(2)
    @DisplayName("Dog Gender Test Male")
    public void genderTestMale()
    {
        actualDog = new DogEntity(AnimalType.WILD, Skin.UNKNOWN,Gender.MALE, Breed.UNKNOWN);
        assertEquals(Gender.MALE, actualDog.getGender(), "Expecting Male Gender!");
    }

    @Test
    @Order(2)
    @DisplayName("Dog Breed Test Maltese")
    public void genderDogBreed() {
        actualDog = new DogEntity(AnimalType.WILD, Skin.UNKNOWN,Gender.FEMALE, Breed.MALTESE);//arrange
        assertEquals(Breed.MALTESE, actualDog.getBreed(), "Expecting Breed Maltese!");//act and assert
    }

    @Test
    @Order(2)
    @DisplayName("Dog Speak Grr Tests")
    public void dogGoesGrrTest()
    {
        actualDog = new DogEntity(AnimalType.WILD, Skin.UNKNOWN,Gender.UNKNOWN, Breed.UNKNOWN);
        assertEquals("The dog goes Grr! Grr!", actualDog.speaks(), "I was expecting Grr");
    }

    @Test
    @Order(2)
    @DisplayName("Dog Speak Bark Tests 1")
    public void dogGoesBarkTest()
    {
        actualDog = new DogEntity(AnimalType.UNKNOWN, Skin.UNKNOWN,Gender.UNKNOWN, Breed.UNKNOWN);
        assertEquals("The dog goes Bark! Bark!", actualDog.speaks(), "I was expecting Bark");
    }
}
