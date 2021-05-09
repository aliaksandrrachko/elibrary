package by.it.academy.grodno.elibrary.web;

import by.it.academy.grodno.elibrary.entities.books.Author;
import by.it.academy.grodno.elibrary.web.converters.OpenLibraryAuthorResponseConverter;
import by.it.academy.grodno.elibrary.web.responseentities.OpenLibraryAuthorResponse;
import by.it.academy.grodno.elibrary.web.responseentities.OpenLibraryBookResponse;
import by.it.academy.grodno.elibrary.api.utils.BookDataSearchStrategy;
import by.it.academy.grodno.elibrary.entities.books.Book;
import by.it.academy.grodno.elibrary.web.converters.OpenLibraryResponseBookConverter;
import by.it.academy.grodno.elibrary.web.responseentities.OpenLibraryWorkResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownContentTypeException;

import java.net.URI;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OpenLibraryBookStrategy implements BookDataSearchStrategy {

    private static final String OPEN_LIBRARY_URL = "https://openlibrary.org";

    private static final String REQUEST_SUFFIX = ".json";

    private static final Converter<OpenLibraryBookResponse, Book> openLibraryResponseBookConverter
            = new OpenLibraryResponseBookConverter();

    private final RestOperations restOperations;
    public OpenLibraryBookStrategy() {
        this.restOperations = createRestTemplate();
    }

    private RestTemplate createRestTemplate() {
        return new RestTemplate();
    }

    private static final String OPEN_LIBRARY_BOOK_ISBN_API_URI = "https://openlibrary.org/isbn/%s.json";

    @Override
    public Optional<Book> searchBookData(String isbn) {
        String preparingRequest = String.format(OPEN_LIBRARY_BOOK_ISBN_API_URI, isbn);
        URI requestUri = URI.create(preparingRequest);
        RequestEntity<?> requestEntity = new RequestEntity<>(HttpMethod.GET, requestUri);
        ResponseEntity<?> responseEntity = getResponse(requestEntity, OpenLibraryBookResponse.class);
        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            OpenLibraryBookResponse openLibraryBookResponseBody = (OpenLibraryBookResponse) responseEntity.getBody();
            if (openLibraryBookResponseBody != null) {
                Book book = openLibraryResponseBookConverter.convert(openLibraryBookResponseBody);
                if (book != null) {
                    Set<String> setOfAuthorPath = openLibraryBookResponseBody.getAuthorPath().get(0)
                            .values().stream().map(String.class::cast).collect(Collectors.toSet());
                    book.setAuthors(getAuthors(setOfAuthorPath));
                    book.setDescription(getDescription((String) openLibraryBookResponseBody.getWorks().get(0).get("key")));
                    return Optional.of(book);
                }
            }
        }
        return Optional.empty();
    }

    private ResponseEntity<?> getResponse(RequestEntity<?> request, Class<?> response){
        try {
            return restOperations.exchange(request, response);
        } catch (UnknownContentTypeException ex) {
            log.error(ex.getResponseBodyAsString());
        } catch (RestClientException ex) {
            log.error(ex.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private static final Converter<OpenLibraryAuthorResponse, Author> openLibraryAuthorResponseConverter =
            new OpenLibraryAuthorResponseConverter();

    /**
     * Returns set of authors from  <a href= https://openlibrary.org>openlibrary.org</a> {@code API}.
     * @param setOfAuthorPath set of authors path (ex. '/authors/OL34184A')
     * @return set of authors
     */
    public Set<Author> getAuthors(Set<String> setOfAuthorPath) {
        Set<Author> authors = new HashSet<>();
        setOfAuthorPath.forEach(s -> {
            URI authorRequestUri = URI.create(OPEN_LIBRARY_URL + s + REQUEST_SUFFIX);
            RequestEntity<?> requestEntity = new RequestEntity<>(HttpMethod.GET, authorRequestUri);
            ResponseEntity<?> response = getResponse(requestEntity, OpenLibraryAuthorResponse.class);
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                OpenLibraryAuthorResponse openLibraryAuthorResponseBody = (OpenLibraryAuthorResponse) response.getBody();
                if (openLibraryAuthorResponseBody != null) {
                    authors.add(openLibraryAuthorResponseConverter.convert(openLibraryAuthorResponseBody));
                }
            }
        });
        return authors;
    }

    /**
     * Returns the books' description from <a href= https://openlibrary.org>openlibrary.org</a> {@code API}.
     * @param relativeWorkPath the string of path (ex. '/works/OL34184A')
     * @return the book's description
     */
    public String getDescription(String relativeWorkPath) {
        URI authorRequestUri = URI.create(OPEN_LIBRARY_URL + relativeWorkPath + REQUEST_SUFFIX);
        RequestEntity<?> requestEntity = new RequestEntity<>(HttpMethod.GET, authorRequestUri);
        ResponseEntity<?> response = getResponse(requestEntity, OpenLibraryWorkResponse.class);
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            OpenLibraryWorkResponse openLibraryWorkResponse = (OpenLibraryWorkResponse) response.getBody();
            return  openLibraryWorkResponse != null ? openLibraryWorkResponse.getDescription() : null;
        }
        return null;
    }
}
