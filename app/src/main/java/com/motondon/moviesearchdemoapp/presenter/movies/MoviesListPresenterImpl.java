package com.motondon.moviesearchdemoapp.presenter.movies;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.motondon.moviesearchdemoapp.businesslogic.interactor.movie.FetchMovieInteractor;
import com.motondon.moviesearchdemoapp.businesslogic.interactor.movie.FetchMovieInteractorImpl;
import com.motondon.moviesearchdemoapp.model.data.general.MovieAndSeriesType;
import com.motondon.moviesearchdemoapp.model.data.movie.Movies;

public class MoviesListPresenterImpl implements MoviesListPresenter {

    public interface MoviesListView {

        void displayMovies(Movies movies);
        void showProgressDialog();
        void hideProgressDialog();
        void requestFailure(String errorMessage);
        void loadImage(Context context, String coverUrl, String imageId, ImageView ivCover);
    }

    private static final String TAG = MoviesListPresenterImpl.class.getSimpleName();

    private MoviesListView mView;
    private FetchMovieInteractor fetchMovieInteractor;

    public MoviesListPresenterImpl(MoviesListView view) {
        this.mView = view;

        // Create business logic object to interact with
        fetchMovieInteractor = new FetchMovieInteractorImpl();
    }

    /**
     * Called by the view layer. It will request Movies data to the server. When data is received (asynchronously)
     * it will call a view method which will be responsible to manipulate it.
     *
     * @param type
     * @param page
     */
    @Override
    public void fetchMovies(MovieAndSeriesType type, int page) {
        Log.d(TAG, "fetchMovies() - Request to the server a list of movies of type " + type + ". Page " + page);

        mView.showProgressDialog();

        fetchMovieInteractor.execute(type, page, new FetchMovieInteractor.Callback() {
            @Override
            public void onFetchMovieSuccessful(Movies movieList) {
                mView.hideProgressDialog();
                mView.displayMovies(movieList);
            }

            @Override
            public void onFetchMovieFailure(String errorMessage) {
                mView.hideProgressDialog();
                mView.requestFailure(errorMessage);
            }
        });
    }
}
