package ru.jabka.x6_user.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import ru.jabka.x6_user.exception.BadRequestException;
import ru.jabka.x6_user.model.UserRequest;
import ru.jabka.x6_user.model.UserResponse;
import ru.jabka.x6_user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository x6UserRepository;

    public UserResponse createX6User(final UserRequest x6UserRequest) {
        validate(x6UserRequest);
        return x6UserRepository.createUser(x6UserRequest);
    }

    public UserResponse updateX6User(final Long userId, final UserRequest userRequest) {
        validate(userRequest);
        return x6UserRepository.updateUser(userId, userRequest);
    }

    public Boolean isExistUser(final Long idUser) {
        return x6UserRepository.isExistUser(idUser);
    }

    public UserResponse getUserById(final Long userId) {
        return x6UserRepository.getUserById(userId);
    }

    private void validate(final UserRequest x6User) {
        if(x6User == null) {
            throw new BadRequestException("Введите информацию о пользователе");
        }
        if(!StringUtils.hasText(x6User.getName())) {
            throw new BadRequestException("Введите имя пользователя");
        }
        if(!StringUtils.hasText(x6User.getEmail())) {
            throw new BadRequestException("Введите имя пользователя");
        }
    }

}