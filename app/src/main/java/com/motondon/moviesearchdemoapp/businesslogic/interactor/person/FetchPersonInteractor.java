package com.motondon.moviesearchdemoapp.businesslogic.interactor.person;

import com.motondon.moviesearchdemoapp.model.data.people.PersonList;

public interface FetchPersonInteractor {

    void execute(int page, Callback callback);

    interface Callback {
        void onFetchPersonSuccessful(PersonList personList);
        void onFetchPersonFailure(String errorMessage);
    }
}