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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestOptions;
import com.example.otakureader.api.RetrofitBuilder;
import com.example.otakureader.api.pojo.MangaDetailPOJO;
import com.example.otakureader.database.AppDatabase;
import com.example.otakureader.database.Manga;
import com.example.otakureader.database.dao.ChapterDao;
import com.example.otakureader.database.dao.MangaDao;
import com.example.otakureader.tools.Chapter;
import com.example.otakureader.tools.adapters.ChapterAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
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

        RetrofitBuilder.getMangaEdenApi().getManga(mId).enqueue(
                new Callback<MangaDetailPOJO>() {
                    @Override
                    public void onResponse(Call<MangaDetailPOJO> call, Response<MangaDetailPOJO> response) {
                        List<List<String>> chaps = response.body().getChapters();
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

                        final ArrayAdapter<Chapter> adapter = new ChapterAdapter(
                                ChapterSelectActivity.this,
                                R.layout.content_chapter,
                                chapters);

                        ProgressBar pb = findViewById(R.id.chapProgressBar);
                        pb.setVisibility(View.GONE);

                        ListView lv = findViewById(R.id.chapListView);
                        lv.setVisibility(View.VISIBLE);

                        lv.setOnItemClickListener((adapterView, view, position, l) -> {
                            if (position == 0) {
                                return;
                            }
                            new AddChapterAsyncTask(
                                    AppDatabase.getAppDatabase(getApplicationContext()).chapterDao(),
                                    new com.example.otakureader.database.Chapter(chapters.get(position - 1).getId(), mId, false)).execute();

                            final Intent intent = new Intent(ChapterSelectActivity.this, FullscreenView.class);
                            intent.putExtra(CHAPTER_ID, chapters.get(position - 1).getId());
                            intent.putExtra(MANGA_ID, mId);
                            intent.putExtra(CHAPTER_LIST, (ArrayList<Chapter>) chapters);
                            startActivity(intent);
                        });

                        View v = getLayoutInflater().inflate(R.layout.chapter_detail_header, null, false);
                        final ImageView imageView = v.findViewById(R.id.chapImage);
                        imageView.setClickable(false);

                        String imageUrl = getString(R.string.api_image_url) + response.body().getImage();
                        Glide.with(ChapterSelectActivity.this).load(imageUrl).placeholder(R.drawable.default_image).into(imageView);

                        ImageView saveBtn = v.findViewById(R.id.addMangaBtn);
                        ImageView removeBtn = v.findViewById(R.id.removeMangaBtn);

                        new MangaPresentAsyncTask(AppDatabase.getAppDatabase(getApplicationContext()).mangaDao(), mId, saveBtn, removeBtn).execute();

                        TextView title = v.findViewById(R.id.chapMangaTitle);
                        title.setText(response.body().getTitle());

                        TextView author = v.findViewById(R.id.chapMangaAuthor);
                        author.setText(response.body().getAuthor());

                        TextView desc = v.findViewById(R.id.chapMangaDesc);
                        desc.setText(response.body().getDescription());

                        ImageView expandButt = v.findViewById(R.id.descExpand);
                        ImageView collapseButt = v.findViewById(R.id.descCollapse);

                        expandButt.setOnClickListener(arg0 -> {
                            desc.setSingleLine(false);
                            collapseButt.setVisibility(View.VISIBLE);
                            expandButt.setVisibility(View.GONE);
                        });

                        collapseButt.setOnClickListener(arg0 -> {
                            desc.setLines(3);
                            expandButt.setVisibility(View.VISIBLE);
                            collapseButt.setVisibility(View.GONE);
                        });

                        saveBtn.setOnClickListener(l -> {
                            new SaveMangaAsyncTask(AppDatabase.getAppDatabase(getApplicationContext()).mangaDao(), getApplicationContext(), saveBtn, removeBtn,
                                    Manga.convert(response.body(), mId)).execute();
                        });
                        removeBtn.setOnClickListener(l -> {
                            new DeleteMangaAsyncTask(AppDatabase.getAppDatabase(getApplicationContext()).mangaDao(), getApplicationContext(), saveBtn, removeBtn,
                                    Manga.convert(response.body(), mId)).execute();
                        });

                        lv.addHeaderView(v);

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
        private final ImageView saveBtn;
        private final ImageView removeBtn;

        public SaveMangaAsyncTask(MangaDao mDao, Context context, ImageView saveBtn, ImageView removeBtn, Manga manga) {
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
        private final ImageView saveBtn;
        private final ImageView removeBtn;

        public DeleteMangaAsyncTask(MangaDao mDao, Context context, ImageView saveBtn, ImageView removeBtn, Manga manga) {
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
        private final ImageView saveBtn;
        private final ImageView removeBtn;

        public MangaPresentAsyncTask(MangaDao mDao, String id, ImageView saveBtn, ImageView removeBtn) {
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
