package com.motondon.moviesearchdemoapp.businesslogic.interactor.series;

import android.util.Log;

import com.motondon.moviesearchdemoapp.businesslogic.http.api.series.SeriesApi;
import com.motondon.moviesearchdemoapp.businesslogic.http.manager.ApiTypes;
import com.motondon.moviesearchdemoapp.businesslogic.http.manager.ServiceGenerator;
import com.motondon.moviesearchdemoapp.model.data.general.MovieAndSeriesType;
import com.motondon.moviesearchdemoapp.model.data.series.SeriesList;

public class FetchSeriesInteractorImpl implements FetchSeriesInteractor {

    private static final String TAG = FetchSeriesInteractorImpl.class.getSimpleName();

    @Override
    public void execute(MovieAndSeriesType type, int page, final Callback callback) {
        Log.d(TAG, "execute() - Request to the server a series list of type " + type + ". Page " + page);

        SeriesApi seriesApi = (SeriesApi) ServiceGenerator.getInstance().getApiOfType(ApiTypes.SERIES_API);
        seriesApi.fetchSeries(type, page, new SeriesApi.FetchSeriesCallback() {
            @Override
            public void onFetchSeriesSuccessful(SeriesList seriesList) {
                callback.onFetchSeriesSuccessful(seriesList);
            }

            @Override
            public void onFetchSeriesFailure(String errorMessage) {
                callback.onFetchSeriesFailure(errorMessage);
            }
        });
    }
}
