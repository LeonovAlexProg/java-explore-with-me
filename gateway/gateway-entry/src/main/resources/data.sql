INSERT INTO roles (name) VALUES ('ROLE_ADMIN');
INSERT INTO roles (name) VALUES ('ROLE_PRIVATE');

INSERT INTO users (username, password) VALUES ('admin', '$2a$12$rASpqOhi9AqjQm0fbJslbOIWjsuiGlxTQPUQl2Wu.7kBs0zlahS0C');
INSERT INTO users_roles(user_id, roles_id) VALUES (1, 1);