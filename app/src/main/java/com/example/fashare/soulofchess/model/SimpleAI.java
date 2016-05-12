package com.example.fashare.soulofchess.model;

import android.graphics.Point;
import android.text.TextUtils;
import android.util.Log;

import com.example.fashare.soulofchess.constant.ChessType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;


/**
 * Created by apple on 2016/1/27.
 */
public class SimpleAI {
    private static final String TAG = "SimpleAI";

    public GameInfo gameInfo;
    public ChessMap chessMap;
    private ChessType[][] graph;
    private static final int MAX_DEP = 2;
    private static final int INF = 0x3f3f3f3f;
    private Point pos;      //AI决定的落子位置
    private ChessType AIType;

    private class Pos{
        public Point pos;
        public double score;

        public Pos(Point pos, double score) {
            this.pos = pos;
            this.score = score;
        }

        @Override
        public String toString() {
            return String.format("(%d, %d) = (%f, %f)", pos.x, pos.y, score, myLog(score));
        }
    }

    private List<Pos> posList = new ArrayList<>();

    private void setPos(Point pos) {
        this.pos = pos;
    }

    public SimpleAI(GameInfo gameInfo){
        this.gameInfo = gameInfo;
    }

    public Point AIThinking() {
        if(gameInfo.getStep() == 0)
            return new Point(GameInfo.DEFAULT_SIZE/2, GameInfo.DEFAULT_SIZE/2);
        initState();
        getScore(0, AIType, INF);
        
        return randomPos();
        //return pos;
    }

    private void initState() {
        chessMap = (ChessMap) gameInfo.getBaseMap();
        graph = chessMap.getGraph();    //直接引用，不深拷贝
        AIType = gameInfo.getCurPlayer().getType();
        setPos(null);
        posList.clear();
    }

    private Point randomPos() {
        final double LIMIT = 0.000001;
        Collections.sort(posList, (a, b) -> (int)myLog(b.score - a.score)); //防止 double 直接转 int 时, 溢出
        int len = 0;
        for(int i=0; i<posList.size(); i++)
            if(myLog(posList.get(0).score) - myLog(posList.get(i).score) < LIMIT)
                len = i;
            else break;
        len ++;
        Log.d(TAG, TextUtils.join(",", posList));

        //[0, len)
        int randomIndex = new Random().nextInt(len);
        Log.d(TAG, String.format("[%d, %d): %d", 0, len, randomIndex));
        return posList.get(randomIndex).pos;
    }

    private double myLog(double x){
        return x == 0? 0: x>0? Math.log10(x): -Math.log10(-x);
    }

    /**
     * 极大极小博弈，a-b剪枝，同时计算出落子位置 pos
     * <br/><br/>
     * @param depth     递归深度
     * @param type      玩家棋子类型：BLACK or WHITE
     * @param faScore   父节点的分数
     * @return          当前局势的分数
     */
    private double getScore(int depth, ChessType type, double faScore) {
        if(depth == MAX_DEP)    return calcScore();

        Point tmpPos = null;
        double curScore = type==AIType? -INF: INF;
        for(int i=0; i< graph.length; i++)
            for(int j=0; j< graph[i].length; j++)
                if(graph[i][j] == chessMap.getDefaultType() && chessMap.existChess(i, j, 1)){
                    if(depth ==0 && tmpPos == null)  tmpPos = new Point(j, i);

                    graph[i][j] = type;
                    double sonScore = getScore(depth+1, type.oppositeType(), curScore);
                    graph[i][j] = chessMap.getDefaultType();    //回溯

                    if((type == AIType && curScore < sonScore)      //curScore = max{son}
                    || (type != AIType && curScore > sonScore)){    //curScore = min{son}
                        curScore = sonScore;
                        if(depth == 0){
                            tmpPos.x = j;
                            tmpPos.y = i;                           // (j, i) 对应 (x, y)
                        }
                    }

                    if(depth == 0)
                        posList.add(new Pos(new Point(j, i), sonScore));

                    if((type == AIType && curScore > faScore)       //curScore太大，即可剪枝
                    || (type != AIType && curScore < faScore))      //curScore太小，即可剪枝
                        return curScore;
                }
        if(depth == 0)  setPos(tmpPos);
        return curScore;
    }

    private double calcScore() {
        return calcScore(AIType) - calcScore(AIType.oppositeType());
        //己方正分，敌方负分
    }

    public double calcScore(ChessType playerType) {
        double ans = 0;
        for(int len=5; len>=1 && ans==0; len--)
            ans += cnt(playerType, len) * Math.pow(50f, len-1);
        return ans;
    }

    private int cnt(ChessType playerType, int len) {
        final int di[] = {0, 1, -1, 1};
        final int dj[] = {1, 1, 1, 0};
        int ans = 0;
        for(int i=0; i< graph.length; i++)
            for(int j=0; j< graph[0].length; j++)
                if(graph[i][j] == playerType)
                    for (int k = 0; k < 4; k++) {
                        int cnt = 0;
                        boolean get = true;
                        for (int d = -1; d <= len; d++) {
                            int ni = i + di[k] * d, nj = j + dj[k] * d;
                            if(d == -1 || d == len){
                                if(chessMap.inMap(ni, nj)){
                                    if(graph[ni][nj] == playerType)
                                        get = false;
                                    else if(graph[ni][nj] == chessMap.getDefaultType())
                                        cnt ++;
                                }
                            }else if (!chessMap.inMap(ni, nj) || graph[ni][nj] != playerType)
                                get = false;
                        }
                        if (get){
                            if(len == 5)        ans += 20;
                            else if(cnt == 1)   ans ++;
                            else if(cnt == 2)   ans += len;
                        }
                    }
        return ans;
    }
}