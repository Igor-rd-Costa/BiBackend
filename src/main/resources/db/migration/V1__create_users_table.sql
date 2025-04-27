CREATE TABLE users (
    id UUID PRIMARY KEY UNIQUE NOT NULL,
    email varchar(254) UNIQUE NOT NULL,
    normalized_email varchar(254) UNIQUE NOT NULL,
    username varchar(100) NOT NULL,
    first_name varchar(254),
    last_name varchar(254),
    password varchar(60) NOT NULL,
    role smallint NOT NULL,
    email_verified boolean NOT NULL,
    online boolean NOT NULL
);

INSERT INTO users (id, email, normalized_email, username, first_name, last_name, password, role, email_verified, online)
VALUES (
    gen_random_uuid(), 'igor@gmail.com', 'IGOR@GMAIL.COM', 'igor',
    NULL, NULL,
    '$2a$10$.XshCuZMwPa6kvJImTSLc.Jvt.MDj3kwktJv7BGLlzgP5cR41CCcW',
    1, true, false
);