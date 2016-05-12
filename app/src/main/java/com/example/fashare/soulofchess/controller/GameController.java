package com.example.fashare.soulofchess.controller;

import android.graphics.Point;
import android.os.AsyncTask;
import android.util.Log;

import com.example.fashare.soulofchess.constant.ChessType;
import com.example.fashare.soulofchess.controller.base.BaseController;
import com.example.fashare.soulofchess.model.ChessMap;
import com.example.fashare.soulofchess.model.GameInfo;
import com.example.fashare.soulofchess.model.Player;
import com.example.fashare.soulofchess.view.ChessBoardView;
import com.example.fashare.soulofchess.view.base.BaseBoardView;

/**
 * Created by apple on 2016/1/25.
 */
public class GameController extends BaseController {
    private static final String TAG = "GameController";
    private SwapCallback swapCallback;

    public void setSwapCallback(SwapCallback swapCallback) {
        this.swapCallback = swapCallback;
    }

    public GameController(BaseBoardView baseBoardView, GameInfo gameInfo) {
        super(baseBoardView, gameInfo);
    }

    public void swapPlayer() {
        gameInfo.swapPlayer();
        if(swapCallback != null)
            swapCallback.afterSwap();

        if(gameInfo.getCurPlayer().isAI()) {
            new AsyncTaskForAI().execute();
//            Point p = gameInfo.getCurPlayer().getAI().AIThinking();
//            baseBoardView.putChess(p);
        }
    }

    private class AsyncTaskForAI extends AsyncTask<Void, Void, Point>{
        @Override
        protected Point doInBackground(Void... voids) {
            return gameInfo.getCurPlayer().getAI().AIThinking();
        }

        @Override
        protected void onPostExecute(Point point) {
            super.onPostExecute(point);
            baseBoardView.putChess(point);
        }
    }


    public interface SwapCallback{
        void afterSwap();
    }
}
