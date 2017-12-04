package com.motondon.moviesearchdemoapp.presenter.filmography;

import com.motondon.moviesearchdemoapp.model.data.general.SearchType;

public interface FilmographyListPresenter {
    void fetchFilmography(SearchType type, String actorId);
}
