package com.motondon.moviesearchdemoapp.view.adapters.movies;

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
import com.motondon.moviesearchdemoapp.model.data.general.MovieAndSeriesType;
import com.motondon.moviesearchdemoapp.model.data.movie.Movie;
import com.motondon.moviesearchdemoapp.presenter.movies.MoviesListPresenterImpl;
import com.motondon.moviesearchdemoapp.view.activity.movies.MovieDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder>{

    private List<Movie> movies = new ArrayList<>();

    private MovieAndSeriesType movieType;

    private MoviesListPresenterImpl.MoviesListView fragment;
    private Context context;

    public MoviesAdapter(MoviesListPresenterImpl.MoviesListView fragment, Context context, MovieAndSeriesType movieType) {
        this.fragment = fragment;
        this.context = context;
        this.movieType = movieType;
    }

    public void setMovies(List<Movie> movie) {
        movies.addAll(movie);
        notifyDataSetChanged();
    }

    public void clearData() {
        movies.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Movie movie = movies.get(position);

        holder.imageId = movie.getId().toString();

        // Request to the fragment the image to be shown (asynchronously)
        fragment.loadImage(context, AppConfig.BASE_SMALL_IMAGE_URL + movie.getPosterPath(), holder.imageId, holder.cover);

        holder.title.setText(movie.getTitle());

        // Depends on the type of the series it will show different data in the second line.
        if (movieType == null) {
            holder.additionalInfo.setText("Vote Average: " + movie.getVoteAverage().toString());
            return;
        }

        switch (movieType) {
            case POPULAR:
                holder.additionalInfo.setText("Popularity: " + movie.getPopularity().toString());
                break;

            case TOP_RATED:
                holder.additionalInfo.setText("Vote Average: " + movie.getVoteAverage().toString());
                break;

            case UPCOMING:
                holder.additionalInfo.setText("Release Date: " + movie.getReleaseDate().toString());
                break;

            default:
                holder.additionalInfo.setText("Vote Average: " + movie.getVoteAverage().toString());
                break;
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
        private String imageId;

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
                    Intent i = new Intent(context, MovieDetailsActivity.class);
                    i.putExtra(MovieDetailsActivity.MOVIE_ID, movies.get(getLayoutPosition()).getId().toString());
                    i.putExtra(MovieDetailsActivity.POSTER_PATH, movies.get(getLayoutPosition()).getPosterPath());
                    context.startActivity(i);
                }
            }, 200);
        }
    }
}