package com.example.fashare.soulofchess.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.example.fashare.soulofchess.R;
import com.example.fashare.soulofchess.constant.ChessType;
import com.example.fashare.soulofchess.controller.GameController;
import com.example.fashare.soulofchess.model.ChessMap;
import com.example.fashare.soulofchess.model.GameInfo;
import com.example.fashare.soulofchess.model.Player;
import com.example.fashare.soulofchess.view.base.BaseBoardView;


/**
 * Created by apple on 2016/1/1.
 */

public class ChessBoardView extends BaseBoardView{
    private static final String TAG = "ChessBoardView";
    private GameController gameController;
    private ChessMap chessMap;
    private WinCallback winCallback;

    @Override
    public void setGameInfo(GameInfo gameInfo) {
        super.setGameInfo(gameInfo);
        this.chessMap = (ChessMap)gameInfo.getBaseMap();        // 何时 gameInfo 非空 ???
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void setWinCallback(WinCallback winCallback) {
        this.winCallback = winCallback;
    }

    public ChessBoardView(Context context) {
        this(context, null);
    }

    public ChessBoardView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean canNotTouch() {
        return chessMap.isWin() || gameInfo.getCurPlayer().isAI();
    }

    @Override
    public void putChess(Point p) {
        if(gameController != null) {
            if(gameController.putChess(p)) {
                drawChess(p, gameInfo.getCurPlayer().getType());
                checkWin();
                if(!chessMap.isWin())
                    gameController.swapPlayer();
            }
        }else
            Log.e(TAG, "未绑定gameController");
    }

    private void checkWin() {
        if(chessMap.checkWin() && winCallback != null){
            winCallback.afterWin(gameInfo.getCurPlayer());
        }
        //Toast.makeText(getContext(), gameInfo.getCurPlayer().toString()+" win", Toast.LENGTH_LONG).show();
    }

    public interface WinCallback{
        void afterWin(Player curPlayer);
    }
}

