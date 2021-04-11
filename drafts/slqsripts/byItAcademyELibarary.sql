-- -----------------------------------------------------
-- Schema by_it_academy_grodno_elibrary
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS by_it_academy_grodno_elibrary;
CREATE SCHEMA IF NOT EXISTS by_it_academy_grodno_elibrary;
USE by_it_academy_grodno_elibrary;

-- -----------------------------------------------------
-- Table by_it_academy_grodno_elibrary.user
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS user
(
    id           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE COMMENT 'User id',
    email        VARCHAR(80) UNIQUE COMMENT 'Email',
    username     VARCHAR(45)     NOT NULL COMMENT 'Users name',
    first_name   VARCHAR(45) COMMENT 'First name',
    last_name    VARCHAR(45) COMMENT 'Last name',
    phone_number JSON COMMENT 'User phone numbers',
    address_id   BIGINT UNSIGNED COMMENT 'Id of address',
    gender       CHAR(1)         NOT NULL DEFAULT 'u' COMMENT 'Gender: m-male, f-female, u-unknown',
    birthday     DATE COMMENT 'Date of birthday',
    password     VARCHAR(64),
    enabled      BOOLEAN                  DEFAULT TRUE COMMENT 'User lock',
    CONSTRAINT pk_user PRIMARY KEY (id)
);

-- -----------------------------------------------------
-- Table by_it_academy_grodno_elibrary.user_social_id
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS user_social_id
(
    user_id   BIGINT UNSIGNED NOT NULL COMMENT 'Users id',
    social_id BIGINT UNSIGNED NOT NULL UNIQUE COMMENT 'Users social id (ex. facebook, github, google)',
    CONSTRAINT fk_user_user_social_id FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- Table by_it_academy_grodno_elibrary.user_story
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS users_story
(
    user_id BIGINT UNSIGNED NOT NULL UNIQUE COMMENT 'Users id',
    created DATETIME        NOT NULL DEFAULT NOW() COMMENT 'Date of creating',
    updated DATETIME        NOT NULL DEFAULT NOW() COMMENT 'Date of updated',
    CONSTRAINT fk_user_users_story FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- Trigger by insert user
-- -----------------------------------------------------
CREATE
    TRIGGER user_insert
    AFTER INSERT
    ON user
    FOR EACH ROW
    INSERT INTO users_story (user_id, created, updated)
    VALUES (new.id, NOW(), NOW());

CREATE
    TRIGGER user_update
    AFTER UPDATE
    ON user
    FOR EACH ROW
    UPDATE users_story
    SET users_story.updated = NOW()
    WHERE users_story.user_id = NEW.id;

-- -----------------------------------------------------
-- Table by_it_academy_grodno_elibrary.role
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS role
(
    id   INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE COMMENT 'Roles id',
    role_name VARCHAR(45)  NOT NULL UNIQUE COMMENT 'Roles name',
    CONSTRAINT pk_role PRIMARY KEY (id)
);

-- -----------------------------------------------------
-- Table by_it_academy_grodno_elibrary.user_has_role
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS user_has_role
(
    user_id BIGINT UNSIGNED NOT NULL,
    role_id INT UNSIGNED    NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_has_role_role1 FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_user_has_role_user1 FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- Table by_it_academy_grodno_elibrary.address
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS address
(
    id   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE COMMENT 'A surrogate primary key used to uniquely identify each address in the table.',
    address_1    VARCHAR(50) COMMENT 'The first line of an address.',
    address_2    VARCHAR(50) COMMENT 'An optional second line of an address.',
    district     VARCHAR(50)     NOT NULL COMMENT 'The region of an address, this may be a state, province, prefecture, etc.',
    city_name    VARCHAR(20)     NOT NULL COMMENT 'A foreign key pointing to the city table.',
    postal_code  VARCHAR(32) COMMENT 'The postal code or ZIP code of the address (where applicable).',
    house        VARCHAR(10)     NOT NULL COMMENT 'The house number',
    apt          VARCHAR(10) COMMENT 'Number of room',
    last_updated DATE COMMENT 'Date of creating or last updating',
    CONSTRAINT pk_address_id PRIMARY KEY (id)
);

ALTER TABLE user
    ADD
        CONSTRAINT fk_user_address
            FOREIGN KEY (address_id) REFERENCES address (id) ON DELETE CASCADE ON UPDATE NO ACTION;

-- -----------------------------------------------------
-- Table by_it_academy_grodno_elibrary.books_category
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS books_category
(
    id   INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE COMMENT 'Books category',
    category_name VARCHAR(45)  NOT NULL UNIQUE COMMENT 'Name of books category',
    CONSTRAINT pk_category PRIMARY KEY (id)
);

-- -----------------------------------------------------
-- Table by_it_academy_grodno_elibrary.section
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS books_section
(
    id   INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE COMMENT 'Books sections id',
    section_name VARCHAR(45)  NOT NULL UNIQUE COMMENT 'Section of books category',
    category_id  INT UNSIGNED NOT NULL COMMENT 'Category id from category table',
    CONSTRAINT pk_section PRIMARY KEY (id),
    CONSTRAINT fk_section_category FOREIGN KEY (category_id) REFERENCES books_category (id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- Table by_it_academy_grodno_elibrary.publisher
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS publisher
(
    publisher_id   INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE COMMENT 'Books publisher id',
    publisher_name VARCHAR(100) NOT NULL UNIQUE COMMENT 'Name of books publisher',
    CONSTRAINT pk_book_publisher PRIMARY KEY (publisher_id)
);

-- -----------------------------------------------------
-- Table by_it_academy_grodno_elibrary.author
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS author
(
    author_id   INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE COMMENT 'The author id',
    author_name VARCHAR(50)  NOT NULL UNIQUE COMMENT 'The author name',
    CONSTRAINT pk_author PRIMARY KEY (author_id)
);

-- -----------------------------------------------------
-- Table by_it_academy_grodno_elibrary.book
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS book
(
    book_id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE COMMENT 'Book id',
    isbn_10         VARCHAR(10)     NOT NULL COMMENT 'Books isbn in format ISBN-10',
    isbn_13         VARCHAR(13)     NOT NULL COMMENT 'Boks isbn in format ISBN-13',
    section_id      INT UNSIGNED    NOT NULL COMMENT 'Sections id',
    publisher       INT UNSIGNED COMMENT 'Book publisher',
    language        VARCHAR(3) COMMENT 'Language of book by alpha-3/ISO 639-2 Code',
    date_publishing DATE COMMENT 'The year and month of publishing',
    picture_url     VARCHAR(2083) COMMENT 'Books cover image',
    total_count     TINYINT UNSIGNED COMMENT 'Total count of books',
    available_count TINYINT UNSIGNED COMMENT 'Available count',
    #cover INT COMMENT 'The books cover',
    CONSTRAINT pk_book PRIMARY KEY (book_id),
    CONSTRAINT fk_book_publisher FOREIGN KEY (publisher) REFERENCES publisher (publisher_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- Table by_it_academy_grodno_elibrary.attribute
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS attribute
(
    book_id   BIGINT UNSIGNED NOT NULL UNIQUE COMMENT 'The book id',
    attribute JSON            NOT NULL COMMENT 'The books attributes',
    CONSTRAINT pk_attribute PRIMARY KEY (book_id),
    CONSTRAINT fk_attribute_book FOREIGN KEY (book_id) REFERENCES book (book_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- Table by_it_academy_grodno_elibrary.books_story
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS books_story
(
    book_id     BIGINT UNSIGNED NOT NULL UNIQUE COMMENT 'Books id',
    book_rating INT UNSIGNED COMMENT 'Book rating, count of viewing',
    added       DATETIME        NOT NULL DEFAULT NOW() COMMENT 'The date of adding book',
    CONSTRAINT pk_books_story PRIMARY KEY (book_id),
    CONSTRAINT fk_book_books_story FOREIGN KEY (book_id) REFERENCES book (book_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- Trigger by book insert
-- -----------------------------------------------------
CREATE
    TRIGGER book_insert
    AFTER INSERT
    ON book
    FOR EACH ROW
    INSERT INTO books_story (book_id, book_rating, added)
    VALUES (new.book_id, 0, NOW());

-- -----------------------------------------------------
-- Table by_it_academy_grodno_elibrary.book_has_author
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS book_has_author
(
    book_id   BIGINT UNSIGNED NOT NULL COMMENT 'Book id from table book',
    author_id INT UNSIGNED    NOT NULL COMMENT 'Author id from table author',
    CONSTRAINT fk_book_has_author_book1 FOREIGN KEY (book_id) REFERENCES book (book_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_book_has_author_author1 FOREIGN KEY (author_id) REFERENCES author (author_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- Table by_it_academy_grodno_elibrary.books_review
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS books_review
(
    review_id   BIGINT UNSIGNED NOT NULL COMMENT 'Review id',
    book_id     BIGINT UNSIGNED NOT NULL COMMENT 'Books id',
    created     DATETIME        NOT NULL DEFAULT NOW() COMMENT 'Date of creating',
    user_id     BIGINT UNSIGNED NOT NULL COMMENT 'Users id',
    review_text TEXT            NOT NULL COMMENT 'Review text',
    CONSTRAINT fk_books_review_book FOREIGN KEY (book_id) REFERENCES book (book_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_books_review_user FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- Table by_it_academy_grodno_elibrary.sharing_status
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS sharing_status
(
    sharing_status_code INT UNSIGNED NOT NULL COMMENT 'Status id',
    status_duration     INT UNSIGNED NOT NULL COMMENT 'Duration of the event for givening status',
    CONSTRAINT pk_sharing_status PRIMARY KEY (sharing_status_code)
);

INSERT INTO sharing_status (sharing_status_code, status_duration)
VALUES (1, 1),
       (2, 10),
       (3, 2);

-- -----------------------------------------------------
-- Table by_it_academy_grodno_elibrary.sharing
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS sharing
(
    sharing_id  INT UNSIGNED    NOT NULL COMMENT 'Orders id',
    status_code INT UNSIGNED    NOT NULL COMMENT 'Status',
    user_id     BIGINT UNSIGNED NOT NULL COMMENT 'Users id',
    created     DATETIME        NOT NULL DEFAULT NOW() COMMENT 'Date of creating',
    deadline    DATETIME        NOT NULL DEFAULT NOW() COMMENT 'Deadline',
    CONSTRAINT pk_sharing PRIMARY KEY (sharing_id),
    CONSTRAINT fk_sharing_user FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_sharing_sharing_status FOREIGN KEY (status_code) REFERENCES sharing_status (sharing_status_code) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS sharing_has_book
(
    book_id    BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE COMMENT 'Book id',
    sharing_id INT UNSIGNED    NOT NULL COMMENT 'Sharing id',
    CONSTRAINT fk_books_order_has_book_book1 FOREIGN KEY (book_id) REFERENCES book (book_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_books_order_has_book_order1 FOREIGN KEY (sharing_id) REFERENCES sharing (sharing_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- Trigger on sharing insert increment book_rating
-- -----------------------------------------------------
CREATE
    TRIGGER sharing_has_book_insert
    AFTER INSERT
    ON sharing_has_book
    FOR EACH ROW
    INSERT INTO books_story (book_id, book_rating)
    VALUES (new.book_id, (SELECT book_rating FROM books_story WHERE book_id = new.book_id) + 1);

-- -----------------------------------------------------
-- Trigger on sharing insert (adds duration for deadline)
-- -----------------------------------------------------
CREATE
    TRIGGER sharing_insert_deadline
    AFTER INSERT
    ON sharing
    FOR EACH ROW
    UPDATE sharing
    SET deadline =
            DATE_ADD((SELECT deadline FROM sharing WHERE sharing_id = new.sharing_id),
                     INTERVAL
                     (SELECT status_duration FROM sharing_status WHERE sharing_status_code = new.status_code)
                     DAY);

-- -----------------------------------------------------
-- Trigger on sharing update status (add duration for deadline)
-- -----------------------------------------------------
CREATE
    TRIGGER sharing_update_deadline
    AFTER UPDATE
    ON sharing
    FOR EACH ROW
    UPDATE sharing
    SET deadline =
            DATE_ADD((SELECT deadline FROM sharing WHERE sharing_id = new.sharing_id),
                     INTERVAL
                     (SELECT status_duration FROM sharing_status WHERE sharing_status_code = new.status_code)
                     DAY);

-- -----------------------------------------------------
-- Initial data by_it_academy_grodno_elibrary.role
-- -----------------------------------------------------
INSERT INTO role (id, role_name)
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN'),
       (3, 'ROLE_USER_FACEBOOK'),
       (4, 'ROLE_USER_GITHUB'),
       (5, 'ROLE_USER_GOOGLE'),
       (6, 'ROLE_LIBRARIAN');

# Fake users
INSERT INTO user (username, email, password)
VALUES ('Administrator_1', 'archive_administration@mail.ru',
        '$2a$10$qgMgWz48ATUc8vxwpfoeu.Jh6HEihthp7RyZuZGlpDKonEFV1kas.'), /*Qwerty123*/
       ('User1', 'User1@mail.ru', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq'), /*12345*/
       ('User2', 'User2@mail.ru', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq'),
       ('User3', 'User3@mail.ru', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq'),
       ('User4', 'User4@mail.ru', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq'),
       ('User5', 'User5@mail.ru', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq');

# Filing table
INSERT INTO users_story (user_id, created, updated)
SELECT id, NOW(), NOW()
FROM user;

INSERT INTO user_has_role (user_id, role_id)
VALUES (1, 2),
       (1, 1),
       (2, 1),
       (3, 1),
       (4, 1),
       (5, 1),
       (6, 1);