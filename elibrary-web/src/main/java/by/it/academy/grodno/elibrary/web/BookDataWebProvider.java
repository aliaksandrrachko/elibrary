package by.it.academy.grodno.elibrary.web;

import by.it.academy.grodno.elibrary.api.utils.BookDataProvider;
import by.it.academy.grodno.elibrary.entities.books.Book;
import by.it.academy.grodno.elibrary.api.utils.BookDataSearchStrategy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookDataWebProvider implements BookDataProvider {

    private final List<BookDataSearchStrategy> scraper;

    public BookDataWebProvider(List<BookDataSearchStrategy> scraper) {
        this.scraper = scraper;
    }

    public Optional<Book> getBook(String isbn){
        for (BookDataSearchStrategy bookSearchStrategy : scraper) {
            Optional<Book> bookOptional = bookSearchStrategy.searchBookData(isbn);
            if (bookOptional.isPresent()){
                return bookOptional;
            }
        }
        return Optional.empty();
    }
}
