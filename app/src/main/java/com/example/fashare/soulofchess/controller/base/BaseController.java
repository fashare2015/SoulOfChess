package com.example.fashare.soulofchess.controller.base;

import android.graphics.Point;
import android.util.Log;

import com.example.fashare.soulofchess.constant.ChessType;
import com.example.fashare.soulofchess.model.ChessMap;
import com.example.fashare.soulofchess.model.GameInfo;
import com.example.fashare.soulofchess.model.PatternAI;
import com.example.fashare.soulofchess.model.Player;
import com.example.fashare.soulofchess.model.SimpleAI;
import com.example.fashare.soulofchess.model.base.BaseMap;
import com.example.fashare.soulofchess.view.ChessBoardView;
import com.example.fashare.soulofchess.view.base.BaseBoardView;

/**
 * Created by apple on 2016/2/13.
 */
public class BaseController {
    private static final String TAG = "BaseController";
    public BaseBoardView baseBoardView;
    public GameInfo gameInfo;
    public BaseMap baseMap;

    public BaseController(BaseBoardView baseBoardView, GameInfo gameInfo){
        this.baseBoardView = baseBoardView;
        this.gameInfo = gameInfo;
        this.baseMap = gameInfo.getBaseMap();     //??????ü???
    }

    /**
     * ?? View ????????????????? Model ?? <br/>
     * ???? baseMap ???????????
     *
     * @param p ????λ?? p
     * @return ?????????
     */
    public boolean putChess(Point p) {
        if(baseMap != null){
            if(baseMap.canPutChess(p)) {
                //Player curPlayer = gameInfo.getCurPlayer();     //???????????
                ChessType[][] graph = baseMap.getGraph();
                ChessType type = gameInfo.getCurPlayer().getType();
                graph[p.y][p.x] = type;       //?????????
                //chessMap[p.y][p.x] = curPlayer.getType()==ChessType.BLACK? ChessType.BLACK: ChessType.WHITE;    //????
                gameInfo.AddStep();

//                if(baseMap instanceof ChessMap) {
//                    SimpleAI ai = gameInfo.getWhitePlayer().getAI();
//                    Log.d(TAG, "BLACK SCORE: " + ai.calcScore(ChessType.BLACK) + "");
//                    Log.d(TAG, "WHITE SCORE: " + ai.calcScore(ChessType.WHITE) + "");
//                }

                return true;
            }else
                Log.e(TAG, "??????");
        }else
            Log.e(TAG, "chessMap???");
        return false;
    }
}
