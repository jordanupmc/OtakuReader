package com.example.otakureader.mangaeden;

import com.example.otakureader.mangaeden.interfaces.IMangaEdenApi;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {

    private static final String API_URL = "https://www.mangaeden.com/api/";
    private static IMangaEdenApi mApi = null;
    private static Retrofit mApiClient = null;

    private RetrofitBuilder() {
    }

    public static IMangaEdenApi getApi() {
        if (mApi == null) {
            mApi = getClient(API_URL).create(IMangaEdenApi.class);
        }
        return mApi;
    }

    private static Retrofit getClient(String baseUrl) {
        if (mApiClient == null) {
            mApiClient = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mApiClient;
    }
}
