package com.example.otakureader.api;

import com.example.otakureader.api.interfaces.IMangaEdenApi;
import com.example.otakureader.api.interfaces.IOtakuReaderApi;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {
    private static final String MANGA_EDEN_API_URL = "https://www.mangaeden.com/api/";
    private static final String OTAKU_READER_API_URL = "https://manga-eden-api-manga-db.herokuapp.com/";
    private static IMangaEdenApi mangaEdenApi = null;
    private static IOtakuReaderApi otakuReaderApi = null;
    private static Retrofit mangaReaderApiClient = null;
    private static Retrofit otakuReaderApiClient = null;

    private RetrofitBuilder() {
    }

    public static IMangaEdenApi getMangaEdenApi() {
        if (mangaEdenApi == null) {
            mangaEdenApi = getMangaEdenClient(MANGA_EDEN_API_URL).create(IMangaEdenApi.class);
        }
        return mangaEdenApi;
    }

    private static Retrofit getMangaEdenClient(String baseUrl) {
        if (mangaReaderApiClient == null) {
            mangaReaderApiClient = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mangaReaderApiClient;
    }

    public static IOtakuReaderApi getOtakuReaderApi() {
        if (otakuReaderApi == null) {
            otakuReaderApi = getOtakuReaderClient(OTAKU_READER_API_URL).create(IOtakuReaderApi.class);
        }
        return otakuReaderApi;
    }

    private static Retrofit getOtakuReaderClient(String baseUrl) {
        if (otakuReaderApiClient == null) {
            otakuReaderApiClient = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return otakuReaderApiClient;
    }
}
