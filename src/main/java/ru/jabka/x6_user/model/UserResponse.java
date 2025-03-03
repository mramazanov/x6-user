package ru.jabka.x6_user.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
@Builder
public class UserResponse {
    private final Long idUser;
    private final String name;
    private final String email;
}
