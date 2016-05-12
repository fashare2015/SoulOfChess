package com.example.fashare.soulofchess.model;

import android.util.Log;

import com.example.fashare.soulofchess.component.file.FileHelper;
import com.example.fashare.soulofchess.constant.ChessType;

import java.util.List;

/**
 * Created by apple on 2016/2/7.
 */
public class PatternAI extends SimpleAI {
    private static final String TAG = "PatternAI";
    private List<List<PatternMap>> patternMaps;
    private AIInfo aiInfo;

    public PatternAI(GameInfo gameInfo, AIInfo aiInfo){
        super(gameInfo);
        this.aiInfo = aiInfo;
        initPattern();
    }

    private void initPattern() {
        //patternMaps = FileHelper.readPatterns();
        patternMaps = aiInfo.getPatternMaps();
        Log.d("initPattern", patternMaps.size() + "");
    }

    @Override
    public double calcScore(ChessType playerType) {
        if(patternMaps == null){
            Log.e(TAG, "patternMaps is null");
            return 0;
        }

        if(chessMap == null)
            chessMap = (ChessMap)gameInfo.getBaseMap();
        chessMap.cut(1); //
        //Log.d("chessMap: i", chessMap.getStartXY().y + " - " + chessMap.getEndXY().y);
        //Log.d("chessMap: j", chessMap.getStartXY().x + " - " + chessMap.getEndXY().x);
        double ans = 0;
        int n = patternMaps.size();
        for(int i=0; i<n && ans == 0; i++){
            List<PatternMap> tmpMapList = patternMaps.get(i);
            int cnt = 0;
            for(PatternMap patternMap: tmpMapList) {
                cnt += super.chessMap.cnt(patternMap, playerType != ChessType.BLACK);
            }
            ans += cnt * Math.pow(100f, n-1-i);     //100为基底
        }
        return ans;
    }
}
