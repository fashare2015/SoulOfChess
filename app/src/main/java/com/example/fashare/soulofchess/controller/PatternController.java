package com.example.fashare.soulofchess.controller;

import android.graphics.Point;
import android.util.Log;

import com.example.fashare.soulofchess.constant.ChessType;
import com.example.fashare.soulofchess.controller.base.BaseController;
import com.example.fashare.soulofchess.model.ChessMap;
import com.example.fashare.soulofchess.model.GameInfo;
import com.example.fashare.soulofchess.model.PatternMap;
import com.example.fashare.soulofchess.model.Player;
import com.example.fashare.soulofchess.view.base.BaseBoardView;

/**
 * Created by apple on 2016/2/13.
 */
public class PatternController extends BaseController {
    private static final String TAG = "PatternController";

    public PatternController(BaseBoardView baseBoardView, GameInfo gameInfo) {
        super(baseBoardView, gameInfo);
    }

    public boolean putChess(Point p) {
        ChessType type = gameInfo.getCurPlayer().getType();
        if(type != ChessType.ERASER)
            return super.putChess(p);

        if(baseMap != null){
            baseMap.getGraph()[p.y][p.x] = ChessType.ARBITRARY;
            return true;
        }else
            Log.e(TAG, "chessMap???");
        return false;
    }


}
