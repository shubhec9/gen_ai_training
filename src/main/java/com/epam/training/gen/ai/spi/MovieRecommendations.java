package com.epam.training.gen.ai.spi;

import com.epam.training.gen.ai.model.Movie;
import java.util.List;

public interface MovieRecommendations {

    void addMovies(List<Movie> movies);

    List<Movie> getRecommendations(String userPreference);

}
