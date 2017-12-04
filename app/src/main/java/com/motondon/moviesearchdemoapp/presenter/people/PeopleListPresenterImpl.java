package com.motondon.moviesearchdemoapp.presenter.people;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.motondon.moviesearchdemoapp.businesslogic.interactor.person.FetchPersonInteractor;
import com.motondon.moviesearchdemoapp.businesslogic.interactor.person.FetchPersonInteractorImpl;
import com.motondon.moviesearchdemoapp.model.data.people.PersonList;

public class PeopleListPresenterImpl implements PeopleListPresenter {
    private static final String TAG = PeopleListPresenterImpl.class.getSimpleName();

    public interface PeopleListView {

        void displayPeople(PersonList personList);
        void showProgressDialog();
        void hideProgressDialog();
        void requestFailure(String errorMessage);
        void loadImage(Context context, String coverUrl, String imageId, ImageView ivCover);
    }

    private PeopleListView mView;
    private FetchPersonInteractor fetchPersonInteractor;

    public PeopleListPresenterImpl(PeopleListView view) {
        this.mView = view;

        // Create business logic object to interact with.
        fetchPersonInteractor = new FetchPersonInteractorImpl();
    }

    /**
     * Called by the view layer. It will request Person data to the server. When data is received (asynchronously)
     * it will call a view method which will be responsible to manipulate it.
     *
     * @param page
     */
    @Override
    public void fetchPeople(int page) {
        Log.d(TAG, "fetchPeople() - Request to the server a list of people. Page " + page);

        fetchPersonInteractor.execute(page, new FetchPersonInteractor.Callback() {
            @Override
            public void onFetchPersonSuccessful(PersonList personList) {
                mView.hideProgressDialog();
                mView.displayPeople(personList);
            }

            @Override
            public void onFetchPersonFailure(String errorMessage) {
                mView.hideProgressDialog();
                mView.requestFailure(errorMessage);
            }
        });
    }
}
