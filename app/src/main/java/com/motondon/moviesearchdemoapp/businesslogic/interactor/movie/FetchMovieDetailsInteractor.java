package com.motondon.moviesearchdemoapp.businesslogic.interactor.movie;

import com.motondon.moviesearchdemoapp.model.data.movie.moviedetails.MovieDetail;

public interface FetchMovieDetailsInteractor {
    void execute(final String movieId, Callback callback);

    interface Callback {
        void onFetchMovieDetailsSuccessful(MovieDetail movieDetail);
        void onFetchMovieDetailsFailure(String errorMessage);
    }
}
