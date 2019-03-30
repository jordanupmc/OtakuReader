package com.example.otakureader;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.otakureader.api.pojo.MangaPOJO;
import com.example.otakureader.tools.CollectionManga;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.otakureader.tools.LocalData.LOCAL_DATA;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button read = findViewById(R.id.btn_go_chapter_view);

        read.setOnClickListener(l -> {
            final Intent intent = new Intent(this, MangaListActivity.class);
            startActivity(intent);
        });

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        TextView tv = findViewById(R.id.tmpTvId);

        //si le fichier n'est pas present on le cree avec un chapitre


        CollectionManga cm = LOCAL_DATA.getCollectionManga(this);
        LOCAL_DATA.addManga(this, new MangaPOJO());

        tv.setText("TEST LOCAL DATA:\n OLD: \n" + cm.size() + "\nNEW:\n" + LOCAL_DATA.getCollectionManga(this).size());
    }
}