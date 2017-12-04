package com.motondon.moviesearchdemoapp.businesslogic.interactor.filmography;

import android.util.Log;

import com.motondon.moviesearchdemoapp.businesslogic.http.api.filmography.FilmographyApi;
import com.motondon.moviesearchdemoapp.businesslogic.http.manager.ApiTypes;
import com.motondon.moviesearchdemoapp.businesslogic.http.manager.ServiceGenerator;
import com.motondon.moviesearchdemoapp.model.data.filmography.Filmography;
import com.motondon.moviesearchdemoapp.model.data.general.SearchType;


public class FetchFilmographyInteractorImpl implements FetchFilmographyInteractor {

    private static final String TAG = FetchFilmographyInteractorImpl.class.getSimpleName();

    @Override
    public void execute(SearchType type, String actorId, final Callback callback) {
        Log.d(TAG, "execute() - Request to the server a list of movies of type " + type + " for actor (id) " + actorId);

        FilmographyApi filmographyApi = (FilmographyApi) ServiceGenerator.getInstance().getApiOfType(ApiTypes.FILMOGRAPHY_API);

        switch (type) {
            case MOVIE:

                filmographyApi.fetchFilmsByActor(actorId, new FilmographyApi.FetchFilmographyCallback() {
                    @Override
                    public void onFetchFilmographySuccessful(Filmography filmography) {
                        callback.onFetchFilmographySuccessful(filmography);
                    }

                    @Override
                    public void onFetchFilmographyFailure(String errorMessage) {
                        callback.onFetchFilmographyFailure(errorMessage);
                    }
                });
                break;

            case SERIES:
                filmographyApi.fetchSeriesByActor(actorId, new FilmographyApi.FetchFilmographyCallback() {
                    @Override
                    public void onFetchFilmographySuccessful(Filmography filmography) {
                        callback.onFetchFilmographySuccessful(filmography);
                    }

                    @Override
                    public void onFetchFilmographyFailure(String errorMessage) {
                        callback.onFetchFilmographyFailure(errorMessage);
                    }
                });
                break;
        }
    }
}
