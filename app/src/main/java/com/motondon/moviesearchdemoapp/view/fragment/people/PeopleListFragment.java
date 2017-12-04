package com.motondon.moviesearchdemoapp.view.fragment.people;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.motondon.moviesearchdemoapp.R;
import com.motondon.moviesearchdemoapp.model.data.people.PersonList;
import com.motondon.moviesearchdemoapp.presenter.people.PeopleListPresenter;
import com.motondon.moviesearchdemoapp.presenter.people.PeopleListPresenterImpl;
import com.motondon.moviesearchdemoapp.view.adapters.people.PeopleAdapter;
import com.squareup.picasso.Picasso;

public class PeopleListFragment extends Fragment implements PeopleListPresenterImpl.PeopleListView {

    public static final String TAG = PeopleListFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private PeopleAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private PersonList personList;

    // Presenter object to be used when requesting any data from the model (i.e.: server data manipulation)
    private PeopleListPresenter peopleListPresenterImpl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_people_list, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.lvPeople);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.contentView);

        // initialize the adapter
        adapter = new PeopleAdapter(this, getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(adapter);

        // This listener will request newer data from the server (if available) when the user swipe down the list view.
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (personList == null) {
                    swipeRefreshLayout.setRefreshing(false);
                    return;
                }

                if (personList.getPage() >= personList.getTotalPages()) {
                    swipeRefreshLayout.setRefreshing(false);
                    return;
                }

                // POJO returned by the server contains the number of the actual page, so we just get it and increment by one
                int nextPage = personList.getPage() + 1;

                fetchPeople(nextPage);
            }
        });

        // Init Movie List Presenter
        peopleListPresenterImpl = new PeopleListPresenterImpl(this);

        fetchPeople(1);

        return root;
    }


    /**
     * This is the method which will request server for person list.
     *
     * @param page
     */
    public void fetchPeople(int page) {
        Log.d(TAG, "fetchPeople() - Requesting person data to the server...");

        peopleListPresenterImpl.fetchPeople(page);
    }

    /**
     * When server returns some data, this method will be called asynchronously. Then we set this data to the adapter in order
     * for the data to be visible.
     *
     * @param personList
     */
    @Override
    public void displayPeople(PersonList personList) {
        Log.d(TAG, "displayPeople() - Received person data from the server. Number of received items: " + personList.getPersonList().size() + ".");

        this.personList = personList;
        adapter.setPeople(personList.getPersonList());
    }

    @Override
    public void showProgressDialog() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgressDialog() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void requestFailure(String errorMessage) {
        Log.e(TAG, "requestFailure() - Fail when trying to fetch some server data: " + errorMessage);
    }

    @Override
    public void loadImage(Context context, String coverUrl, String imageId, final ImageView ivCover) {
        Log.d(TAG, "loadImage() - coverUrl: " + coverUrl + " - imageId: " + imageId);

        Picasso.with(context).load(Uri.parse(coverUrl)).placeholder(R.drawable.ic_movie_placeholder).error(R.drawable.ic_no_cover).into(ivCover);
    }
}
