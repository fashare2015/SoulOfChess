package com.example.fashare.soulofchess.model;

import android.util.Log;

import java.util.List;

/**
 * Created by apple on 2016/3/4.
 */
public class User {
    private String username;
    //private String password;
    private Player player;
    private List<PatternDir> patternDirList;
    private List<AIInfo> aiInfoList;

    public String getUsername() {
        return username;
    }

    public List<PatternDir> getPatternDirList() {
        return patternDirList;
    }

    public void setPatternDirList(List<PatternDir> patternDirList) {
        this.patternDirList = patternDirList;
    }

    public List<AIInfo> getAiInfoList() {
        return aiInfoList;
    }

    public void setAiInfoList(List<AIInfo> aiInfoList) {
        this.aiInfoList = aiInfoList;
    }

    public User(String username) {
        this.username = username;
        //this.password = password;
        this.player = null;
    }

    @Override
    public String toString() {
        return String.format("User[\nusername = %s, \nplay = %s, \npatternDirList = %s, \naiInfoList = %s\n]",
                username,
                player != null ? player.toString() : null,
                patternDirList != null ? patternDirList.get(0).toString() : null,
                aiInfoList != null ? aiInfoList.get(0).toString() :null);
    }
}
