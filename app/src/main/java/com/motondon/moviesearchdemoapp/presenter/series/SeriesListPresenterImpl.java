package com.motondon.moviesearchdemoapp.presenter.series;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.motondon.moviesearchdemoapp.businesslogic.interactor.series.FetchSeriesInteractor;
import com.motondon.moviesearchdemoapp.businesslogic.interactor.series.FetchSeriesInteractorImpl;
import com.motondon.moviesearchdemoapp.model.data.general.MovieAndSeriesType;
import com.motondon.moviesearchdemoapp.model.data.series.SeriesList;

public class SeriesListPresenterImpl implements SeriesListPresenter {

    public interface SeriesListView {

        void displaySeriesList(SeriesList seriesList);
        void showProgressDialog();
        void hideProgressDialog();
        void requestFailure(String errorMessage);
        void loadImage(Context context, String coverUrl, String imageId, ImageView ivCover);
    }

    private static final String TAG = SeriesListPresenterImpl.class.getSimpleName();

    private SeriesListView mView;

    // Business Logic Layer
    private FetchSeriesInteractor fetchSeriesInteractor;

    public SeriesListPresenterImpl(SeriesListView view) {
        this.mView = view;

        // Create an instance of a business layer object.
        this.fetchSeriesInteractor = new FetchSeriesInteractorImpl();
    }

    /**
     * Called by the view layer. It will request Series data to the server. When data is received (asynchronously)
     * it will call a view method which will be responsible to manipulate it.
     *
     * @param type
     * @param page
     */
    @Override
    public void fetchSeries(MovieAndSeriesType type, int page) {
        Log.d(TAG, "fetchSeries() - Request to the server a series list of type " + type + ". Page " + page);

        mView.showProgressDialog();

        // Delegate this request to the business layer
        fetchSeriesInteractor.execute(type, page, new FetchSeriesInteractor.Callback() {
            @Override
            public void onFetchSeriesSuccessful(SeriesList seriesList) {
                mView.hideProgressDialog();
                mView.displaySeriesList(seriesList);
            }

            @Override
            public void onFetchSeriesFailure(String errorMessage) {
                mView.hideProgressDialog();
                mView.requestFailure(errorMessage);
            }
        });
    }
}
