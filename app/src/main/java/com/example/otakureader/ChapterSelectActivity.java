package com.example.otakureader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

        RetrofitBuilder.getApi().getManga(mId).enqueue(
                new Callback<MangaDetailPOJO>() {
                    @Override
                    public void onResponse(Call<MangaDetailPOJO> call, Response<MangaDetailPOJO> response) {
                        List<List<String>> chaps = response.body().getChapters();
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
                            final Intent intent = new Intent(ChapterSelectActivity.this, FullscreenView.class);
                            intent.putExtra(CHAPTER_ID, chapters.get(position).getId());
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
}
