package com.motondon.moviesearchdemoapp.businesslogic.http.api.filmography;

import com.motondon.moviesearchdemoapp.model.data.filmography.Filmography;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * This interface defines all the Person methods related to movie/tv credits to be requested to the server (TMDB server) based on its
 * REST API (person endpoint).
 *
 * Each method contains specific parameters which will end up in a complete url to be sent to the server in order to
 * consume a REST API method.
 *
 * All these methods are used by the FilmographyAPI class which uses Retrofit library
 *
 */
public interface FilmographyService {

    @GET("3/person/{id}/movie_credits")
    Call<Filmography> fetchFilmsByActor(@Path("id") String id, @Query("api_key") String apiKey);

    @GET("3/person/{id}/tv_credits")
    Call<Filmography> fetchSeriesByActor(@Path("id") String id, @Query("api_key") String apiKey);
}
