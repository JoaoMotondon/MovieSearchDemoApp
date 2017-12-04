package com.motondon.moviesearchdemoapp.businesslogic.interactor.movie;

import android.util.Log;

import com.motondon.moviesearchdemoapp.businesslogic.http.api.movie.MovieApi;
import com.motondon.moviesearchdemoapp.businesslogic.http.manager.ApiTypes;
import com.motondon.moviesearchdemoapp.businesslogic.http.manager.ServiceGenerator;
import com.motondon.moviesearchdemoapp.model.data.movie.moviedetails.MovieCast;

public class FetchMovieCastInteractorImpl implements FetchMovieCastInteractor {

    private static final String TAG = FetchMovieCastInteractorImpl.class.getSimpleName();

    @Override
    public void execute(String movieId, final Callback callback) {
        Log.d(TAG, "execute() - Request to the server a cast of a movie with id " + movieId);

        MovieApi movieApi = (MovieApi) ServiceGenerator.getInstance().getApiOfType(ApiTypes.MOVIE_API);
        movieApi.getMovieCast(movieId, new MovieApi.MovieCastRequestCallback() {
            @Override
            public void onCastRequestSuccessful(MovieCast movieCast) {
                callback.onFetchMovieCastSuccessful(movieCast);
            }

            @Override
            public void onCastRequestFailure(String errorMessage) {
                callback.onFetchMovieCastFailure(errorMessage);
            }
        });
    }
}
