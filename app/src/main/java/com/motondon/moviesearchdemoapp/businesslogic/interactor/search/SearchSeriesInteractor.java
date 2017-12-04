package com.motondon.moviesearchdemoapp.businesslogic.interactor.search;

import com.motondon.moviesearchdemoapp.model.data.series.SeriesList;

public interface SearchSeriesInteractor {

    void execute(String query, int page, Callback callback);

    interface Callback {
        void onSearchSeriesSuccessful(SeriesList seriesList);
        void onSearchSeriesFailure(String errorMessage);
    }
}