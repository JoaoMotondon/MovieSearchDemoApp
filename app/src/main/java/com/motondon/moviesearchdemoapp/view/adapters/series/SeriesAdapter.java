package com.motondon.moviesearchdemoapp.view.adapters.series;

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
import com.motondon.moviesearchdemoapp.model.data.series.Series;
import com.motondon.moviesearchdemoapp.presenter.series.SeriesListPresenterImpl;
import com.motondon.moviesearchdemoapp.view.activity.series.SeriesDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.ViewHolder> {

    private List<Series> seriesList = new ArrayList<>();

    private MovieAndSeriesType seriesType;

    private SeriesListPresenterImpl.SeriesListView fragment;
    private Context context;

    public SeriesAdapter(SeriesListPresenterImpl.SeriesListView fragment, Context context, MovieAndSeriesType seriesType) {
        this.fragment = fragment;
        this.context = context;

        this.seriesType = seriesType;
    }

    public void setSeriesList(List<Series> series) {
        seriesList.addAll(series);
        notifyDataSetChanged();
    }

    public void clearData() {
        seriesList.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Series series = seriesList.get(position);
        holder.imageUrl = series.getId().toString();

        // Request to the fragment the image to be shown (asynchronously)
        fragment.loadImage(context, AppConfig.BASE_SMALL_IMAGE_URL + series.getPosterPath(), holder.imageUrl, holder.cover);

        holder.title.setText(series.getName());

        // Depends on the type of the series it will show different data in the second line.
        if (seriesType == null) {
            holder.additionalInfo.setText("Vote Average: " + series.getVoteAverage().toString());
            return;
        }

        switch (seriesType) {
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
    }

    @Override
    public int getItemCount() {
        return seriesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {

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
                    Intent i = new Intent(context, SeriesDetailsActivity.class);
                    i.putExtra(SeriesDetailsActivity.SERIES_ID, seriesList.get(getLayoutPosition()).getId().toString());
                    i.putExtra(SeriesDetailsActivity.POSTER_PATH, seriesList.get(getLayoutPosition()).getPosterPath());
                    context.startActivity(i);
                }
            }, 200);
        }
    }
}