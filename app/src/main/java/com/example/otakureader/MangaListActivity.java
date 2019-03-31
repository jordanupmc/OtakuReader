package com.example.otakureader;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.otakureader.api.RetrofitBuilder;
import com.example.otakureader.api.pojo.MangaDetailPOJO;
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

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView =
                (SearchView) searchItem.getActionView() ;
        searchView.setQueryHint("Search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                RetrofitBuilder.getOtakuReaderApi().searchManga(query).enqueue(
                        new Callback<MangaPOJO>() {
                            @Override
                            public void onResponse(Call<MangaPOJO> call, Response<MangaPOJO> response) {
                                String searchId = response.body().getId();

                                if(searchId != null) {
                                    final Intent intent = new Intent(MangaListActivity.this, ChapterSelectActivity.class);
                                    intent.putExtra(MANGA_ID, searchId);
                                    startActivity(intent);
                                }else{
                                    searchView.clearFocus();
                                    searchItem.collapseActionView();
                                    Toast.makeText(MangaListActivity.this, "No Results Found", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<MangaPOJO> call, Throwable t) {
                                Log.e("MangaListActivity", "API CALL SEARCH ERROR");
                            }
                        });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

}
