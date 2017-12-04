package com.motondon.moviesearchdemoapp.businesslogic.http.api.people;

import android.util.Log;

import com.motondon.moviesearchdemoapp.businesslogic.http.manager.RetrofitApi;
import com.motondon.moviesearchdemoapp.businesslogic.http.manager.ServiceGenerator;
import com.motondon.moviesearchdemoapp.config.AppConfig;
import com.motondon.moviesearchdemoapp.model.data.people.PersonList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * This class is intended to hide Retrofit specific implementation and exposes some methods to the presenter layer.
 *
 * It also defines a callback interface (SearchPersonCallback interface) which must be implemented by the callers (i.e.: presenter
 * layer). Then, when a Retrofit callback is received, it will be converted to a SearchPersonCallback respective method and passed back to
 * the presenter layer, hiding Retrofit implementation.
 *
 * It uses PersonService interface which defines the TMDB REST API methods.
 *
 */
public class PersonApi extends RetrofitApi {

    private static final String TAG = PersonApi.class.getSimpleName();

    /**
     * Callback used when fetching a person (by fetchPerson() method)
     */
    public interface FetchPersonCallback {
        void onFetchPersonSuccessful(PersonList personLis);
        void onFetchPersonFailure(String errorMessage);
    }

    /**
     * Callback used when searching a person (by searchPerson() method)
     */
    public interface SearchPersonCallback {
        void onSearchPersonSuccessful(PersonList personList);
        void onSearchPersonFailure(String errorMessage);
    }

    private PersonService personService;

    public PersonApi() {
        personService = ServiceGenerator.getInstance().createService(PersonService.class);
    }

    /**
     * Request to the server all people (actors) which name matches with the name typed by the user. A response (either a successful or
     * an error) will be received asynchronously
     *
     * @param query
     * @param page
     * @param callback
     */
    public void searchPerson(final String query, int page, final SearchPersonCallback callback) {

        try {
            Log.d(TAG, "searchPerson() - Searching for a person. Query: " + query);

            personService.searchPerson(AppConfig.TMDB_API_KEY, query, page, false).enqueue(new Callback<PersonList>() {
                @Override
                public void onResponse(Call<PersonList> call, Response<PersonList> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "searchPerson::onResponse() - Successful response when searching for a person. Query: " + query);
                        callback.onSearchPersonSuccessful(response.body());

                    } else {
                        Log.d(TAG, "searchPerson::onResponse() - Response not successful when searching for a person. Query: " + query + ". Response code: " + response.code());
                        callback.onSearchPersonFailure("Response not successful: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<PersonList> call, Throwable t) {
                    Log.d(TAG, "searchPerson::onFailure() - Fail when searching for a person. Query: " + query + ". Message: " + t.getMessage());
                    callback.onSearchPersonFailure("Fail when searching for a person. Query: " + query + ". Message: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            Log.d(TAG, "searchPerson() - Exception when searching for a person. Query: " + query + ". Message: " + e.getMessage());
            callback.onSearchPersonFailure("Exception wwhen searching for a person. Query: " + query + ". Message: " + e.getMessage());
        }
    }

    /**
     * Request to the server information about a person (i.e.: an actor or an actress). A response (either a successful or an error)
     * will be received asynchronously.
     *
     * @param page
     * @param callback
     */
    public void fetchPerson(final int page, final FetchPersonCallback callback) {

        try {
            Log.d(TAG, "fetchPerson() - Request to the server a person list. Page: " + page);

            personService.fetchPeople(AppConfig.TMDB_API_KEY, page).enqueue(new Callback<PersonList>() {
                @Override
                public void onResponse(Call<PersonList> call, Response<PersonList> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "fetchPerson::onResponse() - Successful response when requesting to the server a person list. Page: " + page);
                        callback.onFetchPersonSuccessful(response.body());

                    } else {
                        Log.d(TAG, "fetchPerson::onResponse() - Response not successful when requesting to the server a person list. Page: " + page + ". Response code: " + response.code());
                        callback.onFetchPersonFailure("Response not successful: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<PersonList> call, Throwable t) {
                    Log.d(TAG, "fetchPerson::onFailure() - Fail when requesting to the server a person list. Page: " + page + ". Message: " + t.getMessage());
                    callback.onFetchPersonFailure("Fail when requesting to the server a person list. Page: " + page + ". Message: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            Log.d(TAG, "fetchPerson() - Exception when requesting to the server a person list. Page: " + page + ". Message: " + e.getMessage());
            callback.onFetchPersonFailure("Exception when requesting to the server a person list. Page: " + page + ". Message: " + e.getMessage());
        }
    }
}
