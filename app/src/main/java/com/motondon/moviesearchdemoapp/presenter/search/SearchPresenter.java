package com.motondon.moviesearchdemoapp.presenter.search;

import com.motondon.moviesearchdemoapp.model.data.general.SearchType;

public interface SearchPresenter {
    void fetchData(String query, SearchType searchType, int page);
}
