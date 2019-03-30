package com.example.otakureader.database.dao;

import com.example.otakureader.database.Manga;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MangaDao {

    @Query("SELECT * FROM Manga")
    List<Manga> getAll();

    @Query("SELECT * FROM Manga WHERE id = :idManga")
    Manga loadAllByIds(String idManga);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Manga... mangas);

    @Update
    void update(Manga... mangas);

    @Delete
    void delete(Manga... mangas);
}
