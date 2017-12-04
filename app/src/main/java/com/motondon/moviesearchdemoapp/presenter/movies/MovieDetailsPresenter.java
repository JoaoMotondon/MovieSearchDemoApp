package com.motondon.moviesearchdemoapp.presenter.movies;

public interface MovieDetailsPresenter {
    void loadMovieDetails(final String movieIdr);
    void loadMovieCast(final String movieId);
}
