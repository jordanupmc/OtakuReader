package com.example.otakureader;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import java.util.Arrays;
import java.util.List;

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

    private static List<Integer> getExampleChapterRessource() {
        return Arrays.asList(
                R.drawable.o00,
                R.drawable.o01,
                R.drawable.o02,
                R.drawable.o04,
                R.drawable.o05,
                R.drawable.o06,
                R.drawable.o07,
                R.drawable.o08,
                R.drawable.o09,
                R.drawable.o10,
                R.drawable.o11,
                R.drawable.o12,
                R.drawable.o13,
                R.drawable.o14,
                R.drawable.o15,
                R.drawable.o16,
                R.drawable.o17
        );
    }
}
