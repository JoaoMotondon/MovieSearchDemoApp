package com.motondon.moviesearchdemoapp.businesslogic.interactor.series;

import com.motondon.moviesearchdemoapp.model.data.series.seriesdetails.SeriesDetails;

public interface FetchSeriesDetailsInteractor {
    void execute(String seriesId, Callback callback);

    interface Callback {
        void onFetchSeriesDetailsSuccessful(SeriesDetails seriesDetails);
        void onFetchSeriesDetailsFailure(String errorMessage);
    }
}
