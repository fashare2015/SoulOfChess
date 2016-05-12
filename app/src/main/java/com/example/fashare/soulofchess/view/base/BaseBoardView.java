package com.example.fashare.soulofchess.view.base;

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
import android.view.View;

import com.example.fashare.soulofchess.R;
import com.example.fashare.soulofchess.constant.ChessType;
import com.example.fashare.soulofchess.model.GameInfo;

/**
 * Created by apple on 2016/2/13.
 */
public class BaseBoardView extends SurfaceView implements SurfaceHolder.Callback{
    private static final String TAG = "BaseBoardView";
    private SurfaceHolder holder;

    // 棋盘大小参数
    private int chessBoardColNum = 0;
    private int chessBoardRowNum = 0;
    private int gridSize = 70;

    // 画笔
    private Paint boardPaint = new Paint();
    private int boardLineWidth = 1;
    private Paint chessPaint = new Paint();
    private Paint clearPaint = new Paint();

    // 棋子图片
    private Bitmap bmpBlack = null;
    private Bitmap bmpWhite = null;
    private Bitmap bmpFocus = null;
    private Bitmap bmpEmpty = null;
    private Bitmap bmpEraser = null;

    public GameInfo gameInfo;

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public BaseBoardView(Context context){ this(context, null); }

    public BaseBoardView(Context context, AttributeSet attr){
        super(context, attr);
        initPaint();
        init();
    }

    private void initPaint() {
        boardPaint.setColor(Color.BLACK);
        boardPaint.setStrokeWidth(boardLineWidth);   //线条宽度
        chessPaint.setAntiAlias(true);  //防锯齿
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    private void init() {
        holder = this.getHolder();
        holder.addCallback(this);
        //设置透明
        setZOrderOnTop(true);
        holder.setFormat(PixelFormat.TRANSPARENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 设置高度与宽度成比例
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        if(gameInfo != null){
            int row = gameInfo.getBaseMap().getHeight(),
                col = gameInfo.getBaseMap().getWidth();
            //width = width / col * col;
            float scale = ((float)row) / col;
            int height = (int) (width*scale);
            setMeasuredDimension(width, height);
        } else
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // super.onLayout(changed, left, top, right, bottom);
        // assert gameInfo != null: "gameInfo为null";
        if(gameInfo != null) {
            // 设置棋盘大小
            chessBoardRowNum = gameInfo.getBaseMap().getHeight();
            chessBoardColNum = gameInfo.getBaseMap().getWidth();
            // 设置格子大小
            gridSize = (right - left) / chessBoardColNum;
            initChess(gridSize, gridSize);
        }else{
            Log.e(TAG, "未绑定gameInfo");
        }
    }

    private void initChess(int width, int height) {
        bmpBlack = createChess(width, height, ChessType.BLACK);
        bmpWhite = createChess(width, height, ChessType.WHITE);
        bmpFocus = createChess(width, height, ChessType.FOCUS);
        bmpEmpty = createChess(width, height, ChessType.EMPTY);
        bmpEraser = createChess(width, height, ChessType.ERASER);
    }

    private Bitmap createChess(int width, int height, ChessType type) {
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Drawable d = null;
        if(type == ChessType.BLACK)
            d = getResources().getDrawable(R.drawable.black_large);
        else if(type == ChessType.WHITE)
            d = getResources().getDrawable(R.drawable.white_large);
        else if(type == ChessType.FOCUS)
            d = getResources().getDrawable(R.drawable.focus);
        else if(type == ChessType.EMPTY)
            d = getResources().getDrawable(R.drawable.empty);
        else if(type == ChessType.ERASER)
            d = getResources().getDrawable(R.drawable.eraser);

        d.setBounds(0, 0, width, height);   //???
        d.draw(canvas);
        return bmp;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(canNotTouch())   return true;
        //if(gameInfo.getBaseMap().isWin() || gameInfo.getCurPlayer().isAI())    return true;

        Point p = getCoordinate((int) event.getX(), (int) event.getY());
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if(gameInfo.getCurPlayer().getType() == ChessType.ERASER){
                    drawChess(p, ChessType.ERASER);
                }else
                    drawChess(p, ChessType.FOCUS);
                break;

            case MotionEvent.ACTION_UP:
                putChess(p);
                break;

            default:
                break;
        }
        return true;    //表示消费了事件
        //return super.onTouchEvent(event);
    }

    public boolean canNotTouch() {
        return false;
    }

    public void putChess(Point p) {
        drawChess(p, gameInfo.getCurPlayer().getType());
    }

    private Point getCoordinate(int x, int y) {
        return new Point(Math.max(0, Math.min(chessBoardColNum-1, x/gridSize)),
                Math.max(0, Math.min(chessBoardRowNum-1, y/gridSize)));
    }

    public void redraw(){
        drawChess(new Point(-1, -1), ChessType.EMPTY);
    }

    public void drawChess(Point p, ChessType type) {
        Canvas canvas = null;
        if(holder == null || (canvas=holder.lockCanvas()) == null)  return ;

        // 待优化
        canvas.drawPaint(clearPaint);       // 清屏
        drawBoard(canvas);                  // 要重新画一下棋盘
        drawChess(canvas, p, type);
        holder.unlockCanvasAndPost(canvas);
    }

    private void drawChess(Canvas canvas, Point p, ChessType type) {
        //画已下的棋子
        ChessType[][] chessMap = gameInfo.getBaseMap().getGraph();
        for(int i=0; i<chessMap.length; i++)
            for(int j=0; j<chessMap[i].length; j++)
                if(chessMap[i][j] == ChessType.BLACK)
                    canvas.drawBitmap(bmpBlack, j * gridSize, i * gridSize, chessPaint);
                else if(chessMap[i][j] == ChessType.WHITE)
                    canvas.drawBitmap(bmpWhite, j * gridSize, i * gridSize, chessPaint);
                else if(gameInfo.getBaseMap().getDefaultType() != ChessType.EMPTY
                    && chessMap[i][j] == ChessType.EMPTY)
                    canvas.drawBitmap(bmpEmpty, j * gridSize, i * gridSize, chessPaint);

        if(!gameInfo.getBaseMap().inMap(p.y, p.x))  return ;

        //画刚落下的棋子
        if(type == ChessType.BLACK) {
            canvas.drawBitmap(bmpBlack, p.x * gridSize, p.y * gridSize, chessPaint);
            canvas.drawBitmap(bmpFocus, p.x * gridSize, p.y * gridSize, chessPaint);
        }else if(type == ChessType.WHITE) {
            canvas.drawBitmap(bmpWhite, p.x * gridSize, p.y * gridSize, chessPaint);
            canvas.drawBitmap(bmpFocus, p.x * gridSize, p.y * gridSize, chessPaint);
        }else if(type == ChessType.FOCUS)
            canvas.drawBitmap(bmpFocus, p.x * gridSize, p.y * gridSize, chessPaint);
        else if(type == ChessType.ERASER)
            canvas.drawBitmap(bmpEraser, p.x * gridSize, p.y * gridSize, chessPaint);


    }

    private void drawBoard() {
        Canvas canvas = null;
        if(holder == null || (canvas=holder.lockCanvas()) == null)  return ;
        drawBoard(canvas);
        holder.unlockCanvasAndPost(canvas);
    }

    private void drawBoard(Canvas canvas) {
        Log.d(TAG, "drawBoard:");
        int startX, startY, endX, endY;
        startX = gridSize/2;
        startY = gridSize/2;
        endX = startX + (chessBoardColNum-1) * gridSize;
        endY = startY + (chessBoardRowNum-1) * gridSize;
        // 画竖直线
        for(int i=0; i<chessBoardColNum; i++)
            canvas.drawLine(startX + i*gridSize, startY, startX + i*gridSize, endY, boardPaint);
        // 画水平线
        for(int i=0; i<chessBoardRowNum; i++)
            canvas.drawLine(startX, startY + i*gridSize, endX, startY + i*gridSize, boardPaint);
        // 画中心点
        int circleX = startX + chessBoardColNum/2 * gridSize;
        int circleY = startY + chessBoardRowNum/2 * gridSize;
        int radius = 5;
        canvas.drawCircle(circleX, circleY, radius, boardPaint);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        redraw();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
