CREATE TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    application varchar(50),
    uri varchar(50),
    ip varchar(20),
    datetime timestamp
);