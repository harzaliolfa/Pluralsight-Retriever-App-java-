CREATE TABLE IF NOT EXISTS COURSES (
    ID VARCHAR PRIMARY KEY NOT NULL, -- Unique identifier for each course
    NAME VARCHAR NOT NULL,           -- Course name
    DURATION INTEGER  NOT NULL,                  -- Course length in minutes
    URL VARCHAR(2048) NOT NULL       -- Course URL (max length: 2048 characters)
);