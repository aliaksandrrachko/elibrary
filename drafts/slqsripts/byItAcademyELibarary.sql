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
-- Trigger on insert user
-- -----------------------------------------------------
# CREATE
#     TRIGGER p_user_insert
#     BEFORE INSERT
#     ON user
#     FOR EACH ROW
#     SET NEW.user_created = NOW(),
#         NEW.user_updated = NOW();

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
    category_name VARCHAR(45)  NOT NULL UNIQUE COMMENT 'Name of book category',
    CONSTRAINT pk_category PRIMARY KEY (id)
);

-- -----------------------------------------------------
-- Table by_it_academy_grodno_elibrary.section
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS section
(
    id           INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE COMMENT 'Books sections id',
    section_name VARCHAR(45)  NOT NULL UNIQUE COMMENT 'Section of books category',
    category_id  INT UNSIGNED NOT NULL COMMENT 'Category id from category table',
    CONSTRAINT pk_section PRIMARY KEY (id),
    CONSTRAINT fk_section_category FOREIGN KEY (category_id) REFERENCES category (id) ON DELETE CASCADE ON UPDATE CASCADE
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
    section_id      INT UNSIGNED               NOT NULL COMMENT 'Sections id',
    publisher_id    INT UNSIGNED COMMENT 'Book publisher',
    language        VARCHAR(3) COMMENT 'Language of book by alpha-3/ISO 639-2 Code',
    publishing_date DATE COMMENT 'The year and month of publishing',
    print_length    INT UNSIGNED               NOT NULL COMMENT 'Count of pages',
    picture_url     VARCHAR(2083) COMMENT 'Books cover image',
    total_count     INT UNSIGNED COMMENT 'Total count of books',
    available_count INT UNSIGNED COMMENT 'Available count',
    available       BOOLEAN      DEFAULT TRUE COMMENT 'Available for booking',
    book_rating     INT UNSIGNED DEFAULT 0 NOT NULL COMMENT 'Book rating, count of viewing',
    book_created    DATETIME     DEFAULT NOW() NOT NULL COMMENT 'The date of adding book',
    book_updated    DATETIME     DEFAULT NOW() NOT NULL COMMENT 'The date of adding book',
    #cover INT COMMENT 'The books cover',
    CONSTRAINT pk_book PRIMARY KEY (id),
    CONSTRAINT fk_book_section FOREIGN KEY (section_id) REFERENCES section (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_book_publisher FOREIGN KEY (publisher_id) REFERENCES publisher (id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- Trigger on insert book
-- -----------------------------------------------------
# CREATE
#     TRIGGER p_book_insert
#     BEFORE INSERT
#     ON book
#     FOR EACH ROW
#     SET NEW.book_created = NOW(),
#         NEW.book_updated = NOW();

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
-- Table by_it_academy_grodno_elibrary.attribute
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS attribute
(
    book_id   BIGINT UNSIGNED NOT NULL UNIQUE COMMENT 'The book id',
    attribute JSON            NOT NULL COMMENT 'The books attributes',
    CONSTRAINT pk_attribute PRIMARY KEY (book_id),
    CONSTRAINT fk_attribute_book FOREIGN KEY (book_id) REFERENCES book (id) ON DELETE CASCADE ON UPDATE CASCADE
);

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
    id             BIGINT UNSIGNED        NOT NULL COMMENT 'Review id',
    book_id        BIGINT UNSIGNED        NOT NULL COMMENT 'Books id',
    review_created DATETIME DEFAULT NOW() NOT NULL COMMENT 'Date of creating',
    user_id        BIGINT UNSIGNED        NOT NULL COMMENT 'Users id',
    review_text    TEXT                   NOT NULL COMMENT 'Review text',
    CONSTRAINT fk_review_book FOREIGN KEY (book_id) REFERENCES book (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- Table by_it_academy_grodno_elibrary.subscription_status
-- -----------------------------------------------------
# CREATE TABLE IF NOT EXISTS status
# (
#     status_code     INT UNSIGNED NOT NULL COMMENT 'Status id',
#     status_duration INT UNSIGNED NOT NULL COMMENT 'Duration of the event for giving status',
#     CONSTRAINT pk_subscription_status PRIMARY KEY (status_code)
# );
#
# INSERT INTO status (status_code, status_duration)
# VALUES (1, 1),
#        (2, 10),
#        (3, 2),
#        (4, 0);

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
-- Trigger on subscription insert increment book_rating
-- -----------------------------------------------------
# CREATE
#     TRIGGER p_subscription_book_insert
#     AFTER INSERT
#     ON subscription
#     FOR EACH ROW BEGIN
#     UPDATE book
#     SET book.book_rating = ((SELECT book_rating FROM book WHERE id = NEW.book_id) + 1)
#     WHERE book.id = NEW.book_id;
#     END;
#
# Drop trigger p_subscription_book_insert;

-- -----------------------------------------------------
-- Trigger on subscription insert (adds duration for deadline)
-- -----------------------------------------------------
# CREATE
#     TRIGGER p_subscription_insert
#     BEFORE INSERT
#     ON subscription
#     FOR EACH ROW
#     SET NEW.subscription_deadline =
#             DATE_ADD((SELECT subscription_deadline FROM subscription WHERE id = new.id),
#                      INTERVAL
#                      (SELECT status_duration FROM status WHERE status_code = new.status_code)
#                      DAY);

-- -----------------------------------------------------
-- Trigger on subscription update status (add duration for deadline)
-- -----------------------------------------------------
# CREATE
#     TRIGGER p_subscription_update
#     BEFORE UPDATE
#     ON subscription
#     FOR EACH ROW
#     SET NEW.subscription_deadline =
#             DATE_ADD((SELECT subscription_deadline FROM subscription WHERE id = new.id),
#                      INTERVAL
#                      (SELECT status_duration FROM status WHERE status_code = new.status_code)
#                      DAY);

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

-- -----------------------------------------------------
-- Initial data by_it_academy_grodno_elibrary.address
-- -----------------------------------------------------
INSERT INTO address (id, region, district, city, street, postal_code, house, apt)
values (1, 'Гродненская', 'Гродненский', 'Kuangyuan', 'Erie', 230005, '278', '4720'),
       (2, 'Гродненская', 'Гродненский', 'Kutorejo', 'Gerald', 230005, '07', '4292'),
       (3, 'Гродненская', 'Гродненский', 'Shangshuai', 'Prairieview', 230005, '05843', '07'),
       (4, 'Гродненская', 'Rhône-Alpes', 'Valbonë', 'Talmadge', 230005, '1', '6'),
       (5, 'Гродненская', 'Гродненский', 'Montes Claros', 'Kensington', '39400-000', '78523', '77'),
       (6, 'Гродненская', 'Гродненский', 'Yanhe', 'Heffernan', 230005, '49220', '1096'),
       (7, 'Québec', 'Гродненский', 'Matagami', 'Barnett', 'N2M', '0866', '245'),
       (8, 'Гродненская', 'Гродненский', 'Moñitos', 'Stuart', '231008', '114', '0'),
       (9, 'Гродненская', 'Гродненский', 'Tongqiao', 'International', 230005, '88', '753'),
       (10, 'Гродненская', 'Гродненский', 'San Marcos', 'Armistice', '704038', '387', '578'),
       (11, 'Гродненская', 'Гродненский', 'Shuicha', 'Fulton', 230005, '8546', '702'),
       (12, 'Гродненская', 'Гродненский', 'Sangumata', 'American', 230005, '22', '031');

-- -----------------------------------------------------
-- Initial data by_it_academy_grodno_elibrary.user
-- -----------------------------------------------------
INSERT INTO user (email, username, first_name, last_name, middle_name, phone_number, address_id, gender, birthday, password)
VALUES ('admin@mail.ru', 'Admin', 'Dima', 'Petrov', 'Petrovich', '{"code": "29", "number": "2965416"}', 1, 'm', '1995-04-05', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq'); /*12345*/

INSERT INTO user (email, username, first_name, last_name, middle_name, phone_number, address_id, gender, birthday, password)
VALUES ('eget.odio@Donec.ca', 'Armand Parrish', 'Cleo', 'Macias', 'Gretchen', '{
  "code": "67",
  "number": "6908407"
}', 2, 'm', '2021-03-01', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq'),
       ('ligula.eu@litoratorquent.net', 'Azalia Rosario', 'Raven', 'Barry', 'Jordan', '{
         "code": "87",
         "number": "6270309"
       }', 3, 'm', '2022-02-25', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq'),
       ('id@acarcuNunc.edu', 'Leah Moody', 'Mark', 'Mckinney', 'Dalton', '{
         "code": "97",
         "number": "9685203"
       }', 4, 'm', '2020-10-01', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq'),
       ('urna.Vivamus.molestie@suscipitest.ca', 'Isabella Ford', 'Keefe', 'Terry', 'Celeste', '{
         "code": "44",
         "number": "3526222"
       }', 5, 'm', '2021-08-23', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq'),
       ('dictum@mus.net', 'Jaime Castillo', 'Fredericka', 'York', 'Armand', '{
         "code": "24",
         "number": "8526313"
       }', 6, 'f', '2022-04-04', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq'),
       ('nec@dolorsit.co.uk', 'Hollee Mejia', 'Winter', 'Vasquez', 'Brenda', '{
         "code": "73",
         "number": "7728818"
       }', 7, 'f', '2020-12-07', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq'),
       ('ac.mattis.ornare@elementumat.org', 'Octavius Case', 'Phyllis', 'Christian', 'Luke', '{
         "code": "21",
         "number": "9467819"
       }', 8, 'u', '2021-04-02', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq'),
       ('erat.Sed.nunc@temporbibendum.org', 'Xena Albert', 'Fulton', 'Mcbride', 'Marny', '{
         "code": "07",
         "number": "3555666"
       }', 9, 'u', '2022-03-22', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq'),
       ('dolor.nonummy.ac@neque.com', 'Ivy Whitfield', 'Lara', 'Forbes', 'Anjolie', '{
         "code": "34",
         "number": "4574811"
       }', 10, 'f', '2022-03-21', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq'),
       ('mauris@malesuada.org', 'Claire Contreras', 'Kimberly', 'Castaneda', 'Darryl', '{
         "code": "34",
         "number": "2424054"
       }', 11, 'm', '2021-10-23', '$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq');

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
       (6, 1);

-- -----------------------------------------------------
-- Filing data by_it_academy_grodno_elibrary.category
-- -----------------------------------------------------
INSERT INTO category (id, category_name)
VALUES (1, 'Художественная литература'),
       (2, 'Нехудожественная литература'),
       (3, 'Учебная, методическая литература и словари'),
       (4, 'Билингвы и книги на иностранных языках'),
       (5, 'Комиксы, Манга, Артбуки'),
       (6, 'Периодические издания'),
       (7, 'Книги для детей');

-- -----------------------------------------------------
-- Filing data by_it_academy_grodno_elibrary.section
-- -----------------------------------------------------
INSERT INTO section (id, section_name, category_id)
VALUES (1, 'Современная литература', 1),
       (2, 'Басни', 1),
       (3, 'Детективы', 1),
       (4, 'Драматургия', 1),
       (5, 'Историческая проза', 1),
       (6, 'Классическая проза', 1),
       (7, 'Поэзия', 1),
       (8, 'Приключения', 1),
       (9, 'Сентиментальная проза', 1),
       (10, 'Современная проза', 1),
       (11, 'Фантастика', 1),
       (12, 'Фэнтези', 1),
       (13, 'Эпос и фольклор', 1),
-- -----------------------------------------------------
       (14, 'Бизнес. Экономика', 2),
       (15, 'Естественные науки', 2),
       (16, 'История. Исторические науки', 2),
-- -----------------------------------------------------
       (17, 'Вспомогательные материалы для студентов', 3),
       (18, 'Демонстрационные материалы', 3),
       (19, 'Дополнительное образование для детей', 3),
-- -----------------------------------------------------
       (20, 'Билингвы', 4),
       (21, 'Литература на иностранном языке', 4),
       (22, 'Литература на иностранном языке для детей', 4),
-- -----------------------------------------------------
       (23, 'Артбуки. Игровые миры. Вселенные', 5),
       (24, 'Комиксы', 5),
       (25, 'Комиксы для детей', 5),
       (26, 'Манга', 5),
       (27, 'Манга для детей', 5),
       (28, 'Новеллизации', 5),
-- -----------------------------------------------------
       (29, 'Газеты', 6),
       (30, 'Журналы', 6),
       (31, 'Научные журналы', 6),
-- -----------------------------------------------------
       (32, 'Детская художественная литература', 7),
       (33, 'Детский досуг', 7),
       (34, 'Познавательная литература', 7);

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
INSERT INTO book (id, title, description, isbn_10, isbn_13, section_id, publisher_id, language, publishing_date, print_length, picture_url, total_count, available_count, available)
 VALUES (1, 'Voyna i mir. Kniga 1',
         'Издательство АСТ Серия Лучшая мировая классика Год издания 2018 ISBN 9785171123857 Кол-во страниц 736 Формат 20.6 x 13.5 x 3 Тип обложки Твердая бумажная Тираж 10000 Вес, г 500 Возрастные ограничения 12+ Аннотация "Война и мир" — роман-эпопея Льва Толстого, одно из крупнейших произведений мировой литературы, описывающее жизнь русского общества в эпоху Наполеоновских войн. "Война и мир" — это масштабная картина жизни России, взятая во всех ее социальных слоях (от крестьян до императора Александра I), и детальное описание хода военных действий, и осмысление поведения человека на войне, но главное — это глубокое философское осмысление и исследование жизни как таковой — в быту, в семье, в мирное время, на войне. Именно поэтому "Войну и мир" можно читать и перечитывать всю жизнь — этот роман никогда не потеряет своей актуальности.',
         '5171123853',
         '9785171123857',
         6, 1, 'rus', '2018-01-01', 736,
         'https://m.media-amazon.com/images/I/513ZIFMK6VL.jpg',
         2, 2, TRUE);
INSERT INTO book_has_author (book_id, author_id)
VALUES (1, 11);

INSERT INTO book (id, title, description, isbn_10, isbn_13, section_id, publisher_id, language, publishing_date, print_length, picture_url, total_count, available_count, available)
VALUES (2, 'Джордж Оруэлл: Да здравствует фикус!',
        'Роман "Да здравствует фикус!" Джорджа Оруэлла (1903- 1950), автора знаменитой антиутопии "1984", во многом автобиографичен. Главный герой, талантливый и непризнанный поэт, презирает материальное благополучие и пошлость обыденной жизни, символом которой служит фикус. Только любовь и стремление к земному счастью и благополучию заставляют его понять, что помимо высокого искусства существуют простые радости, а в стремлении заработать деньги нет ничего постыдного.',
        '5751616804',
        '9785751616809',
        6, 2, 'rus', '2021-01-01', 256,
        'https://img4.labirint.ru/rc/9ef51d4cefee60b3906bae41a47d7c02/220x340/books79/786912/cover.png?1616581505',
        2, 2, TRUE);
INSERT INTO book_has_author (book_id, author_id)
VALUES (2, 15);

INSERT INTO book (id, title, description, isbn_10, isbn_13, section_id, publisher_id, language, publishing_date, print_length, picture_url, total_count, available_count, available)
VALUES (3, 'Джордж Оруэлл: 1984',
        'Прошло всего три года после окончания Второй мировой войны, когда Джордж Оруэлл (1903-1950) написал самое знаменитое свое произведение - роман-антиутопию "1984". Многое из того, о чем писал Джордж Оруэлл, покажется вам до безумия знакомым. Некоторые исследователи считают, что ни один западный читатель не постигнет суть "1984" так глубоко, как человек родом из Советского Союза.',
        '5751616790',
        '9785751616793',
        6, 1, 'rus', '2021-01-01', 320,
        'https://img4.labirint.ru/rc/6b0e6c27a90bf6b792d1b8363df35293/220x340/books80/790566/cover.png?1618316737',
        2, 2, TRUE);
INSERT INTO book_has_author (book_id, author_id)
VALUES (3, 15);

INSERT INTO book (id, title, description, isbn_10, isbn_13, section_id, publisher_id, language, publishing_date, print_length, picture_url, total_count, available_count, available)
VALUES (4, 'Станислав Лем: Фиаско',
        'Космос - пространство загадки, пространство вечного поиска, и любая ошибка, продиктованная небрежностью или неверной логической посылкой, способна привести исследователя к краху. Экспедиция "Гермеса", призванная установить контакт с обитателями планеты Квинта, совершает ряд промахов, цена которых слишком высока…',
        '5171350728',
        '9785171350727',
        6, 1, 'rus', '2021-01-01', 448,
        'https://img3.labirint.ru/rc/ce8a22290d7021c1308f1b75b3b9edaa/220x340/books81/800623/cover.jpg?1618550720',
        2, 2, TRUE);
INSERT INTO book_has_author (book_id, author_id)
VALUES (4, 16);

INSERT INTO book (id, title, description, isbn_10, isbn_13, section_id, publisher_id, language, publishing_date, print_length, picture_url, total_count, available_count, available)
VALUES (5, 'Рэй Брэдбери: Вино из одуванчиков',
        'Яркое, фантастическое лето 1928 года: двенадцатилетний Дуглас Сполдинг ведет записи о событиях того лета, которые складываются в отдельные истории, гротескные искажения ординарных будней маленького городка, где живут Дуглас и его семья. Там все кажется не тем, чем является, а сила детского воображения создает новую реальность, которую не отличить от вымысла. Выросший из отдельных рассказов, филигранных в своей лиричности, роман "Вино из одуванчиков" — классическая хроника детства Рэя Брэдбери, окно в творческий мир писателя, создавшего такие шедевры мировой литературы, как "Марсианские хроники" и "451 градус по Фаренгейту".',
        '5171350728',
        '9785040988358',
        6, 3, 'rus', '2019-01-01', 256,
        'https://img3.labirint.ru/rc/3ed50890722042ff78db15eb83c84530/220x340/books73/720915/cover.jpg?1617258303',
        2, 2, TRUE);
INSERT INTO book_has_author (book_id, author_id)
VALUES (5, 17);

# insert
# into subscription (book_id, subscription_created, subscription_deadline, returned, status_code, took, user_id)
# values (1, NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY), 0, 1, 2, 2);

