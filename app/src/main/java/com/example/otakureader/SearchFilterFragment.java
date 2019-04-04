package com.example.otakureader;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.example.otakureader.api.RetrofitBuilder;
import com.example.otakureader.api.pojo.MangaListPOJO;
import com.example.otakureader.api.pojo.MangaPOJO;
import com.example.otakureader.tools.adapters.MangaGridAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import static com.example.otakureader.ChapterSelectActivity.MANGA_ID;

public class SearchFilterFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<MangaPOJO> mangas;

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
        final View view = inflater.inflate(R.layout.fragment_search_filter, container, false);
        ProgressBar pb = view.findViewById(R.id.mangaListProgressBar);

        if (search.length() == 0) {
            pb.setVisibility(View.GONE);
            LinearLayout tv = view.findViewById(R.id.noContent);
            tv.setVisibility(View.VISIBLE);
            return view;
        }

        boolean orientationPortrait = view.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        if (savedInstanceState == null) {
            getSearchResultFromAPI(view, orientationPortrait, pb);
        } else {
            mangas = savedInstanceState.getParcelableArrayList("mangas");
            initView(mangas, view, orientationPortrait, pb);
        }


        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("mangas", mangas);
    }

    private void getSearchResultFromAPI(View view, boolean orientationPortrait, ProgressBar pb) {
        RetrofitBuilder.getOtakuReaderApi().searchMangaFilter(search).enqueue(
                new Callback<MangaListPOJO>() {
                    @Override
                    public void onResponse(Call<MangaListPOJO> call, Response<MangaListPOJO> response) {
                        mangas = response.body().getMangas();

                        recyclerView = view.findViewById(R.id.mangaGrid);
                        recyclerView.setHasFixedSize(true);
                        layoutManager = new GridLayoutManager(view.getContext(), orientationPortrait ? 2 : 4);
                        recyclerView.setLayoutManager(layoutManager);

                        mAdapter = new MangaGridAdapter(mangas, item -> {
                            final Intent intent = new Intent(view.getContext(), ChapterSelectActivity.class);
                            intent.putExtra(MANGA_ID, item.getId());
                            startActivity(intent);
                        });

                        recyclerView.setAdapter(mAdapter);

                        pb.setVisibility(View.GONE);

                        recyclerView.setVisibility(View.VISIBLE);

                        LinearLayout tv = view.findViewById(R.id.noContent);
                        if (mangas.isEmpty()) {
                            view.findViewById(R.id.mangaListLayout).setVisibility(View.GONE);
                            tv.setVisibility(View.VISIBLE);
                        } else {
                            view.findViewById(R.id.mangaListLayout).setVisibility(View.VISIBLE);
                            tv.setVisibility(View.GONE);
                        }

                        search = "";
                    }

                    @Override
                    public void onFailure(Call<MangaListPOJO> call, Throwable t) {
                        Log.e("MangaListActivity", "API CALL LIST ERROR");
                    }
                });
    }

    private void initView(ArrayList<MangaPOJO> mangas, View view, boolean orientationPortrait, ProgressBar pb) {

        recyclerView = view.findViewById(R.id.mangaGrid);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(view.getContext(), orientationPortrait ? 2 : 4);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new MangaGridAdapter(mangas, item -> {
            final Intent intent = new Intent(view.getContext(), ChapterSelectActivity.class);
            intent.putExtra(MANGA_ID, item.getId());
            startActivity(intent);
        });

        recyclerView.setAdapter(mAdapter);

        pb.setVisibility(View.GONE);

        recyclerView.setVisibility(View.VISIBLE);

        LinearLayout tv = view.findViewById(R.id.noContent);
        if (mangas.isEmpty()) {
            view.findViewById(R.id.mangaListLayout).setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.mangaListLayout).setVisibility(View.VISIBLE);
            tv.setVisibility(View.GONE);
        }

        search = "";
    }
}
