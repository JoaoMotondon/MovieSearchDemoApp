package com.motondon.moviesearchdemoapp.presenter.series;

import android.content.Context;
import android.util.Log;

import com.motondon.moviesearchdemoapp.businesslogic.interactor.series.FetchSeriesCastInteractor;
import com.motondon.moviesearchdemoapp.businesslogic.interactor.series.FetchSeriesCastInteractorImpl;
import com.motondon.moviesearchdemoapp.businesslogic.interactor.series.FetchSeriesDetailsInteractor;
import com.motondon.moviesearchdemoapp.businesslogic.interactor.series.FetchSeriesDetailsInteractorImpl;
import com.motondon.moviesearchdemoapp.model.data.movie.moviedetails.MovieCast;
import com.motondon.moviesearchdemoapp.model.data.series.seriesdetails.SeriesDetails;

public class SeriesDetailsPresenterImpl implements SeriesDetailsPresenter {

    private static final String TAG = SeriesDetailsPresenterImpl.class.getSimpleName();

    public interface SeriesDetailsView {
        void displaySeriesDetails(SeriesDetails seriesDetails);
        void displaySeriesCast(MovieCast movieCast);
        void requestFailure(String errorMessage);
    }

    private SeriesDetailsView mView;
    private Context mContext;

    private FetchSeriesDetailsInteractor fetchSeriesDetailsInteractor;
    private FetchSeriesCastInteractor fetchSeriesCastInteractor;

    public SeriesDetailsPresenterImpl(SeriesDetailsView view, Context context) {
        this.mView = view;
        this.mContext = context;

        // Create business logic objects to interact with
        fetchSeriesDetailsInteractor = new FetchSeriesDetailsInteractorImpl();
        fetchSeriesCastInteractor = new FetchSeriesCastInteractorImpl();
    }

    /**
     * Called by the view layer. It will request Series details data to the server. When data is received (asynchronously)
     * it will call a view method which will be responsible to manipulate it.
     *
     * @param seriesId
     */
    @Override
    public void loadSeriesDetails(final String seriesId) {
        Log.d(TAG, "loadSeriesDetails() - Request to the server details of a series with id " + seriesId);

        fetchSeriesDetailsInteractor.execute(seriesId, new FetchSeriesDetailsInteractor.Callback() {
            @Override
            public void onFetchSeriesDetailsSuccessful(SeriesDetails seriesDetails) {
                mView.displaySeriesDetails(seriesDetails);
            }

            @Override
            public void onFetchSeriesDetailsFailure(String errorMessage) {
                mView.requestFailure(errorMessage);
            }
        });
    }

    /**
     * Called by the view layer. It will request Series cast data to the server. When data is received (asynchronously)
     * it will call a view method which will be responsible to manipulate it.
     *
     * @param seriesId
     */
    @Override
    public void loadSeriesCast(final String seriesId) {
        Log.d(TAG, "loadSeriesCast() - Request to the server a cast of a series with id " + seriesId);

        fetchSeriesCastInteractor.execute(seriesId, new FetchSeriesCastInteractor.Callback() {
            @Override
            public void onFetchSeriesCastSuccessful(MovieCast movieCast) {
                mView.displaySeriesCast(movieCast);
            }

            @Override
            public void onFetchSeriesCastFailure(String errorMessage) {
                mView.requestFailure(errorMessage);

            }
        });
    }
}
