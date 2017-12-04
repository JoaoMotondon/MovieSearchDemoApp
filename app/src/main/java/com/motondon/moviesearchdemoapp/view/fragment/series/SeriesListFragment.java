package com.motondon.moviesearchdemoapp.view.fragment.series;

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
import com.motondon.moviesearchdemoapp.model.data.series.SeriesList;
import com.motondon.moviesearchdemoapp.presenter.series.SeriesListPresenter;
import com.motondon.moviesearchdemoapp.presenter.series.SeriesListPresenterImpl;
import com.motondon.moviesearchdemoapp.view.adapters.series.SeriesAdapter;
import com.squareup.picasso.Picasso;

public class SeriesListFragment extends Fragment implements SeriesListPresenterImpl.SeriesListView {

    public static final String TAG = SeriesListFragment.class.getSimpleName();

    // These TAGS are used for recycler stuff by the MainActivity.
    public static final String TAG_COMEDY = "TAG_COMEDY";
    public static final String TAG_ADVENTURE = "TAG_ADVENTURE";
    public static final String TAG_ACTION = "TAG_ACTION";
    public static final String TAG_DOCUMENTARY = "TAG_DOCUMENTARY";

    private RecyclerView recyclerViewMovies;
    private SeriesAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private MovieAndSeriesType seriesType;

    // POJO returned by the server containing data to be shown by the adapter.
    private SeriesList seriesList;

    // Presenter object to be used when requesting any data from the model (i.e.: server data manipulation)
    private SeriesListPresenter seriesListPresenterImpl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        seriesType = (MovieAndSeriesType) getArguments().getSerializable("TYPE");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_series_list, container, false);

        recyclerViewMovies = (RecyclerView) root.findViewById(R.id.lvMovie);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.contentView);

        // initialize the adapter
        adapter = new SeriesAdapter(this, getContext(), seriesType);
        recyclerViewMovies.setHasFixedSize(true);
        recyclerViewMovies.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerViewMovies.setAdapter(adapter);

        // This listener will request newer data from the server (if available) when the user swipe down the list view.
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (seriesList == null) {
                    swipeRefreshLayout.setRefreshing(false);
                    return;
                }

                if (seriesList.getPage() >= seriesList.getTotalPages()) {
                    swipeRefreshLayout.setRefreshing(false);
                    return;
                }

                // POJO returned by the server contains the number of the actual page, so we just get it and increment by one
                int nextPage = seriesList.getPage() + 1;

                fetchSeries(nextPage);
            }
        });

        // Init Movie List Presenter
        seriesListPresenterImpl = new SeriesListPresenterImpl(this);

        fetchSeries(1);

        return root;
    }

    /**
     * This is the method which will request server for series.
     *
     * @param page
     */
    private void fetchSeries(int page) {
        Log.d(TAG, "fetchSeries() - Requesting series data to the server...");

        seriesListPresenterImpl.fetchSeries(seriesType, page);
    }

    /**
     * When server returns some data, this method will be called asynchronously. Then we set this data to the adapter in order
     * for the data to be visible.
     *
     * @param seriesList
     */
    @Override
    public void displaySeriesList(SeriesList seriesList) {
        Log.d(TAG, "displaySeriesList() - Received series data from the server. Number of received items: " + seriesList.getSeries().size() + ".");

        this.seriesList = seriesList;
        adapter.setSeriesList(seriesList.getSeries());
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
