package ru.jabka.x6_user.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
@Builder
public class UserRequest {
    private final String name;
    private final String email;
}
