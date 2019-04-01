package com.example.otakureader;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.otakureader.api.pojo.MangaPOJO;
import com.example.otakureader.database.AppDatabase;
import com.example.otakureader.database.Manga;
import com.example.otakureader.database.dao.MangaDao;
import com.example.otakureader.tools.adapters.MangaGridAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.otakureader.ChapterSelectActivity.MANGA_ID;

public class FavorisFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.favoris_layout, container, false);
        new GetFavAsyncTask(view, AppDatabase.getAppDatabase(view.getContext()).mangaDao()).execute();

        return view;
    }

    private static class GetFavAsyncTask extends AsyncTask<Void, Void, List<MangaPOJO>> {
        private final MangaDao mDao;
        //TODO Weakref
        private final View view;

        public GetFavAsyncTask(View view, MangaDao mDao) {
            this.mDao = mDao;
            this.view = view;
        }

        @Override
        protected void onPostExecute(List<MangaPOJO> mangaList) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mangaList.size(); i++) {
                sb.append(mangaList.get(i).getTitle()).append("\n");
            }
            RecyclerView recyclerView = view.findViewById(R.id.mangaGrid);
            recyclerView.setHasFixedSize(true);
            GridLayoutManager layoutManager = new GridLayoutManager(view.getContext(), 2);
            recyclerView.setLayoutManager(layoutManager);

            MangaGridAdapter mAdapter = new MangaGridAdapter(mangaList, item -> {
                final Intent intent = new Intent(view.getContext(), ChapterSelectActivity.class);
                intent.putExtra(MANGA_ID, item.getId());
                view.getContext().startActivity(intent);
            });
            recyclerView.setAdapter(mAdapter);
            ProgressBar pb = view.findViewById(R.id.mangaListProgressBar);
            pb.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            LinearLayout tv = view.findViewById(R.id.noContent);
            if (mangaList.isEmpty()) {
                view.findViewById(R.id.mangaListLayout).setVisibility(View.GONE);
                tv.setVisibility(View.VISIBLE);
            } else {
                view.findViewById(R.id.mangaListLayout).setVisibility(View.VISIBLE);
                tv.setVisibility(View.GONE);
            }

        }

        @Override
        protected List<MangaPOJO> doInBackground(Void... voids) {
            List<Manga> mangaList = mDao.getAll();
            List<MangaPOJO> res = new ArrayList<>();
            if (mangaList == null) {
                return res;
            }

            for (int i = 0; i < mangaList.size(); i++) {
                res.add(Manga.convert(mangaList.get(i)));
            }
            return res;
        }
    }

}
