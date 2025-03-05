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
            WITH insert_user AS (
                INSERT INTO x6user.user (name, email)
                VALUES (:name, :email)
                RETURNING *
            ), insert_meta_user_creation AS (
                INSERT INTO x6user.meta_user_create (id, create_date)
                (SELECT insert_user.id, now() FROM insert_user)
            )
            SELECT * FROM insert_user;
            """;

    private static final String UPDATE = """
            WITH update_user AS (
                UPDATE x6user.user
                SET name = :name, email = :email
                WHERE id = :id
                RETURNING *
            ), insert_meta_user_update AS (
                INSERT INTO x6user.meta_user_update (user_id, update_date)
                (SELECT update_user.id, now() FROM update_user)
            )
            SELECT * FROM update_user;
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