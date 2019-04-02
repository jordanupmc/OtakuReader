package com.example.otakureader;

import android.content.Intent;
import android.os.Bundle;

import com.example.otakureader.api.RetrofitBuilder;
import com.example.otakureader.api.pojo.MangaListPOJO;
import com.example.otakureader.api.pojo.MangaPOJO;
import com.example.otakureader.tools.adapters.MangaGridAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import static com.example.otakureader.ChapterSelectActivity.MANGA_ID;

public class SearchFilterFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<MangaPOJO> mangas;

    String search;

    static SearchFilterFragment newInstance(final String search) {
        final SearchFilterFragment f = new SearchFilterFragment();
        final Bundle args = new Bundle();
        args.putString("search", search);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        search = getArguments() != null ? getArguments().getString("search") : "";
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.activity_manga_list, container, false);

        RetrofitBuilder.getOtakuReaderApi().getTrendingList(null).enqueue(
                new Callback<MangaListPOJO>() {
                    @Override
                    public void onResponse(Call<MangaListPOJO> call, Response<MangaListPOJO> response) {
                        mangas = response.body().getMangas();

                        recyclerView = view.findViewById(R.id.mangaGrid);
                        recyclerView.setHasFixedSize(true);
                        layoutManager = new GridLayoutManager(view.getContext(), 2);
                        recyclerView.setLayoutManager(layoutManager);

                        mAdapter = new MangaGridAdapter(mangas, item -> {
                            final Intent intent = new Intent(view.getContext(), ChapterSelectActivity.class);
                            intent.putExtra(MANGA_ID, item.getId());
                            startActivity(intent);
                        });

                        recyclerView.setAdapter(mAdapter);

                        ProgressBar pb = view.findViewById(R.id.mangaListProgressBar);
                        pb.setVisibility(View.GONE);

                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<MangaListPOJO> call, Throwable t) {
                        Log.e("MangaListActivity", "API CALL LIST ERROR");
                    }
                });
        return view;
    }

}
