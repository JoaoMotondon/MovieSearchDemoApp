package com.motondon.moviesearchdemoapp.view.adapters.search;

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
import com.motondon.moviesearchdemoapp.model.data.general.SearchType;
import com.motondon.moviesearchdemoapp.model.data.movie.Movie;
import com.motondon.moviesearchdemoapp.model.data.people.Person;
import com.motondon.moviesearchdemoapp.model.data.series.Series;
import com.motondon.moviesearchdemoapp.presenter.search.SearchPresenterImpl;
import com.motondon.moviesearchdemoapp.view.activity.filmography.FilmographyActivity;
import com.motondon.moviesearchdemoapp.view.activity.movies.MovieDetailsActivity;
import com.motondon.moviesearchdemoapp.view.activity.series.SeriesDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{

    private SearchType searchType;
    private List<Movie> movies = new ArrayList<>();
    private List<Series> seriesList = new ArrayList<>();
    private List<Person> personList = new ArrayList<>();

    private MovieAndSeriesType movieAndSeriesType;

    private SearchPresenterImpl.SearchView fragment;
    private Context context;

    public SearchAdapter(SearchPresenterImpl.SearchView fragment, Context context, SearchType searchType, MovieAndSeriesType movieAndSeriesType) {
        this.fragment = fragment;
        this.context = context;
        this.searchType = searchType;

        // For search fragment, it will be null!
        this.movieAndSeriesType = movieAndSeriesType;
    }

    public void setMovies(List<Movie> movieList, boolean cleanBefore) {

        // When making a search for the first time, first clean adapter up. We do not want append search results.
        if (cleanBefore) {
            movies.clear();
        }

        movies.addAll(movieList);
        notifyDataSetChanged();
    }

    public void setSeriesList(List<Series> series, boolean cleanBefore) {

        // When making a search for the first time, first clean adapter up. We do not want to append search results.
        if (cleanBefore) {
            seriesList.clear();
        }

        seriesList.addAll(series);
        notifyDataSetChanged();
    }


    public void setPersonList(List<Person> person, boolean cleanBefore) {

        // When making a search for the first time, first clean adapter up. We do not want to append search results.
        if (cleanBefore) {
            personList.clear();
        }

        this.personList.addAll(person);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        switch (searchType) {
            case MOVIE:
                Movie movie = movies.get(position);

                holder.imageUrl = movie.getId().toString();

                // Request to the fragment the image to be shown (asynchronously)
                fragment.loadImage(context, AppConfig.BASE_SMALL_IMAGE_URL + movie.getPosterPath(), holder.imageUrl, holder.cover);

                holder.title.setText(movie.getTitle());

                if (movieAndSeriesType == null) {
                    holder.additionalInfo.setText("Vote Average: " + movie.getVoteAverage().toString());
                    return;
                }

                switch (movieAndSeriesType) {
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
                break;

            case SERIES:
                Series series = seriesList.get(position);

                holder.imageUrl = series.getId().toString();

                // Request to the fragment the image to be shown (asynchronously)
                fragment.loadImage(context, AppConfig.BASE_SMALL_IMAGE_URL + series.getPosterPath(), holder.imageUrl, holder.cover);

                holder.title.setText(series.getName());

                if (movieAndSeriesType == null) {
                    holder.additionalInfo.setText("Vote Average: " + series.getVoteAverage().toString());
                    return;
                }

                switch (movieAndSeriesType) {
                    case POPULAR:
                        holder.additionalInfo.setText("Popularity: " + series.getPopularity().toString());
                        break;

                    case TOP_RATED:
                        holder.additionalInfo.setText("Vote Average: " + series.getVoteAverage().toString());
                        break;

                    case ON_THE_AIR: // For on the air, we will show first air date.
                        holder.additionalInfo.setText("First Air Date: " + series.getFirstAirDate().toString());
                        break;

                    default:
                        holder.additionalInfo.setText("Vote Average: " + series.getVoteAverage().toString());
                        break;
                }
                break;

            case PEOPLE:
                Person person = personList.get(position);

                holder.imageUrl = person.getId().toString();

                // Request to the fragment the image to be shown (asynchronously)
                fragment.loadImage(context, AppConfig.BASE_SMALL_IMAGE_URL + person.getProfilePath(), holder.imageUrl, holder.cover);

                holder.title.setText(person.getName());
                holder.additionalInfo.setText("Popularity: " + person.getPopularity().toString());

                break;
        }
    }

    @Override
    public int getItemCount() {

        switch (searchType) {
            case MOVIE:
                return movies.size();

            case SERIES:
                return seriesList.size();

            case PEOPLE:
                return personList.size();
        }

        return 0;
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

            switch (searchType) {
                case MOVIE:

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(context, MovieDetailsActivity.class);
                            i.putExtra(MovieDetailsActivity.MOVIE_ID, movies.get(getLayoutPosition()).getId().toString());
                            i.putExtra(MovieDetailsActivity.POSTER_PATH, movies.get(getLayoutPosition()).getPosterPath());
                            context.startActivity(i);
                        }
                    }, 200);

                    break;

                case SERIES:

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(context, SeriesDetailsActivity.class);
                            i.putExtra(SeriesDetailsActivity.SERIES_ID, seriesList.get(getLayoutPosition()).getId().toString());
                            i.putExtra(SeriesDetailsActivity.POSTER_PATH, seriesList.get(getLayoutPosition()).getPosterPath());
                            context.startActivity(i);
                        }
                    }, 200);

                    break;

                case PEOPLE:
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(context, FilmographyActivity.class);
                            i.putExtra("TYPE", MovieAndSeriesType.TOP_RATED);
                            i.putExtra("ACTOR_ID", personList.get(getLayoutPosition()).getId().toString());
                            i.putExtra("ACTOR_NAME", personList.get(getLayoutPosition()).getName());
                            context.startActivity(i);
                        }
                    }, 200);

                    break;
            }
        }
    }
}