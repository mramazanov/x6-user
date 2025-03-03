package ru.jabka.x6_user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import ru.jabka.x6_user.model.UserRequest;
import ru.jabka.x6_user.model.UserResponse;
import ru.jabka.x6_user.service.UserService;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "Пользователь")
public class UserController {

    private final UserService x6UserService;

    @PostMapping
    @Operation(summary = "Создать пользователя")
    public UserResponse createUser(final @RequestBody UserRequest x6User) {
        return x6UserService.createX6User(x6User);
    }

    @PatchMapping
    @Operation(summary = "Обновить пользователя")
    public UserResponse updateUser(@RequestParam final Long userId, @RequestBody final UserRequest userRequest) {
        return x6UserService.updateX6User(userId, userRequest);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по id")
    public UserResponse getUserById(@PathVariable final Long id) {
        return x6UserService.getUserById(id);
    }

    @GetMapping("/userexists/{id}")
    @Operation(summary = "Проверить существование пользователя по id")
    public Boolean isExistUser(@PathVariable final Long id) {
        return x6UserService.isExistUser(id);
    }
}