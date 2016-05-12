package com.example.fashare.soulofchess.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by apple on 2016/2/28.
 */
public class PatternDir{
    private static final String TAG = "PatternDir";
    //private static Bitmap defaultBmp;
    private int dirId;
    private String name;
    //private Bitmap bmp;
    private List<PatternFile> fileList;

    static {
        //defaultBmp = BitmapFactory.decodeResource(, );
    }

    public int getDirId() {
        return dirId;
    }

    public String getName() {
        return name;
    }

    public List<PatternFile> getFileList() {
        return fileList;
    }

    public PatternDir(String name, List<PatternFile> fileList) {
        this.name = name;
        //this.bmp = null;
        this.fileList = fileList;
    }

    @Override
    public String toString() {
        return String.format("PatternDir[name = %s,\n fileList = %s\n]",
                name,
                "[\n" + TextUtils.join("\n", fileList) + "\n]");
    }
}
