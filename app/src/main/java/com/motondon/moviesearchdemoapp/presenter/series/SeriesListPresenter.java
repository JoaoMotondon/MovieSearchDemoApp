package com.motondon.moviesearchdemoapp.presenter.series;

import com.motondon.moviesearchdemoapp.model.data.general.MovieAndSeriesType;

public interface SeriesListPresenter {
    void fetchSeries(MovieAndSeriesType type, int page);
}
