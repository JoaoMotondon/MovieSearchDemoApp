package com.motondon.moviesearchdemoapp.businesslogic.interactor.series;

import com.motondon.moviesearchdemoapp.model.data.movie.moviedetails.MovieCast;

public interface FetchSeriesCastInteractor {
    void execute(final String seriesId, Callback callback);

    interface Callback {
        void onFetchSeriesCastSuccessful(MovieCast movieCast);
        void onFetchSeriesCastFailure(String errorMessage);
    }
}
