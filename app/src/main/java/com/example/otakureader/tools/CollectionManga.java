package com.example.otakureader.tools;

import android.content.Context;

import com.example.otakureader.mangaeden.pojo.MangaPOJO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CollectionManga {
    private List<MangaPOJO> collection = new ArrayList<>();


    public void removeDuplicate() {
        collection = new ArrayList<>(new LinkedHashSet<>(collection));
    }

    public int size() {
        return collection.size();
    }

    public boolean isEmpty() {
        return collection.isEmpty();
    }

    public boolean contains(@Nullable Object o) {
        return collection.contains(o);
    }

    public boolean add(MangaPOJO mangaPOJO) {
        return collection.add(mangaPOJO);
    }

    public boolean addAll(@NonNull Collection<? extends MangaPOJO> c) {
        return collection.addAll(c);
    }

    public MangaPOJO get(int index) {
        return collection.get(index);
    }

    public void clear() {
        collection.clear();
    }

    public MangaPOJO remove(int index) {
        return collection.remove(index);
    }

    public boolean remove(@Nullable Object o) {
        return collection.remove(o);
    }

    public void replaceInStorage(Context context) {
        LocalData.LOCAL_DATA.replaceCollectionManga(context, this);
    }

    @Override
    public String toString() {
        return "CollectionManga{" +
                "collection=" + collection +
                '}';
    }
}
