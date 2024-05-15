package funtionaltests;


import com.petstore.PetEntity;

import com.petstore.PetStoreReader;
import com.petstore.animals.attributes.PetType;
import com.petstoreservices.exceptions.PetDataStoreException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;

import org.junit.jupiter.api.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Get Pet Entity Tests by PetType only.  The class has some functional and error message tests
 * The test class is using rest assured to help with functional testing
 */
public class GetInventoryByPetTypeTests
{
    private List<PetEntity> expectedResults;
    private static Headers headers;
    @BeforeEach
    public void retrieveDataStore() throws PetDataStoreException {
        PetStoreReader psReader = new PetStoreReader();
        expectedResults = psReader.readJsonFromFile();

        RestAssured.baseURI  = "http://localhost:8080/";

        Header contentType = new Header("Content-Type", ContentType.JSON.toString());
        Header accept = new Header("Accept", ContentType.JSON.toString());
        headers = new Headers(contentType, accept);
    }

    @TestFactory
    @DisplayName("Pet Entity By Type[Dog] Tests")
    public Stream<DynamicNode> getInventoryDogsTest()
    {
        List<PetEntity> dogs =
                expectedResults.stream()
                        .filter(p -> p.getPetType().equals(PetType.DOG))
                        .sorted(Comparator.comparingInt(PetEntity::getPetId))
                        .collect(Collectors.toList());
        Response response = RestAssured.get("inventory/search/DOG");
        List<PetEntity> actualResults = response.body().jsonPath().getList(".", PetEntity.class);
        List<DynamicNode> testNodes = new ArrayList<DynamicNode>();

        List<DynamicTest> responseTests = Arrays.asList(
                DynamicTest.dynamicTest("Status Code 200 Test",
                        ()-> assertEquals(200, response.getStatusCode())),
                DynamicTest.dynamicTest("Content Type text/plain;charset=UTF-8 Test ",
                        ()-> assertTrue("text/plain;charset=UTF-8"
                                .equalsIgnoreCase( response.getContentType()))),
                DynamicTest.dynamicTest("Size of results test[" + dogs.size() + "]",
                                ()-> assertEquals(dogs.size(), actualResults.size())));

        DynamicContainer responseContainer = DynamicContainer.dynamicContainer("Response Tests", responseTests);
        testNodes.add(responseContainer);
        testNodes.add(PetEntityValidator.addPetEntityBodyTests(dogs, actualResults));

        return testNodes.stream();

    }



    @Test
    @DisplayName("Get Pet Entity By Type[Dog] Gherkin Tests")
    public void getInventoryDogsGherkinTest()
    {
        List<PetEntity> dogs =
                expectedResults.stream()
                        .filter(p -> p.getPetType().equals(PetType.DOG))
                        .sorted(Comparator.comparingInt(PetEntity::getPetId))
                        .collect(Collectors.toList());
        RestAssured.registerParser("application/json", Parser.JSON);
        List<PetEntity> actualResults =
               given()
                       .headers(headers)
               .when()
                       .get("inventory/search/DOG")
               .then()
                       .log().all()
                       .assertThat().statusCode(200)
                       .extract()
                       .jsonPath()
                       .getList(".", PetEntity.class);

       for(PetEntity dog: dogs)
       {
           boolean foundDog =false;
           for(PetEntity actualPet :  actualResults)
           {
               if(dog.getPetId() == actualPet.getPetId())
               {
                   foundDog=true;
                   assertThat(actualPet.getPetId(), equalTo(dog.getPetId()));
                   break;
               }
           }
           if(!foundDog)
           {
               assertThat("Pet Id " + dog.getPetId(), false, equalTo(true));
           }
       }


    }

    @TestFactory
    @DisplayName("Get Pet Entity By Missing Pet Entity Tests")
    public Stream<DynamicTest> getInventoryMissingPetEntityTest()
    {
        RestAssured.registerParser("application/json", Parser.JSON);
        BadRequestResponseBody body =
           given()
                .headers(headers)
           .when()
                .get("inventory/search/")
           .then()
                .log().all()
                .assertThat().statusCode(404)
                .extract()
                .jsonPath().getObject(".", BadRequestResponseBody.class);

        return body.executeTests("Not Found", "No static resource inventory/search.",
                "/inventory/search/", 404).stream();
    }
    @TestFactory
    @DisplayName("Get Pet Entity By Invalid Pet Entity Tests")
    public Stream<DynamicTest> getInventoryInvalidPetEntityTest()
    {
        RestAssured.registerParser("application/json", Parser.JSON);
        BadRequestResponseBody body =
             given()
                .headers(headers)
             .when()
                .get("inventory/search/FROGGER")
             .then()
                .log().all()
                .assertThat().statusCode(400)
                .extract()
                .jsonPath()
                .getObject(".", BadRequestResponseBody.class);

        return body.executeTests("Bad Request", "Failed to convert value of type 'java.lang.String' to " +
                 "required type 'com.petstore.animals.attributes.PetType'; Failed to convert from type " +
                 "[java.lang.String] to type [@org.springframework.web.bind.annotation.PathVariable " +
                 "com.petstore.animals.attributes.PetType] for value [FROGGER]",
                "/inventory/search/FROGGER", 400).stream();
    }

}
