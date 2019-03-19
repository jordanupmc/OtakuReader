package com.example.otakureader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button read = findViewById(R.id.btn_go_chapter_view);

        read.setOnClickListener(l->{
            Intent intent = new Intent(this, FullscreenView.class);
            startActivity(intent);
        });
    }
}
