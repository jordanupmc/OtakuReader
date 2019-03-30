package com.example.otakureader.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = {"id", "manga"})
public class Chapter {

    @NonNull
    public String id;
    @NonNull
    public String manga;
    public boolean readingComplete;


    public Chapter(@NonNull String id, @NonNull String manga, boolean readingComplete) {
        this.id = id;
        this.manga = manga;
        this.readingComplete = readingComplete;
    }
}
