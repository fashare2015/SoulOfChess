package com.example.fashare.soulofchess.model;

import android.graphics.Point;

import com.example.fashare.soulofchess.constant.ChessType;
import com.example.fashare.soulofchess.model.base.BaseMap;

/**
 * Created by apple on 2016/2/7.
 */
public class ChessMap extends BaseMap {
    private boolean isWin;
    private ChessType[][] chessGraph;

    public boolean isWin() {
        return isWin;
    }

    public ChessMap(int h, int w){
        super(h, w, ChessType.EMPTY);
        this.isWin = false;
        this.chessGraph = getGraph();
    }

    public boolean checkWin(){
        final int di[] = {0, 1, -1, 1};
        final int dj[] = {1, 1, 1, 0};
        final int len=5;
        for(int i=0; i<getHeight(); i++)
            for(int j=0; j<getWidth(); j++) {
                if(chessGraph[i][j] == getDefaultType())
                    continue;
                for (int k = 0; k < 4; k++) {
                    boolean get = true;
                    for (int d = 1; d < len; d++) {
                        int ni = i + di[k] * d, nj = j + dj[k] * d;
                        if (!inMap(ni, nj) || chessGraph[i][j] != chessGraph[ni][nj])
                            get = false;
                    }
                    if (get) return isWin = true;
                }
            }
        return false;
    }

    public int cnt(PatternMap patternMap, boolean isOppositeType) {
        ChessType[][] patGraph = patternMap.getCutGraph();
        //int len_i = patternMap.getHeight(), len_j = patternMap.getWidth();  //错，cutGraph不是原图
        int len_i = patGraph.length, len_j = patGraph[0].length;
        Point s=getStartXY(), e=getEndXY();
        if(!inCutMap(s.y + len_i-1, s.x + len_j-1))  return 0;                //越界!!!

        int ans = 0;
        for(int di=s.y; di<=e.y; di++)
            for(int dj=s.x; dj<=e.x; dj++){
                boolean isMatch = true;
                if(!inCutMap(di + len_i-1, dj + len_j-1))  continue;        //越界!!!
                for(int i=0; i<len_i && di+i<=e.y && isMatch; i++)
                    for(int j=0; j<len_j && dj+j<=e.x && isMatch; j++)
                        if(!chessGraph[di+i][dj+j].myEquals(isOppositeType?
                        patGraph[i][j].oppositeType(): patGraph[i][j]))     //不相等
                            isMatch = false;
                if(isMatch) ans ++;
            }
        return ans;
    }

}
