package com.motondon.moviesearchdemoapp.businesslogic.http.api.filmography;

import android.util.Log;

import com.motondon.moviesearchdemoapp.config.AppConfig;
import com.motondon.moviesearchdemoapp.model.data.filmography.Filmography;
import com.motondon.moviesearchdemoapp.businesslogic.http.manager.RetrofitApi;
import com.motondon.moviesearchdemoapp.businesslogic.http.manager.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * This class is intended to hide Retrofit specific implementation and exposes some methods to the presenter layer.
 *
 * It also defines a callback interface (FetchFilmographyCallback interface) which must be implemented by the callers (i.e.: presenter
 * layer). Then, when a Retrofit callback is received, it will be converted to a FetchFilmographyCallback respective method and passed back to
 * the presenter layer, hiding Retrofit implementation.
 *
 * It uses FilmographyService interface which defines the TMDB REST API methods.
 *
 */
public class FilmographyApi extends RetrofitApi {

    private static final String TAG = FilmographyApi.class.getSimpleName();

    public interface FetchFilmographyCallback {
        void onFetchFilmographySuccessful(Filmography filmography);
        void onFetchFilmographyFailure(String errorMessage);
    }

    private FilmographyService filmographyService;

    public FilmographyApi() {
        filmographyService = ServiceGenerator.getInstance().createService(FilmographyService.class);
    }

    /**
     * Fetch all films for a given actor (id). A response (either a successful or an error) will be
     * received asynchronously.
     *
     * @param actorId
     * @param callback
     */
    public void fetchFilmsByActor(final String actorId, final FetchFilmographyCallback callback) {
        try {
            Log.d(TAG, "fetchFilmsByActor() - Request to the server a movie list for actor (id) " + actorId);

            filmographyService.fetchFilmsByActor(actorId, AppConfig.TMDB_API_KEY).enqueue(new Callback<Filmography>() {
                @Override
                public void onResponse(Call<Filmography> call, Response<Filmography> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "fetchFilmsByActor::onResponse() - Successful response for a movie list for actor (id) " + actorId);
                        callback.onFetchFilmographySuccessful(response.body());

                    } else {
                        Log.d(TAG, "fetchFilmsByActor::onResponse() - Response not successful for a movie list for actor (id) " + actorId + ". Response code: " + response.code());
                        callback.onFetchFilmographyFailure("Response not successful: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Filmography> call, Throwable t) {
                    Log.d(TAG, "fetchFilmsByActor::onFailure() - Fail when requesting a movie list for actor (id) " + actorId + ". Message: " + t.getMessage());
                    callback.onFetchFilmographyFailure("Fail when requesting a movie list for actor (id) " + actorId + ". Message: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            Log.d(TAG, "fetchFilmsByActor() - Exception when requesting a movie list for actor (id) " + actorId + ". Message: " + e.getMessage());
            callback.onFetchFilmographyFailure("Exception when requesting a movie list for actor (id) " + actorId + ". Message: " + e.getMessage());
        }
    }

    /**
     * Fetch all series for a given actor (id). A response (either a successful or an error) will be
     * received asynchronously.
     *
     * @param actorId
     * @param callback
     */
    public void fetchSeriesByActor(final String actorId, final FetchFilmographyCallback callback) {
        try {
            Log.d(TAG, "fetchSeriesByActor() - Request to the server a series list for actor (id) " + actorId);

            filmographyService.fetchSeriesByActor(actorId, AppConfig.TMDB_API_KEY).enqueue(new Callback<Filmography>() {
                @Override
                public void onResponse(Call<Filmography> call, Response<Filmography> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "fetchSeriesByActor::onResponse() - Successful response for a series list for actor (id) " + actorId);
                        callback.onFetchFilmographySuccessful(response.body());

                    } else {
                        Log.d(TAG, "fetchSeriesByActor::onResponse() - Response not successful for a series list for actor (id) " + actorId + ". Response code: " + response.code());
                        callback.onFetchFilmographyFailure("Response not successful: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Filmography> call, Throwable t) {
                    Log.d(TAG, "fetchSeriesByActor::onFailure() - Fail when requesting a series list for actor (id) " + actorId + ". Message: " + t.getMessage());
                    callback.onFetchFilmographyFailure("Fail when requesting a series list for actor (id) " + actorId + ". Message: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            Log.d(TAG, "fetchSeriesByActor() - Exception when requesting a series list for actor (id) " + actorId + ". Message: " + e.getMessage());
            callback.onFetchFilmographyFailure("Exception when requesting a series list for actor (id) " + actorId + ". Message: " + e.getMessage());
        }
    }
}
