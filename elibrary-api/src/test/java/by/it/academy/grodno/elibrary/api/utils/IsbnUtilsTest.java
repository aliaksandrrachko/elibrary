package by.it.academy.grodno.elibrary.api.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IsbnUtilsTest {

    private static final String[] CORRECT_ISBN_10 = {
            "0004499123",
            "5932860057",
            "5389047311",
            "5389050800",
            "5041135894",
            "5961467996" ,
            "032193315X"};

    private static final String[] CORRECT_ISBN_13 = {
            "9780004499123",
            "9785932860052",
            "9785389047310",
            "9785389050808",
            "9785041135898",
            "9785961467994",
            "9780321933157"};

    private static final String[] INCORRECT_ISBN = {
            "9780004499124",
            "9785932860056",
            "9785389047315",
            "9785041135890",
            "9785041135894",
            "9785961467998"};


    @Test
    void isValid() {
        for (String isbn10 : CORRECT_ISBN_10) {
            assertThat(IsbnUtils.isIsbn10(isbn10)).isTrue();
        }
        for (String isbn13 : CORRECT_ISBN_13) {
            assertThat(IsbnUtils.isIsbn13(isbn13)).isTrue();
        }
        for (String isbn : INCORRECT_ISBN) {
            assertThat(IsbnUtils.isValid(isbn)).isFalse();
        }
    }

    @Test
    void toIsbn10() {
        for (int i = 0; i < CORRECT_ISBN_13.length; i++) {
            assertThat(IsbnUtils.toIsbn10(CORRECT_ISBN_13[i])).isEqualTo(CORRECT_ISBN_10[i]);
        }
    }

    @Test
    void toIsbn13() {
        for (int i = 0; i < CORRECT_ISBN_10.length; i++) {
            assertThat(IsbnUtils.toIsbn13(CORRECT_ISBN_10[i])).isEqualTo(CORRECT_ISBN_13[i]);
        }
    }
}
