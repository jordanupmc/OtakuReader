package com.example.otakureader.tools;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Chapter implements Serializable, Parcelable {
    private static int cpt = 0;
    private String number;
    private String date;
    private String title;
    private String id;
    private int localId;
    private Boolean status;

    public Chapter(String number, String date, String title, String id) {
        this.number = number;
        this.date = date;
        this.title = title;
        this.id = id;
        localId = cpt++;
    }

    public Chapter(Parcel in) {
        number = in.readString();
        date = in.readString();
        title = in.readString();
        id = in.readString();
        localId = in.readInt();
        boolean[] tmp = new boolean[1];
        in.readBooleanArray(tmp);
        status = tmp[0];
    }

    public static int getCpt() {
        return cpt;
    }

    public static void setCpt(int cpt) {
        Chapter.cpt = cpt;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLocalId() {
        return this.localId;
    }

    public Boolean isStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(number);
        dest.writeString(date);
        dest.writeString(title);
        dest.writeString(id);
        dest.writeInt(localId);
        boolean[] tmp = new boolean[1];
        tmp[0] = status;
        dest.writeBooleanArray(tmp);
    }

    public static final Parcelable.Creator<Chapter> CREATOR = new Parcelable.Creator<Chapter>() {
        public Chapter createFromParcel(Parcel in)
        {
            return new Chapter(in);
        }
        public Chapter[] newArray(int size)
        {
            return new Chapter[size];
        }
    };
}