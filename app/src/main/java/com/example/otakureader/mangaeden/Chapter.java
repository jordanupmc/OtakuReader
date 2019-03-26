package com.example.otakureader.mangaeden;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Chapter {

    @SerializedName("images")
    @Expose
    private List<List<String>> images = null;

    public List<List<String>> getImages() {
        return images;
    }

}
