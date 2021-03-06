package com.example.otakureader.api.interfaces;

import com.example.otakureader.api.pojo.ChapterPagesPOJO;
import com.example.otakureader.api.pojo.MangaDetailPOJO;
import com.example.otakureader.api.pojo.MangaListPOJO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IMangaEdenApi {

    @GET("manga/{MANGA_ID}")
    Call<MangaDetailPOJO> getManga(@Path("MANGA_ID") String mangaId);

    @GET("chapter/{chapterId}")
    Call<ChapterPagesPOJO> getChapter(@Path("chapterId") String chapterId);

    @GET("list/0/")
    Call<MangaListPOJO> getMangaList(@Query("p") int page, @Query("l") int length);

    @GET("list/0")
    Call<MangaListPOJO> getAllMangaList();

}
