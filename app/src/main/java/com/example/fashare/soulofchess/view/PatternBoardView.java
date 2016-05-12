package com.example.fashare.soulofchess.view;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;

import com.example.fashare.soulofchess.constant.ChessType;
import com.example.fashare.soulofchess.controller.GameController;
import com.example.fashare.soulofchess.controller.PatternController;
import com.example.fashare.soulofchess.model.ChessMap;
import com.example.fashare.soulofchess.model.PatternMap;
import com.example.fashare.soulofchess.view.base.BaseBoardView;

/**
 * Created by apple on 2016/2/13.
 */
public class PatternBoardView extends BaseBoardView {
    private static final String TAG = "PatternBoardView";
    private PatternController patternController;
    private PatternMap patternMap;

    public void setPatternController(PatternController patternController) {
        this.patternController = patternController;
    }

    public PatternBoardView(Context context) {
        super(context);
    }

    public PatternBoardView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    @Override
    public void putChess(Point p) {
        if(patternController != null) {
            if(patternController.putChess(p)) {
                ChessType type = gameInfo.getCurPlayer().getType();
                if(type.equals(ChessType.ERASER))
                    redraw();
                else
                    drawChess(p, type);
            }
        }else
            Log.e(TAG, "patternController is null");
    }


}
