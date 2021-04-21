DROP SCHEMA IF EXISTS by_it_academy_grodno_elibrary;
CREATE SCHEMA IF NOT EXISTS by_it_academy_grodno_elibrary;
use by_it_academy_grodno_elibrary;

update
    user
set
    address_id=1,
    birthday='1995-04-24',
    email='pamidorchik@mail.ru',
    enabled=true,
    first_name='Ales',
    gender='m',
    last_name='Pamidorchik',
    middle_name='Pilic',
    password='13515',
    phone_number='{"code": "29", "number": "2224222"}',
    username='Ales Pamidorchik'
where
        id=12;

SELECT * FROM user WHERE user.id=12;