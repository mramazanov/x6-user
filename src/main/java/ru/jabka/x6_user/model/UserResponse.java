package ru.jabka.x6_user.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
@Data
@Builder
public class UserResponse {
    private final Long id;
    private final String name;
    private final String email;
}