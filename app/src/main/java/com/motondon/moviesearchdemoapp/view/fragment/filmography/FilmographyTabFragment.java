package com.motondon.moviesearchdemoapp.view.fragment.filmography;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.motondon.moviesearchdemoapp.R;
import com.motondon.moviesearchdemoapp.model.data.general.SearchType;
import com.motondon.moviesearchdemoapp.view.adapters.viewpager.ViewPagerAdapter;

public class FilmographyTabFragment extends Fragment {

    public static final String TAG = FilmographyTabFragment.class.getSimpleName();

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private String actorId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actorId = getArguments().getString("ACTOR_ID");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movies_tab, container, false);

        viewPager = (ViewPager) root.findViewById(R.id.tabanim_viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) root.findViewById(R.id.tabanim_tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabListener();

        return root;
    }

    private void setupTabListener() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {

        // Do not forget to use getChildFragmentManager() instead of getFragmentManager(), since this class is already a
        // fragment and we want to add child fragment in it.
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        FilmographyListFragment moviesListFragment;
        Bundle args;

        moviesListFragment = new FilmographyListFragment();
        args = new Bundle();
        args.putSerializable("TYPE", SearchType.MOVIE);
        args.putString("ACTOR_ID", actorId);
        moviesListFragment.setArguments(args);
        adapter.addFrag(moviesListFragment, "Films");

        moviesListFragment = new FilmographyListFragment();
        args = new Bundle();
        args.putSerializable("TYPE", SearchType.SERIES);
        args.putString("ACTOR_ID", actorId);
        moviesListFragment.setArguments(args);
        adapter.addFrag(moviesListFragment, "Series");

        viewPager.setAdapter(adapter);
    }
}
