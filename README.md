<img src="elibrary-controller/src/main/resources/static/img/logo/logo_200_200.png" alt="E-Library" align="right">

# E-Library

E-Library - the user-friendly general reference tool—delivers designed to support every range of user, including 
elementary students, college-prep and college-level researchers, and professional educators.

Using this web-application you can/be able to:
* Check all books available in the library now, look for necessary book using category, author or title and immediately book it
* Then you can to get selected book pick it up at the real library
* Track the status of your booking (reading)
* In case you have forgotten to return the book to the library you will get email, just as kind reminder.

The E-Library application does reading simple!

## Installing / Getting started

```shell
mvn spring-boot:run
```

When you execute the code above the application will run.

## Api code-contract (generated with swagger framework)

```shell
/v2/api-docs
```

### Using docker

1. Create a docker container for MySQL
```shell
docker run --name mysql-docker-container -e MYSQL_ROOT_PASSWORD=1234 -d mysql:8.0.22
```
[comment]: <> (docker run --name elibrary-mysql-data -v elibrary-mysql-data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=1234 -dp 3306:3306 mysql:latest)

2. Building the docker image from project
```shell
docker build -f Dockerfile -t elibrary-spring-boot .
```

3. Running the built docker image
```shell
docker run -t --name elibrary-spring-boot-container --link mysql-docker-container:mysql -p 8080:8080 elibrary-spring-boot
```
[comment]: <> (docker run -t --name elibrary-spring-boot --link elibrary-mysql-data:mysql -p 8080:8080 elibrary-spring-boot)

## Developing

### Modules
* elibrary-main
* elibrary-entity
* elibrary-service
* elibrary-api
* elibrary-controller
* elibrary-rest
* elibrary-web
* elibrary-utils
* elibrary-entity-metadata

### Built With
* Java 1.8
* Spring Boot 2.4.2
* Spring Data JPA
* Spring Web
* OAuth2 Client
* ModelMapper 2.3.9
* Hibernate Types 52 2.10.4
* H2 Database Engine 1.4.200
* Thymeleaf 3.0.12
* Mockito 3.6.28
* Lombok 1.18.16
* Liquibase	3.10.3

## Versioning

* 0.0.1-SNAPSHOT

## Tests

There is test some JpaRepositories and AuthorService

```shell
mvn test
```

## Style guide

[Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)

## Database

Has been used MySQL Community Server 8.0.25

Track, version, and deploy database changes with Liquibase

[Data base diagram](db-diagram.svg), [SQL scripts](db-scripts.sql)

## Licensing

No license