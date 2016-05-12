package com.example.fashare.soulofchess.model;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by apple on 2016/2/28.
 */
public class AIInfo implements Serializable{
    private static final String TAG = "AIInfo";
    private String name;
    private String data;

    private List<List<PatternMap>> patternMaps;


    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }

    public List<List<PatternMap>> getPatternMaps() {
        return patternMaps != null? patternMaps: dataToMaps();
    }

    private List<List<PatternMap>> dataToMaps() {
        patternMaps = new ArrayList<>();
        List<PatternDir> patternDirList = UserCenter.getInstance().getUser().getPatternDirList();
        String datas[] = data.split("\n");
        Collections.sort(patternDirList, (a, b) -> getOrder(a.getName()) - getOrder(b.getName()));
        //事先排好序
        for(int i=0; i<datas.length; i++) {
            PatternDir dir = patternDirList.get(i);
            List<PatternMap> mapList = new ArrayList<>();
            List<PatternFile> patternFileList = dir.getFileList();
            Log.d("dataToMaps", dir.getName());
            for (PatternFile file : patternFileList) {
                PatternMap tmpMap = new PatternMap(GameInfo.DEFAULT_SIZE, GameInfo.DEFAULT_SIZE);
                tmpMap.rebuild(file.getData());
                tmpMap.cut();
                mapList.add(tmpMap);
            }
            patternMaps.add(mapList);
        }

        return patternMaps;
    }

    private int getOrder(String name) {
        String datas[] = data.split("\n");
        for(int i=0; i<datas.length; i++)
            if(datas[i].equals(name))
                return i;
        return datas.length;
    }

    public AIInfo(String name, String data) {
        this.name = name;
        this.data = data;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
