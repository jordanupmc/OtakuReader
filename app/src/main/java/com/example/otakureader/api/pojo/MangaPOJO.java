package com.example.otakureader.api.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MangaPOJO implements Parcelable {

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

    public MangaPOJO(Parcel in) {
        this.alias = in.readString();
        in.readStringList(this.category);
        this.hits = in.readLong();
        this.id = in.readString();
        this.image = in.readString();
        this.lastChapterDate = in.readDouble();
        this.status = in.readLong();
        this.title = in.readString();
    }

    public MangaPOJO() {}

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(alias);
        dest.writeStringList(category);
        dest.writeLong(hits);
        dest.writeString(id);
        dest.writeString(image);
        dest.writeDouble(lastChapterDate);
        dest.writeLong(status);
        dest.writeString(title);
    }

    public static final Parcelable.Creator<MangaPOJO> CREATOR = new Parcelable.Creator<MangaPOJO>()
    {
        public MangaPOJO createFromParcel(Parcel in)
        {
            return new MangaPOJO(in);
        }
        public MangaPOJO[] newArray(int size)
        {
            return new MangaPOJO[size];
        }
    };
}
