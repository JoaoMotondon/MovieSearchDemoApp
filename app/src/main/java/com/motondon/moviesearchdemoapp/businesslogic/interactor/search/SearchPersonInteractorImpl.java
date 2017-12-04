package com.motondon.moviesearchdemoapp.businesslogic.interactor.search;

import android.util.Log;

import com.motondon.moviesearchdemoapp.businesslogic.http.api.people.PersonApi;
import com.motondon.moviesearchdemoapp.businesslogic.http.manager.ApiTypes;
import com.motondon.moviesearchdemoapp.businesslogic.http.manager.ServiceGenerator;
import com.motondon.moviesearchdemoapp.model.data.people.PersonList;

public class SearchPersonInteractorImpl implements SearchPersonInteractor {

    private static final String TAG = SearchPersonInteractorImpl.class.getSimpleName();

    @Override
    public void execute(String query, int page, final Callback callback) {
        Log.d(TAG, "execute() - Request to the server a person list which contains a query " + query + ". Page " + page);

        PersonApi personApi = (PersonApi) ServiceGenerator.getInstance().getApiOfType(ApiTypes.PEOPLE_API);
        personApi.searchPerson(query, page, new PersonApi.SearchPersonCallback() {
            @Override
            public void onSearchPersonSuccessful(PersonList personList) {
                callback.onSearchPersonSuccessful(personList);
            }

            @Override
            public void onSearchPersonFailure(String errorMessage) {
                callback.onSearchPersonFailure(errorMessage);
            }
        });

    }
}
