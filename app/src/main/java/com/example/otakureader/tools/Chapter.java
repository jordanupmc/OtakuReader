package com.example.otakureader.tools;

import java.io.Serializable;

public class Chapter implements Serializable {
    private static int cpt = 0;
    private String number;
    private String date;
    private String title;
    private String id;
    private int localId;

    public Chapter(String number, String date, String title, String id) {
        this.number = number;
        this.date = date;
        this.title = title;
        this.id = id;
        localId = cpt++;
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
}