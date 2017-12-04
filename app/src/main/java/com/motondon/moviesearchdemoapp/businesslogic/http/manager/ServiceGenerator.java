package com.motondon.moviesearchdemoapp.businesslogic.http.manager;

import com.motondon.moviesearchdemoapp.config.AppConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This class was created based on the following article:
 * http://coders-mill.com/retrofit-in-android-app/
 *
 * This separates protocol/library specifics (in this case Retrofit issues) from view
 *
 */
public class ServiceGenerator {

    private static ServiceGenerator instance;

    private Retrofit retrofit;

    private ServiceGenerator() {

        // See link below for Retrofit2 log details:
        // https://futurestud.io/blog/retrofit-2-log-requests-and-responses
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.API_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ServiceGenerator getInstance() {
        if (instance == null) {
            instance = new ServiceGenerator();
        }

        return instance;
    }

    /**
     * Creates a Retrofit service based on the serviceClass parameter
     *
     * @param serviceClass
     * @param <T>
     * @return
     */
    public <T> T createService(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }

    /**
     * Used by the consumer (i.e. Activities, etc) in order to hidden creation details.
     *
     * @param type
     * @return
     */
    public RetrofitApi getApiOfType(ApiTypes type) {
        return type.getApiType();
    }
}
