package com.example.otakureader.api.pojo;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MangaListPOJO {

    @SerializedName("end")
    @Expose
    private long end;

    @SerializedName("manga")
    @Expose
    private ArrayList<MangaPOJO> mangas = null;

    @SerializedName("page")
    @Expose
    private long page;

    @SerializedName("start")
    @Expose
    private long start;

    @SerializedName("total")
    @Expose
    private long total;

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public ArrayList<MangaPOJO> getMangas() {
        return mangas;
    }

    public void setMangas(ArrayList<MangaPOJO> mangas) {
        this.mangas = mangas;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}