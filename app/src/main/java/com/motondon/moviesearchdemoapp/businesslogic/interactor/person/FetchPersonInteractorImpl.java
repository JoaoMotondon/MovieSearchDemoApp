package com.motondon.moviesearchdemoapp.businesslogic.interactor.person;

import android.util.Log;

import com.motondon.moviesearchdemoapp.businesslogic.http.api.people.PersonApi;
import com.motondon.moviesearchdemoapp.businesslogic.http.manager.ApiTypes;
import com.motondon.moviesearchdemoapp.businesslogic.http.manager.ServiceGenerator;
import com.motondon.moviesearchdemoapp.model.data.people.PersonList;

public class FetchPersonInteractorImpl implements FetchPersonInteractor {

    private static final String TAG = FetchPersonInteractorImpl.class.getSimpleName();

    @Override
    public void execute(int page, final Callback callback) {
        Log.d(TAG, "execute() - Request to the server a person list. Page " + page);

        PersonApi personApi = (PersonApi) ServiceGenerator.getInstance().getApiOfType(ApiTypes.PEOPLE_API);
        personApi.fetchPerson(page, new PersonApi.FetchPersonCallback() {
            @Override
            public void onFetchPersonSuccessful(PersonList personList) {
                callback.onFetchPersonSuccessful(personList);
            }

            @Override
            public void onFetchPersonFailure(String errorMessage) {
                callback.onFetchPersonFailure(errorMessage);
            }
        });
    }
}
