package unittests.servicetests;

import com.petstore.AnimalType;
import com.petstore.animals.CatEntity;
import com.petstore.animals.attributes.Breed;
import com.petstore.animals.attributes.Gender;
import com.petstore.animals.attributes.PetType;
import com.petstore.animals.attributes.Skin;
import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.assertEquals;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CatTests{

    private static CatEntity actualCat;

    @BeforeAll
    public static void createAnimals()
    {
        //Arrange
        actualCat = new CatEntity(AnimalType.DOMESTIC, Skin.FUR, Gender.UNKNOWN, Breed.UNKNOWN);
    }

    @Test
    @Order(1)
    @DisplayName("PetType Tests")
    public void petTypeTests()
    {
        //act and assert the value
        assertEquals(PetType.CAT, actualCat.getPetType(), "Pet Type Expected[" + PetType.CAT
                + "] Actual[" + actualCat.getPetType() + "]");
    }

    @Test
    @Order(1)
    @DisplayName("Animal Test Type Tests Domestic")
    public void animalTypeTests()
    {
        //act and assert the value
        assertEquals(AnimalType.DOMESTIC, actualCat.getAnimalType(), "Animal Type Expected[" + AnimalType.DOMESTIC
                + "] Actual[" + actualCat.getAnimalType() + "]");
    }

    @Test
    @Order(1)
    @DisplayName("Cat Speak Prrr Tests")
    public void catGoesMeowTest()
    {
        assertEquals("The cat goes prr! prr!", actualCat.speaks(), "I was expecting meow");
    }

    @Test
    @Order(1)
    @DisplayName("Cat Fur is it Hyperallergetic")
    public void catHyperAllergeticTests()
    {
        assertEquals("The cat is not hyperallergetic!", actualCat.catHypoallergenic(),
                "The cat is not hyperallergetic!");
    }

    @Test
    @Order(1)
    @DisplayName("Cat has legs Test")
    public void legTests()
    {
        Assertions.assertNotNull(actualCat.getLegs());
    }

    @Test
    @Order(2)
    @DisplayName("Cat Gender Test FeMale")
    public void genderTestFeMale()
    {
        actualCat = new CatEntity(AnimalType.WILD, Skin.UNKNOWN,Gender.FEMALE, Breed.UNKNOWN);
        assertEquals(Gender.FEMALE, actualCat.getGender(), "Expecting Male Gender!");
    }

    @Test
    @Order(2)
    @DisplayName("Cat Breed Test BURMESE")
    public void genderCatBreed()
    {
        actualCat = new CatEntity(AnimalType.WILD, Skin.UNKNOWN,Gender.FEMALE, Breed.BURMESE);
        //act and assert
        assertEquals(Breed.BURMESE, actualCat.getBreed(), "Expecting Breed Maltese!");
    }

    @Test
    @Order(2)
    @DisplayName("Cat Speak Hiss Tests")
    public void dogGoesGrrTest()
    {
        actualCat = new CatEntity(AnimalType.WILD, Skin.UNKNOWN,Gender.UNKNOWN, Breed.UNKNOWN);
        assertEquals("The cat goes Hiss! Hiss!", actualCat.speaks(), "I was expecting hiss");
    }

    @Test
    @Order(2)
    @DisplayName("Cat Speak Prr Tests")
    public void catGoesPrrTest()
    {
        actualCat = new CatEntity(AnimalType.UNKNOWN, Skin.UNKNOWN,Gender.UNKNOWN, Breed.UNKNOWN);
        assertEquals("The cat goes Meow! Meow!", actualCat.speaks(), "I was expecting Prr");
    }
}
