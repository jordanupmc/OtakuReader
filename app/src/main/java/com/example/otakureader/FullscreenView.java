package com.example.otakureader;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
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
    private List<String> pagesUrl;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_view);
        SeekBar sb = findViewById(R.id.seekBarPages);

        int colorSeekBar = getResources().getColor(R.color.secondaryTextColor);

        sb.getProgressDrawable().setColorFilter(colorSeekBar, PorterDuff.Mode.SRC_IN);
        sb.getThumb().setColorFilter(colorSeekBar, PorterDuff.Mode.SRC_IN);

        Intent myIntent = getIntent();
        if (savedInstanceState == null) {
            getPagesFromChapter(myIntent.getStringExtra(CHAPTER_ID), myIntent.getStringExtra(MANGA_ID), (ArrayList<Chapter>) myIntent.getSerializableExtra(CHAPTER_LIST));
        } else {
            pagesUrl = savedInstanceState.getStringArrayList("imgUrl");
            initView((ArrayList<Chapter>) myIntent.getSerializableExtra(CHAPTER_LIST), myIntent.getStringExtra(CHAPTER_ID), myIntent.getStringExtra(MANGA_ID), true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("imgUrl", (ArrayList<String>) pagesUrl);
    }

    private void getPagesFromChapter(String chapterId, String mangaId, ArrayList<Chapter> chapters) {
        RetrofitBuilder.getMangaEdenApi().getChapter(chapterId).enqueue(new Callback<ChapterPagesPOJO>() {
            @Override
            public void onResponse(Call<ChapterPagesPOJO> call, Response<ChapterPagesPOJO> response) {
                if (!response.isSuccessful()) {
                    onFailure(call, new Exception());
                    return;
                }
                pagesUrl = new ArrayList<>();

                for (int i = 0; response.body().getImages() != null && i < response.body().getImages().size(); i++) {
                    pagesUrl.add(getBaseContext().getResources().getString(R.string.api_image_url) + response.body().getImages().get(i).get(1));
                }
                initView(chapters, chapterId, mangaId, false);

            }

            @Override
            public void onFailure(Call<ChapterPagesPOJO> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error : Cannot load pages", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void initView(ArrayList<Chapter> chapters, String chapterId, String mangaId, boolean saved) {
        final ChapterReaderPagerAdapter mAdapter = new ChapterReaderPagerAdapter(getSupportFragmentManager(), pagesUrl, chapters, chapterId);
        final ViewPager mPager = findViewById(R.id.fullscreen_pager);

        SeekBar sb = findViewById(R.id.seekBarPages);

        TextView tv = findViewById(R.id.numPageTv);
        tv.setText("0/" + (pagesUrl.size() - 1));
        sb.setMax(pagesUrl.size());
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) { //Si l'utilisateur a changer la progression de la seekbar on change d'item, sinon c'est qu'un swipe a eu lieu on ne change donc pas de fragment
                    mPager.setCurrentItem((pagesUrl.size()) - progress, true);
                }
                tv.setText(progress + "/" + pagesUrl.size());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

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
                    if (chapterId.equals(chapters.get(0).getId())) {
                        Toast.makeText(getApplicationContext(), "You have read all chapter ! Have a break !", Toast.LENGTH_SHORT).show();
                    }
                }
                sb.setProgress((pagesUrl.size()) - position);
                sb.refreshDrawableState();
            }
        });
        if (!saved) {
            Toast toast = new Toast(getApplicationContext());
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast_reading_indication,
                    findViewById(R.id.custom_toast_layout_id));
            toast.setView(layout);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }
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
