CREATE TABLE users (
    id UUID PRIMARY KEY,
    email varchar(254) UNIQUE NOT NULL,
    "normalizedEmail" varchar(254) UNIQUE NOT NULL,
    username varchar(100) NOT NULL,
    "firstName" varchar(254),
    "lastName" varchar(254),
    password varchar(60) NOT NULL,
    role smallint NOT NULL,
    "emailVerified" boolean NOT NULL,
    online boolean NOT NULL
);

INSERT INTO users (id, email, "normalizedEmail", username, "firstName", "lastName", password, role, "emailVerified", online)
VALUES (
    gen_random_uuid(), 'igor@gmail.com', 'IGOR@GMAIL.COM', 'igor',
    NULL, NULL,
    '$2a$10$.XshCuZMwPa6kvJImTSLc.Jvt.MDj3kwktJv7BGLlzgP5cR41CCcW',
    1, true, false
);