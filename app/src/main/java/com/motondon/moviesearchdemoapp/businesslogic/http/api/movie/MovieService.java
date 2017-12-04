package com.motondon.moviesearchdemoapp.businesslogic.http.api.movie;

import com.motondon.moviesearchdemoapp.model.data.movie.Movies;
import com.motondon.moviesearchdemoapp.model.data.movie.moviedetails.MovieCast;
import com.motondon.moviesearchdemoapp.model.data.movie.moviedetails.MovieDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * This interface defines all the Movie methods to be requested to the server (TMDB server) based on its REST API (movie endpoint).
 *
 * Each method contains specific parameters which will end up in a complete url to be sent to the server in order to
 * consume a REST API method.
 *
 * All these methods are used by the MovieAPI class which uses Retrofit library
 *
 */
public interface MovieService {

    @GET("3/search/movie")
    Call<Movies> searchMovies(@Query("api_key") String apiKey, @Query("query") String movieName, @Query("page") Integer page);

    @GET("3/movie/{id}")
    Call<MovieDetail> getExtraMovieDetails(@Path("id") String id, @Query("api_key") String apiKey);

    @GET("3/discover/movie/")
    Call<Movies> fetchMoviesByGenre(@Query("api_key") String apiKey, @Query("with_genres") int genre, @Query("page") Integer page);

    @GET("3/movie/{type}")
    Call<Movies> fetchMoviesByType(@Path("type") String type, @Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("3/movie/{id}/credits")
    Call<MovieCast> getMovieCast(@Path("id") String id, @Query("api_key") String apiKey);
}
