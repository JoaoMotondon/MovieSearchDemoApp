package com.motondon.moviesearchdemoapp.businesslogic.interactor.series;

import android.util.Log;

import com.motondon.moviesearchdemoapp.businesslogic.http.api.series.SeriesApi;
import com.motondon.moviesearchdemoapp.businesslogic.http.manager.ApiTypes;
import com.motondon.moviesearchdemoapp.businesslogic.http.manager.ServiceGenerator;
import com.motondon.moviesearchdemoapp.model.data.movie.moviedetails.MovieCast;

public class FetchSeriesCastInteractorImpl implements FetchSeriesCastInteractor {

    private static final String TAG = FetchSeriesCastInteractorImpl.class.getSimpleName();

    @Override
    public void execute(String seriesId, final Callback callback) {
        Log.d(TAG, "execute() - Request to the server a cast of a series with id " + seriesId);

        SeriesApi seriesApi = (SeriesApi) ServiceGenerator.getInstance().getApiOfType(ApiTypes.SERIES_API);
        seriesApi.getSeriesCast(seriesId, new SeriesApi.SeriesCastRequestCallback() {
            @Override
            public void onSeriesCastRequestSuccessful(MovieCast movieCast) {
                callback.onFetchSeriesCastSuccessful(movieCast);
            }

            @Override
            public void onSeriesCastRequestFailure(String errorMessage) {
                callback.onFetchSeriesCastFailure(errorMessage);
            }
        });
    }
}
