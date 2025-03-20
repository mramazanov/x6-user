package ru.jabka.x6_user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.jabka.x6_user.model.UserRequest;
import ru.jabka.x6_user.model.UserResponse;
import ru.jabka.x6_user.service.UserService;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "Пользователь")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Создать пользователя")
    public UserResponse createUser(final @RequestBody UserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    @PatchMapping
    @Operation(summary = "Обновить пользователя")
    public UserResponse updateUser(@RequestParam final Long id, @RequestBody final UserRequest userRequest) {
        return userService.updateUser(id, userRequest);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по id")
    public UserResponse getUserById(@PathVariable final Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/exist/{id}")
    @Operation(summary = "Проверить существование пользователя по id")
    public Boolean exist(@PathVariable final Long id) {
        return userService.exist(id);
    }
}