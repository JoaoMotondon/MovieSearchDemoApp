package com.motondon.moviesearchdemoapp.businesslogic.http.api.series;

import com.motondon.moviesearchdemoapp.model.data.movie.moviedetails.MovieCast;
import com.motondon.moviesearchdemoapp.model.data.series.SeriesList;
import com.motondon.moviesearchdemoapp.model.data.series.seriesdetails.SeriesDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * This interface defines all the Series methods to be requested to the server (TMDB server) based on its REST API (TV Endpoint).
 *
 * Each method contains specific parameters which will end up in a complete url to be sent to the server in order to
 * consume a REST API method.
 *
 * All these methods are used by the SeriesAPI class which uses Retrofit library
 *
 */
public interface SeriesService {

    @GET("3/search/tv")
    Call<SeriesList> searchSeries(@Query("api_key") String apiKey, @Query("query") String serieName, @Query("page") Integer page);

    @GET("3/tv/{id}")
    Call<SeriesDetails> getExtraSeriesDetails(@Path("id") String id, @Query("api_key") String apiKey);

    @GET("3/discover/tv/")
    Call<SeriesList> fetchSeriesByGenre(@Query("api_key") String apiKey, @Query("with_genres") int genre, @Query("page") Integer page);

    @GET("3/tv/{type}")
    Call<SeriesList> fetchSeriesByType(@Path("type") String type, @Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("3/tv/{id}/credits")
    Call<MovieCast> getSeriesCast(@Path("id") String id, @Query("api_key") String apiKey);
}
