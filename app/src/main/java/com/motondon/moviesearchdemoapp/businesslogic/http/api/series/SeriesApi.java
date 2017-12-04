package com.motondon.moviesearchdemoapp.businesslogic.http.api.series;

import android.util.Log;

import com.motondon.moviesearchdemoapp.config.AppConfig;
import com.motondon.moviesearchdemoapp.model.data.general.MovieAndSeriesType;
import com.motondon.moviesearchdemoapp.model.data.movie.moviedetails.MovieCast;
import com.motondon.moviesearchdemoapp.model.data.series.SeriesList;
import com.motondon.moviesearchdemoapp.model.data.series.seriesdetails.SeriesDetails;
import com.motondon.moviesearchdemoapp.businesslogic.http.api.helper.ApiResolver;
import com.motondon.moviesearchdemoapp.businesslogic.http.manager.RetrofitApi;
import com.motondon.moviesearchdemoapp.businesslogic.http.manager.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * This class is intended to hide Retrofit specific implementation and exposes some methods to the presenter layer.
 *
 * It also defines a callback interface (ISeriesApiCallback interface) which must be implemented by the callers (i.e.: presenter
 * layer). Then, when a Retrofit callback is received, it will be converted to a ISeriesApiCallback respective method and passed back to
 * the presenter layer, hiding Retrofit implementation.
 *
 * It uses SeriesService interface which defines the TMDB REST API methods.
 *
 */
public class SeriesApi extends RetrofitApi {

    private static final String TAG = SeriesApi.class.getCanonicalName();

    /**
     * Callback used when fetching a series (by fetchSeries() method)
     */
    public interface FetchSeriesCallback {
        void onFetchSeriesSuccessful(SeriesList seriesList);
        void onFetchSeriesFailure(String errorMessage);
    }

    /**
     * Callback used when searching a series (by searchSeries() method)
     */
    public interface SearchSeriesCallback {
        void onSearchSeriesSuccessful(SeriesList seriesList);
        void onSearchSeriesFailure(String errorMessage);
    }

    /**
     * Callback used when fetching a series details (by getExtraSeriesDetails() method)
     */
    public interface SeriesDetailsRequestCallback {
        void onSeriesDetailsRequestSuccessful(SeriesDetails seriesDetails);
        void onSeriesDetailsRequestFailure(String errorMessage);
    }

    /**
     * Callback used when requesting a series cast (by getSeriesCast() method)
     */
    public interface SeriesCastRequestCallback {
        void onSeriesCastRequestSuccessful(MovieCast movieCast);
        void onSeriesCastRequestFailure(String errorMessage);
    }

    private SeriesService seriesService;

    public SeriesApi() {
        seriesService = ServiceGenerator.getInstance().createService(SeriesService.class);
    }

    /**
     * Request to the server all series which name matches with the name typed by the user. A response (either a successful or
     * an error) will be received asynchronously
     *
     * @param query
     * @param page
     * @param callback
     */
    public void searchSeries(final String query, final int page, final SearchSeriesCallback callback)  {
        Log.d(TAG, "searchSeries() - Request to the server a series list for query: " + query + ". Page " + page);

        try {

            seriesService.searchSeries(AppConfig.TMDB_API_KEY, query, page).enqueue(new Callback<SeriesList>() {
                @Override
                public void onResponse(Call<SeriesList> call, Response<SeriesList> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "searchSeries::onResponse() - Successful response for a series list query for query: " + query + ". Page " + page);
                        callback.onSearchSeriesSuccessful(response.body());

                    } else {
                        Log.d(TAG, "searchSeries::onResponse() - Response not successful for a series list query for query: " + query + ". Page " + page + ". Response code: " + response.code());
                        callback.onSearchSeriesFailure("Response not successful: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<SeriesList> call, Throwable t) {
                    Log.d(TAG, "searchSeries::onFailure() - Fail when requesting a series list for query: " + query + ". Page " + page + ". Message: " + t.getMessage());
                    callback.onSearchSeriesFailure("Fail when requesting a series list for query: " + query + ". Page " + page + ". Message: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            Log.d(TAG, "searchSeries() - Exception when requesting a series list for query: " + query + ". Page " + page + ". Message: " + e.getMessage());
            callback.onSearchSeriesFailure("Exception when requesting a series list for query: " + query + ". Page " + page + ". Message: " + e.getMessage());
        }
    }

    /**
     * Request to the server a series details given a series id. A response (either a successful or an error) will be
     * received asynchronously
     *
     * @param id
     * @param callback
     */
    public void getExtraSeriesDetails(final String id, final SeriesDetailsRequestCallback callback) {
        Log.d(TAG, "getExtraSeriesDetails() - Request to the server a series details for series (id): " + id);

        try {

            seriesService.getExtraSeriesDetails(id, AppConfig.TMDB_API_KEY).enqueue(new Callback<SeriesDetails>() {
                @Override
                public void onResponse(Call<SeriesDetails> call, Response<SeriesDetails> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "getExtraSeriesDetails::onResponse() - Successful response for a series details for series (id): " + id);
                        callback.onSeriesDetailsRequestSuccessful(response.body());
                    } else {
                        Log.d(TAG, "getExtraSeriesDetails::onResponse() - Response not successful for a series details for series (id): " + id + ". Response code: " + response.code());
                        callback.onSeriesDetailsRequestFailure("Response not successful: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<SeriesDetails> call, Throwable t) {
                    Log.d(TAG, "getExtraSeriesDetails::onFailure() - Fail when requesting a series details for series (id): " + id + ". Message: " + t.getMessage());
                    callback.onSeriesDetailsRequestFailure("Fail when requesting a series details for series (id): " + id + ". Message: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            Log.d(TAG, "getExtraSeriesDetails() - Exception when requesting a series details for series (id): " + id + ". Message: " + e.getMessage());
            callback.onSeriesDetailsRequestFailure("Exception when requesting a series details for series (id): " + id + ". Message: " + e.getMessage());
        }
    }

    /**
     * Request to the server a specific series by genre or by type (split into two different methods). A response
     * (either a successful or an error) will be received asynchronously.
     *
     * @param movieAndSeriesType
     * @param page
     * @param callback
     */
    public void fetchSeries(MovieAndSeriesType movieAndSeriesType, int page, final FetchSeriesCallback callback) {
        switch (movieAndSeriesType) {
            case COMEDY:
            case ADVENTURE:
            case ACTION:
            case DOCUMENTARY:
                fetchSeriesByGenre(movieAndSeriesType, page, callback);
                break;

            case ON_THE_AIR:
            case TOP_RATED:
            case POPULAR:
                fetchSeriesByType(movieAndSeriesType, page, callback);
                break;
        }
    }


    private void fetchSeriesByGenre(final MovieAndSeriesType genre, final int page, final FetchSeriesCallback callback) {
        try {
            Log.d(TAG, "fetchSeriesByGenre() - Request to the server a series list of genre " + genre + ". Page " + page);

            int iGenre = ApiResolver.resolveGenre(genre);

            seriesService.fetchSeriesByGenre(AppConfig.TMDB_API_KEY, iGenre, page).enqueue(new Callback<SeriesList>() {
                @Override
                public void onResponse(Call<SeriesList> call, Response<SeriesList> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "fetchSeriesByGenre::onResponse() - Successful response for a series list of genre " + genre + ". Page " + page);
                        callback.onFetchSeriesSuccessful(response.body());

                    } else {
                        Log.d(TAG, "fetchSeriesByGenre::onResponse() - Response not successful for a series list of genre " + genre + ". Page " + page + ". Response code: " + response.code());
                        callback.onFetchSeriesFailure("Response not successful: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<SeriesList> call, Throwable t) {
                    Log.d(TAG, "fetchSeriesByGenre::onFailure() - Fail when requesting a series list of genre " + genre + ". Page " + page + ". Message: " + t.getMessage());
                    callback.onFetchSeriesFailure("Fail when requesting a series list of genre " + genre + ". Page " + page + ". Message: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            Log.d(TAG, "fetchSeriesByGenre() - Exception when requesting a series list of genre " + genre + ". Page " + page + ". Message: " + e.getMessage());
            callback.onFetchSeriesFailure("Exception when requesting a series list of genre " + genre + ". Page " + page + ". Message: " + e.getMessage());
        }
    }


    private void fetchSeriesByType(final MovieAndSeriesType type, final int page, final FetchSeriesCallback callback) {
        try {
            Log.d(TAG, "fetchSeriesByType() - Request to the server a series list of type " + type + ". Page " + page);

            String sType = ApiResolver.resolveType(type);

            seriesService.fetchSeriesByType(sType, AppConfig.TMDB_API_KEY, page).enqueue(new Callback<SeriesList>() {
                @Override
                public void onResponse(Call<SeriesList> call, Response<SeriesList> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "fetchSeriesByType::onResponse() - Successful response for a series list of type " + type + ". Page " + page);
                        callback.onFetchSeriesSuccessful(response.body());

                    } else {
                        Log.d(TAG, "fetchSeriesByType::onResponse() - Response not successful for a series list of type " + type + ". Page " + page + ". Response code: " + response.code());
                        callback.onFetchSeriesFailure("Response not successful: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<SeriesList> call, Throwable t) {
                    Log.d(TAG, "fetchSeriesByType::onFailure() - Fail when requesting a series list of genre " + type + ". type " + page + ". Message: " + t.getMessage());
                    callback.onFetchSeriesFailure("Fail when requesting a series list of genre " + type + ". type " + page + ". Message: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            Log.d(TAG, "fetchSeriesByType() - Exception when requesting a series list of type " + type + ". Page " + page + ". Message: " + e.getMessage());
            callback.onFetchSeriesFailure("Exception when requesting a series list of type " + type + ". Page " + page + ". Message: " + e.getMessage());
        }
    }

    /**
     * Request to the server a series cast given a series id. A response (either a successful or an error) will be
     * received asynchronously
     *
     * @param movieId
     * @param callback
     */
    public void getSeriesCast(final String movieId, final SeriesCastRequestCallback callback) {
        try {

            Log.d(TAG, "getSeriesCast() - Request to the server a series cast for movieId " + movieId);

            seriesService.getSeriesCast(movieId, AppConfig.TMDB_API_KEY).enqueue(new Callback<MovieCast>() {
                @Override
                public void onResponse(Call<MovieCast> call, Response<MovieCast> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "getSeriesCast::onResponse() - Successful response for a series cast for movieId " + movieId);
                        callback.onSeriesCastRequestSuccessful(response.body());
                    } else {
                        Log.d(TAG, "getSeriesCast::onResponse() - Response not successful for a series cast for movieId " + movieId + ". Response code: " + response.code());
                        callback.onSeriesCastRequestFailure("Response not successful: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<MovieCast> call, Throwable t) {
                    Log.d(TAG, "getSeriesCast::onFailure() - Fail when requesting a series cast for movieId " + movieId + ". Message: " + t.getMessage());
                    callback.onSeriesCastRequestFailure("Fail when requesting a series cast for movieId " + movieId + ". Message: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            Log.d(TAG, "getSeriesCast() - Exception when requesting a series cast for movieId " + movieId + ". Message: " + e.getMessage());
            callback.onSeriesCastRequestFailure("Exception when requesting a series cast for movieId " + movieId + ". Message: " + e.getMessage());
        }
    }
}
