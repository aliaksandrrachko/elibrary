package by.it.academy.grodno.elibrary.api.utils;

/**
 * Utility class for validation and converting ISBN-10 and ISBN-13 number.
 * <p> ISBN (International Standard Book Number) is a unique number assigned to each book.
 * <ul>ISBN-10:
 *     <li>The number has 9 information digits and ends with 1 check digit.</li>
 *     <li>Assuming the digits are "abcdefghi-j" where j is the check digit.
 *         Then the check digit is computed by the following formula:
 *         <p>j = ( [a b c d e f g h i] * [1 2 3 4 5 6 7 8 9] ) mod 11</p>
 *     </li>
 * <ul> ISBN-13:
 *     <li>The number has 12 information digits and ends with 1 check digit.</li>
 *     <li>Assuming the digits are "abcdefghijkl-m" where m is the check digit.
 *         Then the check digit is computed by the following formula:
 *         <p>m = ( [a b c d e f g h i j k l] * [1 3 1 3 1 3 1 3 1 3 1 3] ) mod 10</p>
 *     </li>
 * </ul>
 * <p>Note: The Roman numeral X is used in lieu of 10 where ten would occur as a check digit.</p>
 */
public final class IsbnUtils {

    private IsbnUtils() {
    }

    private static final String CONSTANT_PREFIX_GS1 = "978";

    public static boolean isValid(String isbnString){
        return isIsbn10(isbnString) || isIsbn13(isbnString);
    }

    public static boolean isIsbn10(String isbn10){
        isbn10 = getOnlyDigit(isbn10);
        int checkNumber = getCheckNumberFromIsbn(isbn10.substring(9));
        return isbn10.length() == 10 &&
                calculateCheekNumberIsbn10(isbn10.substring(0,9)) == checkNumber;
    }

    private static int getCheckNumberFromIsbn(String substring) {
        if (substring.equals("X")){
            return 10;
        }
        return Integer.parseInt(substring);
    }

    public static boolean isIsbn13(String isbn13){
        isbn13 = getOnlyDigit(isbn13);
        int checkNumber = getCheckNumberFromIsbn(isbn13.substring(12));
        return isbn13.length() == 13 &&
                isbn13.startsWith(CONSTANT_PREFIX_GS1) &&
                calculateCheekNumberIsbn13(isbn13.substring(0,12)) == checkNumber;
    }

    /**
     * Deletes in given string all no digit characters exclude 'X'.
     * @param string the string
     * @return the cleaning string
     */
    public static String getOnlyDigit(String string){
            return string.replaceAll("[^\\dX.]", "");
    }

    public static String toIsbn10(String isbn13){
        String cleanerIsbn = getOnlyDigit(isbn13);
        if (isIsbn13(cleanerIsbn)) {
            String informationDigits = cleanerIsbn.substring(3, 12);
            int cheekNumber = calculateCheekNumberIsbn10(informationDigits);
            String cheekNumberAsString = cheekNumber == 10 ? "X" : String.valueOf(cheekNumber);
            return informationDigits + cheekNumberAsString;
        } else if (isIsbn10(cleanerIsbn)){
            return cleanerIsbn;
        } else {
            throw new IllegalArgumentException(
                    String.format("The number '%s' is not ISBN", isbn13));
        }
    }

    public static String toIsbn13(String isbn10){
        String cleanerIsbn = getOnlyDigit(isbn10);
        if (isIsbn10(cleanerIsbn)) {
            String informationDigits = CONSTANT_PREFIX_GS1 + cleanerIsbn.substring(0, 9);
            int cheekNumber = calculateCheekNumberIsbn13(informationDigits);
            String cheekNumberAsString = cheekNumber == 10 ? "X" : String.valueOf(cheekNumber);
            return informationDigits + cheekNumberAsString;
        } else if (isIsbn13(cleanerIsbn)) {
            return cleanerIsbn;
        } else {
            throw new IllegalArgumentException(
                    String.format("The number '%s' is not ISBN", isbn10));
        }
    }

    private static final int[] ISBN_10_COEFFICIENTS = {1, 2, 3, 4, 5, 6, 7, 8, 9};

    private static int calculateCheekNumberIsbn10(String informationDigits){
        int sum = 0;
        for (int i = 0; i < informationDigits.length(); i++) {
            sum += Integer.parseInt(String.valueOf(informationDigits.charAt(i))) *
                    ISBN_10_COEFFICIENTS[i];
        }
        return sum % 11;
    }

    private static final int[] ISBN_13_COEFFICIENTS = {1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3};

    private static int calculateCheekNumberIsbn13(String informationDigits){
        int sum = 0;
        for (int i = 0; i < informationDigits.length(); i++) {
            sum += Integer.parseInt(String.valueOf(informationDigits.charAt(i))) *
                    ISBN_13_COEFFICIENTS[i];
        }
        int r = 10 - (sum % 10);
        return r == 10 ? 0 : r;
    }
}
