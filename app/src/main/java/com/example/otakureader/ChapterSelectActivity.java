package com.example.otakureader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.otakureader.api.RetrofitBuilder;
import com.example.otakureader.api.pojo.MangaDetailPOJO;
import com.example.otakureader.tools.Chapter;
import com.example.otakureader.tools.adapters.ChapterAdapter;
import com.github.chrisbanes.photoview.PhotoView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.otakureader.FullscreenView.CHAPTER_ID;

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

                        ListView lv = findViewById(R.id.chapListView);
                        lv.setVisibility(View.VISIBLE);

                        lv.setOnItemClickListener((adapterView, view, position, l) -> {
                            if(position==0)
                                return;
                            final Intent intent = new Intent(ChapterSelectActivity.this, FullscreenView.class);
                            intent.putExtra(CHAPTER_ID, chapters.get(position-1).getId());
                            startActivity(intent);
                        });

                        View v = getLayoutInflater().inflate(R.layout.chapter_detail_header, null, false);
                        final PhotoView imageView = (PhotoView) v.findViewById(R.id.chapImage);
                        imageView.setZoomable(false);
                        imageView.setClickable(false);
                        Glide.with(ChapterSelectActivity.this).load(imageUrl).into(imageView);

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
}
