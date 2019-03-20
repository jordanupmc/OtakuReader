package com.example.otakureader;

import android.os.Bundle;
import android.view.Window;

import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class FullscreenView extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_view);
        final ChapterReaderAdapter mAdapter = new ChapterReaderAdapter(getSupportFragmentManager(), getExampleChapterRessource());

        final ViewPager mPager = findViewById(R.id.fullscreen_pager);
        mPager.setAdapter(mAdapter);
    }

    private static List<String> getExampleChapterRessource() {
        return Arrays.asList(
                "https://cdn.mangaeden.com/mangasimg/a1/a1b3bd00f7b68e96ecade3b93618babf9c43eecfc6f19c9561bc0e95.jpg",
                "https://cdn.mangaeden.com/mangasimg/a1/a1b3bd00f7b68e96ecade3b93618babf9c43eecfc6f19c9561bc0e95.jpg",
                "https://cdn.mangaeden.com/mangasimg/a1/a1b3bd00f7b68e96ecade3b93618babf9c43eecfc6f19c9561bc0e95.jpg",
                "https://cdn.mangaeden.com/mangasimg/a1/a1b3bd00f7b68e96ecade3b93618babf9c43eecfc6f19c9561bc0e95.jpg",
                "https://cdn.mangaeden.com/mangasimg/a1/a1b3bd00f7b68e96ecade3b93618babf9c43eecfc6f19c9561bc0e95.jpg",
                ""
        );
    }
}
