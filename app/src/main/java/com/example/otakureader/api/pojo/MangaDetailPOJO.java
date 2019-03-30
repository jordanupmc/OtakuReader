package com.example.otakureader.api.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MangaDetailPOJO {

    @SerializedName("aka")
    @Expose
    private List<String> aka = null;
    @SerializedName("alias")
    @Expose
    private String alias;
    @SerializedName("artist")
    @Expose
    private String artist;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("categories")
    @Expose
    private List<String> categories = null;
    @SerializedName("chapters")
    @Expose
    private List<List<String>> chapters = null;
    @SerializedName("created")
    @Expose
    private double created;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("released")
    @Expose
    private long released;
    @SerializedName("status")
    @Expose
    private long status;
    @SerializedName("title")
    @Expose
    private String title;

    public List<String> getAka() {
        return aka;
    }

    public String getAlias() {
        return alias;
    }

    public String getArtist() {
        return artist;
    }

    public String getAuthor() {
        return author;
    }

    public List<String> getCategories() {
        return categories;
    }

    public List<List<String>> getChapters() {
        return chapters;
    }

    public double getCreated() {
        return created;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public long getReleased() {
        return released;
    }

    public long getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }
}
