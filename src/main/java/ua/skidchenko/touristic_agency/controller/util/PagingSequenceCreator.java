package ua.skidchenko.touristic_agency.controller.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PagingSequenceCreator {

    private PagingSequenceCreator() {
    }

    public static List<Integer> getPagingSequence(int startPageNum, int totalPages) {
        return IntStream.rangeClosed(startPageNum, totalPages)
                .boxed()
                .collect(Collectors.toList());
    }
}
