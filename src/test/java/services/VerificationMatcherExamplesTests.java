package services;


import com.petstore.exceptions.PetNotFoundSaleException;
import com.petstoreservices.exceptions.PetDataStoreException;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Arrays;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VerificationMatcherExamplesTests {
    /**
     * Inner class used for argument matcher examples for value based ArgumentMatchers
     */
    class IsListOfTwoElements implements ArgumentMatcher<List> {

        @Override
        public boolean matches(List list) {
            return ((List) list).size() == 2;
        }

        @Override
        public Class<?> type() {
            return ArgumentMatcher.super.type();
        }
    }
    @Test
    @DisplayName("Verification Matcher AddAll Test")
    public void verificationMatcherAddAllTest() throws PetNotFoundSaleException, PetDataStoreException
    {
        List mock = mock(List.class);
        //arrange
        Mockito.when(mock.addAll(argThat(new IsListOfTwoElements()))).thenReturn(true);
        //act
        mock.addAll(Arrays.asList("one", "two"));
        //assert
        verify(mock).addAll(argThat(list -> list.size() == 2));//verification matchers

    }

    @Test
    @DisplayName("Verification Matcher AddAll Find value 1 Test")
    public void verificationMatcherFindValue1() throws PetNotFoundSaleException, PetDataStoreException
    {
        List mock = mock(List.class);
        //arrange
        Mockito.when(mock.addAll(argThat(new IsListOfTwoElements()))).thenReturn(true);
        //act
        mock.addAll(Arrays.asList("one", "two"));
        //assert
        verify(mock).addAll(argThat(list -> list.contains("one")));
    }
    @Test
    @DisplayName("Verification Matcher AddAll Find value 2 Test")
    public void verificationMatcherFindValue2() throws PetNotFoundSaleException, PetDataStoreException
    {
        List mock = mock(List.class);
        //arrange
        Mockito.when(mock.addAll(argThat(new IsListOfTwoElements()))).thenReturn(true);
        //act
        mock.addAll(Arrays.asList("one", "two"));
        //assert
        verify(mock).addAll(argThat(list -> list.contains("two")));
    }

    @Test
    @DisplayName("Verification Matcher AddAll Find value 3 Test")
    public void verificationMatcherFindValue3() throws PetNotFoundSaleException, PetDataStoreException
    {
        List mock = mock(List.class);
        //arrange
        Mockito.when(mock.addAll(argThat(new IsListOfTwoElements()))).thenReturn(true);
        //act
        mock.addAll(Arrays.asList("one", "three"));
        //assert verify(mock, times(0)).addAll(argThat(list -> list.contains("three")));
        verify(mock).addAll(argThat(list -> list.contains("three")));
    }
}
