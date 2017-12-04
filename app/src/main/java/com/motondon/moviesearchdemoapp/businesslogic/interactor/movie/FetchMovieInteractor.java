package com.motondon.moviesearchdemoapp.businesslogic.interactor.movie;

import com.motondon.moviesearchdemoapp.model.data.general.MovieAndSeriesType;
import com.motondon.moviesearchdemoapp.model.data.movie.Movies;

public interface FetchMovieInteractor {

    void execute(MovieAndSeriesType type, int page, Callback callback);

    interface Callback {
        void onFetchMovieSuccessful(Movies movieList);
        void onFetchMovieFailure(String errorMessage);
    }
}