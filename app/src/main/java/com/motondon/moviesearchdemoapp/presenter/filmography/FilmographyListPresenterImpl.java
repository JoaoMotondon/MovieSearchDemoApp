package com.motondon.moviesearchdemoapp.presenter.filmography;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.motondon.moviesearchdemoapp.businesslogic.interactor.filmography.FetchFilmographyInteractor;
import com.motondon.moviesearchdemoapp.businesslogic.interactor.filmography.FetchFilmographyInteractorImpl;
import com.motondon.moviesearchdemoapp.model.data.filmography.Filmography;
import com.motondon.moviesearchdemoapp.model.data.general.SearchType;

public class FilmographyListPresenterImpl implements FilmographyListPresenter {

    public interface FilmographyListView {

        void displayMovies(Filmography cast);
        void showProgressDialog();
        void hideProgressDialog();
        void requestFailure(String errorMessage);
        void loadImage(Context context, String coverUrl, String imageId, ImageView ivCover);
    }

    private static final String TAG = FilmographyListPresenterImpl.class.getSimpleName();

    private FilmographyListView mView;
    private FetchFilmographyInteractor fetchFilmographyInteractor;

    public FilmographyListPresenterImpl(FilmographyListView view) {
        this.mView = view;

        // Create the business logic object to interact with
        this.fetchFilmographyInteractor = new FetchFilmographyInteractorImpl();
    }

    /**
     * Called by the view layer. It will request Movies data to the server. When data is received (asynchronously)
     * it will call a view method which will be responsible to manipulate it.
     * @param type
     * @param actorId
     */
    public void fetchFilmography(SearchType type, String actorId) {
        Log.d(TAG, "fetchFilmography() - Request to the server a list of movies for actor (id) " + actorId);

        mView.showProgressDialog();

        fetchFilmographyInteractor.execute(type, actorId, new FetchFilmographyInteractor.Callback() {
            @Override
            public void onFetchFilmographySuccessful(Filmography filmography) {
                mView.hideProgressDialog();
                mView.displayMovies(filmography);
            }

            @Override
            public void onFetchFilmographyFailure(String errorMessage) {
                mView.hideProgressDialog();
                mView.requestFailure(errorMessage);
            }
        });
    }
}
