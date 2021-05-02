package by.it.academy.grodno.elibrary.web;

import by.it.academy.grodno.elibrary.api.utils.BookDataSearchStrategy;
import by.it.academy.grodno.elibrary.entities.books.Book;
import by.it.academy.grodno.elibrary.api.dto.books.OpenLibraryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class OpenLibraryBookScraper implements BookDataSearchStrategy {

    private static final String OPEN_LIBRARY_URL = "https://openlibrary.org";

    private static final String OPEN_LIBRARY_BOOK_ISBN_API_URI = "https://openlibrary.org/isbn/{isbn}.json";

    private final RestOperations restOperations;

    public OpenLibraryBookScraper() {
        this.restOperations = createRestTemplate();
    }

    @Override
    public Optional<Book> searchBookData(String isbn) {
        ResponseEntity<OpenLibraryResponse> responseResponseEntity =
                restOperations.getForEntity(OPEN_LIBRARY_BOOK_ISBN_API_URI, OpenLibraryResponse.class, isbn);
        OpenLibraryResponse openLibraryResponse = responseResponseEntity.getBody();

        return Optional.empty();
    }

    private RestTemplate createRestTemplate() {
        return new RestTemplate();
    }
}
