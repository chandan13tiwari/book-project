package com.karankumar.bookproject.statistics.controller;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.book.model.Book;
import com.karankumar.bookproject.book.model.BookGenre;
import com.karankumar.bookproject.book.service.BookService;
import com.karankumar.bookproject.shelf.service.PredefinedShelfService;
import com.karankumar.bookproject.statistics.RatingStatistics;
import com.karankumar.bookproject.statistics.util.StatisticTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@IntegrationTest
class StatisticsControllerTest {
    private final BookService bookService;
    private final PredefinedShelfService predefinedShelfService;

    private Book bookWithNoRating;
    private Book bookWithHighestRating;

    private Book bookWithMostPages;
    private Book bookWithMostReads;

    private Double avgRatingOfReadBooks;
    private Double avgPageLengthOfReadBooks;
    private BookGenre bookWithMostLikedGenre;

    private final StatisticsController statisticsController;

    @Autowired
    StatisticsControllerTest(BookService bookService, PredefinedShelfService predefinedShelfService, StatisticsController statisticsController) {
        this.bookService = bookService;
        this.predefinedShelfService = predefinedShelfService;
        this.statisticsController = statisticsController;
    }

    @BeforeEach
    public void setUp() {
        bookService.deleteAll(); // reset
        StatisticTestUtils.populateReadBooks(bookService, predefinedShelfService);
        bookWithNoRating = StatisticTestUtils.getBookWithLowestRating();
        bookWithHighestRating = StatisticTestUtils.getBookWithHighestRating();

        bookWithMostPages = StatisticTestUtils.getBookWithMostPages();

        RatingStatistics ratingStatistics = new RatingStatistics(predefinedShelfService);

        bookWithMostReads = ratingStatistics.getReadBooksRated().get(0);

        avgRatingOfReadBooks = StatisticTestUtils.avgRatingForReadBooks;
        avgPageLengthOfReadBooks = StatisticTestUtils.avgPageLengthForReadBooks;

        bookWithMostLikedGenre = StatisticTestUtils.MOST_LIKED_BOOK_GENRE;
    }

    @Test
    @DisplayName("Getting the most liked book from the shelf")
    void getMostLikedBook() {
        List<Book> actual = statisticsController.getMostLikedBooks();
        Assertions.assertEquals(bookWithHighestRating.getTitle(), actual.get(0).getTitle());
    }

    @Test
    @DisplayName("Getting the least liked book from the shelf")
    void getLeastLikedBook() {
        List<Book> actual = statisticsController.getLeastLikedBooks();
        Assertions.assertEquals(bookWithNoRating.getTitle(), actual.get(0).getTitle());
    }

    @Test
    @DisplayName("Getting the long read book from the shelf")
    void getLongReadBook() {
        List<Book> actual = statisticsController.getLongReadBooks();
        Assertions.assertEquals(bookWithMostPages.getTitle(), actual.get(0).getTitle());
    }

    @Test
    @DisplayName("Getting the most read book from the shelf")
    void getMostRatedBook() {
        List<Book> actual = statisticsController.getMostReadBooks();
        Assertions.assertEquals(bookWithMostReads.getTitle(), actual.get(0).getTitle());
    }

    @Test
    @DisplayName("Getting the average rating book from the shelf")
    void getAvgRatedBook() {
        List<Double> actual = statisticsController.getAvgRatingBooks();
        Assertions.assertEquals(avgRatingOfReadBooks, actual.get(0));
    }

    @Test
    @DisplayName("Getting the average page length book from the shelf")
    void getAvgPageLengthBook() {
        List<Double> actual = statisticsController.getAvgPageLengthBooks();
        Assertions.assertEquals(avgPageLengthOfReadBooks, actual.get(0));
    }

    @Test
    @DisplayName("Getting the most liked book genre from the shelf")
    @Transactional
    void getMostLikedGenreBook() {
        List<BookGenre> actual = statisticsController.getMostLikedGenre();
        Assertions.assertEquals(bookWithMostLikedGenre, actual.get(0));
    }
}
