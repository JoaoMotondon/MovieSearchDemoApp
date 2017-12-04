package com.motondon.moviesearchdemoapp.view.activity.main;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.motondon.moviesearchdemoapp.R;
import com.motondon.moviesearchdemoapp.model.data.general.MovieAndSeriesType;
import com.motondon.moviesearchdemoapp.model.data.general.SearchType;
import com.motondon.moviesearchdemoapp.view.fragment.help.HelpFragment;
import com.motondon.moviesearchdemoapp.view.fragment.movies.MoviesListFragment;
import com.motondon.moviesearchdemoapp.view.fragment.movies.MoviesTabFragment;
import com.motondon.moviesearchdemoapp.view.fragment.people.PeopleListFragment;
import com.motondon.moviesearchdemoapp.view.fragment.search.SearchFragment;
import com.motondon.moviesearchdemoapp.view.fragment.series.SeriesListFragment;
import com.motondon.moviesearchdemoapp.view.fragment.series.SeriesTabFragment;

/**
 * This app is intended to retrieve movies information from OpenLibrary (http://api.themoviedb.org/) using its public API.
 *
 * It was based on this project https://github.com/thiagokimo/TMDb
 *
 */
public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private static String STATE_SELECTED_POSITION = "STATE_SELECTED_POSITION";

    private DrawerLayout mDrawerLayout;

    // This is the navigation drawer itself.
    private NavigationView mNavigationView;

    // Used to show a animated toggle button in the toolbar.
    private ActionBarDrawerToggle mDrawerToggle;

    // Hold a reference to the drawer menu. It will be used to hide genre menu group when selecting people in the drawer
    private Menu mDrawerMenu;

    private Toolbar mToolbar;

    private SearchView searchView;

    // Used to restore navigation drawer selected menu during orientation change.
    // See this link for details: http://blog.grafixartist.com/easy-navigation-drawer-with-design-support-library/
    private Integer mCurrentSelectedPosition = -1;

    private MovieAndSeriesType movieAndSeriesType = MovieAndSeriesType.NOW_PLAYING;
    private SearchType searchType = SearchType.MOVIE;

    private SearchFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        setupNavigationDrawer();

        // Set "Movies" as default option
        setTitle("Movies");
        createFragmentHelper(MoviesTabFragment.class, MoviesTabFragment.TAG, searchType, null);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION, -1);

        if (mCurrentSelectedPosition > -1) {
            Menu menu = mNavigationView.getMenu();
            menu.getItem(mCurrentSelectedPosition).setChecked(true);
        }
    }

    private void setupNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nvView);
        setupDrawerToggle();
        setupDrawerContent(mNavigationView);
    }

    private void setupDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,  R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(drawerView.getWindowToken(), 0);
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        mDrawerMenu = mNavigationView.getMenu();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }

    private void selectDrawerItem(MenuItem item) {
        searchView.setQuery("", false);
        searchView.setIconified(true);

        switch (item.getItemId()) {
            case R.id.nav_movies_fragment:
                Log.d(TAG, "Clicked over Movies navigation drawer item");
                searchType = SearchType.MOVIE;
                item.setChecked(true);

                searchView.setQueryHint("Search for movies...");
                mDrawerMenu.setGroupVisible(R.id.nav_genre_group, true);
                createFragmentHelper(MoviesTabFragment.class, MoviesTabFragment.TAG, null, null);
                break;

            case R.id.nav_series_fragment:
                Log.d(TAG, "Clicked over Series navigation drawer item");
                searchType = SearchType.SERIES;
                item.setChecked(true);

                searchView.setQueryHint("Search for series...");
                mDrawerMenu.setGroupVisible(R.id.nav_genre_group, true);
                createFragmentHelper(SeriesTabFragment.class, SeriesTabFragment.TAG, null, null);
                break;

            case R.id.nav_people_fragment:
                Log.d(TAG, "Clicked over People navigation drawer item");
                searchType = SearchType.PEOPLE;
                item.setChecked(true);

                searchView.setQueryHint("Search for people...");
                mDrawerMenu.setGroupVisible(R.id.nav_genre_group, false);
                createFragmentHelper(PeopleListFragment.class, PeopleListFragment.TAG, searchType, null);
                break;

            case R.id.nav_comedy_fragment:
                Log.d(TAG, "Clicked over Comedy navigation drawer item");
                movieAndSeriesType = MovieAndSeriesType.COMEDY;
                item.setChecked(true);

                if (searchType == SearchType.MOVIE) {
                    createFragmentHelper(MoviesListFragment.class, MoviesListFragment.TAG_COMEDY, searchType, movieAndSeriesType);
                } else if (searchType == SearchType.SERIES) {
                    createFragmentHelper(SeriesListFragment.class, SeriesListFragment.TAG_COMEDY, searchType, movieAndSeriesType);
                }
                break;

            case R.id.nav_adventure_fragment:
                Log.d(TAG, "Clicked over Adventure navigation drawer item");
                movieAndSeriesType = MovieAndSeriesType.ADVENTURE;
                item.setChecked(true);

                if (searchType == SearchType.MOVIE) {
                    createFragmentHelper(MoviesListFragment.class, MoviesListFragment.TAG_ADVENTURE, searchType, movieAndSeriesType);
                } else if (searchType == SearchType.SERIES) {
                    createFragmentHelper(SeriesListFragment.class, SeriesListFragment.TAG_ADVENTURE, searchType, movieAndSeriesType);
                }
                break;

            case R.id.nav_action_fragment:
                Log.d(TAG, "Clicked over Action navigation drawer item");
                movieAndSeriesType = MovieAndSeriesType.ACTION;
                item.setChecked(true);

                if (searchType == SearchType.MOVIE) {
                    createFragmentHelper(MoviesListFragment.class, MoviesListFragment.TAG_ACTION, searchType, movieAndSeriesType);
                } else if (searchType == SearchType.SERIES) {
                    createFragmentHelper(SeriesListFragment.class, SeriesListFragment.TAG_ACTION, searchType, movieAndSeriesType);
                }
                break;

            case R.id.nav_documentary_fragment:
                Log.d(TAG, "Clicked over Documentary navigation drawer item");
                movieAndSeriesType = MovieAndSeriesType.DOCUMENTARY;
                item.setChecked(true);

                if (searchType == SearchType.MOVIE) {
                    createFragmentHelper(MoviesListFragment.class, MoviesListFragment.TAG_DOCUMENTARY, searchType, movieAndSeriesType);
                } else if (searchType == SearchType.SERIES) {
                    createFragmentHelper(SeriesListFragment.class, SeriesListFragment.TAG_DOCUMENTARY, searchType, movieAndSeriesType);
                }
                break;

            default:
                createFragmentHelper(HelpFragment.class, HelpFragment.TAG, null, null);
        }

        setTitle(item.getTitle());
        mDrawerLayout.closeDrawers();
    }

    private Fragment createFragmentHelper(Class fragmentClass, String tag, SearchType searchType, MovieAndSeriesType movieAndSeriesType) {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            try {
                fragment = (Fragment) fragmentClass.newInstance();

                Bundle args = new Bundle();
                if (searchType != null) {
                    args.putSerializable("SEARCH_TYPE", searchType);
                }

                if (movieAndSeriesType != null) {
                    args.putSerializable("TYPE", movieAndSeriesType);
                }

                if (args.size() > 0) {
                    fragment.setArguments(args);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, fragment, tag).commit();

        return fragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    switch (searchType) {
                        case MOVIE:
                            searchFragment = (SearchFragment) createFragmentHelper(SearchFragment.class, SearchFragment.TAG_SEARCH_BY_MOVIE, searchType, null);
                            break;

                        case SERIES:
                            searchFragment = (SearchFragment) createFragmentHelper(SearchFragment.class, SearchFragment.TAG_SEARCH_BY_SERIES, searchType, null);
                           break;

                        case PEOPLE:
                            searchFragment = (SearchFragment) createFragmentHelper(SearchFragment.class, SearchFragment.TAG_SEARCH_BY_PEOPLE, searchType, null);
                            break;
                    }
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchFragment.fetchData(query, searchType, 1);

                // Reset SearchView
                searchView.clearFocus();
                //searchView.setQuery("", false);
                //searchView.setIconified(true);
                //searchItem.collapseActionView();

                // Set activity title to search query
                MainActivity.this.setTitle(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.action_search:
                // Already processed by the SearchView::onQueryTextSubmit() event. So just return true.
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
