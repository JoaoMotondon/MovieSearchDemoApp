package com.motondon.moviesearchdemoapp.view.fragment.filmography;

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
import com.motondon.moviesearchdemoapp.model.data.filmography.Filmography;
import com.motondon.moviesearchdemoapp.model.data.general.SearchType;
import com.motondon.moviesearchdemoapp.presenter.filmography.FilmographyListPresenter;
import com.motondon.moviesearchdemoapp.presenter.filmography.FilmographyListPresenterImpl;
import com.motondon.moviesearchdemoapp.view.adapters.filmography.FilmographyAdapter;
import com.squareup.picasso.Picasso;

public class FilmographyListFragment extends Fragment implements FilmographyListPresenterImpl.FilmographyListView {

    public static final String TAG = FilmographyListFragment.class.getSimpleName();

    private RecyclerView recyclerViewMovies;
    private FilmographyAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private SearchType searchType;
    private String actorId;

    private Filmography filmography;

    // Presenter object to be used when requesting any data from the model (i.e.: server data manipulation)
    private FilmographyListPresenter filmographyListPresenterImpl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchType = (SearchType) getArguments().getSerializable("TYPE");
        actorId = getArguments().getString("ACTOR_ID");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movies_list, container, false);

        recyclerViewMovies = (RecyclerView) root.findViewById(R.id.lvMovie);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.contentView);

        // initialize the adapter
        adapter = new FilmographyAdapter(this, getContext(), searchType);
        recyclerViewMovies.setHasFixedSize(true);
        recyclerViewMovies.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerViewMovies.setAdapter(adapter);

        // Init Movie List Presenter
        filmographyListPresenterImpl = new FilmographyListPresenterImpl(this);

        fetchFilmography();

        return root;
    }

    /**
     * This is the method which will request server for filmography from an specific actor (based on its id).
     *
     */
    private void fetchFilmography() {
        Log.d(TAG, "fetchFilmography() - Requesting movies data to the server...");

        filmographyListPresenterImpl.fetchFilmography(searchType, actorId);
    }


    @Override
    public  void showProgressDialog() {
        swipeRefreshLayout.setRefreshing(true);
    }

    /**
     * When server returns some data, this method will be called asynchronously. Then we set this data to the adapter in order
     * for the data to be visible.
     *
     * @param filmography
     */
    @Override
    public void displayMovies(Filmography filmography) {
        Log.d(TAG, "displayMovies() - Received movies data from the server. Number of received items: " + filmography.getCast().size() + ".");

        this.filmography = filmography;
        adapter.setFilmography(filmography.getCast());
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
