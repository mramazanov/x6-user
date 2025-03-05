package ru.jabka.x6_user.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.jabka.x6_user.exception.BadRequestException;
import ru.jabka.x6_user.model.UserRequest;
import ru.jabka.x6_user.model.UserResponse;
import ru.jabka.x6_user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void createUser_valid() {
        UserRequest userRequest = buildUserRequest("Vasja", "Vasja@mail.ru");
        UserResponse userResponse = buildUserResponse(1L, userRequest);
        Mockito.when(userRepository.insert(userRequest)).thenReturn(userResponse);
        UserResponse createdUser = userService.createUser(userRequest);
        Assertions.assertEquals(userResponse, createdUser);
        Mockito.verify(userRepository).insert(userRequest);
    }

    @Test
    public void shouldReturnError_whenNameIsEmpty() {
        UserRequest userRequest = buildUserRequest("", "Vasja@mail.ru");
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class, () -> userService.createUser(userRequest)
        );
        Assertions.assertEquals("Укажите имя пользователя", exception.getMessage());
    }

    @Test
    public void updateUser_valid() {
        UserRequest userRequest = buildUserRequest("Vasja", "Vasja@mail.ru");
        UserResponse userResponse = buildUserResponse(1L, userRequest);
        Mockito.when(userRepository.update(1L, userRequest)).thenReturn(userResponse);
        UserResponse createdUser = userService.updateUser(1L, userRequest);
        Assertions.assertEquals(userResponse, createdUser);
        Mockito.verify(userRepository).update(1L,userRequest);
    }

    @Test
    public void getUserById_valid() {
        UserRequest userRequest = buildUserRequest("Vasja", "Vasja@mail.ru");
        UserResponse userResponse = buildUserResponse(1L, userRequest);
        Mockito.when(userRepository.getUserById(1L)).thenReturn(userResponse);
        UserResponse foundUser = userService.getUserById(1L);
        Assertions.assertEquals(userResponse, foundUser);
        Mockito.verify(userRepository).getUserById(1L);
    }

    @Test
    public void shouldReturnError_whenGetUserById() {
        Mockito.when(userRepository.getUserById(100L)).thenThrow(new BadRequestException("Не удалось найти пользователя с id = 100"));
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class, () -> userService.getUserById(100L)
        );
        Assertions.assertEquals("Не удалось найти пользователя с id = 100", exception.getMessage());
    }

    @Test
    public void shouldReturnTrue_whenExistUser() {
        UserRequest userRequest = buildUserRequest("Vasja", "Vasja@mail.ru");
        Mockito.when(userRepository.exist(1L)).thenReturn(Boolean.TRUE);
        Boolean existUser = userService.exist(1L);
        Assertions.assertTrue(existUser);
        Mockito.verify(userRepository).exist(1L);
    }

    private UserRequest buildUserRequest(final String name, final String email) {
        return UserRequest.builder()
                .name(name)
                .email(email)
                .build();
    }

    private UserResponse buildUserResponse(final Long id, final UserRequest userRequest) {
        return UserResponse.builder()
                .id(id)
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .build();
    }
}
