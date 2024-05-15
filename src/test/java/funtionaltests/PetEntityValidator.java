package funtionaltests;

import com.petstore.PetEntity;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Pet Entity Validator class.  Validates a few of the pet entity values
 */
public abstract class PetEntityValidator
{
    /**
     * Execute the response body tests
     * @param expectedResults - Expected answer(s)
     * @param actualResults - actual results from the software under test
     * @return - the test results in a DynamicContainer
     */
    public static DynamicContainer addPetEntityBodyTests(List<PetEntity> expectedResults, List<PetEntity> actualResults)
    {

        List<DynamicNode> responseBodyNodes = new ArrayList<DynamicNode>();
        for(PetEntity dog: expectedResults)
        {
            List<DynamicTest> responseBodyTests = new ArrayList<DynamicTest>();
            boolean foundDog =false;
            for(PetEntity actualPet :  actualResults)
            {
                if(dog.getPetId() == actualPet.getPetId())
                {

                    foundDog=true;
                    responseBodyTests.add(DynamicTest.dynamicTest("Pet Id[" + dog.getPetId() + "]",
                            ()-> assertEquals(dog.getPetId(), actualPet.getPetId())));
                    responseBodyTests.add(DynamicTest.dynamicTest("Pet Type[" + dog.getPetType() + "]",
                            ()-> assertEquals(dog.getPetType(), actualPet.getPetType())));
                    responseBodyTests.add(DynamicTest.dynamicTest("Breed[" + dog.getBreed() + "]",
                            ()-> assertEquals(dog.getBreed(), actualPet.getBreed())));
                    responseBodyTests.add(DynamicTest.dynamicTest("Gender[" + dog.getGender() + "]",
                            ()-> assertEquals(dog.getGender(), actualPet.getGender())));
                    responseBodyTests.add(DynamicTest.dynamicTest("Cost[" + dog.getCost() + "]",
                            ()-> assertEquals(dog.getCost(), actualPet.getCost())));
                    //todo  expand validations to include other variables
                    DynamicContainer petItemContainer = DynamicContainer.dynamicContainer(
                            "Response Body Tests Pet Id[" + dog.getPetId() +"]", responseBodyTests);
                    responseBodyNodes.add(petItemContainer);
                    break;
                }
            }
            if(!foundDog)
            {
                responseBodyTests.add(DynamicTest.dynamicTest("Pet Id[" + dog.getPetId() + "]",
                        ()-> assertEquals(dog.getPetId(), 0)));
                DynamicContainer petItemContainer = DynamicContainer.dynamicContainer("Response Body Tests[" +
                        dog.getPetId() +"]", responseBodyTests);
                responseBodyNodes.add(petItemContainer);
            }
        }

        return DynamicContainer.dynamicContainer("Response Body Tests", responseBodyNodes);
    }
}
