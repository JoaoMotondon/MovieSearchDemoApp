package com.motondon.moviesearchdemoapp.businesslogic.interactor.series;

import com.motondon.moviesearchdemoapp.model.data.general.MovieAndSeriesType;
import com.motondon.moviesearchdemoapp.model.data.series.SeriesList;

public interface FetchSeriesInteractor {

    void execute(MovieAndSeriesType type, int page, Callback callback);

    interface Callback {
        void onFetchSeriesSuccessful(SeriesList series);
        void onFetchSeriesFailure(String errorMessage);
    }
}