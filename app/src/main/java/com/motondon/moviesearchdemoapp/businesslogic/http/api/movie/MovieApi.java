package com.motondon.moviesearchdemoapp.businesslogic.http.api.movie;

import android.util.Log;

import com.motondon.moviesearchdemoapp.businesslogic.http.api.helper.ApiResolver;
import com.motondon.moviesearchdemoapp.businesslogic.http.manager.RetrofitApi;
import com.motondon.moviesearchdemoapp.businesslogic.http.manager.ServiceGenerator;
import com.motondon.moviesearchdemoapp.config.AppConfig;
import com.motondon.moviesearchdemoapp.model.data.general.MovieAndSeriesType;
import com.motondon.moviesearchdemoapp.model.data.movie.Movies;
import com.motondon.moviesearchdemoapp.model.data.movie.moviedetails.MovieCast;
import com.motondon.moviesearchdemoapp.model.data.movie.moviedetails.MovieDetail;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * This class is intended to hide Retrofit specific implementation and exposes some methods to the presenter layer.
 *
 * It also defines a callback interface (IMovieApiCallback interface) which must be implemented by the callers (i.e.: presenter
 * layer). Then, when a Retrofit callback is received, it will be converted to a IMovieApiCallback respective method and passed back to
 * the presenter layer, hiding Retrofit implementation.
 *
 * It uses MoviesService interface which defines the TMDB REST API methods.
 *
 */
public class MovieApi extends RetrofitApi {

    private static final String TAG = MovieApi.class.getSimpleName();

    /**
     * Callback used when fetching a movie (by fetchMovie() method)
     */
    public interface FetchMovieCallback {
        void onFetchMovieSuccessful(Movies movieList);
        void onFetchMovieFailure(String errorMessage);
    }

    /**
     * Callback used when searching a movie (by searchMovie() method)
     */
    public interface SearchMovieCallback {
        void onSearchMovieSuccessful(Movies movieList);
        void onSearchMovieFailure(String errorMessage);
    }

    /**
     * Callback used when fetching a movie details (by getExtraMovieDetails() method)
     */
    public interface MovieDetailsRequestCallback {
        void onMovieDetailsRequestSuccessful(MovieDetail movieDetail);
        void onMovieDetailsRequestFailure(String errorMessage);
    }

    /**
     * Callback used when requesting a series cast (by getMovieCast() method)
     */
    public interface MovieCastRequestCallback {
        void onCastRequestSuccessful(MovieCast movieCast);
        void onCastRequestFailure(String errorMessage);
    }

    private MovieService movieService;

    public MovieApi() {
        movieService = ServiceGenerator.getInstance().createService(MovieService.class);
    }

    /**
     * Request to the server all movies which name matches with the name typed by the user. A response (either a successful or
     * an error) will be received asynchronously
     *
     * @param query
     * @param page
     * @param callback
     */
    public void searchMovies(final String query, final int page, final SearchMovieCallback callback) {
        Log.d(TAG, "searchMovies() - Request to the server a movie list for query: " + query + ". Page " + page);

        try {

            movieService.searchMovies(AppConfig.TMDB_API_KEY, query, page).enqueue(new Callback<Movies>() {
                @Override
                public void onResponse(Call<Movies> call, Response<Movies> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "searchMovies::onResponse() - Successful response for a movie list query for query: " + query + ". Page " + page);
                        callback.onSearchMovieSuccessful(response.body());

                    } else {
                        Log.d(TAG, "searchMovies::onResponse() - Response not successful for a movie list query for query: " + query + ". Page " + page + ". Response code: " + response.code());
                        callback.onSearchMovieFailure("Response not successful: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Movies> call, Throwable t) {
                    Log.d(TAG, "searchMovies::onFailure() - Fail when requesting a movie list for query: " + query + ". Page " + page + ". Message: " + t.getMessage());
                    callback.onSearchMovieFailure("Fail when requesting a movie list for query: " + query + ". Page " + page + ". Message: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            Log.d(TAG, "searchMovies() - Exception when requesting a movie list for query: " + query + ". Page " + page + ". Message: " + e.getMessage());
            callback.onSearchMovieFailure("Exception when requesting a movie list for query: " + query + ". Page " + page + ". Message: " + e.getMessage());
        }
    }

    /**
     * Request to the server a movie details given a movie id. A response (either a successful or an error) will be
     * received asynchronously
     *
     * @param id
     * @param callback
     */
    public void getExtraMovieDetails(final String id, final MovieDetailsRequestCallback callback) {
        Log.d(TAG, "getExtraMovieDetails() - Request to the server a movie details for movie (id): " + id);

        try {

            movieService.getExtraMovieDetails(id, AppConfig.TMDB_API_KEY).enqueue(new Callback<MovieDetail>() {
                @Override
                public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "getExtraMovieDetails::onResponse() - Successful response for a movie details for movie (id): " + id);
                        callback.onMovieDetailsRequestSuccessful(response.body());
                    } else {
                        Log.d(TAG, "getExtraMovieDetails::onResponse() - Response not successful for a movie details for movie (id): " + id + ". Response code: " + response.code());
                        callback.onMovieDetailsRequestFailure("Response not successful: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<MovieDetail> call, Throwable t) {
                    Log.d(TAG, "getExtraMovieDetails::onFailure() - Fail when requesting a movie details for movie (id): " + id + ". Message: " + t.getMessage());
                    callback.onMovieDetailsRequestFailure("Fail when requesting a movie details for movie (id): " + id + ". Message: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            Log.d(TAG, "getExtraMovieDetails() - Exception when requesting a movie details for movie (id): " + id + ". Message: " + e.getMessage());
            callback.onMovieDetailsRequestFailure("Exception when requesting a movie details for movie (id): " + id + ". Message: " + e.getMessage());
        }
    }

    /**
     * Request to the server a specific movie by genre or by type (split into two different methods). A response
     * (either a successful or an error) will be received asynchronously.
     *
     * @param movieGenre
     * @param page
     * @param callback
     */
    public void fetchMovies(final MovieAndSeriesType movieGenre, final int page, final FetchMovieCallback callback) {

        switch (movieGenre) {
            case COMEDY:
            case ADVENTURE:
            case ACTION:
            case DOCUMENTARY:
                fetchMoviesByGenre(movieGenre, page, callback);
                break;

            case NOW_PLAYING:
            case TOP_RATED:
            case UPCOMING:
            case POPULAR:
                fetchMoviesByType(movieGenre, page, callback);
                break;
        }
    }

    private void fetchMoviesByGenre(final MovieAndSeriesType genre, final int page, final FetchMovieCallback callback) {
        try {
            Log.d(TAG, "fetchMoviesByGenre() - Request to the server a movie list of genre " + genre + ". Page " + page);

            final int iGenre = ApiResolver.resolveGenre(genre);

            movieService.fetchMoviesByGenre(AppConfig.TMDB_API_KEY, iGenre, page).enqueue(new Callback<Movies>() {
                @Override
                public void onResponse(Call<Movies> call, Response<Movies> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "fetchMoviesByGenre::onResponse() - Successful response for a movie list of genre " + genre + ". Page " + page);
                        callback.onFetchMovieSuccessful(response.body());

                    } else {
                        Log.d(TAG, "fetchMoviesByGenre::onResponse() - Response not successful for a movie list of genre " + genre + ". Page " + page + ". Response code: " + response.code());
                        callback.onFetchMovieFailure("Response not successful: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Movies> call, Throwable t) {
                    Log.d(TAG, "fetchMoviesByGenre::onFailure() - Fail when requesting a movie list of genre " + genre + ". Page " + page + ". Message: " + t.getMessage());
                    callback.onFetchMovieFailure("Fail when requesting a movie list of genre " + genre + ". Page " + page + ". Message: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            Log.d(TAG, "fetchMoviesByGenre() - Exception when requesting a movie list of genre " + genre + ". Page " + page + ". Message: " + e.getMessage());
            callback.onFetchMovieFailure("Exception when requesting a movie list of genre " + genre + ". Page " + page + ". Message: " + e.getMessage());
        }
    }


    private void fetchMoviesByType(final MovieAndSeriesType type, final int page, final FetchMovieCallback callback) {
        try {
            Log.d(TAG, "fetchMoviesByType() - Request to the server a movie list of type " + type + ". Page " + page);

            String sType = ApiResolver.resolveType(type);

            movieService.fetchMoviesByType(sType, AppConfig.TMDB_API_KEY, page).enqueue(new Callback<Movies>() {
                @Override
                public void onResponse(Call<Movies> call, Response<Movies> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "fetchMoviesByType::onResponse() - Successful response for a movie list of type " + type + ". Page " + page);
                        callback.onFetchMovieSuccessful(response.body());

                    } else {
                        Log.d(TAG, "fetchMoviesByType::onResponse() - Response not successful for a movie list of type " + type + ". Page " + page + ". Response code: " + response.code());
                        callback.onFetchMovieFailure("Response not successful: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Movies> call, Throwable t) {
                    Log.d(TAG, "fetchMoviesByType::onFailure() - Fail when requesting a movie list of genre " + type + ". type " + page + ". Message: " + t.getMessage());
                    callback.onFetchMovieFailure("Fail when requesting a movie list of genre " + type + ". type " + page + ". Message: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            Log.d(TAG, "fetchMoviesByType() - Exception when requesting a movie list of type " + type + ". Page " + page + ". Message: " + e.getMessage());
            callback.onFetchMovieFailure("Exception when requesting a movie list of type " + type + ". Page " + page + ". Message: " + e.getMessage());
        }
    }

    /**
     * Request to the server a movie cast given a movie id. A response (either a successful or an error) will be
     * received asynchronously
     *
     * @param movieId
     * @param callback
     */
    public void getMovieCast(final String movieId, final MovieCastRequestCallback callback) {
        try {
            Log.d(TAG, "getMovieCast() - Request to the server a movie cast for movieId " + movieId);

            movieService.getMovieCast(movieId, AppConfig.TMDB_API_KEY).enqueue(new Callback<MovieCast>() {
                @Override
                public void onResponse(Call<MovieCast> call, Response<MovieCast> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "getMovieCast::onResponse() - Successful response for a movie cast for movieId " + movieId);
                        callback.onCastRequestSuccessful(response.body());
                    } else {
                        Log.d(TAG, "getMovieCast::onResponse() - Response not successful for a movie cast for movieId " + movieId + ". Response code: " + response.code());
                        callback.onCastRequestFailure("Response not successful: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<MovieCast> call, Throwable t) {
                    Log.d(TAG, "getMovieCast::onFailure() - Fail when requesting a movie cast for movieId " + movieId + ". Message: " + t.getMessage());
                    callback.onCastRequestFailure("Fail when requesting a movie cast for movieId " + movieId + ". Message: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            Log.d(TAG, "getMovieCast() - Exception when requesting a movie cast for movieId " + movieId + ". Message: " + e.getMessage());
            callback.onCastRequestFailure("Exception when requesting a movie cast for movieId " + movieId + ". Message: " + e.getMessage());
        }
    }


}
