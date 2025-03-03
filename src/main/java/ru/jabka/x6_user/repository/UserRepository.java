package ru.jabka.x6_user.repository;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import ru.jabka.x6_user.exception.BadRequestException;
import ru.jabka.x6_user.model.UserRequest;
import ru.jabka.x6_user.model.UserResponse;
import ru.jabka.x6_user.repository.maper.UserMaper;

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
    private final UserMaper x6UserMaper;

    public UserResponse createUser(final UserRequest x6UserRequest) {
        return jdbcTemplate.queryForObject(INSERT, userToSql(null, x6UserRequest), x6UserMaper);
    }

    public Boolean isExistUser(final Long userId) {
        return jdbcTemplate.queryForObject(EXISTS, userToSql(userId, null), Boolean.class);
    }

    public UserResponse updateUser(final Long userId, final UserRequest x6UserRequest) {
        return jdbcTemplate.queryForObject(UPDATE, userToSql(userId, x6UserRequest), x6UserMaper);
    }

    public UserResponse getUserById(final Long userId) {
        try {
            return jdbcTemplate.queryForObject(GET_BY_ID, userToSql(userId, null), x6UserMaper);
        } catch (Exception e){
            throw new BadRequestException(String.format("Не удалось найти пользователя с id = %d", userId));
        }
    }

    private MapSqlParameterSource userToSql(final Long userId, final UserRequest x6UserRequest){
        final MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("id", userId);

        if (x6UserRequest != null) {
            params.addValue("name", x6UserRequest.getName());
            params.addValue("email", x6UserRequest.getEmail());
        }

        return params;
    }
}