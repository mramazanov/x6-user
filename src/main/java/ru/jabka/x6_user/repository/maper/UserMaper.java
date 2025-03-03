package ru.jabka.x6_user.repository.maper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ru.jabka.x6_user.model.UserResponse;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserMaper implements RowMapper<UserResponse> {
    @Override
    public UserResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserResponse.builder()
                .idUser(rs.getLong("id"))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .build();
    }
}