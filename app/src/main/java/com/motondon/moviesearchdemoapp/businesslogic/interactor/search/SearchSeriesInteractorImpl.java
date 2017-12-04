package com.motondon.moviesearchdemoapp.businesslogic.interactor.search;

import android.util.Log;

import com.motondon.moviesearchdemoapp.businesslogic.http.api.series.SeriesApi;
import com.motondon.moviesearchdemoapp.businesslogic.http.manager.ApiTypes;
import com.motondon.moviesearchdemoapp.businesslogic.http.manager.ServiceGenerator;
import com.motondon.moviesearchdemoapp.model.data.series.SeriesList;

public class SearchSeriesInteractorImpl implements SearchSeriesInteractor {

    private static final String TAG = SearchSeriesInteractorImpl.class.getSimpleName();

    @Override
    public void execute(String query, int page, final Callback callback) {
        Log.d(TAG, "execute() - Request to the server a series list which contains a query " + query + ". Page " + page);

        SeriesApi seriesApi = (SeriesApi) ServiceGenerator.getInstance().getApiOfType(ApiTypes.SERIES_API);
        seriesApi.searchSeries(query, page, new SeriesApi.SearchSeriesCallback() {
            @Override
            public void onSearchSeriesSuccessful(SeriesList seriesList) {
                callback.onSearchSeriesSuccessful(seriesList);
            }

            @Override
            public void onSearchSeriesFailure(String errorMessage) {
                callback.onSearchSeriesFailure(errorMessage);
            }
        });
    }
}
