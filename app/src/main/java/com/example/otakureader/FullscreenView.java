package com.example.otakureader;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.otakureader.mangaeden.RetrofitBuilder;
import com.example.otakureader.mangaeden.pojo.ChapterPagesPOJO;
import com.example.otakureader.tools.Chapter;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FullscreenView extends AppCompatActivity {

    public static final String CHAPTER_ID = "Chapter_Id";
    public static final String CHAPTER_LIST = "Chapter_List";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_view);

        Intent myIntent = getIntent();

        getPagesFromChapter(myIntent.getStringExtra(CHAPTER_ID), (ArrayList<Chapter>) myIntent.getSerializableExtra(CHAPTER_LIST));
    }

    private void getPagesFromChapter(String chapterId, ArrayList<Chapter> chapters) {
        RetrofitBuilder.getApi().getChapter(chapterId).enqueue(new Callback<ChapterPagesPOJO>() {
            @Override
            public void onResponse(Call<ChapterPagesPOJO> call, Response<ChapterPagesPOJO> response) {
                if (!response.isSuccessful()) {
                    onFailure(call, new Exception());
                    return;
                }
                final List<String> pagesUrl = new ArrayList<>();

                for (int i = 0; response.body().getImages() != null && i < response.body().getImages().size(); i++) {
                    pagesUrl.add(getBaseContext().getResources().getString(R.string.api_image_url) + response.body().getImages().get(i).get(1));
                }
                final ChapterReaderPagerAdapter mAdapter = new ChapterReaderPagerAdapter(getSupportFragmentManager(), pagesUrl, chapters, chapterId);
                final ViewPager mPager = findViewById(R.id.fullscreen_pager);
                mPager.setAdapter(mAdapter);
                mPager.setCurrentItem(pagesUrl.size());
            }

            @Override
            public void onFailure(Call<ChapterPagesPOJO> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error : Cannot load pages", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
