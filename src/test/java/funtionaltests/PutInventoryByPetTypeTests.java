package funtionaltests;


import com.petstore.AnimalType;
import com.petstore.PetEntity;
import com.petstore.PetStoreReader;
import com.petstore.animals.DogEntity;
import com.petstore.animals.attributes.Breed;
import com.petstore.animals.attributes.Gender;
import com.petstore.animals.attributes.PetType;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.petstore.animals.attributes.Skin.FUR;
import static io.restassured.RestAssured.given;

/**
 * Put Pet Entity Tests by PetType only.  The class has some functional and error message tests
 * The test class is using rest assured to help with functional testing
 */
public class PutInventoryByPetTypeTests
{
    private static Headers headers;
    @BeforeEach
    public void retrieveDataStore()
    {
        RestAssured.baseURI  = "http://localhost:8080/";

        Header contentType = new Header("Content-Type", ContentType.JSON.toString());
        Header accept = new Header("Accept", ContentType.JSON.toString());
        headers = new Headers(contentType, accept);
    }

    @TestFactory
    @DisplayName("Create Pet Entity Dog Tests")
    public Stream<DynamicNode> putInventoryDogsTest() throws Exception
    {
        PetEntity itemCreated =
                given()
                        .headers(headers)
                        .and()
                        .body(new DogEntity(AnimalType.DOMESTIC, FUR, Gender.FEMALE, Breed.GERMAN_SHEPHERD,
                                new BigDecimal("225.00")))
                        .when()
                        .post("inventory/petType/DOG")
                        .then()
                        .log().all()
                        .assertThat().statusCode(200)
                        .assertThat().contentType("application/json")
                        .extract()
                        .jsonPath()
                        .getObject(".", PetEntity.class);

        PetStoreReader psReader = new PetStoreReader();
        List<PetEntity> expectedResults = psReader.readJsonFromFile();
        List<PetEntity> actualDogs =
                expectedResults.stream()
                        .filter(p -> p.getPetType().equals(PetType.DOG) && p.getPetId()==itemCreated.getPetId())
                        .sorted(Comparator.comparingInt(PetEntity::getPetId))
                        .collect(Collectors.toList());

        List<DynamicNode> testNodes = new ArrayList<DynamicNode>();

        testNodes.add(PetEntityValidator.addPetEntityBodyTests(Arrays.asList(itemCreated), actualDogs));
        return testNodes.stream();

    }

    @TestFactory
    @DisplayName("Put Pet Entity By Missing Pet Entity Tests")
    public Stream<DynamicTest> putInventoryMissingPetEntityTest()
    {
        RestAssured.registerParser("application/json", Parser.JSON);
        BadRequestResponseBody body =
                given()
                        .headers(headers)
                        .and()
                        .body(new DogEntity(AnimalType.DOMESTIC, FUR, Gender.FEMALE, Breed.GERMAN_SHEPHERD,
                                new BigDecimal("225.00"), 4))
                        .param("petType", PetType.DOG)
                        .param("petId", 9)
                        .when()
                        .put("inventory/update/")
                        .then()
                        .log().all()
                        .assertThat().statusCode(404)
                        .assertThat().contentType("application/json")
                        .extract()
                        .jsonPath()
                        .getObject(".", BadRequestResponseBody.class);

        return body.executeTests("Not Found", "No static resource inventory/update.",
                "/inventory/update/", 404).stream();
    }
    @TestFactory
    @DisplayName("Put Pet Entity By Invalid Pet Entity Tests")
    public Stream<DynamicTest> postInventoryInvalidPetEntityTest()
    {
        RestAssured.registerParser("application/json", Parser.JSON);
        BadRequestResponseBody body =  given()
                .headers(headers)
                .and()
                .body(new DogEntity(AnimalType.DOMESTIC, FUR, Gender.MALE, Breed.GERMAN_SHEPHERD,
                        new BigDecimal("300.00"), 4))
                .param("petType", "BIGFOOT")
                .param("petId", 4)
                .when()
                .put("inventory/update")
                .then()
                .log().all()
                .assertThat().statusCode(400)
                .assertThat().contentType("application/json")
                .extract()
                .jsonPath()
                .getObject(".", BadRequestResponseBody.class);

        return body.executeTests("Bad Request", "Failed to convert value of type 'java.lang.String' to " +
                        "required type 'com.petstore.animals.attributes.PetType'; Failed to convert from type " +
                        "[java.lang.String] to type [@org.springframework.web.bind.annotation.RequestParam " +
                        "com.petstore.animals.attributes.PetType] for value [BIGFOOT]",
                "/inventory/update", 400).stream();
    }

}
