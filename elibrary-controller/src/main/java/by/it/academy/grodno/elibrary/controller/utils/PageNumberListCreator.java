package by.it.academy.grodno.elibrary.controller.utils;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class PageNumberListCreator {

    private static final int COUNT_SHOW_OF_PAGE_BEFORE_AND_AFTER_THAT = 10;

    public List<Integer> getListOfPagesNumber(int numberPage, int totalPages){
        int startPage = Math.max(numberPage - 1 - COUNT_SHOW_OF_PAGE_BEFORE_AND_AFTER_THAT, 0);
        int finalPages = Math.min(numberPage - 1 + COUNT_SHOW_OF_PAGE_BEFORE_AND_AFTER_THAT, totalPages);
        return IntStream.range(startPage, finalPages)
                .boxed()
                .collect(Collectors.toList());
    }
}