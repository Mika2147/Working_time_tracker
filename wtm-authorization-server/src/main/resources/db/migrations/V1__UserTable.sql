CREATE TABLE users(
    id UUID NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    isAdmin BOOLEAN NOT NULL
);