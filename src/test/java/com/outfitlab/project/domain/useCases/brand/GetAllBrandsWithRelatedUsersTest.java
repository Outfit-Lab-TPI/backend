package com.outfitlab.project.domain.useCases.brand;

import com.outfitlab.project.domain.exceptions.PageLessThanZeroException;
import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.dto.UserWithBrandsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetAllBrandsWithRelatedUsersTest {

    private UserRepository userRepository = mock(UserRepository.class);
    private GetAllBrandsWithRelatedUsers getAllBrandsWithRelatedUsers;

    private final int VALID_PAGE = 5;
    private final int INVALID_PAGE = -1;

    @BeforeEach
    void setUp() {
        getAllBrandsWithRelatedUsers = new GetAllBrandsWithRelatedUsers(userRepository);
    }


    @Test
    public void shouldReturnPageOfUsersWithBrandsWhenPageIsValid() {
        Page<UserWithBrandsDTO> expectedPage = givenRepositoryReturnsPage(VALID_PAGE);

        Page<UserWithBrandsDTO> result = whenExecuteGetAllBrands(VALID_PAGE);

        thenReturnedPageMatchesExpected(result, expectedPage);
        thenRepositoryWasCalledOnce(VALID_PAGE);
    }

    @Test
    public void shouldThrowPageLessThanZeroExceptionWhenPageIsNegative() {
        assertThrows(PageLessThanZeroException.class,
                () -> whenExecuteGetAllBrands(INVALID_PAGE),
                "Se esperaba PageLessThanZeroException para una página negativa.");

        thenRepositoryWasNeverCalled();
    }


    //privadoss
    private Page<UserWithBrandsDTO> givenRepositoryReturnsPage(int page) {
        Page<UserWithBrandsDTO> mockPage = new PageImpl<>(Collections.singletonList(new UserWithBrandsDTO()));

        when(userRepository.getAllBrandsWithUserRelated(page)).thenReturn(mockPage);
        return mockPage;
    }

    private Page<UserWithBrandsDTO> whenExecuteGetAllBrands(int page) {
        return getAllBrandsWithRelatedUsers.execute(page);
    }

    private void thenReturnedPageMatchesExpected(Page<UserWithBrandsDTO> actual, Page<UserWithBrandsDTO> expected) {
        assertNotNull(actual, "La página resultante no debe ser nula.");
        assertEquals(expected, actual, "El resultado debe coincidir con la página simulada.");
    }

    private void thenRepositoryWasCalledOnce(int page) {
        verify(userRepository, times(1)).getAllBrandsWithUserRelated(page);
    }

    private void thenRepositoryWasNeverCalled() {
        verify(userRepository, never()).getAllBrandsWithUserRelated(anyInt());
    }
}