package com.motondon.moviesearchdemoapp.businesslogic.http.api.helper;

import com.motondon.moviesearchdemoapp.model.data.general.MovieAndSeriesType;

/**
 * A Helper class responsible to convert an enum item into a code known by the TMDB server
 *
 */
public class ApiResolver {

    public static int resolveGenre(MovieAndSeriesType movieGenre) {
        int genre = 28;

        switch (movieGenre) {
            case ACTION:
                genre = 28;
                break;

            case ADVENTURE:
                genre = 12;
                break;

            case COMEDY:
                genre = 35;
                break;

            case DOCUMENTARY:
                genre = 99;
                break;
        }
        return genre;
    }

    public static String resolveType(MovieAndSeriesType movieAndSeriesType) {
        String type = "now_playing";

        switch (movieAndSeriesType) {
            case NOW_PLAYING:
                type = "now_playing";
                break;

            case TOP_RATED:
                type = "top_rated";
                break;

            case UPCOMING:
                type = "upcoming";
                break;

            case POPULAR:
                type = "popular";
                break;

            case ON_THE_AIR:
                type = "on_the_air";
                break;
        }

        return type;
    }
}
