package com.motondon.moviesearchdemoapp.businesslogic.interactor.search;

import android.util.Log;

import com.motondon.moviesearchdemoapp.businesslogic.http.api.movie.MovieApi;
import com.motondon.moviesearchdemoapp.businesslogic.http.manager.ApiTypes;
import com.motondon.moviesearchdemoapp.businesslogic.http.manager.ServiceGenerator;
import com.motondon.moviesearchdemoapp.model.data.movie.Movies;

public class SearchMovieInteractorImpl implements SearchMovieInteractor {

    private static final String TAG = SearchMovieInteractorImpl.class.getSimpleName();

    @Override
    public void execute(String query, int page, final Callback callback) {
        Log.d(TAG, "execute() - Request to the server a movie list which contains a query " + query + ". Page " + page);

        MovieApi movieApi = (MovieApi) ServiceGenerator.getInstance().getApiOfType(ApiTypes.MOVIE_API);
        movieApi.searchMovies(query, page, new MovieApi.SearchMovieCallback() {
            @Override
            public void onSearchMovieSuccessful(Movies movieList) {
                callback.onSearchMovieSuccessful(movieList);
            }

            @Override
            public void onSearchMovieFailure(String errorMessage) {
                callback.onSearchMovieFailure(errorMessage);
            }
        });
    }
}
