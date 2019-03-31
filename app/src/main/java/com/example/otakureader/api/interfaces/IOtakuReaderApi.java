package com.example.otakureader.api.interfaces;

import com.example.otakureader.api.pojo.ChapterPagesPOJO;
import com.example.otakureader.api.pojo.MangaDetailPOJO;
import com.example.otakureader.api.pojo.MangaListPOJO;
import com.example.otakureader.api.pojo.MangaPOJO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IOtakuReaderApi {

    @GET("getTrendingList")
    Call<MangaListPOJO> getTrendingList(@Query("limite") Integer limite);

    @GET("searchManga")
    Call<MangaPOJO> searchManga(@Query("title") String title);

}
