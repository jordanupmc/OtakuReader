package com.example.otakureader.api.interfaces;

import com.example.otakureader.api.pojo.MangaListPOJO;
import com.example.otakureader.api.pojo.MangaPOJO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOtakuReaderApi {

    @GET("getTrendingList")
    Call<MangaListPOJO> getTrendingList(@Query("limite") Integer limite);

    @GET("searchManga")
    Call<MangaPOJO> searchManga(@Query("title") String title);

//    @GET("searchMangaFilter")
//    Call<MangaListPOJO> searchMangaFilter(@Query("title") String title);

}
