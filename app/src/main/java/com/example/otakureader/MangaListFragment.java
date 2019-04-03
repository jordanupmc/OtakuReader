package com.example.otakureader;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.otakureader.api.RetrofitBuilder;
import com.example.otakureader.api.pojo.MangaListPOJO;
import com.example.otakureader.api.pojo.MangaPOJO;
import com.example.otakureader.tools.adapters.MangaGridAdapter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.otakureader.ChapterSelectActivity.MANGA_ID;

public class MangaListFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<MangaPOJO> mangas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_manga_list, container, false);

        boolean orientationPortrait = view.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        if (savedInstanceState == null) {
            getMangasFromAPI(view, orientationPortrait);
        } else {
            mangas = savedInstanceState.getParcelableArrayList("mangas");
            initView(mangas, view, orientationPortrait);
        }
        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("mangas", mangas);
    }

    private void getMangasFromAPI(View view, boolean orientationPortrait) {

        RetrofitBuilder.getOtakuReaderApi().getTrendingList(null).enqueue(
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

                        ProgressBar pb = view.findViewById(R.id.mangaListProgressBar);
                        pb.setVisibility(View.GONE);

                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<MangaListPOJO> call, Throwable t) {
                        Log.e("MangaListFragment", "API CALL LIST ERROR");
                    }
                });
    }

    private void initView(ArrayList<MangaPOJO> mangas, View view, boolean orientationPortrait) {

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

        ProgressBar pb = view.findViewById(R.id.mangaListProgressBar);
        pb.setVisibility(View.GONE);

        recyclerView.setVisibility(View.VISIBLE);
    }

}
