package com.example.otakureader;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.example.otakureader.api.RetrofitBuilder;
import com.example.otakureader.api.pojo.ChapterPagesPOJO;
import com.example.otakureader.database.AppDatabase;
import com.example.otakureader.database.dao.ChapterDao;
import com.example.otakureader.tools.Chapter;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.otakureader.ChapterSelectActivity.MANGA_ID;

public class FullscreenView extends AppCompatActivity {

    public static final String CHAPTER_ID = "Chapter_Id";
    public static final String CHAPTER_LIST = "Chapter_List";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_view);

        Intent myIntent = getIntent();

        getPagesFromChapter(myIntent.getStringExtra(CHAPTER_ID), myIntent.getStringExtra(MANGA_ID), (ArrayList<Chapter>) myIntent.getSerializableExtra(CHAPTER_LIST));
    }

    private void getPagesFromChapter(String chapterId, String mangaId, ArrayList<Chapter> chapters) {
        RetrofitBuilder.getMangaEdenApi().getChapter(chapterId).enqueue(new Callback<ChapterPagesPOJO>() {
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
                mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }

                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        if (position == 0) {
                            new UpdateChapterAsyncTask(AppDatabase.getAppDatabase(getApplicationContext()).chapterDao(),
                                    new com.example.otakureader.database.Chapter(chapterId, mangaId, true)).execute();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<ChapterPagesPOJO> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error : Cannot load pages", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private static class UpdateChapterAsyncTask extends AsyncTask<Void, Void, Void> {
        private final ChapterDao mDao;
        private final com.example.otakureader.database.Chapter chapter;

        public UpdateChapterAsyncTask(ChapterDao mDao, com.example.otakureader.database.Chapter chapter) {
            this.mDao = mDao;
            this.chapter = chapter;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mDao.update(chapter);
            return null;
        }
    }


}
