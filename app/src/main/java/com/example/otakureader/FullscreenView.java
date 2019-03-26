package com.example.otakureader;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.example.otakureader.R;
import com.example.otakureader.mangaeden.RetrofitBuilder;
import com.example.otakureader.mangaeden.pojo.ChapterPOJO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FullscreenView extends AppCompatActivity {

    public static final String CHAPTER_ID = "Chapter_Id";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_view);

        Intent myIntent = getIntent();
        System.out.println(myIntent.getStringExtra(CHAPTER_ID));
        getExampleChapterPOJORessource();
    }

    private void getExampleChapterPOJORessource() {
        RetrofitBuilder.getApi().getChapter("5bfad4c0719a162df4c7eb5e").enqueue(new Callback<ChapterPOJO>() {
            @Override
            public void onResponse(Call<ChapterPOJO> call, Response<ChapterPOJO> response) {
                final List<String> ChapterPOJOs = new ArrayList<>();
                for (int i = 0; i < response.body().getImages().size(); i++) {
                    ChapterPOJOs.add("https://cdn.mangaeden.com/mangasimg/" + response.body().getImages().get(i).get(1));
                }
                Collections.reverse(ChapterPOJOs);

                final ChapterReaderAdapter mAdapter = new ChapterReaderAdapter(getSupportFragmentManager(), ChapterPOJOs);
                final ViewPager mPager = findViewById(R.id.fullscreen_pager);
                mPager.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<ChapterPOJO> call, Throwable t) {

            }
        });
    }
}
