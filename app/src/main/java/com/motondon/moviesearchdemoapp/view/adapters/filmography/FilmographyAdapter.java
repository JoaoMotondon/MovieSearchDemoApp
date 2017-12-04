package com.motondon.moviesearchdemoapp.view.adapters.filmography;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.motondon.moviesearchdemoapp.R;
import com.motondon.moviesearchdemoapp.config.AppConfig;
import com.motondon.moviesearchdemoapp.model.data.filmography.Cast;
import com.motondon.moviesearchdemoapp.model.data.general.SearchType;
import com.motondon.moviesearchdemoapp.presenter.filmography.FilmographyListPresenterImpl;
import com.motondon.moviesearchdemoapp.view.activity.movies.MovieDetailsActivity;
import com.motondon.moviesearchdemoapp.view.activity.series.SeriesDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class FilmographyAdapter extends RecyclerView.Adapter<FilmographyAdapter.ViewHolder>{

    private List<Cast> movies = new ArrayList<>();

    private FilmographyListPresenterImpl.FilmographyListView fragment;
    private Context context;
    private SearchType searchType;

    public FilmographyAdapter(FilmographyListPresenterImpl.FilmographyListView fragment, Context context, SearchType searchType) {
        this.fragment = fragment;
        this.context = context;
        this.searchType = searchType;
    }

    public void setFilmography(List<Cast> moviesList) {

        movies.clear();

        // First remove "adult" movies
        List<Cast> tempMovies = purgeAdultMovies(moviesList);

        movies.addAll(tempMovies);
        notifyDataSetChanged();
    }

    private List<Cast> purgeAdultMovies(List<Cast> moviesList) {
        List<Cast> tempMovies = new ArrayList<>();

        // This will avoid show "adult" movies
        for (Cast cast : moviesList) {
            if (cast.getAdult() != null &&  cast.getAdult()) {
                continue;
            }
            tempMovies.add(cast);
        }
        return tempMovies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Cast movie = movies.get(position);

        holder.imageUrl = movie.getId().toString();

        // Request to the fragment the image to be shown (asynchronously)
        fragment.loadImage(context, AppConfig.BASE_SMALL_IMAGE_URL + movie.getPosterPath(), holder.imageUrl, holder.cover);

        // Depends on the movie (and on the series), name can be stored in different places.
        if (movie.getTitle() != null && !movie.getTitle().isEmpty()) {
            holder.title.setText(movie.getTitle());
        } else if (movie.getName() != null && !movie.getName().isEmpty()) {
            holder.title.setText(movie.getName());
        } else if (movie.getOriginalName() != null && !movie.getOriginalName().isEmpty()) {
            holder.title.setText(movie.getOriginalName());
        } else if (movie.getOriginalTitle() != null && !movie.getOriginalTitle().isEmpty()) {
            holder.title.setText(movie.getOriginalTitle());
        }

        if ( movie.getReleaseDate() != null) {
            holder.additionalInfo.setText("Release Date: " + movie.getReleaseDate().toString());
        } else if (movie.getFirstAirDate() != null) {
            holder.additionalInfo.setText("Release Date: " + movie.getFirstAirDate().toString());
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener{

        private ImageView cover;
        private TextView title;
        private TextView additionalInfo;
        private String imageUrl;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            additionalInfo = (TextView) itemView.findViewById(R.id.additionalInfo);
            cover = (ImageView) itemView.findViewById(R.id.image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (searchType == SearchType.MOVIE) {
                        Intent i = new Intent(context, MovieDetailsActivity.class);
                        i.putExtra(MovieDetailsActivity.MOVIE_ID, movies.get(getLayoutPosition()).getId().toString());
                        i.putExtra(MovieDetailsActivity.POSTER_PATH, movies.get(getLayoutPosition()).getPosterPath());
                        context.startActivity(i);
                    } else if (searchType == SearchType.SERIES) {
                        Intent i = new Intent(context, SeriesDetailsActivity.class);
                        i.putExtra(SeriesDetailsActivity.SERIES_ID, movies.get(getLayoutPosition()).getId().toString());
                        i.putExtra(SeriesDetailsActivity.POSTER_PATH, movies.get(getLayoutPosition()).getPosterPath());
                        context.startActivity(i);
                    }
                }
            }, 200);
        }
    }
}