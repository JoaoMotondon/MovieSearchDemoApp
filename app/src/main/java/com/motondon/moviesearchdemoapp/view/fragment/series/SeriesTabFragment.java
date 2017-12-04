package com.motondon.moviesearchdemoapp.view.fragment.series;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.motondon.moviesearchdemoapp.R;
import com.motondon.moviesearchdemoapp.model.data.general.MovieAndSeriesType;
import com.motondon.moviesearchdemoapp.view.adapters.viewpager.ViewPagerAdapter;

public class SeriesTabFragment extends Fragment {

    public static final String TAG = SeriesTabFragment.class.getSimpleName();

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_series_tab, container, false);

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
        
        SeriesListFragment seriesListFragment;
        Bundle args;

        seriesListFragment = new SeriesListFragment();
        args = new Bundle();
        args.putSerializable("TYPE", MovieAndSeriesType.ON_THE_AIR);
        seriesListFragment.setArguments(args);
        adapter.addFrag(seriesListFragment, "On the Air");

        seriesListFragment = new SeriesListFragment();
        args = new Bundle();
        args.putSerializable("TYPE", MovieAndSeriesType.TOP_RATED);
        seriesListFragment.setArguments(args);
        adapter.addFrag(seriesListFragment, "Top Rated");

        seriesListFragment = new SeriesListFragment();
        args = new Bundle();
        args.putSerializable("TYPE", MovieAndSeriesType.POPULAR);
        seriesListFragment.setArguments(args);
        adapter.addFrag(seriesListFragment, "Popular");

        viewPager.setAdapter(adapter);
    }
}
