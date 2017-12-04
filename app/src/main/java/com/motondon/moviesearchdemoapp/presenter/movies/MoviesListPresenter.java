package com.motondon.moviesearchdemoapp.presenter.movies;

import com.motondon.moviesearchdemoapp.model.data.general.MovieAndSeriesType;

public interface MoviesListPresenter {
    void fetchMovies(MovieAndSeriesType type, int page);
}
