package com.example.otakureader;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.example.otakureader.mangaeden.Chapter;
import com.example.otakureader.mangaeden.RetrofitBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FullscreenView extends AppCompatActivity {

    public static final String CHAPTER_ID = "chapterId";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_view);

        Intent myIntent = getIntent();
        System.out.println(myIntent.getStringExtra(CHAPTER_ID));
        getExampleChapterRessource();
    }

    private void getExampleChapterRessource() {
        RetrofitBuilder.getApi().getChapter("5bfad4c0719a162df4c7eb5e").enqueue(new Callback<Chapter>() {
            @Override
            public void onResponse(Call<Chapter> call, Response<Chapter> response) {
                final List<String> chapters = new ArrayList<>();
                for (int i = 0; i < response.body().getImages().size(); i++) {
                    chapters.add("https://cdn.mangaeden.com/mangasimg/" + response.body().getImages().get(i).get(1));
                }
                Collections.reverse(chapters);

                final ChapterReaderAdapter mAdapter = new ChapterReaderAdapter(getSupportFragmentManager(), chapters);
                final ViewPager mPager = findViewById(R.id.fullscreen_pager);
                mPager.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<Chapter> call, Throwable t) {

            }
        });
    }
}
