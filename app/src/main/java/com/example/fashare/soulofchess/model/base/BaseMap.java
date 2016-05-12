package com.example.fashare.soulofchess.model.base;

import android.graphics.Point;
import android.util.Log;

import com.example.fashare.soulofchess.constant.ChessType;
import com.example.fashare.soulofchess.model.GameInfo;

/**
 * Created by apple on 2016/2/7.
 */
public class BaseMap {
    private static final String TAG = "BaseMap";
    private static String rowSeparator = "\n";
    private static String colSeparator = ",";

    private ChessType[][] graph;
    private int height, width;
    private ChessType defaultType;

    private Point startXY, endXY;
    private ChessType[][] cutGraph;

    public ChessType[][] getGraph() {
        return graph;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public ChessType getDefaultType() {
        return defaultType;
    }

    public Point getStartXY() {
        return startXY;
    }

    public Point getEndXY() {
        return endXY;
    }

    public ChessType[][] getCutGraph() {
        return cutGraph;
    }

    public BaseMap(int h, int w, ChessType defaultType){
        this.height = h;
        this.width = w;
        this.defaultType = defaultType;
        graph = new ChessType[height][width];
        initMap();
    }

    public void initMap() {
        for(int i=0; i<height; i++)
            for(int j=0; j<width; j++)
                graph[i][j] = defaultType;
        startXY = new Point(0, 0);
        endXY = new Point(width-1, height-1);
    }

    public boolean canPutChess(Point p) {
        return graph[p.y][p.x] == defaultType;
    }

    @Override
    public String toString() {
        String ans = "";
        for(int i=0; i<graph.length; i++){
            String line = "";
            for(int j=0; j<graph[i].length; j++)
                line += graph[i][j].toString() + colSeparator;
            ans += line + rowSeparator;
        }
        return ans;
    }

    public ChessType[][] rebuild(String s){
        if(s == null){
            Log.e(TAG, "rebuild: String is null");
            return graph;
        }
        String[] lines = s.split(rowSeparator);
        for(int i=0; i<graph.length && i<lines.length; i++) {
            String[] values = lines[i].split(colSeparator);
            for (int j = 0; j < graph[i].length && j<values.length; j++)
                graph[i][j] = ChessType.values()[ getInt(values[j]) ];

        }
        return graph;
    }

    private int getInt(String str){     //过滤空白字符
        for(int i=0; i<str.length(); i++) {
            char ch = str.charAt(i);
            if(Character.isDigit(ch))
                return Integer.parseInt(ch+"");
        }
        return 0;
    }

    public boolean inMap(int i, int j) {
        return i>=0&&i<getHeight() && j>=0&&j<getWidth();
    }

    public boolean inCutMap(int i, int j) {
        return i>=startXY.y&&i<=endXY.y && j>=startXY.x&&j<=endXY.x;
    }

    public boolean existChess(int ci, int cj, int len){				//����len����Ҫ������
        for(int di=-len; di<=len; di++)
            for(int dj=-len; dj<=len; dj++){
                int ni=ci+di, nj=cj+dj;
                //if(ni==ci && nj==cj)	continue;
                if(inMap(ni, nj) && graph[ni][nj] != getDefaultType())
                    return true;
            }
        return false;
    }

    /**
     * ����������ӵ����� [startXY.y - endXY.y][startXY.x - endXY.x]
     */
    public void getRegion(int emptyEdgeSize){
        startXY = new Point(width-1, height-1);
        endXY = new Point(0, 0);
        for(int i=0; i<graph.length; i++)
            for(int j=0; j<graph[i].length; j++){
                if(existChess(i, j, emptyEdgeSize)){
                //if(graph[i][j] != getDefaultType()){
                    startXY.x = Math.min(startXY.x, j);
                    startXY.y = Math.min(startXY.y, i);
                    endXY.x = Math.max(endXY.x, j);
                    endXY.y = Math.max(endXY.y, i);
                }
            }
    }

    public void cut(){  cut(0); }

    public void cut(int emptyEdgeSize) {
        getRegion(emptyEdgeSize);
        Point s=getStartXY(), e=getEndXY();

        cutGraph = new ChessType[e.y-s.y+1][e.x-s.x+1];
        for(int i=s.y; i<=e.y; i++)
            for(int j=s.x; j<=e.x; j++)
                cutGraph[i-s.y][j-s.x] = graph[i][j];
    }
}
