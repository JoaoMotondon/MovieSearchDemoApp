package com.motondon.moviesearchdemoapp.view.activity.filmography;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.motondon.moviesearchdemoapp.R;
import com.motondon.moviesearchdemoapp.view.fragment.filmography.FilmographyTabFragment;

public class FilmographyActivity extends AppCompatActivity {

    public static final String TAG = FilmographyActivity.class.getSimpleName();

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filmography);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        String actorName = getIntent().getExtras().getString("ACTOR_NAME");
        setTitle(actorName + " Filmography");

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FilmographyTabFragment.TAG);

        if (fragment == null) {
            fragment = new FilmographyTabFragment();
            fragment.setArguments(getIntent().getExtras()); // pass parameters directly to the fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.flContent, fragment, FilmographyTabFragment.TAG).commit();
        }
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
}
