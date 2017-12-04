package com.motondon.moviesearchdemoapp.presenter.search;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.motondon.moviesearchdemoapp.businesslogic.interactor.search.SearchMovieInteractor;
import com.motondon.moviesearchdemoapp.businesslogic.interactor.search.SearchMovieInteractorImpl;
import com.motondon.moviesearchdemoapp.businesslogic.interactor.search.SearchPersonInteractor;
import com.motondon.moviesearchdemoapp.businesslogic.interactor.search.SearchPersonInteractorImpl;
import com.motondon.moviesearchdemoapp.businesslogic.interactor.search.SearchSeriesInteractor;
import com.motondon.moviesearchdemoapp.businesslogic.interactor.search.SearchSeriesInteractorImpl;
import com.motondon.moviesearchdemoapp.model.data.general.SearchType;
import com.motondon.moviesearchdemoapp.model.data.movie.Movies;
import com.motondon.moviesearchdemoapp.model.data.people.PersonList;
import com.motondon.moviesearchdemoapp.model.data.series.SeriesList;

public class SearchPresenterImpl implements SearchPresenter {

    public interface SearchView {

        void displayMovies(Movies movies);
        void displaySeriesList(SeriesList seriesList);
        void displayPeople(PersonList personList);
        void showProgressDialog();
        void hideProgressDialog();
        void requestFailure(String errorMessage);
        void loadImage(Context context, String coverUrl, String imageId, ImageView ivCover);
    }

    private static final String TAG = SearchPresenterImpl.class.getSimpleName();

    private SearchView mView;
    private SearchType searchType;

    private SearchMovieInteractor searchMovieInteractor;
    private SearchSeriesInteractor searchSeriesInteractor;
    private SearchPersonInteractor searchPersonInteractor;

    public SearchPresenterImpl(SearchView view) {
        this.mView = view;

        // Create the business layer objects.
        searchMovieInteractor = new SearchMovieInteractorImpl();
        searchSeriesInteractor = new SearchSeriesInteractorImpl();
        searchPersonInteractor = new SearchPersonInteractorImpl();
    }

    /**
     * Called by the view layer. It will request Movies, Series or Person data to the server. When data is received (asynchronously)
     * it will call a view method which will be responsible to manipulate it.
     *
     * @param query
     * @param searchType
     * @param page
     */
    @Override
    public void fetchData(String query, SearchType searchType, int page) {
        this.searchType = searchType;

        mView.showProgressDialog();

        switch (searchType) {
            case MOVIE:
                Log.d(TAG, "fetchData() - Request to the server a list of movies. Query: " + query + ". Page " + page);
                searchMovies(query, page);
                break;

            case SERIES:
                Log.d(TAG, "fetchData() - Request to the server a list of series. Query: " + query + ". Page " + page);
                searchSeries(query, page);
                break;

            case PEOPLE:
                Log.d(TAG, "fetchData() - Request to the server a list of person. Query: " + query + ". Page " + page);
                searchPerson(query, page);
                break;
        }
    }

    private void searchMovies(String query, int page) {
        Log.d(TAG, "searchMovies() - query: " + query + ". Page " + page);

        searchMovieInteractor.execute(query, page, new SearchMovieInteractor.Callback() {
            @Override
            public void onSearchMovieSuccessful(Movies movieList) {
                mView.hideProgressDialog();
                mView.displayMovies(movieList);
            }

            @Override
            public void onSearchMovieFailure(String errorMessage) {
                mView.hideProgressDialog();
                mView.requestFailure(errorMessage);
            }
        });
    }

    private void searchSeries(String query, int page) {
        Log.d(TAG, "searchSeries() - query: " + query + ". Page " + page);

        searchSeriesInteractor.execute(query, page, new SearchSeriesInteractor.Callback() {
            @Override
            public void onSearchSeriesSuccessful(SeriesList seriesList) {
                mView.hideProgressDialog();
                mView.displaySeriesList(seriesList);
            }

            @Override
            public void onSearchSeriesFailure(String errorMessage) {
                mView.hideProgressDialog();
                mView.requestFailure(errorMessage);
            }
        });
    }

    private void searchPerson(String query, int page) {
        Log.d(TAG, "searchPerson() - query: " + query + ". Page " + page);

        searchPersonInteractor.execute(query, page, new SearchPersonInteractor.Callback() {
            @Override
            public void onSearchPersonSuccessful(PersonList personList) {
                mView.hideProgressDialog();
                mView.displayPeople(personList);
            }

            @Override
            public void onSearchPersonFailure(String errorMessage) {
                mView.hideProgressDialog();
                mView.requestFailure(errorMessage);
            }
        });
    }
}
