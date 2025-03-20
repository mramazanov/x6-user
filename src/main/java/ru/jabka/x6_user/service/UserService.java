package ru.jabka.x6_user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.jabka.x6_user.exception.BadRequestException;
import ru.jabka.x6_user.model.UserRequest;
import ru.jabka.x6_user.model.UserResponse;
import ru.jabka.x6_user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    public UserResponse createUser(final UserRequest userRequest) {
        validate(userRequest);
        return userRepository.insert(userRequest);
    }

    @Transactional(rollbackFor = Exception.class)
    @CachePut(value = "user", key = "#id")
    public UserResponse updateUser(final Long id, final UserRequest userRequest) {
        validate(userRequest);
        return userRepository.update(id, userRequest);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "userExist", key = "#id")
    public Boolean exist(final Long id) {
        return userRepository.exist(id);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "user", key = "#id")
    public UserResponse getUserById(final Long id) {
        return userRepository.getUserById(id);
    }

    private void validate(final UserRequest userRequest) {
        if(userRequest == null) {
            throw new BadRequestException("Введите информацию о пользователе");
        }
        if(!StringUtils.hasText(userRequest.getName())) {
            throw new BadRequestException("Укажите имя пользователя");
        }
        if(!StringUtils.hasText(userRequest.getEmail())) {
            throw new BadRequestException("Укажите почту пользователя");
        }
    }
}