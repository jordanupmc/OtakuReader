package com.example.otakureader.database.dao;

import com.example.otakureader.database.Chapter;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ChapterDao {

    @Query("SELECT * FROM Chapter  WHERE manga = :mangaId")
    List<Chapter> getChapterFromManga(String mangaId);

    @Query("SELECT * FROM CHAPTER")
    List<Chapter> getAllChapters();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Chapter... chapters);

    @Update
    void update(Chapter... chapters);

    @Delete
    void delete(Chapter... chapters);
}
