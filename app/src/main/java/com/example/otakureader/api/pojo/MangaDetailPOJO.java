package com.example.otakureader.api.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MangaDetailPOJO implements Parcelable {

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

    public MangaDetailPOJO(Parcel in) {
        in.readStringList(aka);
        alias = in.readString();
        artist = in.readString();
        author = in.readString();
        in.readStringList(categories);
        created = in.readDouble();
        description = in.readString();
        image = in.readString();
        released = in.readLong();
        status = in.readLong();
        title = in.readString();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(aka);
        dest.writeString(alias);
        dest.writeString(artist);
        dest.writeString(author);
        dest.writeStringList(categories);
        dest.writeDouble(created);
        dest.writeString(description);
        dest.writeString(image);
        dest.writeLong(released);
        dest.writeLong(status);
        dest.writeString(title);
    }

    public static final Parcelable.Creator<MangaDetailPOJO> CREATOR = new Parcelable.Creator<MangaDetailPOJO>()
    {
        public MangaDetailPOJO createFromParcel(Parcel in)
        {
            return new MangaDetailPOJO(in);
        }
        public MangaDetailPOJO[] newArray(int size)
        {
            return new MangaDetailPOJO[size];
        }
    };
}
