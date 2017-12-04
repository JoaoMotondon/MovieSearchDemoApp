package com.motondon.moviesearchdemoapp.businesslogic.interactor.movie;

import com.motondon.moviesearchdemoapp.model.data.movie.moviedetails.MovieCast;

public interface FetchMovieCastInteractor {
    void execute(final String movieId, Callback callback);

    interface Callback {
        void onFetchMovieCastSuccessful(MovieCast movieCast);
        void onFetchMovieCastFailure(String errorMessage);
    }
}
