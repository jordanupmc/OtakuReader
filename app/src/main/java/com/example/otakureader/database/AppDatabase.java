package com.example.otakureader.database;

import android.content.Context;

import com.example.otakureader.database.dao.ChapterDao;
import com.example.otakureader.database.dao.MangaDao;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Manga.class, Chapter.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract MangaDao mangaDao();

    public abstract ChapterDao chapterDao();


    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                INSTANCE =
                        Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "user-database")
                                .build();
            }
        }
        return INSTANCE;
    }
}
