package com.motondon.moviesearchdemoapp.view.fragment.movies;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.motondon.moviesearchdemoapp.model.data.general.MovieAndSeriesType;
import com.motondon.moviesearchdemoapp.model.data.movie.Movies;
import com.motondon.moviesearchdemoapp.presenter.movies.MoviesListPresenter;
import com.motondon.moviesearchdemoapp.presenter.movies.MoviesListPresenterImpl;
import com.motondon.moviesearchdemoapp.view.adapters.movies.MoviesAdapter;
import com.squareup.picasso.Picasso;

public class MoviesListFragment extends Fragment implements MoviesListPresenterImpl.MoviesListView {

    public static final String TAG = MoviesListFragment.class.getSimpleName();

    // These TAGS are used for recycler stuff by the MainActivity.
    public static final String TAG_COMEDY = "TAG_COMEDY";
    public static final String TAG_ADVENTURE = "TAG_ADVENTURE";
    public static final String TAG_ACTION = "TAG_ACTION";
    public static final String TAG_DOCUMENTARY = "TAG_DOCUMENTARY";

    private RecyclerView recyclerViewMovies;
    private MoviesAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private MovieAndSeriesType movieType;

    private Movies movies;

    // Presenter object to be used when requesting any data from the model (i.e.: server data manipulation)
    private MoviesListPresenter moviesListPresenterImpl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        movieType = (MovieAndSeriesType) getArguments().getSerializable("TYPE");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movies_list, container, false);

        recyclerViewMovies = (RecyclerView) root.findViewById(R.id.lvMovie);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.contentView);

        // initialize the adapter
        adapter = new MoviesAdapter(this, getContext(), movieType);
        recyclerViewMovies.setHasFixedSize(true);
        recyclerViewMovies.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerViewMovies.setAdapter(adapter);

        // This listener will request newer data from the server (if available) when the user swipe down the list view.
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (movies == null) {
                    swipeRefreshLayout.setRefreshing(false);
                    return;
                }

                if (movies.getPage() >= movies.getTotalPages()) {
                    swipeRefreshLayout.setRefreshing(false);
                    return;
                }

                // POJO returned by the server contains the number of the actual page, so we just get it and increment by one
                int nextPage = movies.getPage() + 1;

                fetchMovies(nextPage);
            }
        });

        // Init Movie List Presenter
        moviesListPresenterImpl = new MoviesListPresenterImpl(this);

        fetchMovies(1);

        return root;
    }

    /**
     * This is the method which will request server for movies.
     *
     * @param page
     */
    private void fetchMovies(int page) {
        Log.d(TAG, "fetchFilmography() - Requesting movies data to the server...");

        moviesListPresenterImpl.fetchMovies(movieType, page);
    }

    /**
     * When server returns some data, this method will be called asynchronously. Then we set this data to the adapter in order
     * for the data to be visible.
     *
     * @param movies
     */
    @Override
    public void displayMovies(Movies movies) {
        Log.d(TAG, "displayMovies() - Received movies data from the server. Number of received items: " + movies.getMovies().size() + ".");

        this.movies = movies;
        adapter.setMovies(movies.getMovies());
    }

    @Override
    public  void showProgressDialog() {
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
