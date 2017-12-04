package com.motondon.moviesearchdemoapp.businesslogic.http.manager;

import com.motondon.moviesearchdemoapp.businesslogic.http.api.filmography.FilmographyApi;
import com.motondon.moviesearchdemoapp.businesslogic.http.api.movie.MovieApi;
import com.motondon.moviesearchdemoapp.businesslogic.http.api.people.PersonApi;
import com.motondon.moviesearchdemoapp.businesslogic.http.api.series.SeriesApi;

public enum ApiTypes {

    MOVIE_API(new MovieApi()),
    SERIES_API(new SeriesApi()),
    PEOPLE_API(new PersonApi()),
    FILMOGRAPHY_API(new FilmographyApi());

    private final RetrofitApi instance;

    ApiTypes(RetrofitApi instance) {
        this.instance = instance;
    }

    public RetrofitApi getApiType() {
        return instance;
    }
}