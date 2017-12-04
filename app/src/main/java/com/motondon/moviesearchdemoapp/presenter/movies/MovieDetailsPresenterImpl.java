package com.motondon.moviesearchdemoapp.presenter.movies;

import android.content.Context;
import android.util.Log;

import com.motondon.moviesearchdemoapp.businesslogic.interactor.movie.FetchMovieCastInteractor;
import com.motondon.moviesearchdemoapp.businesslogic.interactor.movie.FetchMovieCastInteractorImpl;
import com.motondon.moviesearchdemoapp.businesslogic.interactor.movie.FetchMovieDetailsInteractor;
import com.motondon.moviesearchdemoapp.businesslogic.interactor.movie.FetchMovieDetailsInteractorImpl;
import com.motondon.moviesearchdemoapp.model.data.movie.moviedetails.MovieCast;
import com.motondon.moviesearchdemoapp.model.data.movie.moviedetails.MovieDetail;

public class MovieDetailsPresenterImpl implements MovieDetailsPresenter {

    private static final String TAG = MovieDetailsPresenterImpl.class.getSimpleName();

    public interface MovieDetailsView {
        void displayMovieDetails(MovieDetail movieDetails);
        void displayMovieCast(MovieCast movieCast);
        void requestFailure(String errorMessage);
    }

    private MovieDetailsView mView;
    private Context mContext;

    private FetchMovieDetailsInteractor fetchMovieDetailsInteractor;
    private FetchMovieCastInteractor fetchMovieCastInteractor;

    public MovieDetailsPresenterImpl(MovieDetailsView view, Context context) {
        this.mView = view;
        this.mContext = context;

        // Create business logic objects to interact with
        fetchMovieDetailsInteractor = new FetchMovieDetailsInteractorImpl();
        fetchMovieCastInteractor = new FetchMovieCastInteractorImpl();
    }

    /**
     * Called by the view layer. It will request a movie details data to the server. When data is received (asynchronously)
     * it will call a view method which will be responsible to manipulate it.
     *
     * @param movieId
     */
    @Override
    public void loadMovieDetails(final String movieId) {
        Log.d(TAG, "loadMovieDetails() - Request to the server details of a movie with id " + movieId);

        fetchMovieDetailsInteractor.execute(movieId, new FetchMovieDetailsInteractor.Callback() {
            @Override
            public void onFetchMovieDetailsSuccessful(MovieDetail movieDetails) {
                mView.displayMovieDetails(movieDetails);
            }

            @Override
            public void onFetchMovieDetailsFailure(String errorMessage) {
                mView.requestFailure(errorMessage);
            }
        });
    }

    /**
     * Called by the view layer. It will request a movie cast data to the server. When data is received (asynchronously)
     * it will call a view method which will be responsible to manipulate it.
     *
     * @param movieId
     */
    @Override
    public void loadMovieCast(final String movieId) {
        Log.d(TAG, "loadMovieCast() - Request to the server a cast of a movie with id " + movieId);

        fetchMovieCastInteractor.execute(movieId, new FetchMovieCastInteractor.Callback() {
            @Override
            public void onFetchMovieCastSuccessful(MovieCast movieCast) {
                mView.displayMovieCast(movieCast);
            }

            @Override
            public void onFetchMovieCastFailure(String errorMessage) {
                mView.requestFailure(errorMessage);
            }
        });
    }
}
