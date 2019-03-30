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
import android.widget.ProgressBar;

import com.example.otakureader.api.RetrofitBuilder;
import com.example.otakureader.api.pojo.MangaListPOJO;
import com.example.otakureader.api.pojo.MangaPOJO;
import com.example.otakureader.tools.adapters.MangaGridAdapter;

import java.util.List;

import static com.example.otakureader.ChapterSelectActivity.MANGA_ID;

public class MangaListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<MangaPOJO> mangas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_list);


        RetrofitBuilder.getOtakuReaderApi().getTrendingList(null).enqueue(
                new Callback<MangaListPOJO>() {
                    @Override
                    public void onResponse(Call<MangaListPOJO> call, Response<MangaListPOJO> response) {
                        mangas = response.body().getMangas();

                        recyclerView = findViewById(R.id.mangaGrid);
                        recyclerView.setHasFixedSize(true);
                        layoutManager = new GridLayoutManager(MangaListActivity.this, 2);
                        recyclerView.setLayoutManager(layoutManager);

                        mAdapter = new MangaGridAdapter(mangas, item -> {
                            final Intent intent = new Intent(MangaListActivity.this, ChapterSelectActivity.class);
                            intent.putExtra(MANGA_ID, item.getId());
                            startActivity(intent);
                        });

                        recyclerView.setAdapter(mAdapter);

                        ProgressBar pb = findViewById(R.id.mangaListProgressBar);
                        pb.setVisibility(View.GONE);

                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<MangaListPOJO> call, Throwable t) {
                        Log.e("MangaListActivity", "API CALL LIST ERROR");
                    }
                });
    }

}
