package com.motondon.moviesearchdemoapp.view.fragment.search;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.motondon.moviesearchdemoapp.R;
import com.motondon.moviesearchdemoapp.model.data.general.SearchType;
import com.motondon.moviesearchdemoapp.model.data.movie.Movies;
import com.motondon.moviesearchdemoapp.model.data.people.PersonList;
import com.motondon.moviesearchdemoapp.model.data.series.SeriesList;
import com.motondon.moviesearchdemoapp.presenter.search.SearchPresenter;
import com.motondon.moviesearchdemoapp.presenter.search.SearchPresenterImpl;
import com.motondon.moviesearchdemoapp.view.adapters.search.SearchAdapter;
import com.squareup.picasso.Picasso;

public class SearchFragment extends Fragment implements SearchPresenterImpl.SearchView {

    public static final String TAG = SearchFragment.class.getSimpleName();

    // These TAGS are used for recycler stuff by the MainActivity.
    public static final String TAG_SEARCH_BY_MOVIE = "TAG_SEARCH_BY_MOVIE";
    public static final String TAG_SEARCH_BY_SERIES = "TAG_SEARCH_BY_SERIES";
    public static final String TAG_SEARCH_BY_PEOPLE = "TAG_SEARCH_BY_PEOPLE";

    private RecyclerView recyclerView;
    private SearchAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Movies movies;
    private SeriesList seriesList;
    private PersonList personList;

    private String currentQuery;
    private SearchType searchType = SearchType.MOVIE;

    // Presenter object to be used when requesting any data from the model (i.e.: server data manipulation)
    private SearchPresenter mSearchPresenterImpl;

    private Boolean cleanBeforeFillAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchType = (SearchType) getArguments().getSerializable("SEARCH_TYPE");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_list, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.lvSeries);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.contentView);

        // initialize the adapter
        adapter = new SearchAdapter(this, getContext(), searchType, null);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(adapter);

        // This listener will request newer data from the server (if available) when the user swipe down the list view.
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                int nextPage = 0;

                switch (searchType) {
                    case MOVIE:

                        if (movies == null) {
                            swipeRefreshLayout.setRefreshing(false);
                            return;
                        }

                        if (movies.getPage() >= movies.getTotalPages()) {
                            swipeRefreshLayout.setRefreshing(false);
                            return;
                        }

                        // POJO returned by the server contains the number of the actual page, so we just get it and increment by one
                        nextPage = movies.getPage() + 1;

                        break;

                    case SERIES:

                        if (seriesList == null) {
                            swipeRefreshLayout.setRefreshing(false);
                            return;
                        }

                        if (seriesList.getPage() >= seriesList.getTotalPages()) {
                            swipeRefreshLayout.setRefreshing(false);
                            return;
                        }

                        // POJO returned by the server contains the number of the actual page, so we just get it and increment by one
                        nextPage = seriesList.getPage() + 1;
                        break;

                    case PEOPLE:

                        if (personList == null) {
                            swipeRefreshLayout.setRefreshing(false);
                            return;
                        }

                        if (personList.getPage() >= personList.getTotalPages()) {
                            swipeRefreshLayout.setRefreshing(false);
                            return;
                        }

                        // POJO returned by the server contains the number of the actual page, so we just get it and increment by one
                        nextPage = personList.getPage() + 1;
                        break;
                }

                fetchData(currentQuery, searchType, nextPage);
            }
        });

        // Init Presenter Layer
        mSearchPresenterImpl = new SearchPresenterImpl(this);

        return root;
    }

    /**
     * This is the method which will request to the server to fetch movies, series or person, depending on the searchType.
     *
     * @param query
     * @param searchType
     * @param page
     */
    public void fetchData(String query, SearchType searchType, int page) {

        // Just for log purpose
        switch (searchType) {
            case MOVIE:
                Log.d(TAG, "fetchData() - Requesting movies to the server. Query: " + query + "...");
                break;

            case SERIES:
                Log.d(TAG, "fetchData() - Requesting series to the server. Query: " + query + "...");
                break;

            case PEOPLE:
                Log.d(TAG, "fetchData() - Requesting person to the server. Query: " + query + "...");
                break;
        }

        // When detecting a new search (i.e.: page equals one), tells the adapter to first clean its data up before fill it up with new data.
        if (page == 1) {
            cleanBeforeFillAdapter = true;
        } else {
            cleanBeforeFillAdapter = false;
        }

        // Save it to the class scope so we can use it when swipe to refresh recycleview.
        this.currentQuery = query;

        // Delegates movies request task to the MoviesListPresenterImpl
        mSearchPresenterImpl.fetchData(query, searchType, page);
    }

    /**
     * When server returns a movie list, this method will be called asynchronously. Then we set this data to the adapter in order
     * for the data to be visible.
     *
     * @param movies
     */
    @Override
    public void displayMovies(Movies movies) {
        Log.d(TAG, "displayMovies() - Received movies data from the server. Number of received items: " + movies.getMovies().size() + ".");

        this.movies = movies;
        adapter.setMovies(movies.getMovies(), cleanBeforeFillAdapter);
    }

    /**
     * When server returns a series list, this method will be called asynchronously. Then we set this data to the adapter in order
     * for the data to be visible.
     *
     * @param seriesList
     */
    @Override
    public void displaySeriesList(SeriesList seriesList) {
        Log.d(TAG, "displaySeriesList() - Received series data from the server. Number of received items: " + seriesList.getSeries().size() + ".");

        this.seriesList = seriesList;
        adapter.setSeriesList(seriesList.getSeries(), cleanBeforeFillAdapter);
    }

    /**
     * When server returns a people list, this method will be called asynchronously. Then we set this data to the adapter in order
     * for the data to be visible.
     *
     * @param personList
     */
    @Override
    public void displayPeople(PersonList personList) {
        Log.d(TAG, "displayPeople() - Received person data from the server. Number of received items: " + personList.getPersonList().size() + ".");

        this.personList = personList;
        adapter.setPersonList(personList.getPersonList(), cleanBeforeFillAdapter);
    }

    @Override
    public void showProgressDialog() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgressDialog() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void requestFailure(String errorMessage) {
        Log.e(TAG, "requestFailure() - Fail when trying to fetch some server data: " + errorMessage);
    }

    @Override
    public void loadImage(Context context, String coverUrl, String imageId, final ImageView ivCover) {
        Log.d(TAG, "loadImage() - coverUrl: " + coverUrl + " - imageId: " + imageId);

        Picasso.with(context).load(Uri.parse(coverUrl)).placeholder(R.drawable.ic_movie_placeholder).error(R.drawable.ic_no_cover).into(ivCover);
    }
}