package com.motondon.moviesearchdemoapp.businesslogic.interactor.movie;

import android.util.Log;

import com.motondon.moviesearchdemoapp.businesslogic.http.api.movie.MovieApi;
import com.motondon.moviesearchdemoapp.businesslogic.http.manager.ApiTypes;
import com.motondon.moviesearchdemoapp.businesslogic.http.manager.ServiceGenerator;
import com.motondon.moviesearchdemoapp.model.data.general.MovieAndSeriesType;
import com.motondon.moviesearchdemoapp.model.data.movie.Movies;

public class FetchMovieInteractorImpl implements FetchMovieInteractor {

    private static final String TAG = FetchMovieInteractorImpl.class.getSimpleName();

    @Override
    public void execute(MovieAndSeriesType type, int page, final Callback callback) {
        Log.d(TAG, "execute() - Request to the server a list of movies of type " + type + ". Page " + page);

        MovieApi movieApi = (MovieApi) ServiceGenerator.getInstance().getApiOfType(ApiTypes.MOVIE_API);
        movieApi.fetchMovies(type, page, new MovieApi.FetchMovieCallback() {
            @Override
            public void onFetchMovieSuccessful(Movies movieList) {
                callback.onFetchMovieSuccessful(movieList);
            }

            @Override
            public void onFetchMovieFailure(String errorMessage) {
                callback.onFetchMovieFailure(errorMessage);
            }
        });
    }
}
