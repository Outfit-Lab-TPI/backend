package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.UserModel;
import com.outfitlab.project.presentation.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetAllUsersTest {

    private UserRepository userRepository = mock(UserRepository.class);
    private GetAllUsers getAllUsers;

    private final int USER_COUNT = 3;

    @BeforeEach
    void setUp() {
        getAllUsers = new GetAllUsers(userRepository);
    }


    @Test
    public void shouldReturnListOfUsersWhenUsersAreFound() {
        List<UserModel> mockModels = givenRepositoryReturnsUserModels(USER_COUNT);
        List<UserDTO> expectedDTOs = createExpectedUserDTOs(USER_COUNT);

        givenRepositoryReturnsUserModels(mockModels);

        List<UserDTO> result = whenExecuteGetAllUsers();

        thenResultMatchesExpectedList(result, expectedDTOs, USER_COUNT);
        thenRepositoryFindAllWasCalled(1);
    }

    @Test
    public void shouldReturnEmptyListWhenNoUsersAreFound() {
        givenRepositoryReturnsUserModels(Collections.emptyList());

        List<UserDTO> result = whenExecuteGetAllUsers();

        thenResultMatchesExpectedList(result, Collections.emptyList(), 0);
        thenRepositoryFindAllWasCalled(1);
    }

    @Test
    public void shouldPropagateRuntimeExceptionWhenRepositoryFails() {
        givenRepositoryThrowsRuntimeException();

        assertThrows(RuntimeException.class,
                () -> whenExecuteGetAllUsers(),
                "Se espera que el RuntimeException se propague si el repositorio falla.");

        thenRepositoryFindAllWasCalled(1);
    }


    //privadosss
    private void givenRepositoryReturnsUserModels(List<UserModel> models) {
        when(userRepository.findAllWithRoleUserAndAdmin()).thenReturn(models);
    }

    private void givenRepositoryThrowsRuntimeException() {
        doThrow(new RuntimeException("Simulated DB error"))
                .when(userRepository).findAllWithRoleUserAndAdmin();
    }

    private List<UserModel> givenRepositoryReturnsUserModels(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> mock(UserModel.class))
                .toList();
    }

    private List<UserDTO> createExpectedUserDTOs(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> mock(UserDTO.class))
                .toList();
    }

    private List<UserDTO> whenExecuteGetAllUsers() {
        return getAllUsers.execute();
    }

    private void thenResultMatchesExpectedList(List<UserDTO> actual, List<UserDTO> expected, int expectedCount) {
        assertNotNull(actual, "La lista resultante no debe ser nula.");
        assertEquals(expectedCount, actual.size(), "El tamaño de la lista de DTOs debe coincidir.");
    }

    private void thenRepositoryFindAllWasCalled(int times) {
        verify(userRepository, times(times)).findAllWithRoleUserAndAdmin();
    }
}