CREATE SEQUENCE IF NOT EXISTS TRANSACTION_SEQUENCE START WITH 1 BELONGS_TO_TABLE;

CREATE TABLE IF NOT EXISTS USER(
    "USER_ID" BIGINT DEFAULT NEXT VALUE FOR "TRANSACTION_SEQUENCE" NOT NULL NULL_TO_DEFAULT SEQUENCE "TRANSACTION_SEQUENCE",
    "FIRST_NAME" VARCHAR(255),
    "LAST_NAME" VARCHAR(255),
    "USERNAME" VARCHAR(255),
    PRIMARY KEY("USER_ID")
);

CREATE TABLE IF NOT EXISTS ACCOUNT(
    "ACCOUNT_NUMBER" INTEGER,
    "USER_USER_ID" BIGINT NOT NULL,
    PRIMARY KEY("USER_USER_ID")
);

ALTER TABLE ACCOUNT ADD FOREIGN KEY (USER_USER_ID) REFERENCES USER (USER_ID) NOCHECK;

CREATE TABLE IF NOT EXISTS TRANSACTION(
    "TRANSACTION_ID" VARCHAR(255) NOT NULL,
    "AMOUNT" DOUBLE,
    "BALANCE" DOUBLE,
    "CREATION_DATE" TIMESTAMP,
    "TYPE" INTEGER,
    "ACCOUNT_USER_USER_ID" BIGINT,
    PRIMARY KEY("TRANSACTION_ID")
);

ALTER TABLE TRANSACTION ADD FOREIGN KEY (ACCOUNT_USER_USER_ID) REFERENCES ACCOUNT (USER_USER_ID) NOCHECK;