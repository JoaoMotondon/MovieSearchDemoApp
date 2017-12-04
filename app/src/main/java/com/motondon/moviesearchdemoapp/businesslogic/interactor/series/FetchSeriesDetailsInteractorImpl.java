package com.motondon.moviesearchdemoapp.businesslogic.interactor.series;

import android.util.Log;

import com.motondon.moviesearchdemoapp.businesslogic.http.api.series.SeriesApi;
import com.motondon.moviesearchdemoapp.businesslogic.http.manager.ApiTypes;
import com.motondon.moviesearchdemoapp.businesslogic.http.manager.ServiceGenerator;
import com.motondon.moviesearchdemoapp.model.data.series.seriesdetails.SeriesDetails;

public class FetchSeriesDetailsInteractorImpl implements FetchSeriesDetailsInteractor {

    private static final String TAG = FetchSeriesDetailsInteractorImpl.class.getSimpleName();

    @Override
    public void execute(String seriesId, final Callback callback) {
        Log.d(TAG, "execute() - Request to the server details of a series with id " + seriesId);

        SeriesApi seriesApi = (SeriesApi) ServiceGenerator.getInstance().getApiOfType(ApiTypes.SERIES_API);
        seriesApi.getExtraSeriesDetails(seriesId, new SeriesApi.SeriesDetailsRequestCallback() {
            @Override
            public void onSeriesDetailsRequestSuccessful(SeriesDetails seriesDetails) {
                callback.onFetchSeriesDetailsSuccessful(seriesDetails);
            }

            @Override
            public void onSeriesDetailsRequestFailure(String errorMessage) {
                callback.onFetchSeriesDetailsFailure(errorMessage);
            }
        });
    }
}
