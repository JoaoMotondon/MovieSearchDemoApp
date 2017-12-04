package com.motondon.moviesearchdemoapp.view.activity.series;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.motondon.moviesearchdemoapp.R;
import com.motondon.moviesearchdemoapp.config.AppConfig;
import com.motondon.moviesearchdemoapp.model.data.movie.moviedetails.Cast;
import com.motondon.moviesearchdemoapp.model.data.movie.moviedetails.MovieCast;
import com.motondon.moviesearchdemoapp.model.data.series.seriesdetails.SeriesDetails;
import com.motondon.moviesearchdemoapp.presenter.series.SeriesDetailsPresenterImpl;
import com.motondon.moviesearchdemoapp.view.activity.filmography.FilmographyActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SeriesDetailsActivity extends AppCompatActivity implements SeriesDetailsPresenterImpl.SeriesDetailsView {

    public static final String TAG = SeriesDetailsActivity.class.getSimpleName();

    public static final String SERIES_ID = "SERIES_ID";
    public static final String POSTER_PATH = "POSTER_PATH";

    private Toolbar mToolbar;

    private ImageView coverImage;
    private TextView title;
    private TextView year;
    private TextView homepage;
    private TextView companies;
    private View seriesCastContainer;
    private TextView seriesCast;
    private View genresContainer;
    private TextView genres;
    private View seasonsContainer;
    private TextView numberOfSeasons;
    private TextView numberOfEpisodes;
    private View overviewContainer;
    private TextView overview;

    private SeriesDetailsPresenterImpl seriesDetailsPresenterImpl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_detail);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("Series Details");

        String seriesId = getIntent().getExtras().getString(SERIES_ID);
        String posterPath = getIntent().getExtras().getString(POSTER_PATH);

        coverImage = (ImageView) findViewById(R.id.ivMovieCover);
        title = (TextView) findViewById(R.id.title);
        year = (TextView) findViewById(R.id.year);
        homepage = (TextView) findViewById(R.id.homepage);
        companies = (TextView) findViewById(R.id.companies);
        seriesCastContainer = findViewById(R.id.cast_container);
        seriesCast = (TextView) findViewById(R.id.series_cast);
        genresContainer = findViewById(R.id.genres_container);
        genres = (TextView) findViewById(R.id.genres);
        seasonsContainer  = findViewById(R.id.seasons_container);
        numberOfSeasons = (TextView) findViewById(R.id.number_of_seasons);
        numberOfEpisodes = (TextView) findViewById(R.id.number_of_episodes);
        overviewContainer = findViewById(R.id.overview_container);
        overview = (TextView) findViewById(R.id.overview);

        // Initialize presenter layer
        seriesDetailsPresenterImpl = new SeriesDetailsPresenterImpl(this, getApplicationContext());

        // Use the movie to populate the data into our views
        loadSeriesDetails(seriesId, posterPath);
        loadSeriesCast(seriesId);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Request to the server details about a series.
     *
     * @param seriesId
     * @param posterPath
     */
    private void loadSeriesDetails(String seriesId, String posterPath) {
        Log.d(TAG, "loadSeriesDetails() - Request to the server a series details. Series id: " + seriesId);

        // Populate data using Picasso library which will take care of loading the image from the Internet
        // and cache it.
        Picasso.with(getApplicationContext()).load(Uri.parse(AppConfig.BASE__LARGE_IMAGE_URL + posterPath)).placeholder(R.drawable.ic_movie_placeholder).error(R.drawable.ic_no_cover).into(coverImage);

        seriesDetailsPresenterImpl.loadSeriesDetails(seriesId);
    }

    /**
     * Request to the server cast for the series (i.e. actor names list)
     *
     * @param seriesId
     */
    private void loadSeriesCast(String seriesId) {
        Log.d(TAG, "loadSeriesCast() - Request to the server a series cast. Series id: " + seriesId);

        seriesDetailsPresenterImpl.loadSeriesCast(seriesId);
    }

    /**
     * Called (asynchronously) when server returns with a series details data. Then, just update some fields.
     *
     * @param seriesDetails
     */
    @Override
    public void displaySeriesDetails(SeriesDetails seriesDetails) {
        Log.d(TAG, "displaySeriesDetails() - Received a series details data from the server. Series name: " + seriesDetails.getName());

        title.setText(seriesDetails.getName());

        if (!TextUtils.isEmpty(seriesDetails.getFirstAirDate())) {
            year.setText(seriesDetails.getFirstAirDate());
        }

        if (!TextUtils.isEmpty(seriesDetails.getHomepage())) {
            homepage.setText(seriesDetails.getHomepage());
        }

        if (seriesDetails.getProductionCompanies().size() > 0) {
            companies.setText(TextUtils.join(", ", seriesDetails.getProductionCompanies()));
        }

        if (seriesDetails.getGenres().size() > 0) {
            genres.setText(TextUtils.join(", ", seriesDetails.getGenres()));
        }

        if (seriesDetails.getNumberOfSeasons() > 0) {
            numberOfSeasons.setText(seriesDetails.getNumberOfSeasons().toString());
        }

        if (seriesDetails.getNumberOfEpisodes() > 0) {
            numberOfEpisodes.setText(seriesDetails.getNumberOfEpisodes().toString());
        }

        if (!TextUtils.isEmpty(seriesDetails.getOverview())) {
            overview.setText(seriesDetails.getOverview());
        }
    }

    /**
     * Called (asynchronously) when server returns with cast data. It will then get the fist five actor names and
     * create a SpannableString with all these actors and set a span for each one, allowing user to click over any
     * actor name. When user click over an actor name, it will call the showFilmography() method which will show all
     * the movies/series for the clicked actor.
     *
     * @param movieCastList
     */
    @Override
    public void displaySeriesCast(MovieCast movieCastList) {
        Log.d(TAG, "displaySeriesDetails() - Received a series cast data from the server");

        List<Cast> castList = movieCastList.getCast();

        List<Cast> fiveCastList = new ArrayList<>();
        int i = 0;
        for (Cast cast : castList) {
            if (cast.getCharacter() != null) { // means an actor and not a crew
                fiveCastList.add(cast);
                i++;
            }

            // Copy only the first five actors
            if (i >=5) {
                break;
            }
        }

        // Create a SpannableString object with all actors names.
        SpannableString ss = new SpannableString(TextUtils.join(", ", fiveCastList));

        // Now, create one span for each actor and set a listener for each one
        for (final Cast cast : fiveCastList) {
            setSpanOnLink(ss, cast.toString(), new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    Log.i("", "Clicked on actor: " + cast.getName().toString());
                    showFilmography(cast);
                    //TODO run item
                }
            });
        }

        // Set the view text with the cast list
        seriesCast.setText(ss);
        seriesCast.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void requestFailure(String errorMessage) {
        // TODO: Process server error here. Maybe show a warning to the user
    }

    private void setSpanOnLink(SpannableString ss, String link, ClickableSpan cs) {
        String text = ss.toString();
        int start = text.indexOf(link);
        int end = start + link.length();
        ss.setSpan(cs, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * This method will be called when user clicks over a actor name in the span views. It will start
     * filmography activity
     *
     * @param cast
     */
    private void showFilmography(final Cast cast) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), FilmographyActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("ACTOR_ID", cast.getId().toString());
                i.putExtra("ACTOR_NAME", cast.getName().toString());
                getApplicationContext().startActivity(i);
            }
        }, 200);
    }
}
