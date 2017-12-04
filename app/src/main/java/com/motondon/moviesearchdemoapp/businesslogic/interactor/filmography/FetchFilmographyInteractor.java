package com.motondon.moviesearchdemoapp.businesslogic.interactor.filmography;

import com.motondon.moviesearchdemoapp.model.data.filmography.Filmography;
import com.motondon.moviesearchdemoapp.model.data.general.SearchType;

public interface FetchFilmographyInteractor {

    void execute(SearchType type, String actorId, Callback callback);

    interface Callback {
        void onFetchFilmographySuccessful(Filmography filmography);
        void onFetchFilmographyFailure(String errorMessage);
    }
}