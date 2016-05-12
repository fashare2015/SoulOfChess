package com.example.fashare.soulofchess.model;

import android.util.Log;

import com.example.fashare.soulofchess.constant.ChessType;
import com.example.fashare.soulofchess.model.base.BaseMap;

/**
 * Created by apple on 2016/1/25.
 */
public class GameInfo {
    private static final String TAG = "GameInfo";
    public static final int DEFAULT_SIZE = 15;

    //黑棋先
    private Player blackPlayer;
    private Player whitePlayer;
    private Player emptyPlayer;
    private Player eraserPlayer;
    private Player[] players;

    private Player curPlayer;

    private BaseMap baseMap;

    private int step;

    public Player getBlackPlayer() {
        return blackPlayer;
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public Player getCurPlayer() {
        return curPlayer;
    }

    public void setCurPlayer(Player curPlayer) {
        this.curPlayer = curPlayer;
    }

    public void selectCurPlayer(int curIndex) {
        if(curIndex>=0 && curIndex<players.length)
            curPlayer = players[curIndex];
        else
            Log.e(TAG, "selectCurPlayer: curIndex越界");
    }

    public BaseMap getBaseMap() {
        return baseMap;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void AddStep(){
        this.step ++;
    }

    public GameInfo(BaseMap map){
        initPlayer();
        baseMap = map;
        setStep(0);
    }

    private void initPlayer() {
        blackPlayer = new Player(ChessType.BLACK);
        whitePlayer = new Player(ChessType.WHITE);
        emptyPlayer = new Player(ChessType.EMPTY);
        eraserPlayer = new Player(ChessType.ERASER);

        //whitePlayer.setAI(new SimpleAI(this));
        //whitePlayer.setAI(new PatternAI(this));
        //blackPlayer.setAI(new PatternAI(this));
        curPlayer = blackPlayer;

        Player tmp[] = {blackPlayer, whitePlayer, emptyPlayer, eraserPlayer};
        players = new Player[tmp.length];
        for(int i=0; i<players.length; i++)
            players[i] = tmp[i];
    }

    public void swapPlayer(){
        if(curPlayer.equals(blackPlayer))
            curPlayer = whitePlayer;
        else
            curPlayer = blackPlayer;
    }

}
