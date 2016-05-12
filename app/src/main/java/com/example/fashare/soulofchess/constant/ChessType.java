package com.example.fashare.soulofchess.constant;

/**
 * Created by apple on 2016/1/26.<br/>
 * ARBITRARY:   任意（白或黑或空）<br/>
 * BLACK:       黑棋  <br/>
 * WHITE:       白棋  <br/>
 * EMPTY:       空    <br/>
 * FOCUS:       光标  <br/>
 */
public enum ChessType {
    ARBITRARY, BLACK, WHITE, EMPTY, FOCUS, ERASER;

    public ChessType oppositeType() {
        if(this!=BLACK && this!=WHITE)  return this;
        return this==BLACK? WHITE: BLACK;
    }

    @Override
    public String toString() {
        ChessType[] values = ChessType.values();
        for(int i=0; i<values.length; i++)
            if(this == values[i])
                return i + "";
        return -1 + "";
    }

    public boolean myEquals(ChessType type) {
        return this==ARBITRARY || type==ARBITRARY || this==type;
    }
}