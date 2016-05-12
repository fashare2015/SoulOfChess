package com.example.fashare.soulofchess.model;

import android.graphics.Point;

import com.example.fashare.soulofchess.constant.ChessType;
import com.example.fashare.soulofchess.model.base.BaseMap;

/**
 * Created by apple on 2016/2/7.
 */
public class PatternMap extends BaseMap {
    public PatternMap(int h, int w){
        super(h, w, ChessType.ARBITRARY);
    }


}
