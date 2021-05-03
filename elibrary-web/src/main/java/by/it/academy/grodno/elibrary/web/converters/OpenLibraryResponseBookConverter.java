package by.it.academy.grodno.elibrary.web.converters;

import by.it.academy.grodno.elibrary.entities.books.Book;
import by.it.academy.grodno.elibrary.entities.books.Publisher;
import by.it.academy.grodno.elibrary.web.responseentities.OpenLibraryBookResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OpenLibraryResponseBookConverter implements Converter<OpenLibraryBookResponse, Book> {

    @Override
    public Book convert(OpenLibraryBookResponse source) {
        return Book.builder()
                .isbn10(getRepresentValueFromList(source.getIsbn10()))
                .isbn13(getRepresentValueFromList(source.getIsbn13()))
                .title(source.getTitle())
                .datePublishing(source.getDatePublishing())
                .language(getLanguage(getValueByKeyFromListOfMap(source.getLanguagesPath())))
                .pictureUrl(buildPictureUri(getRepresentValueFromList(source.getCovers())))
                .printLength(source.getPrintLength())
                .publisher(new Publisher(getRepresentValueFromList(source.getPublishers())))
                .build();
    }

    private String getRepresentValueFromList(List<String> source) {
        if (source != null) {
            return source.stream().filter(StringUtils::hasText).collect(Collectors.toList()).get(0);
        } else {
            return null;
        }
    }

    private String getValueByKeyFromListOfMap(List<Map<String, Object>> mapList) {
        if (mapList != null) {
            Map<String, Object> map = mapList.get(0);
            return (String) map.get("key");
        } else {
            return null;
        }
    }

    private String getLanguage(String languageLastThreeSymbol) {
        if (languageLastThreeSymbol != null) {
            String[] arrayLanguagePathVariables = languageLastThreeSymbol.split("/");
            return arrayLanguagePathVariables[arrayLanguagePathVariables.length - 1];
        } else {
            return null;
        }
    }

    private static final String PICTURE_COVER_URI_PATTERN = "https://covers.openlibrary.org/b/id/%s-L.jpg";

    private String buildPictureUri(String coverCode) {
        if (coverCode != null) {
            return String.format(PICTURE_COVER_URI_PATTERN, coverCode);
        } else {
            return null;
        }
    }
}
