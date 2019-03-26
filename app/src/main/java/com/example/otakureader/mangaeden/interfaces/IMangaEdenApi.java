package com.example.otakureader.mangaeden.interfaces;

import com.example.otakureader.mangaeden.Chapter;
import com.example.otakureader.mangaeden.Manga;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IMangaEdenApi {

    @GET("manga/{mangaId}")
    Call<Manga> getManga(@Path("mangaId") String mangaId);

    @GET("chapter/{chapterId}")
    Call<Chapter> getChapter(@Path("chapterId") String chapterId);

}
