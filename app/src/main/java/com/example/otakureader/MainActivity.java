package com.example.otakureader;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button read = findViewById(R.id.btn_go_chapter_view);

        read.setOnClickListener(l -> {
            final Intent intent = new Intent(this, FullscreenView.class);
            startActivity(intent);
        });
    }
}
