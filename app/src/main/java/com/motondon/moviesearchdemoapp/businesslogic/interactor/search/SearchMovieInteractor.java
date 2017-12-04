package com.motondon.moviesearchdemoapp.businesslogic.interactor.search;

import com.motondon.moviesearchdemoapp.model.data.movie.Movies;

public interface SearchMovieInteractor {

    void execute(String query, int page, Callback callback);

    interface Callback {
        void onSearchMovieSuccessful(Movies movieList);
        void onSearchMovieFailure(String errorMessage);
    }
}