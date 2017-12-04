package com.motondon.moviesearchdemoapp.businesslogic.interactor.movie;

import android.util.Log;

import com.motondon.moviesearchdemoapp.businesslogic.http.api.movie.MovieApi;
import com.motondon.moviesearchdemoapp.businesslogic.http.manager.ApiTypes;
import com.motondon.moviesearchdemoapp.businesslogic.http.manager.ServiceGenerator;
import com.motondon.moviesearchdemoapp.model.data.movie.moviedetails.MovieDetail;

public class FetchMovieDetailsInteractorImpl implements FetchMovieDetailsInteractor {

    private static final String TAG = FetchMovieDetailsInteractorImpl.class.getSimpleName();

    @Override
    public void execute(final String movieId, final Callback callback) {
        Log.d(TAG, "loadMovieDetails() - Request to the server details of a movie with id " + movieId);

        MovieApi movieApi = (MovieApi) ServiceGenerator.getInstance().getApiOfType(ApiTypes.MOVIE_API);
        movieApi.getExtraMovieDetails(movieId, new MovieApi.MovieDetailsRequestCallback() {
            @Override
            public void onMovieDetailsRequestSuccessful(MovieDetail movieDetail) {
                callback.onFetchMovieDetailsSuccessful(movieDetail);
            }

            @Override
            public void onMovieDetailsRequestFailure(String errorMessage) {
                callback.onFetchMovieDetailsFailure(errorMessage);
            }
        });
    }
}
