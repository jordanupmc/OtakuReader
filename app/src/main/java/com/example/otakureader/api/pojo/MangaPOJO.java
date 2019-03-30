package com.example.otakureader.api.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MangaPOJO {

    @SerializedName("a")
    @Expose
    private String alias;

    @SerializedName("c")
    @Expose
    private List<String> category = null;

    @SerializedName("h")
    @Expose
    private long hits;

    @SerializedName("i")
    @Expose
    private String id;

    @SerializedName("im")
    @Expose
    private String image;

    @SerializedName("ld")
    @Expose
    private double lastChapterDate;

    @SerializedName("s")
    @Expose
    private long status;

    @SerializedName("t")
    @Expose
    private String title;


    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public long getHits() {
        return hits;
    }

    public void setHits(long hits) {
        this.hits = hits;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getLastChapterDate() {
        return lastChapterDate;
    }

    public void setLastChapterDate(double lastChapterDate) {
        this.lastChapterDate = lastChapterDate;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
