package com.motondon.moviesearchdemoapp.view.adapters.people;

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
import com.motondon.moviesearchdemoapp.model.data.people.Person;
import com.motondon.moviesearchdemoapp.presenter.people.PeopleListPresenterImpl;
import com.motondon.moviesearchdemoapp.view.activity.filmography.FilmographyActivity;

import java.util.ArrayList;
import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder>{

    private List<Person> personList = new ArrayList<>();

    private PeopleListPresenterImpl.PeopleListView fragment;
    private Context context;

    public PeopleAdapter(PeopleListPresenterImpl.PeopleListView fragment, Context context) {
        this.fragment = fragment;
        this.context = context;
    }

    public void setPeople(List<Person> people) {
        personList.addAll(people);
        notifyDataSetChanged();
    }

    public void clearData() {
        personList.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Person person = personList.get(position);
        holder.imageUrl = person.getId().toString();

        // Request to the fragment the image to be shown (asynchronously)
        fragment.loadImage(context, AppConfig.BASE_SMALL_IMAGE_URL + person.getProfilePath(), holder.imageUrl, holder.cover);

        holder.title.setText(person.getName());
        holder.additionalInfo.setText("Popularity: " + person.getPopularity().toString());
    }

    @Override
    public int getItemCount() {
        return personList.size();
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
                    Intent i = new Intent(context, FilmographyActivity.class);
                    i.putExtra("ACTOR_ID", personList.get(getLayoutPosition()).getId().toString());
                    i.putExtra("ACTOR_NAME", personList.get(getLayoutPosition()).getName().toString());
                    context.startActivity(i);
                }
            }, 200);
        }
    }
}