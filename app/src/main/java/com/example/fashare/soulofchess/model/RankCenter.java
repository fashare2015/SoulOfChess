package com.example.fashare.soulofchess.model;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by apple on 2016/3/4.
 */
public class RankCenter {
    private HashMap<String, User> playerMap;
    private static RankCenter instance;
    private static String curPlayerName;

    private RankCenter(){
        playerMap = new HashMap<>();
    }
    public static synchronized RankCenter getInstance() {
        if (instance == null)
            instance = new RankCenter();
        return instance;
    }

    public User getPlayer(String playerName){
        return playerMap != null? playerMap.get(playerName): null;
    }

    public void putPlayer(String playerName, User player){
        if(playerMap != null && getPlayer(playerName) == null)
            playerMap.put(playerName, player);
    }

    public static String getCurPlayerName() {
        return curPlayerName;
    }

    public static void setCurPlayerName(String curPlayerName) {
        RankCenter.curPlayerName = curPlayerName;
    }

    public User getCurPlayer(){
        return getPlayer(curPlayerName);
    }

}
