package com.motondon.moviesearchdemoapp.businesslogic.interactor.search;

import com.motondon.moviesearchdemoapp.model.data.people.PersonList;

public interface SearchPersonInteractor {

    void execute(String query, int page, Callback callback);

    interface Callback {
        void onSearchPersonSuccessful(PersonList personList);
        void onSearchPersonFailure(String errorMessage);
    }
}