-- Filing data elibrary.category
INSERT INTO category (id, parent_id, category_name)
VALUES (1, null, 'Художественная литература'),
       (2, null, 'Нехудожественная литература'),
       (3, null, 'Учебная, методическая литература и словари'),
       (4, null, 'Билингвы и книги на иностранных языках'),
       (5, null, 'Комиксы, Манга, Артбуки'),
       (6, null, 'Периодические издания'),
       (7, null, 'Книги для детей');

-- Filing data elibrary.category1
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
-- Filing data elibrary.category2
-- -----------------------------------------------------
INSERT INTO category (id, parent_id, category_name)
VALUES (47, 13, 'Классическая зарубежная проза'),
       (48, 13, 'Классическая отечественная проза'),
-- -----------------------------------------------------
       (49, 10, 'Современный зарубежный детектив'),
       (50, 10, 'Классический отечественный детектив'),
       (51, 10, 'Классический зарубежный детектив');

-- -----------------------------------------------------
-- Filing data elibrary.author
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
SELECT setval('author_id_seq', (SELECT max(id) FROM author));

-- -----------------------------------------------------
-- Filing data elibrary.publisher
-- -----------------------------------------------------
INSERT INTO publisher (id, publisher_name)
VALUES (1, 'AST'),
       (2, 'TEXT'),
       (3, 'Эксмо-Пресс');
SELECT setval('publisher_id_seq', (SELECT max(id) FROM publisher));

-- Filing data elibrary.book
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
SELECT setval('book_id_seq', (SELECT max(id) FROM book));

