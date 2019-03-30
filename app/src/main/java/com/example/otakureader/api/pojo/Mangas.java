package com.example.otakureader.api.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Mangas {

    @SerializedName("mangas")
    @Expose
    private List<MangaPOJO> mangas;

    public List<MangaPOJO> getMangas() {
        return mangas;
    }

    public void setMangas(List<MangaPOJO> mangas) {
        this.mangas = mangas;
    }

    public String toString(){
        return "ajajaj";
    }
}
