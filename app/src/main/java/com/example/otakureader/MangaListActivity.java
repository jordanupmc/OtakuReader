package com.example.otakureader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.otakureader.mangaeden.RetrofitBuilder;
import com.example.otakureader.mangaeden.pojo.MangaDetailPOJO;
import com.example.otakureader.mangaeden.pojo.MangaListPOJO;
import com.example.otakureader.mangaeden.pojo.MangaPOJO;
import com.example.otakureader.tools.Chapter;
import com.example.otakureader.tools.adapters.ChapterAdapter;
import com.example.otakureader.tools.adapters.MangaGridAdapter;
import com.github.chrisbanes.photoview.PhotoView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static androidx.core.content.ContextCompat.startActivity;
import static com.example.otakureader.ChapterSelectActivity.MANGA_ID;
import static com.example.otakureader.FullscreenView.CHAPTER_ID;

public class MangaListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<MangaPOJO> mangas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_list);


        RetrofitBuilder.getApi().getMangaList(1, 25).enqueue(
                new Callback<MangaListPOJO>() {
                    @Override
                    public void onResponse(Call<MangaListPOJO> call, Response<MangaListPOJO> response) {
                        System.out.println(">>>>"+response.body().getMangas());
                        mangas = response.body().getMangas();
                        System.out.println(">>>>"+response.body().getTotal());

                        recyclerView = findViewById(R.id.mangaGrid);

                        recyclerView.setHasFixedSize(true);

                        layoutManager = new GridLayoutManager(MangaListActivity.this, 2);
                        recyclerView.setLayoutManager(layoutManager);

                        mAdapter = new MangaGridAdapter(mangas, new MangaGridAdapter.OnItemGridClickListener() {
                            @Override public void onItemClick(MangaPOJO item) {
                                final Intent intent = new Intent(MangaListActivity.this, ChapterSelectActivity.class);
                                intent.putExtra(MANGA_ID, item.getId());
                                startActivity(intent);
                            }
                        });

                        recyclerView.setAdapter(mAdapter);


                    }

                    @Override
                    public void onFailure(Call<MangaListPOJO> call, Throwable t) {
                        Log.e("MangaListActivity", "API CALL CHAPTER ERROR");
                    }
                });
    }

}
