package com.example.otakureader;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.otakureader.database.AppDatabase;
import com.example.otakureader.database.Manga;
import com.example.otakureader.database.dao.ChapterDao;
import com.example.otakureader.database.dao.MangaDao;
import com.example.otakureader.mangaeden.RetrofitBuilder;
import com.example.otakureader.mangaeden.pojo.MangaDetailPOJO;
import com.example.otakureader.tools.Chapter;
import com.example.otakureader.tools.adapters.ChapterAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.otakureader.FullscreenView.CHAPTER_ID;
import static com.example.otakureader.FullscreenView.CHAPTER_LIST;

public class ChapterSelectActivity extends AppCompatActivity {

    public final static String MANGA_ID = "MANGA_ID";

    private List<Chapter> chapters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_select);
        chapters = new ArrayList<>();
        Intent myIntent = getIntent();
        String mId = myIntent.getStringExtra(MANGA_ID);

        Button saveBtn = findViewById(R.id.addMangaBtn);
        Button removeBtn = findViewById(R.id.removeMangaBtn);
        new MangaPresentAsyncTask(AppDatabase.getAppDatabase(getApplicationContext()).mangaDao(), mId, saveBtn, removeBtn).execute();

        RetrofitBuilder.getApi().getManga(mId).enqueue(
                new Callback<MangaDetailPOJO>() {
                    @Override
                    public void onResponse(Call<MangaDetailPOJO> call, Response<MangaDetailPOJO> response) {
                        List<List<String>> chaps = response.body().getChapters();
                        saveBtn.setOnClickListener(l -> {
                            new SaveMangaAsyncTask(AppDatabase.getAppDatabase(getApplicationContext()).mangaDao(), getApplicationContext(), saveBtn, removeBtn, Manga.convert(response.body(), mId)).execute();
                        });
                        removeBtn.setOnClickListener(l -> {
                            new DeleteMangaAsyncTask(AppDatabase.getAppDatabase(getApplicationContext()).mangaDao(), getApplicationContext(), saveBtn, removeBtn, Manga.convert(response.body(), mId)).execute();
                        });

                        new GetChapterAsyncTask(AppDatabase.getAppDatabase(getApplicationContext()).chapterDao(), mId).execute();

                        for (int i = 0; i < chaps.size(); i++) {
                            String chapNb = chaps.get(i).get(0);

                            String tmpDate = chaps.get(i).get(1);
                            tmpDate = tmpDate.substring(0, tmpDate.length() - 2) + "000";

                            Date date = new Date(Long.parseLong(tmpDate));
                            SimpleDateFormat sdf;
                            sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
                            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

                            String chapDate = sdf.format(date);
                            String chapTitle = chaps.get(i).get(2);
                            String chapId = chaps.get(i).get(3);
                            chapters.add(new Chapter(chapNb, chapDate, chapTitle, chapId));
                        }
                        String imageUrl = getString(R.string.api_image_url) + response.body().getImage();

                        final ArrayAdapter<Chapter> adapter = new ChapterAdapter(
                                ChapterSelectActivity.this,
                                R.layout.content_chapter,
                                chapters);

                        ProgressBar pb = findViewById(R.id.chapProgressBar);
                        pb.setVisibility(View.GONE);

                        final ImageView imageView = findViewById(R.id.chapImage);
                        imageView.setVisibility(View.VISIBLE);
                        Glide.with(ChapterSelectActivity.this).load(imageUrl).into(imageView);

                        ListView lv = findViewById(R.id.chapListView);
                        lv.setVisibility(View.VISIBLE);

                        lv.setOnItemClickListener((adapterView, view, position, l) -> {
                            new AddChapterAsyncTask(
                                    AppDatabase.getAppDatabase(getApplicationContext()).chapterDao(),
                                    new com.example.otakureader.database.Chapter(chapters.get(position).getId(), mId, false)).execute();

                            final Intent intent = new Intent(ChapterSelectActivity.this, FullscreenView.class);
                            intent.putExtra(CHAPTER_ID, chapters.get(position).getId());
                            intent.putExtra(MANGA_ID, mId);
                            intent.putExtra(CHAPTER_LIST, (ArrayList<Chapter>) chapters);
                            startActivity(intent);
                        });

                        lv.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<MangaDetailPOJO> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "API CALL CHAPTER ERROR", Toast.LENGTH_LONG).show();
                        Log.e("FullScreenView", "API CALL CHAPTER ERROR");
                    }
                });
    }

    private static class SaveMangaAsyncTask extends AsyncTask<Void, Void, Void> {
        private final MangaDao mDao;
        private final Context context;
        private final Manga manga;
        private final Button saveBtn;
        private final Button removeBtn;

        public SaveMangaAsyncTask(MangaDao mDao, Context context, Button saveBtn, Button removeBtn, Manga manga) {
            this.mDao = mDao;
            this.context = context;
            this.manga = manga;
            this.saveBtn = saveBtn;
            this.removeBtn = removeBtn;
        }

        @Override
        protected void onPostExecute(Void voids) {
            Toast.makeText(context, "Successfully save to collection !", Toast.LENGTH_SHORT).show();
            saveBtn.setVisibility(View.GONE);
            removeBtn.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mDao.insert(manga);
            return null;
        }
    }

    private static class AddChapterAsyncTask extends AsyncTask<Void, Void, Void> {
        private final ChapterDao mDao;
        private final com.example.otakureader.database.Chapter chapter;

        public AddChapterAsyncTask(ChapterDao mDao, com.example.otakureader.database.Chapter chapter) {
            this.mDao = mDao;
            this.chapter = chapter;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            mDao.insert(chapter);
            return null;
        }
    }

    private static class GetChapterAsyncTask extends AsyncTask<Void, Void, List<com.example.otakureader.database.Chapter>> {
        private final ChapterDao mDao;
        private final String mangaId;

        public GetChapterAsyncTask(ChapterDao mDao, String mId) {
            this.mDao = mDao;
            this.mangaId = mId;
        }

        @Override
        protected void onPostExecute(List<com.example.otakureader.database.Chapter> chapters) {
            if (chapters == null) {
                return;
            }
            for (com.example.otakureader.database.Chapter c : chapters) {
                Log.d("GET CHAPTER DEBUG", c.id + " " + c.manga + " " + c.readingComplete);
            }
        }

        @Override
        protected List<com.example.otakureader.database.Chapter> doInBackground(Void... voids) {
            return mDao.getChapterFromManga(mangaId);
        }
    }

    private static class DeleteMangaAsyncTask extends AsyncTask<Void, Void, Void> {
        private final MangaDao mDao;
        private final Context context;
        private final Manga manga;
        private final Button saveBtn;
        private final Button removeBtn;

        public DeleteMangaAsyncTask(MangaDao mDao, Context context, Button saveBtn, Button removeBtn, Manga manga) {
            this.mDao = mDao;
            this.context = context;
            this.manga = manga;
            this.saveBtn = saveBtn;
            this.removeBtn = removeBtn;
        }

        @Override
        protected void onPostExecute(Void voids) {
            Toast.makeText(context, "Successfully delete from collection !", Toast.LENGTH_SHORT).show();
            saveBtn.setVisibility(View.VISIBLE);
            removeBtn.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mDao.delete(manga);
            return null;
        }
    }


    private static class MangaPresentAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private final MangaDao mDao;
        private final String id;
        private final Button saveBtn;
        private final Button removeBtn;

        public MangaPresentAsyncTask(MangaDao mDao, String id, Button saveBtn, Button removeBtn) {
            this.mDao = mDao;
            this.id = id;
            this.saveBtn = saveBtn;
            this.removeBtn = removeBtn;
        }

        @Override
        protected void onPostExecute(Boolean voids) {
            if (voids) {
                saveBtn.setVisibility(View.GONE);
                removeBtn.setVisibility(View.VISIBLE);
            } else {
                saveBtn.setVisibility(View.VISIBLE);
                removeBtn.setVisibility(View.GONE);
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Manga allByIds = mDao.loadAllByIds(id);
            return allByIds != null;
        }
    }


}
