SELECT setval('address_id_seq', (SELECT max(id) FROM address));
SELECT setval('user_id_seq', (SELECT max(id) FROM "user"));
SELECT setval('author_id_seq', (SELECT max(id) FROM author));
SELECT setval('publisher_id_seq', (SELECT max(id) FROM publisher));
SELECT setval('book_id_seq', (SELECT max(id) FROM book));