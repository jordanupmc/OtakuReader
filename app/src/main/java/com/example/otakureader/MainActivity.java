package com.example.otakureader;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.otakureader.database.AppDatabase;
import com.example.otakureader.database.Manga;
import com.example.otakureader.database.dao.MangaDao;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public TextView tv;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button read = findViewById(R.id.btn_go_chapter_view);
        //getApplicationContext().deleteDatabase("user-database");
        read.setOnClickListener(l -> {
            final Intent intent = new Intent(this, MangaListActivity.class);
            startActivity(intent);
        });
        tv = findViewById(R.id.tmpTvId);
        new AgentAsyncTask(this
                , AppDatabase.getAppDatabase(getApplicationContext()).mangaDao()).execute();


    }

    private static class AgentAsyncTask extends AsyncTask<Void, Void, List<Manga>> {
        private final MangaDao mDao;
        private final MainActivity activity;

        public AgentAsyncTask(MainActivity mainActivity, MangaDao mDao) {
            this.mDao = mDao;
            this.activity = mainActivity;
        }

        @Override
        protected void onPostExecute(List<Manga> mangaList) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mangaList.size(); i++) {
                sb.append(mangaList.get(i).title).append("\n");
            }
            activity.tv.setText(sb);
        }

        @Override
        protected List<Manga> doInBackground(Void... voids) {
            List<Manga> mangaList = mDao.getAll();
            return mangaList;
        }
    }
}