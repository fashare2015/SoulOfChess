package com.example.fashare.soulofchess.model;

import com.example.fashare.soulofchess.constant.ChessType;

/**
 * Created by apple on 2016/1/25.
 */
public class Player {
    private ChessType type;
    private boolean isAI;
    private SimpleAI AI;

    public ChessType getType() {
        return type;
    }

    public boolean isAI() {
        return isAI;
    }

    public void setIsAI(boolean isAI) {
        this.isAI = isAI;
    }

    public void setAI(SimpleAI AI) {
        this.AI = AI;
        this.setIsAI(true);
    }

    public SimpleAI getAI() {
        return AI;
    }

    public Player(ChessType type){
        this.type = type;
        this.isAI = false;
    }

    @Override
    public String toString() {
        //return super.toString();
        return type.toString();
    }
}
