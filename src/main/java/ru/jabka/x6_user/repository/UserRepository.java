package ru.jabka.x6_user.repository;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import ru.jabka.x6_user.exception.BadRequestException;
import ru.jabka.x6_user.model.UserRequest;
import ru.jabka.x6_user.model.UserResponse;
import ru.jabka.x6_user.repository.maper.UserMapper;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private static final String INSERT = """
            INSERT INTO x6user.user (name, email, create_date)
            VALUES (:name, :email, now())
            RETURNING *
            """;

    private static final String UPDATE = """
                UPDATE x6user.user
                SET name = :name, email = :email, update_date = now()
                WHERE id = :id
                RETURNING *
            """;

    private static final String EXISTS = """
            SELECT EXISTS(SELECT * FROM x6user.user WHERE id = :id);
            """;

    private static final String GET_BY_ID = """
            SELECT * FROM x6user.user WHERE id = :id;
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    public UserResponse insert(final UserRequest userRequest) {
        return jdbcTemplate.queryForObject(INSERT, userToSql(null, userRequest), userMapper);
    }

    public UserResponse update(final Long id, final UserRequest userRequest) {
        return jdbcTemplate.queryForObject(UPDATE, userToSql(id, userRequest), userMapper);
    }

    public Boolean exist(final Long id) {
        return jdbcTemplate.queryForObject(EXISTS, userToSql(id, null), Boolean.class);
    }

    public UserResponse getUserById(final Long id) {
        try {
            return jdbcTemplate.queryForObject(GET_BY_ID, userToSql(id, null), userMapper);
        } catch (Exception e){
            throw new BadRequestException(String.format("Не удалось найти пользователя с id = %d", id));
        }
    }

    private MapSqlParameterSource userToSql(final Long id, final UserRequest x6UserRequest){
        final MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("id", id);
        if (x6UserRequest != null) {
            params.addValue("name", x6UserRequest.getName());
            params.addValue("email", x6UserRequest.getEmail());
        }

        return params;
    }
}