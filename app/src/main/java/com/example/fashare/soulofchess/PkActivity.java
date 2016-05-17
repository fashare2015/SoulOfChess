package com.example.fashare.soulofchess;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.fashare.soulofchess.base.BaseActivity;
import com.example.fashare.soulofchess.component.http.HttpHelper;
import com.example.fashare.soulofchess.config.UrlConstant;
import com.example.fashare.soulofchess.controller.GameController;
import com.example.fashare.soulofchess.model.AIInfo;
import com.example.fashare.soulofchess.model.ChessMap;
import com.example.fashare.soulofchess.model.GameInfo;
import com.example.fashare.soulofchess.model.PatternAI;
import com.example.fashare.soulofchess.model.Player;
import com.example.fashare.soulofchess.model.RankCenter;
import com.example.fashare.soulofchess.model.SimpleAI;
import com.example.fashare.soulofchess.model.UserCenter;
import com.example.fashare.soulofchess.view.ChessBoardView;
import com.loopj.android.http.RequestParams;


public class PkActivity extends BaseActivity{
    private static final String TAG = "PkActivity";
    private ChessBoardView chessBoardView;
    private GameInfo gameInfo;
    private GameController gameController;

    private int[] bulbIds = {R.id.left_bulb, R.id.right_bulb};
    private ImageView[] bulbs = new ImageView[2];
    private int curBulb;

    private boolean hasTwoAI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pk_activity);
        super.onCreate(savedInstanceState);

        //findViewById(R.id.btn).setOnClickListener(v -> startActivity(new Intent(this, SetPatternActivity.class)));
        //findViewById(R.id.begin).setOnClickListener(v -> gameBegining());
        initTitleBar(R.drawable.back, v -> finish(), "战斗中");

        gameBegining();
    }

    private void gameBegining() {
        curBulb = 1;
        gameInfo.setCurPlayer(gameInfo.getWhitePlayer());
        gameController.swapPlayer();
    }

    @Override
    public void initParam() {
        hasTwoAI = getIntent().getBooleanExtra("hasTwoAI", false);
        Log.d(TAG, "hasTwoAI: " + hasTwoAI);
    }

    public void initView() {
        chessBoardView = (ChessBoardView)findViewById(R.id.cbv);
        chessBoardView.setWinCallback(curPlayer -> {
            if(curPlayer == gameInfo.getBlackPlayer())
                upDateRank();
            showWinDlg(curPlayer);
        });
    }

    private void upDateRank() {
        RequestParams params = new RequestParams();
        params.put("winner", UserCenter.getInstance().getUser().getUsername());
        params.put("loser", RankCenter.getInstance().getCurPlayer().getUsername());
        new HttpHelper(params).setCallback((response) -> {
            setResult(RESULT_OK);
//            usernames = HttpHelper.jsonToList(response);
//            adapter.setUsernames(usernames);
//            //myRank.setText(String.format("我的排名：第%d名", usernames.size()+1));
//            myRank.setText(String.format("我的排名：第%d名", 1));
//            plv.onRefreshComplete();
        }).post(UrlConstant.UPDATE_RANK);
    }

    private void showWinDlg(Player curPlayer) {
        String winInfo = curPlayer == gameInfo.getBlackPlayer()? "您胜利了!": "您失败了!";
        new AlertDialog.Builder(this).setTitle("游戏结果")
                .setMessage(winInfo)
                .setPositiveButton("确定", (a, b) -> finish()).show();
    }

    public void initModel() {
        //test();

//            gameInfo = new GameInfo(new ChessMap(GameInfo.DEFAULT_SIZE, GameInfo.DEFAULT_SIZE));
//            AIInfo userAiInfo = UserCenter.getInstance().getUser().getAiInfoList().get(0);
//            gameInfo.getWhitePlayer().setAI(new PatternAI(gameInfo, userAiInfo));
//            chessBoardView.setGameInfo(gameInfo);

        if(hasTwoAI){
            gameInfo = new GameInfo(new ChessMap(GameInfo.DEFAULT_SIZE, GameInfo.DEFAULT_SIZE));
            AIInfo userAiInfo = UserCenter.getInstance().getUser().getAiInfoList().get(0);
            gameInfo.getBlackPlayer().setAI(new PatternAI(gameInfo, userAiInfo));
            AIInfo playerAiInfo = RankCenter.getInstance().getCurPlayer().getAiInfoList().get(0);
            gameInfo.getWhitePlayer().setAI(new PatternAI(gameInfo, playerAiInfo));
            chessBoardView.setGameInfo(gameInfo);
        }else{
            gameInfo = new GameInfo(new ChessMap(GameInfo.DEFAULT_SIZE, GameInfo.DEFAULT_SIZE));
            AIInfo userAiInfo = UserCenter.getInstance().getUser().getAiInfoList().get(0);
            gameInfo.getWhitePlayer().setAI(new PatternAI(gameInfo, userAiInfo));
            chessBoardView.setGameInfo(gameInfo);
        }

    }

    private void test() {
        gameInfo = new GameInfo(new ChessMap(GameInfo.DEFAULT_SIZE, GameInfo.DEFAULT_SIZE));
        gameInfo.getWhitePlayer().setAI(new SimpleAI(gameInfo));
        gameInfo.getBlackPlayer().setAI(new SimpleAI(gameInfo));
        chessBoardView.setGameInfo(gameInfo);
    }

    public void initController() {
        gameController = new GameController(chessBoardView, gameInfo);
        chessBoardView.setGameController(gameController);

        initBulb();
        gameController.setSwapCallback(() -> swapBulb());
    }

    private void initBulb() {
        for(int i=0; i<bulbs.length; i++)
            bulbs[i] = (ImageView)findViewById(bulbIds[i]);
        setBulbsVisibility(curBulb);
    }

    private void swapBulb() {
        setBulbsVisibility(curBulb ^= 1);
    }

    private void setBulbsVisibility(int curBulb) {
        bulbs[curBulb].setVisibility(View.VISIBLE);
        bulbs[curBulb^1].setVisibility(View.INVISIBLE);
    }

}
