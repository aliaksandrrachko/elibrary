package by.it.academy.grodno.elibrary.web.converters;

import by.it.academy.grodno.elibrary.api.utils.IsbnUtils;
import by.it.academy.grodno.elibrary.entities.books.Book;
import by.it.academy.grodno.elibrary.entities.books.Publisher;
import by.it.academy.grodno.elibrary.web.responseentities.OpenLibraryBookResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OpenLibraryResponseBookConverter implements Converter<OpenLibraryBookResponse, Book> {

    @Override
    public Book convert(OpenLibraryBookResponse source) {
        return Book.builder()
                .isbn10(getIsbn10(source))
                .isbn13(getIsbn13(source))
                .title(source.getTitle())
                .description(getFirstSentence(source))
                .datePublishing(source.getDatePublishing())
                .language(getLanguage(getValueByKeyFromListOfMap(source.getLanguagesPath())))
                .pictureUrl(buildPictureUri(getRepresentValueFromList(source.getCovers())))
                .printLength(source.getPrintLength() == null ? 0 : source.getPrintLength())
                .publisher(Publisher.builder().publisherName(getRepresentValueFromList(source.getPublishers())).build())
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

    private String getIsbn10(OpenLibraryBookResponse source){
        String isbn10 = getRepresentValueFromList(source.getIsbn10());
        String isbn13 = getRepresentValueFromList(source.getIsbn13());
        if (StringUtils.hasText(isbn10)){
            return isbn10;
        } else if (StringUtils.hasText(isbn13)){
            return IsbnUtils.toIsbn10(isbn13);
        } else {
            return null;
        }
    }

    private String getIsbn13(OpenLibraryBookResponse source){
        String isbn10 = getRepresentValueFromList(source.getIsbn10());
        String isbn13 = getRepresentValueFromList(source.getIsbn13());
        if (StringUtils.hasText(isbn13)){
            return isbn13;
        } else if (StringUtils.hasText(isbn10)){
            return IsbnUtils.toIsbn13(isbn10);
        } else {
            return null;
        }
    }

    private String getFirstSentence(OpenLibraryBookResponse source){
        Map<String, String> mapFirstSentence = source.getFirstSentence();
        if (mapFirstSentence != null){
            return mapFirstSentence.getOrDefault("value", null);
        } else {
            return null;
        }
    }
}
