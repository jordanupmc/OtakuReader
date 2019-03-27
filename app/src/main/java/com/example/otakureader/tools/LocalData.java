package com.example.otakureader.tools;

import android.content.Context;

import com.example.otakureader.mangaeden.pojo.MangaPOJO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public enum LocalData {
    LOCAL_DATA;

    public Gson gson = new GsonBuilder().create();
    public String FILENAME = "storage.json";

    /**
     * Add mangaPOJO to the local collection
     *
     * @param context
     * @param mangaPOJO
     * @return
     */
    public boolean addManga(Context context, MangaPOJO mangaPOJO) {
        return addManga(context, gson.toJson(mangaPOJO));
    }

    public boolean addManga(Context context, String jsonString) {
        if (!isFilePresent(context, FILENAME)) {
            return create(context, FILENAME, jsonString);
        }
        String mangaString = read(context, FILENAME);

        if (mangaString == null) {
            return false;
        }

        CollectionManga cm = gson.fromJson(mangaString, CollectionManga.class);
        cm.add(gson.fromJson(jsonString, MangaPOJO.class));
        return create(context, FILENAME, gson.toJson(cm));
    }

    public CollectionManga getCollectionManga(Context context) {
        if (!isFilePresent(context, FILENAME)) {
            return null;
        }
        return gson.fromJson(read(context, FILENAME), CollectionManga.class);
    }


    public boolean replaceCollectionManga(Context context, CollectionManga cm) {
        return create(context, FILENAME, gson.toJson(cm));
    }

    public boolean removeManga(Context context, MangaPOJO mangaPojo) {
        CollectionManga cm = getCollectionManga(context);
        if (cm == null) {
            return false;
        }
        boolean res = cm.remove(mangaPojo);
        return replaceCollectionManga(context, cm) && res;
    }

    public String read(Context context, String fileName) {
        try (FileInputStream fis = context.openFileInput(fileName);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader bufferedReader = new BufferedReader(isr)) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (FileNotFoundException fileNotFound) {
            return null;
        } catch (IOException ioException) {
            return null;
        }
    }

    /**
     * Create a file with jsonString content
     **/
    public boolean create(Context context, String fileName, String jsonString) {
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            if (jsonString != null) {
                fos.write(jsonString.getBytes());
            }
            return true;
        } catch (FileNotFoundException fileNotFound) {
            return false;
        } catch (IOException ioException) {
            return false;
        }

    }

    /**
     * @param context
     * @param fileName
     * @return if fileName present in internal storage
     */
    public boolean isFilePresent(Context context, String fileName) {
        String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        return file.exists();
    }


}
