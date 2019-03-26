package com.example.otakureader.mangaeden.interfaces;

import com.example.otakureader.mangaeden.pojo.ChapterPOJO;
import com.example.otakureader.mangaeden.pojo.MangaPOJO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IMangaEdenApi {

    @GET("manga/{mangaId}")
    Call<MangaPOJO> getManga(@Path("mangaId") String mangaId);

    @GET("chapter/{chapterId}")
    Call<ChapterPOJO> getChapter(@Path("chapterId") String chapterId);

}
