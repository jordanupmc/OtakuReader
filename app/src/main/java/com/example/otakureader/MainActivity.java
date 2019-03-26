package com.example.otakureader;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.otakureader.ChapterSelectActivity.mangaId;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button read = findViewById(R.id.btn_go_chapter_view);

        read.setOnClickListener(l -> {
            final Intent intent = new Intent(this, ChapterSelectActivity.class);
            intent.putExtra(mangaId,"5bdda0a7719a1665d3a37531");
            startActivity(intent);
        });
    }
}

//        RetrofitBuilder.getApi().getManga("5bdda0a7719a1665d3a37531").enqueue(
//                new Callback<MangaPOJO>() {
//@Override
//public void onResponse(Call<MangaPOJO> call, Response<MangaPOJO> response) {
//        for(int i=0; i<response.body().getChapters().size(); i++){
//        chapters.add(response.body().getChapters().get(i).get(3));
//        }
//        Collections.reverse(chapters);
//        System.out.println(">>>>"+chapters);
//        }
//
//@Override
//public void onFailure(Call<MangaPOJO> call, Throwable t) {
//        Log.e("FullScreenView", "API CALL ERROR");
//        }
//        });
//
//        RetrofitBuilder.getApi().getChapter("5bfad4c0719a162df4c7eb5e").enqueue(
//        new Callback<ChapterPOJO>() {
//@Override
//public void onResponse(Call<ChapterPOJO> call, Response<ChapterPOJO> response) {
//        System.out.println(">>>"+response.body().getImages().get(0));
//        }
//
//@Override
//public void onFailure(Call<ChapterPOJO> call, Throwable t) {
//        Log.e("FullScreenView", t.getMessage());
//        }
//        });