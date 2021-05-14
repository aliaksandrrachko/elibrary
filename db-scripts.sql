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
    id           BIGINT UNSIGNED        NOT NULL AUTO_INCREMENT UNIQUE COMMENT 'User id',
    email        VARCHAR(80) UNIQUE NULL COMMENT 'Email',
    username     VARCHAR(30)            NOT NULL COMMENT 'User name',
    first_name   VARCHAR(15) COMMENT 'First name',
    last_name    VARCHAR(15) COMMENT 'Last name',
    middle_name  VARCHAR(15) COMMENT 'Middle name',
    phone_number JSON COMMENT 'User phone number',
    address_id   BIGINT UNSIGNED        NUll COMMENT 'Id of address',
    gender       CHAR(1)                NOT NULL DEFAULT 'u' COMMENT 'Gender: m-male, f-female, u-unknown',
    birthday     DATE COMMENT 'Date of birthday',
    password     VARCHAR(64) COMMENT 'Password encoded with using BCryptPasswordEncoder',
    enabled      BOOLEAN  DEFAULT TRUE COMMENT 'User lock',
    avatar_url   VARCHAR(2083) COMMENT 'Avatars url',
    user_created DATETIME DEFAULT NOW() NOT NULL COMMENT 'Date of creating',
    user_updated DATETIME DEFAULT NOW() NOT NULL COMMENT 'Date of updated',
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
-- Trigger on update user
-- -----------------------------------------------------
CREATE
    TRIGGER p_user_update
    BEFORE UPDATE
    ON user
    FOR EACH ROW
    SET NEW.user_updated = NOW();

-- -----------------------------------------------------
-- Table by_it_academy_grodno_elibrary.role
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS role
(
    id        INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE COMMENT 'Roles id',
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
    id           BIGINT UNSIGNED        NOT NULL AUTO_INCREMENT UNIQUE COMMENT 'A surrogate primary key used to uniquely identify each address in the table.',
    country      VARCHAR(50) COMMENT 'Country name',
    region       VARCHAR(50) COMMENT 'The first line of an address.',
    district     VARCHAR(50)            NOT NULL COMMENT 'The region of an address, this may be a state, province, prefecture, etc.',
    city         VARCHAR(50)            NOT NULL COMMENT 'A foreign key pointing to the city table.',
    street       VARCHAR(50)            NOT NULL COMMENT 'The street name',
    postal_code  VARCHAR(32) COMMENT 'The postal code or ZIP code of the address (where applicable).',
    house        VARCHAR(10)            NOT NULL COMMENT 'The house number',
    apt          VARCHAR(10) COMMENT 'Number of room',
    last_updated DATETIME DEFAULT NOW() NOT NULL COMMENT 'Date of creating or last updating',
    CONSTRAINT pk_address PRIMARY KEY (id)
);

-- -----------------------------------------------------
-- Trigger by insert or update address
-- -----------------------------------------------------
CREATE
    TRIGGER p_address_update
    BEFORE UPDATE
    ON address
    FOR EACH ROW
    SET NEW.last_updated = NOW();

-- -----------------------------------------------------
-- Trigger by insert or insert address
-- -----------------------------------------------------
CREATE
    TRIGGER p_address_insert
    BEFORE INSERT
    ON address
    FOR EACH ROW
    SET NEW.last_updated = NOW();

-- -----------------------------------------------------
-- Alter table user add FK key
-- -----------------------------------------------------
ALTER TABLE user
    ADD
        CONSTRAINT fk_user_address
            FOREIGN KEY (address_id) REFERENCES address (id) ON DELETE CASCADE ON UPDATE CASCADE;

-- -----------------------------------------------------
-- Table by_it_academy_grodno_elibrary.category
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS category
(
    id            INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE COMMENT 'Book category id',
    parent_id     INT UNSIGNED COMMENT 'Parent id',
    category_name VARCHAR(45)  NOT NULL UNIQUE COMMENT 'Name of book category',
    CONSTRAINT pk_category PRIMARY KEY (id),
    CONSTRAINT fk_category_category1 FOREIGN KEY (parent_id) REFERENCES category (id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- Table by_it_academy_grodno_elibrary.publisher
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS publisher
(
    id             INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE COMMENT 'Publisher id',
    publisher_name VARCHAR(100) NOT NULL UNIQUE COMMENT 'Name of books publisher',
    CONSTRAINT pk_publisher PRIMARY KEY (id)
);

-- -----------------------------------------------------
-- Table by_it_academy_grodno_elibrary.author
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS author
(
    id          INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE COMMENT 'Author id',
    author_name VARCHAR(45) COMMENT 'Author name',
    CONSTRAINT pk_author PRIMARY KEY (id)
);

-- -----------------------------------------------------
-- Table by_it_academy_grodno_elibrary.book
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS book
(
    id              BIGINT UNSIGNED            NOT NULL AUTO_INCREMENT UNIQUE COMMENT 'Book id',
    title           VARCHAR(100)               NOT NULL COMMENT 'Book title',
    description     TEXT COMMENT 'Book description',
    isbn_10         VARCHAR(10)                NOT NULL COMMENT 'Books isbn in format ISBN-10',
    isbn_13         VARCHAR(13)                NOT NULL COMMENT 'Books isbn in format ISBN-13',
    category_id      INT UNSIGNED               NOT NULL COMMENT 'Category id',
    publisher_id    INT UNSIGNED COMMENT 'Book publisher',
    language        VARCHAR(3) COMMENT 'Language of book by alpha-3/ISO 639-2 Code',
    publishing_date DATE COMMENT 'The year and month of publishing',
    print_length    INT UNSIGNED               NOT NULL COMMENT 'Count of pages',
    picture_url     VARCHAR(2083) COMMENT 'Books cover image',
    attributes      JSON COMMENT 'Books attributes',
    total_count     INT UNSIGNED COMMENT 'Total count of books',
    available_count INT UNSIGNED COMMENT 'Available count',
    available       BOOLEAN      DEFAULT TRUE COMMENT 'Available for booking',
    book_rating     INT UNSIGNED DEFAULT 0 NOT NULL COMMENT 'Book rating, count of viewing',
    book_created    DATETIME     DEFAULT NOW() NOT NULL COMMENT 'The date of adding book',
    book_updated    DATETIME     DEFAULT NOW() NOT NULL COMMENT 'The date of adding book',
    CONSTRAINT pk_book PRIMARY KEY (id),
    CONSTRAINT fk_book_category FOREIGN KEY (category_id) REFERENCES category (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_book_publisher FOREIGN KEY (publisher_id) REFERENCES publisher (id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- Trigger on update user
-- -----------------------------------------------------
CREATE
    TRIGGER p_book_update
    BEFORE UPDATE
    ON book
    FOR EACH ROW
    SET NEW.book_updated = NOW();

-- -----------------------------------------------------
-- Table by_it_academy_grodno_elibrary.book_has_author
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS book_has_author
(
    book_id   BIGINT UNSIGNED NOT NULL COMMENT 'Book id from table book',
    author_id INT UNSIGNED    NOT NULL COMMENT 'Author id from table author',
    CONSTRAINT fk_book_has_author_book1 FOREIGN KEY (book_id) REFERENCES book (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_book_has_author_author1 FOREIGN KEY (author_id) REFERENCES author (id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- Table by_it_academy_grodno_elibrary.review
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS review
(
    id             BIGINT UNSIGNED AUTO_INCREMENT UNIQUE NOT NULL COMMENT 'Review id',
    book_id        BIGINT UNSIGNED                       NOT NULL COMMENT 'Books id',
    user_id        BIGINT UNSIGNED                       NOT NULL COMMENT 'Users id',
    review_text    TEXT                                  NOT NULL COMMENT 'Review text',
    review_grade   TINYINT UNSIGNED                      NOT NULL COMMENT 'Review gradle',
    review_created DATETIME DEFAULT NOW()                NOT NULL COMMENT 'Date of creating',
    review_updated DATETIME DEFAULT NOW()                NOT NULL COMMENT 'Date of updating',
    CONSTRAINT pk_review PRIMARY KEY (id),
    CONSTRAINT fk_review_book FOREIGN KEY (book_id) REFERENCES book (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- Trigger on update review
-- -----------------------------------------------------
CREATE
    TRIGGER p_review_update
    BEFORE UPDATE
    ON review
    FOR EACH ROW
    SET NEW.review_updated = NOW();

-- -----------------------------------------------------
-- Table by_it_academy_grodno_elibrary.subscription
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS subscription
(
    id                    BIGINT UNSIGNED AUTO_INCREMENT UNIQUE NOT NULL COMMENT 'Subscription id',
    status_code           INT UNSIGNED    NOT NULL COMMENT 'Status',
    user_id               BIGINT UNSIGNED NOT NULL COMMENT 'Users id',
    book_id               BIGINT UNSIGNED NOT NULL COMMENT 'Book id from table book',
    took                  INT UNSIGNED    NOT NULL COMMENT 'Count of book took',
    returned              INT UNSIGNED    NOT NULL DEFAULT 0 COMMENT 'Count of book returned',
    subscription_created  DATETIME        NOT NULL DEFAULT NOW() COMMENT 'Date of creating',
    subscription_deadline DATETIME        NOT NULL DEFAULT NOW() COMMENT 'Deadline',
    CONSTRAINT pk_subscription PRIMARY KEY (id),
    CONSTRAINT fk_subscription_user FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_subscription_book FOREIGN KEY (book_id) REFERENCES book (id) ON DELETE CASCADE ON UPDATE CASCADE
    #CONSTRAINT fk_subscription_status FOREIGN KEY (status_code) REFERENCES status (status_code) ON DELETE CASCADE ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- Initial data by_it_academy_grodno_elibrary.role
-- -----------------------------------------------------
INSERT INTO role (id, role_name)
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN'),
       (3, 'ROLE_USER_FACEBOOK'),
       (4, 'ROLE_USER_GITHUB'),
       (5, 'ROLE_USER_GOOGLE');

-- -----------------------------------------------------
-- Initial data by_it_academy_grodno_elibrary.address
-- -----------------------------------------------------
INSERT INTO address (id, country, region, district, city, street, postal_code, house, apt)
values (1, 'Беларусь', 'Гродненская', 'Гродненский', 'Kuangyuan', 'Erie', '230005', '278', '4720'),
       (2, 'Беларусь', 'Гродненская', 'Гродненский', 'Kutorejo', 'Gerald', '230005', '07', '4292'),
       (3, 'Беларусь', 'Гродненская', 'Гродненский', 'Shangshuai', 'Prairieview', '230005', '05843', '07'),
       (4, 'Беларусь', 'Гродненская', 'Щучинский', 'Valbonë', 'Talmadge', '230005', '1', '6'),
       (5, 'Беларусь', 'Гродненская', 'Гродненский', 'Montes Claros', 'Kensington', '39400-000', '78523', '77'),
       (6, 'Беларусь', 'Гродненская', 'Гродненский', 'Yanhe', 'Heffernan', '230005', '49220', '1096'),
       (7, 'Беларусь', 'Гродненская', 'Гродненский', 'Matagami', 'Barnett', 'N2M', '0866', '245'),
       (8, 'Беларусь', 'Гродненская', 'Гродненский', 'Moñitos', 'Stuart', '231008', '114', '0'),
       (9, 'Беларусь', 'Гродненская', 'Гродненский', 'Tongqiao', 'International', '230005', '88', '83'),
       (10, 'Беларусь', 'Гродненская', 'Гродненский', 'San Marcos', 'Armistice', '704038', '387', '578'),
       (11, 'Беларусь', 'Гродненская', 'Гродненский', 'Shuicha', 'Fulton', '230005', '8546', '702'),
       (12, 'Беларусь', 'Гродненская', 'Гродненский', 'Sangumata', 'American', '230005', '22', '031'),
       (13, 'Беларусь', 'Гродненская', 'Вороновский', 'Вороново', 'International', '230005', '88', '753'),
       (14, 'Беларусь', 'Гродненская', 'Щучинский', 'Мосты', 'International', '230005', '1', '53'),
       (15, 'Беларусь', 'Гродненская', 'Гродненский', 'Гродно', 'International', '230005', '11', '45'),
       (16, 'Беларусь', 'Гродненская', 'Гродненский', 'Гродно', 'Dominicnaskaya', '230005', '58', '76'),
       (17, 'Беларусь', 'Гродненская', 'Гродненский', 'Гродно', 'Dominicnaskaya', '23705', '67', '786'),
       (18, 'Беларусь', 'Гродненская', 'Гродненский', 'Гродно', 'Dominicnaskaya', '28785', '12', '94'),
       (19, 'Беларусь', 'Гродненская', 'Щучинский', 'Василишки', 'International', '29885', '10', '90'),
       (20, 'Беларусь', 'Гродненская', 'Щучинский', 'Василишки', 'International', '29885', '9', '95'),
       (21, 'Беларусь', 'Гродненская', 'Щучинский', 'Василишки', 'International', '29885', '87', '100'),
       (22, 'Stockholm','Гродненская', 'Hägersten', 'Alpine', 'International', '126-36', '47', '1'),
       (23, 'Беларусь', 'Гродненская', 'Filipowice', 'Autumn Leaf', 'Fulton', '32-327', '42', '11605'),
       (24,  'Беларусь','Гродненская', 'Semanding', 'Aberg', 'Fulton', '23056', '96', '01'),
       (25,  'Беларусь', 'Гродненская', 'Kamenka', 'Schurz', 'Stuart', '654sfa', '93', '021'),
       (26,  'Беларусь', 'Гродненская', 'Allkaj', 'Mosinee', 'Stuart', '64165', '76', '60491'),
       (27,  'Беларусь','Гродненская', 'Breu', 'Bluejay', 'Armistice', 'a248dsf', '4', '1'),
       (28,  'Беларусь', 'Гродненская', 'Constanza', 'Montana', 'Armistice', '11154', '0692', '3097'),
       (29, 'Беларусь', 'Veracruz Llave', 'Paleran', 'Swallow', 'Dominicnaskaya', '16541', '046', '2'),
       (30, 'Беларусь', 'Гродненская', 'Dana', 'Gina', 'Dominicnaskaya', '6545498', '7750', '51045'),
       (31, 'Беларусь', 'Гродненская', 'Pirapora', '7th', 'Barnett', '874-408', '2', '18'),
       (32,  'Беларусь', 'Гродненская', 'Tumaco', 'Division', 'Barnett', '984568', '093', '2'),
       (33, 'Беларусь', 'Madrid', 'Laocheng', 'Victoria', 'Barnett', '898', '81', '4'),
       (34, 'Беларусь', 'Madrid', 'Laocheng', 'Victoria', 'Bar', '898', '747', '3');

-- -----------------------------------------------------
-- Initial data by_it_academy_grodno_elibrary.user
-- -----------------------------------------------------
INSERT INTO user (email, username, first_name, last_name, middle_name, phone_number, address_id, gender, birthday, password, avatar_url)
VALUES ('admin@mail.ru', 'Admin', 'Dima', 'Petrov', 'Petrovich', '{"countryCode": "375", "nationalNumber": "292965416"}', 1, 'm', '1995-04-05', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_male_avatar.png'); /*12345*/

INSERT INTO user (email, username, first_name, last_name, middle_name, phone_number, address_id, gender, birthday, password,  avatar_url)
VALUES ('eget.odio@Donec.ca', 'Armand Parrish', 'Cleo', 'Macias', 'Gretchen', '{"countryCode": "375", "nationalNumber": "296908407"}', 2, 'm', '2021-03-01', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_male_avatar.png'),
       ('ligula.eu@litoratorquent.net', 'Azalia Rosario', 'Raven', 'Barry', 'Jordan', '{"countryCode": "375", "nationalNumber": "296270309"}', 3, 'm', '2022-02-25', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_male_avatar.png'),
       ('id@acarcuNunc.edu', 'Leah Moody', 'Mark', 'Mckinney', 'Dalton', '{"countryCode": "375", "nationalNumber": "339685203"}', 4, 'm', '2020-10-01', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_male_avatar.png'),
       ('urna.Vivamus.molestie@suscipitest.ca', 'Isabella Ford', 'Keefe', 'Terry', 'Celeste', '{"countryCode": "375", "nationalNumber": "443526222"}', 5, 'm', '2021-08-23', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_male_avatar.png'),
       ('dictum@mus.net', 'Jaime Castillo', 'Fredericka', 'York', 'Armand', '{"countryCode": "375", "nationalNumber": "338526313"}', 6, 'f', '2022-04-04', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_female_avatar.png'),
       ('nec@dolorsit.co.uk', 'Hollee Mejia', 'Winter', 'Vasquez', 'Brenda', '{"countryCode": "375", "nationalNumber": "337728818"}', 7, 'f', '2020-12-07', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_female_avatar.png'),
       ('ac.mattis.ornare@elementumat.org', 'Octavius Case', 'Phyllis', 'Christian', 'Luke', '{"countryCode": "375", "nationalNumber": "299467819"}', 8, 'u', '2021-04-02', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_unknown_avatar.png'),
       ('erat.Sed.nunc@temporbibendum.org', 'Xena Albert', 'Fulton', 'Mcbride', 'Marny', '{"countryCode": "375", "nationalNumber": "344555666"}', 9, 'u', '2022-03-22', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_unknown_avatar.png'),
       ('dolor.nonummy.ac@neque.com', 'Ivy Whitfield', 'Lara', 'Forbes', 'Anjolie', '{"countryCode": "375", "nationalNumber": "294574811"}', 10, 'f', '2022-03-21', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_unknown_avatar.png'),
       ('mauris@malesuada.org', 'Claire Contreras', 'Kimberly', 'Castaneda', 'Darryl', '{"countryCode": "375", "nationalNumber": "292424054"}', 11, 'm', '2021-10-23', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_unknown_avatar.png'),
        ('wnewellkt@taobao.com', 'wnewellkt', 'Léa', 'Newell', 'Weber', '{"countryCode": "375", "nationalNumber": "7915400347"}', 12, 'u', '2018-04-29', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq' , '/img/users/avatars/default_unknown_avatar.png'),
        ('cjaquemeku@apple.com', 'cjaquemeku', 'Magdalène', 'Jaqueme', 'Christie', '{"countryCode": "375", "nationalNumber": "4306703018"}', 13, 'u', '2020-07-14', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_unknown_avatar.png'),
        ('gcantillonkv@youtube.com', 'gcantillonkv', 'Ruò', 'Cantillon', 'Gardiner', '{"countryCode": "375", "nationalNumber": "9769507984"}', 14, 'u', '2018-06-29', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_unknown_avatar.png'),
        ('evaninkw@godaddy.com', 'evaninkw', 'Mélinda', 'Vanin', 'Evita', '{"countryCode": "375", "nationalNumber": "5178815966"}', 15, 'u', '2020-05-23', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_unknown_avatar.png'),
        ('zwillcottkx@mapy.cz', 'zwillcottkx', 'Vérane', 'Willcott', 'Zorina', '{"countryCode": "375", "nationalNumber": "4145688080"}', 16, 'u', '2019-02-21', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq' , '/img/users/avatars/default_unknown_avatar.png'),
        ('rborthramky@goo.gl', 'rborthramky', 'Marie-hélène', 'Borthram', 'Ryun', '{"countryCode": "375", "nationalNumber": "3589254689"}', 17, 'f', '2019-10-26', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_female_avatar.png'),
        ('lsinnattkz@blogspot.com', 'lsinnattkz', 'Dà', 'Sinnatt', 'Luke', '{"countryCode": "375", "nationalNumber": "9478276926"}', 18, 'f', '2021-04-11', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_female_avatar.png'),
        ('asydenhaml0@elegantthemes.com', 'asydenhaml0', 'Mélissandre', 'Sydenham', 'Alaster', '{"countryCode": "375", "nationalNumber": "2652629456"}', 19, 'f', '2020-08-13', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_female_avatar.png'),
        ('pgiannasil1@dmoz.org', 'pgiannasil1', 'Geneviève', 'Giannasi', 'Paulie', '{"countryCode": "375", "nationalNumber": "9948997234"}', 20, 'f', '2020-03-08', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq' ,'/img/users/avatars/default_female_avatar.png'),
        ('kkentishl2@ted.com', 'kkentishl2', 'Léandre', 'Kentish', 'Kass', '{"countryCode": "375", "nationalNumber": "7911654280"}', 21, 'f', '2018-11-08','$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_female_avatar.png'),
        ('ainchboardl3@cyberchimps.com', 'ainchboardl3', 'Göran', 'Inchboard', 'Andeee', '{"countryCode": "375", "nationalNumber": "5875670233"}', 22, 'f', '2018-09-08', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_female_avatar.png'),
        ('kfourcadel4@reverbnation.com', 'kfourcadel4', 'Aloïs', 'Fourcade', 'Keslie', '{"countryCode": "375", "nationalNumber": "5878816423"}', 23, 'f', '2018-06-30', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq' , '/img/users/avatars/default_female_avatar.png'),
        ('ddorninl5@angelfire.com', 'ddorninl5', 'Intéressant', 'Dornin', 'Dannel', '{"countryCode": "375", "nationalNumber": "7583032901"}', 24, 'f', '2020-07-01', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_female_avatar.png'),
        ('gmollenl6@drupal.org', 'gmollenl6', 'Håkan', 'Mollen', 'Gwyneth', '{"countryCode": "375", "nationalNumber": "9558773126"}', 25, 'f', '2020-10-10', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_female_avatar.png'),
        ('imellyl7@cisco.com', 'imellyl7', 'Thérèse', 'Melly', 'Irvin', '{"countryCode": "375", "nationalNumber": "7127396057"}', 26, 'm', '2020-08-10', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_male_avatar.png'),
        ('zkerrichl8@auda.org.au', 'zkerrichl8', 'Marie-josée', 'Kerrich', 'Zed', '{"countryCode": "375", "nationalNumber": "7807417723"}', 27, 'm', '2018-04-26', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_male_avatar.png'),
        ('mspanswickl9@redcross.org', 'mspanswickl9', 'Rébecca', 'Spanswick', 'Mathias', '{"countryCode": "375", "nationalNumber": "2194902207"}', 28, 'm', '2020-04-03', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_male_avatar.png'),
        ('hkediela@ca.gov', 'hkediela', 'Göran', 'Kedie', 'Hal', '{"countryCode": "375", "nationalNumber": "8775028030"}', 29, 'm', '2020-01-16', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_male_avatar.png'),
        ('rcouttslb@techcrunch.com', 'rcouttslb', 'Maëlle', 'Coutts', 'Roseann', '{"countryCode": "375", "nationalNumber": "3787034170"}', 30, 'm', '2020-05-16', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_male_avatar.png'),
        ('dlewislc@blog.com', 'dlewislc', 'Marieève', 'Lewis', 'Diandra', '{"countryCode": "375", "nationalNumber": "5628160203"}', 31, 'm', '2020-04-08', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_male_avatar.png'),
        ('kmcentagartld@oaic.gov.au', 'kmcentagartld', 'Táng', 'McEntagart', 'Karola', '{"countryCode": "375", "nationalNumber": "9905994967"}', 32, 'm', '2018-11-26', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_male_avatar.png'),
        ('bholthamle@theguardian.com', 'bholthamle', 'Kévina', 'Holtham', 'Ben', '{"countryCode": "375", "nationalNumber": "8487738742"}', 33, 'm', '2018-02-13', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_male_avatar.png'),
        ('csandhamlf@ftc.gov', 'csandhamlf', 'Réservés', 'Sandham', 'Cal', '{"countryCode": "375", "nationalNumber": "6134745518"}', 34, 'f', '2019-12-24', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq', '/img/users/avatars/default_female_avatar.png');

-- -----------------------------------------------------
-- Initial data by_it_academy_grodno_elibrary.user_has_role
-- -----------------------------------------------------
INSERT INTO user_has_role (user_id, role_id)
VALUES (1, 2),
       (1, 1),
       (2, 1),
       (3, 1),
       (4, 1),
       (5, 1),
       (6, 1),
       (7, 1),
       (8, 1),
       (9, 1),
       (10, 1),
       (11, 1),
       (12, 1),
       (13, 1),
       (14, 1),
       (15, 1),
       (16, 1),
       (17, 1),
       (18, 1),
       (19, 1),
       (20, 1),
       (21, 1),
       (22, 1),
       (23, 1),
       (24, 1),
       (25, 1),
       (26, 1),
       (27, 1),
       (28, 1),
       (29, 1),
       (30, 1),
       (31, 1),
       (32, 1),
       (33, 1),
       (34, 1);

-- -----------------------------------------------------
-- Filing data by_it_academy_grodno_elibrary.category
-- -----------------------------------------------------
INSERT INTO category (id, parent_id, category_name)
VALUES (1, null, 'Художественная литература'),
       (2, null, 'Нехудожественная литература'),
       (3, null, 'Учебная, методическая литература и словари'),
       (4, null, 'Билингвы и книги на иностранных языках'),
       (5, null, 'Комиксы, Манга, Артбуки'),
       (6, null, 'Периодические издания'),
       (7, null, 'Книги для детей');

-- -----------------------------------------------------
-- Filing data by_it_academy_grodno_elibrary.category1
-- -----------------------------------------------------
INSERT INTO category (id, parent_id, category_name)
VALUES (8, 1,  'Современная литература'),
       (9, 1, 'Басни'),
       (10, 1, 'Детективы'),
       (11, 1, 'Драматургия'),
       (12, 1, 'Историческая проза'),
       (13, 1, 'Классическая проза'),
       (14, 1, 'Поэзия'),
       (15, 1, 'Приключения'),
       (16, 1, 'Сентиментальная проза'),
       (17, 1, 'Современная проза'),
       (18, 1, 'Фантастика'),
       (19, 1, 'Фэнтези'),
       (20, 1, 'Эпос и фольклор'),
-- -----------------------------------------------------
       (21, 2, 'Бизнес. Экономика'),
       (22, 2, 'Естественные науки'),
       (23, 2, 'История. Исторические науки'),
       (24, 2, 'Компьютеры и Интернет'),
       (25, 2, 'Научно-популярная литература'),
       (26, 2, 'Красота, спорт, питание'),
       (27, 2, 'Научная и специальная литература'),
       (28, 2, 'Творчество, саморазвитие, хобби'),
-- -----------------------------------------------------
       (29, 3, 'Вспомогательные материалы для студентов'),
       (30, 3, 'Демонстрационные материалы'),
       (31, 3,  'Дополнительное образование для детей'),
-- -----------------------------------------------------
       (32, 4, 'Билингвы'),
       (33, 4, 'Литература на иностранном языке'),
       (34, 4, 'Литература на иностранном языке для детей'),
-- -----------------------------------------------------
       (35, 5, 'Артбуки. Игровые миры. Вселенные'),
       (36, 5, 'Комиксы'),
       (37, 5, 'Комиксы для детей'),
       (38, 5, 'Манга'),
       (39, 5, 'Манга для детей'),
       (40, 5, 'Новеллизации'),
-- -----------------------------------------------------
       (41, 6, 'Газеты'),
       (42, 6, 'Журналы'),
       (43, 6, 'Научные журналы'),
-- -----------------------------------------------------
       (44, 7, 'Детская художественная литература'),
       (45, 7, 'Детский досуг'),
       (46, 7, 'Познавательная литература');

-- -----------------------------------------------------
-- Filing data by_it_academy_grodno_elibrary.category2
-- -----------------------------------------------------
INSERT INTO category (id, parent_id, category_name)
VALUES (47, 13, 'Классическая зарубежная проза'),
       (48, 13, 'Классическая отечественная проза'),
-- -----------------------------------------------------
       (49, 10, 'Современный зарубежный детектив'),
       (50, 10, 'Классический отечественный детектив'),
       (51, 10, 'Классический зарубежный детектив');

-- -----------------------------------------------------
-- Filing data by_it_academy_grodno_elibrary.author
-- -----------------------------------------------------
INSERT INTO author (id, author_name)
VALUES (1, 'Александр Сергеевич Пушкин'),
       (2, 'Arthur Conan Doyle'),
       (3, 'Elisabeth Robson'),
       (4, 'Eric Freeman'),
       (5, 'Paul Barry'),
       (6, 'Stephen King'),
       (7, 'Scott Pratt'),
       (8, 'Федор Михайлович Достоевский'),
       (9, 'Иван Александрович Гончаров'),
       (10, 'Николай Васильевич Гоголь'),
       (11, 'Лев Николаевич Толстой'),
       (12, 'Михаил Афанасьевич Булгаков'),
       (13, 'Антон Павлович чехов'),
       (14, 'Иван Алексеевич Бунин'),
       (15, 'George Orwell'),
       (16, 'Станислав Герман Лем'),
       (17, 'Ray Bradbury');

-- -----------------------------------------------------
-- Filing data by_it_academy_grodno_elibrary.publisher
-- -----------------------------------------------------
INSERT INTO publisher (id, publisher_name)
VALUES (1, 'AST'),
        (2, 'TEXT'),
        (3, 'Эксмо-Пресс');

-- -----------------------------------------------------
-- Filing data by_it_academy_grodno_elibrary.book
-- -----------------------------------------------------
INSERT INTO book (id, title, description, isbn_10, isbn_13, category_id, publisher_id, language, publishing_date, print_length, picture_url, total_count, available_count, available)
 VALUES (1, 'Voyna i mir. Kniga 1',
         'Издательство АСТ Серия Лучшая мировая классика Год издания 2018 ISBN 9785171123857 Кол-во страниц 736 Формат 20.6 x 13.5 x 3 Тип обложки Твердая бумажная Тираж 10000 Вес, г 500 Возрастные ограничения 12+ Аннотация "Война и мир" — роман-эпопея Льва Толстого, одно из крупнейших произведений мировой литературы, описывающее жизнь русского общества в эпоху Наполеоновских войн. "Война и мир" — это масштабная картина жизни России, взятая во всех ее социальных слоях (от крестьян до императора Александра I), и детальное описание хода военных действий, и осмысление поведения человека на войне, но главное — это глубокое философское осмысление и исследование жизни как таковой — в быту, в семье, в мирное время, на войне. Именно поэтому "Войну и мир" можно читать и перечитывать всю жизнь — этот роман никогда не потеряет своей актуальности.',
         '5171123853',
         '9785171123857',
         48, 1, 'rus', '2018-01-01', 736,
         'https://m.media-amazon.com/images/I/513ZIFMK6VL.jpg',
         2, 2, TRUE);
INSERT INTO book_has_author (book_id, author_id)
VALUES (1, 11);

INSERT INTO book (id, title, description, isbn_10, isbn_13, category_id, publisher_id, language, publishing_date, print_length, picture_url, total_count, available_count, available)
VALUES (2, 'Джордж Оруэлл: Да здравствует фикус!',
        'Роман "Да здравствует фикус!" Джорджа Оруэлла (1903- 1950), автора знаменитой антиутопии "1984", во многом автобиографичен. Главный герой, талантливый и непризнанный поэт, презирает материальное благополучие и пошлость обыденной жизни, символом которой служит фикус. Только любовь и стремление к земному счастью и благополучию заставляют его понять, что помимо высокого искусства существуют простые радости, а в стремлении заработать деньги нет ничего постыдного.',
        '5751616804',
        '9785751616809',
        47, 2, 'rus', '2021-01-01', 256,
        'https://img4.labirint.ru/rc/9ef51d4cefee60b3906bae41a47d7c02/220x340/books79/786912/cover.png?1616581505',
        2, 2, TRUE);
INSERT INTO book_has_author (book_id, author_id)
VALUES (2, 15);

INSERT INTO book (id, title, description, isbn_10, isbn_13, category_id, publisher_id, language, publishing_date, print_length, picture_url, total_count, available_count, available)
VALUES (3, 'Джордж Оруэлл: 1984',
        'Прошло всего три года после окончания Второй мировой войны, когда Джордж Оруэлл (1903-1950) написал самое знаменитое свое произведение - роман-антиутопию "1984". Многое из того, о чем писал Джордж Оруэлл, покажется вам до безумия знакомым. Некоторые исследователи считают, что ни один западный читатель не постигнет суть "1984" так глубоко, как человек родом из Советского Союза.',
        '5751616790',
        '9785751616793',
        47, 1, 'rus', '2021-01-01', 320,
        'https://img4.labirint.ru/rc/6b0e6c27a90bf6b792d1b8363df35293/220x340/books80/790566/cover.png?1618316737',
        2, 2, TRUE);
INSERT INTO book_has_author (book_id, author_id)
VALUES (3, 15);

INSERT INTO book (id, title, description, isbn_10, isbn_13, category_id, publisher_id, language, publishing_date, print_length, picture_url, total_count, available_count, available)
VALUES (4, 'Станислав Лем: Фиаско',
        'Космос - пространство загадки, пространство вечного поиска, и любая ошибка, продиктованная небрежностью или неверной логической посылкой, способна привести исследователя к краху. Экспедиция "Гермеса", призванная установить контакт с обитателями планеты Квинта, совершает ряд промахов, цена которых слишком высока…',
        '5171350728',
        '9785171350727',
        47, 1, 'rus', '2021-01-01', 448,
        'https://img3.labirint.ru/rc/ce8a22290d7021c1308f1b75b3b9edaa/220x340/books81/800623/cover.jpg?1618550720',
        2, 2, TRUE);
INSERT INTO book_has_author (book_id, author_id)
VALUES (4, 16);

INSERT INTO book (id, title, description, isbn_10, isbn_13, category_id, publisher_id, language, publishing_date, print_length, picture_url, total_count, available_count, available)
VALUES (5, 'Рэй Брэдбери: Вино из одуванчиков',
        'Яркое, фантастическое лето 1928 года: двенадцатилетний Дуглас Сполдинг ведет записи о событиях того лета, которые складываются в отдельные истории, гротескные искажения ординарных будней маленького городка, где живут Дуглас и его семья. Там все кажется не тем, чем является, а сила детского воображения создает новую реальность, которую не отличить от вымысла. Выросший из отдельных рассказов, филигранных в своей лиричности, роман "Вино из одуванчиков" — классическая хроника детства Рэя Брэдбери, окно в творческий мир писателя, создавшего такие шедевры мировой литературы, как "Марсианские хроники" и "451 градус по Фаренгейту".',
        '5171350728',
        '9785040988358',
        48, 3, 'rus', '2019-01-01', 256,
        'https://img3.labirint.ru/rc/3ed50890722042ff78db15eb83c84530/220x340/books73/720915/cover.jpg?1617258303',
        2, 2, TRUE);
INSERT INTO book_has_author (book_id, author_id)
VALUES (5, 17);
