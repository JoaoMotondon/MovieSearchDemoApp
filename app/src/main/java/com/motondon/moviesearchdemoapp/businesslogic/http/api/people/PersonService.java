package com.motondon.moviesearchdemoapp.businesslogic.http.api.people;

import com.motondon.moviesearchdemoapp.model.data.movie.moviedetails.MovieDetail;
import com.motondon.moviesearchdemoapp.model.data.people.PersonList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * This interface defines all the Person methods to be requested to the server (TMDB server) based on its REST API (person endpoint).
 *
 * Each method contains specific parameters which will end up in a complete url to be sent to the server in order to
 * consume a REST API method.
 *
 * All these methods are used by the PersonAPI class which uses Retrofit library
 *
 */
public interface PersonService {

    @GET("3/search/person")
    Call<PersonList> searchPerson(@Query("api_key") String apiKey, @Query("query") String movieName, @Query("page") Integer page, @Query("include_adult") Boolean includeAdult);

    @GET("3/person/{id}")
    Call<MovieDetail> getExtraPersonDetails(@Path("id") String id, @Query("api_key") String apiKey);

    @GET("3/person/popular")
    Call<PersonList> fetchPeople( @Query("api_key") String apiKey, @Query("page") Integer page);
}
