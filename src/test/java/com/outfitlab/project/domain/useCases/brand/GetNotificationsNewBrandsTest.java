package com.outfitlab.project.domain.useCases.brand;

import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.dto.UserWithBrandsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetNotificationsNewBrandsTest {

    private UserRepository userRepository = mock(UserRepository.class);
    private GetNotificationsNewBrands getNotificationsNewBrands;

    private final int BRAND_COUNT = 3;

    @BeforeEach
    void setUp() {
        getNotificationsNewBrands = new GetNotificationsNewBrands(userRepository);
    }


    @Test
    public void shouldReturnListOfNotApprovedBrandsWhenBrandsExist() {
        List<UserWithBrandsDTO> expectedList = givenRepositoryReturnsBrands(BRAND_COUNT);

        List<UserWithBrandsDTO> result = whenExecuteGetNotifications();

        thenResultListMatchesExpected(result, expectedList, BRAND_COUNT);
        thenRepositoryWasCalledOnce();
    }

    @Test
    public void shouldReturnEmptyListWhenNoNotApprovedBrandsExist() {
        List<UserWithBrandsDTO> expectedEmptyList = givenRepositoryReturnsEmptyList();

        List<UserWithBrandsDTO> result = whenExecuteGetNotifications();

        thenResultListMatchesExpected(result, expectedEmptyList, 0);
        thenRepositoryWasCalledOnce();
    }


    //privadoss
    private List<UserWithBrandsDTO> givenRepositoryReturnsBrands(int count) {
        List<UserWithBrandsDTO> mockList = Collections.nCopies(count, new UserWithBrandsDTO());

        when(userRepository.getNotApprovedBrands()).thenReturn(mockList);
        return mockList;
    }

    private List<UserWithBrandsDTO> givenRepositoryReturnsEmptyList() {
        List<UserWithBrandsDTO> emptyList = Collections.emptyList();
        when(userRepository.getNotApprovedBrands()).thenReturn(emptyList);
        return emptyList;
    }

    private List<UserWithBrandsDTO> whenExecuteGetNotifications() {
        return getNotificationsNewBrands.execute();
    }

    private void thenResultListMatchesExpected(List<UserWithBrandsDTO> actual, List<UserWithBrandsDTO> expected, int expectedCount) {
        assertNotNull(actual, "La lista resultante no debe ser nula.");
        assertEquals(expectedCount, actual.size(), "El tamaño de la lista debe coincidir.");
        assertEquals(expected, actual, "El contenido de la lista debe coincidir con la lista simulada.");
    }

    private void thenRepositoryWasCalledOnce() {
        verify(userRepository, times(1)).getNotApprovedBrands();
    }
}