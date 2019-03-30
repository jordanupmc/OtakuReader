package com.example.otakureader.database;

import com.example.otakureader.mangaeden.pojo.MangaDetailPOJO;
import com.example.otakureader.mangaeden.pojo.MangaPOJO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Manga {
    @PrimaryKey
    @NonNull
    public String id;
    public String alias;

    public String category;

    public long hits;

    public String image;

    public double lastChapterDate;

    public long status;

    public String title;

    @Ignore
    public Manga() {
    }

    public Manga(@NonNull String id, String alias, String category, long hits, String image, double lastChapterDate, long status, String title) {
        this.id = id;
        this.alias = alias;
        this.category = category;
        this.hits = hits;
        this.image = image;
        this.lastChapterDate = lastChapterDate;
        this.status = status;
        this.title = title;
    }

    @Override
    public String toString() {
        return "Manga{" +
                "id='" + id + '\'' +
                ", alias='" + alias + '\'' +
                ", category='" + category + '\'' +
                ", hits=" + hits +
                ", image='" + image + '\'' +
                ", lastChapterDate=" + lastChapterDate +
                ", status=" + status +
                ", title='" + title + '\'' +
                '}';
    }

    public static MangaPOJO convert(Manga m) {
        MangaPOJO mangaPOJO = new MangaPOJO();
        mangaPOJO.setAlias(m.alias);
        mangaPOJO.setHits(m.hits);
        mangaPOJO.setId(m.id);
        mangaPOJO.setImage(m.image);
        mangaPOJO.setStatus(m.status);
        mangaPOJO.setLastChapterDate(m.lastChapterDate);
        mangaPOJO.setTitle(m.title);

        if (m.category != null) {
            String[] splits = m.category.replace("[", "").replace("]", "").split(",");
            List<String> arrayList = new ArrayList<>(Arrays.asList(splits));
            mangaPOJO.setCategory(arrayList);
        } else {
            mangaPOJO.setCategory(new ArrayList<>());
        }
        return mangaPOJO;
    }

    public static Manga convert(MangaDetailPOJO m, String idmanga) {
        Manga manga = new Manga();
        manga.alias = m.getAlias();
        manga.hits = -1;
        manga.id = idmanga;
        manga.image = m.getImage();
        manga.status = m.getStatus();
        manga.lastChapterDate = -1;
        manga.title = m.getTitle();

        return manga;
    }
}
