package ru.jabka.x6_user.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequest {
    private final String name;
    private final String email;
}