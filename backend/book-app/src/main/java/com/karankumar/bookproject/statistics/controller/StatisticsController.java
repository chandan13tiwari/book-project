package com.karankumar.bookproject.statistics.controller;

import com.karankumar.bookproject.book.model.Book;
import com.karankumar.bookproject.book.model.BookGenre;
import com.karankumar.bookproject.shelf.service.PredefinedShelfService;
import com.karankumar.bookproject.statistics.GenreStatistics;
import com.karankumar.bookproject.statistics.PageStatistics;
import com.karankumar.bookproject.statistics.RatingStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final PredefinedShelfService predefinedShelfService;

    @Autowired
    public StatisticsController(PredefinedShelfService predefinedShelfService){
        this.predefinedShelfService = predefinedShelfService;
    }

    @GetMapping("/get-most-liked-books")
    public List<Book> getMostLikedBooks(){
        RatingStatistics ratingStatistics = new RatingStatistics(predefinedShelfService);
        return ratingStatistics.findMostLikedBook().stream().collect(Collectors.toList());

    }

    @GetMapping("/get-least-liked-books")
    public List<Book> getLeastLikedBooks(){
        RatingStatistics ratingStatistics = new RatingStatistics(predefinedShelfService);
        return ratingStatistics.findLeastLikedBook().stream().collect(Collectors.toList());
    }

    @GetMapping("/get-long-read-books")
    public List<Book> getLongReadBooks(){
        PageStatistics pageStatistics = new PageStatistics(predefinedShelfService);
        return pageStatistics.findBookWithMostPages().stream().collect(Collectors.toList());
    }

    @GetMapping("/get-most-read-books")
    public List<Book> getMostReadBooks(){
        RatingStatistics ratingStatistics = new RatingStatistics(predefinedShelfService);
        return new ArrayList<>(ratingStatistics.getReadBooksRated());
    }

    @GetMapping("/get-avg-rating-books")
    public List<Double> getAvgRatingBooks(){
        RatingStatistics ratingStatistics = new RatingStatistics(predefinedShelfService);
        return ratingStatistics.calculateAverageRatingGiven().stream().collect(Collectors.toList());
    }

    @GetMapping("/get-most-liked-genre")
    public List<BookGenre> getMostLikedGenre(){
        GenreStatistics genreStatistics = new GenreStatistics(predefinedShelfService);
        return genreStatistics.findMostLikedGenre().stream().collect(Collectors.toList());
    }

    @GetMapping("/get-avg-page-length-books")
    public List<Double> getAvgPageLengthBooks(){
        PageStatistics pageStatistics = new PageStatistics(predefinedShelfService);
        return pageStatistics.calculateAveragePageLength().stream().collect(Collectors.toList());
    }
}
