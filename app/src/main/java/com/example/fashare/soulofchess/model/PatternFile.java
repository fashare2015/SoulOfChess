package com.example.fashare.soulofchess.model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by apple on 2016/2/28.
 */
public class PatternFile implements Serializable{
    private static final String TAG = "PatternDir";
    private static Bitmap defaultBmp;
    private int patternId;
    private String name;
    private Bitmap bmp;
    private String path;
    private String dirName;
    private String data;

    static {
        //defaultBmp = BitmapFactory.decodeResource(, );
    }

    public int getPatternId() {
        return patternId;
    }

    public String getName() {
        return name;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public String getPath() {
        return path;
    }

    public String getDirName() {
        return dirName;
    }

    public String getData() {
        return data;
    }

    public PatternFile(String name, String path, String dirName, String data) {
        this.name = name;
        this.bmp = null;
        this.path = path;
        this.dirName = dirName;
        this.data = data;
    }

    public PatternFile(String name) {
        this(name, null, null, null);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
