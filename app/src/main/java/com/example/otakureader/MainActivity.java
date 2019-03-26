package com.example.otakureader;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.otakureader.mangaeden.Chapter;
import com.example.otakureader.mangaeden.Manga;
import com.example.otakureader.mangaeden.RetrofitBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.otakureader.FullscreenView.CHAPTER_ID;

public class MainActivity extends AppCompatActivity {

    private List<String> chapters = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RetrofitBuilder.getApi().getManga("5bdda0a7719a1665d3a37531").enqueue(
                new Callback<Manga>() {
                    @Override
                    public void onResponse(Call<Manga> call, Response<Manga> response) {
                        for(int i=0; i<response.body().getChapters().size(); i++){
                            chapters.add(response.body().getChapters().get(i).get(3));
                        }
                        Collections.reverse(chapters);
                        System.out.println(">>>>"+chapters);
                    }

                    @Override
                    public void onFailure(Call<Manga> call, Throwable t) {
                        Log.e("FullScreenView", "API CALL ERROR");
                    }
                });

        RetrofitBuilder.getApi().getChapter("5bfad4c0719a162df4c7eb5e").enqueue(
                new Callback<Chapter>() {
                    @Override
                    public void onResponse(Call<Chapter> call, Response<Chapter> response) {
                        System.out.println(">>>"+response.body().getImages().get(0));
                    }

                    @Override
                    public void onFailure(Call<Chapter> call, Throwable t) {
                        Log.e("FullScreenView", t.getMessage());
                    }
                });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button read = findViewById(R.id.btn_go_chapter_view);

        read.setOnClickListener(l->{
            Intent intent = new Intent(this, FullscreenView.class);
            startActivity(intent);
        });
    }
}
