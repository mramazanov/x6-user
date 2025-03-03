CREATE SCHEMA x6user;

CREATE TABLE x6user.user (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    email VARCHAR UNIQUE NOT NULL
);

CREATE TABLE x6user.meta_user_create (
    id INT NOT NULL PRIMARY KEY REFERENCES x6user.user(id),
    create_date TIMESTAMP WITH TIME ZONE
);

CREATE TABLE x6user.meta_user_update (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES x6user.user(id),
    update_date TIMESTAMP WITH TIME ZONE
);