package by.it.academy.grodno.elibrary.api.utils;

import by.it.academy.grodno.elibrary.entities.books.Book;

import java.util.Optional;

public interface BookDataSearchStrategy {
    Optional<Book> searchBookData(String isbn);
}
