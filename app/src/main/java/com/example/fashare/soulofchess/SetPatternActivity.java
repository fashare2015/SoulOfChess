package com.example.fashare.soulofchess;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fashare.soulofchess.base.BaseActivity;
import com.example.fashare.soulofchess.component.file.FileHelper;
import com.example.fashare.soulofchess.component.http.HttpHelper;
import com.example.fashare.soulofchess.controller.PatternController;
import com.example.fashare.soulofchess.model.GameInfo;
import com.example.fashare.soulofchess.model.PatternFile;
import com.example.fashare.soulofchess.model.PatternMap;
import com.example.fashare.soulofchess.model.UserCenter;
import com.example.fashare.soulofchess.view.ChessBoardView;
import com.example.fashare.soulofchess.view.PatternBoardView;
import com.loopj.android.http.RequestParams;

import java.util.List;


public class SetPatternActivity extends BaseActivity {
    private static final String TAG = "SetPatternActivity";
    public static final String PATH = "PATH";
    public static final String PATTERN_FILE = "PATTERN_FILE";

    private String path;
    private PatternFile patternFile;

    private ChessBoardView chessBoardView;
    private GameInfo patternGameInfo;
    private List<List<PatternMap>> patternMaps;
    int cur=0;

    int[] selectIds = {R.id.select_black, R.id.select_white, R.id.select_empty, R.id.select_eraser};
    private View[] selectViews;
    int curIndex = 0;

    private PatternBoardView patternBoardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.set_pattern);
        super.onCreate(savedInstanceState);

        initTitleBar("策 略 设 置", R.drawable.confirm, v -> upload());

        //findViewById(R.id.btn).setOnClickListener(v -> Toast.makeText(this, "haha", Toast.LENGTH_LONG).show());
    }

    public void initParam() {
        path = getIntent().getStringExtra(PATH);
        patternFile = (PatternFile)getIntent().getSerializableExtra(SetPatternActivity.PATTERN_FILE);
    }

    public void initView() {
        initSelectView();
        patternBoardView = (PatternBoardView)findViewById(R.id.pbv);

        //patternMaps = FileHelper.readPatterns();

        //findViewById(R.id.save).setOnClickListener(v -> newSaveDlg());
//        findViewById(R.id.save).setOnClickListener(v -> upload());
//        findViewById(R.id.clear).setOnClickListener(v -> {
//            patternGameInfo.getBaseMap().initMap();
//            patternBoardView.redraw();
//        });
//        findViewById(R.id.rebuild).setOnClickListener(v -> {
//            String ans = FileHelper.readFileFromSD(path);
//            patternGameInfo.getBaseMap().rebuild(ans);
//            patternBoardView.redraw();
//        });
        //findViewById(R.id.next).setOnClickListener(v -> );
    }

    private void upload() {
        String save_pattern = "http://fashare.host87.abc162.com/soul_of_chess/save_pattern.php";
        //startActivity(new Intent(this, IndexActivity.class));
        RequestParams params = new RequestParams();
        params.put("username", UserCenter.getInstance().getUser().getUsername());
        params.put("dir_name", patternFile.getDirName());
        params.put("name", patternFile.getName());
        params.put("data", patternGameInfo.getBaseMap().toString());
        new HttpHelper(params).setCallback((response) -> {

            Toast.makeText(this, "保存成功", Toast.LENGTH_LONG).show();
            setResult(RESULT_OK);
            finish();

        }).post(save_pattern);
    }

    private void newSaveDlg() {
        EditText et = new EditText(this);
        new AlertDialog.Builder(this).setTitle("保存为")
                .setView(et)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialogInterface, i) -> {
                    FileHelper.writeFileToSD(et.getText().toString() + ".txt", patternGameInfo.getBaseMap().toString());
                }).show();
    }

    private void newRebuildDlg() {
        EditText et = new EditText(this);
        new AlertDialog.Builder(this).setTitle("提取")
                .setView(et)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialogInterface, i) -> {
                    String ans = FileHelper.readFileFromSD(path);
                    patternGameInfo.getBaseMap().rebuild(ans);
                    patternBoardView.redraw();
                }).show();
    }

    private void initSelectView() {
        selectViews = new View[selectIds.length];
        for(int i=0; i<selectIds.length; i++) {
            selectViews[i] = findViewById(selectIds[i]);
            selectViews[i].setOnClickListener(v -> {
                if (patternGameInfo != null) {
                    int newIndex = getCurIndex(v.getId());
                    Log.d(TAG, "newIndex = " + newIndex);
                    if(newIndex == -1)  return ;

                    selectViews[curIndex].setBackgroundColor(Color.WHITE);
                    curIndex = newIndex;
                    patternGameInfo.selectCurPlayer(curIndex);
                    selectViews[curIndex].setBackgroundColor(Color.GRAY);
                } else
                    Log.e(TAG, "未绑定gameInfo");
            });
        }
        selectViews[curIndex].setBackgroundColor(Color.GRAY);
    }

    private int getCurIndex(int id) {
        for(int i=0; i<selectIds.length; i++)
            if(id == selectIds[i])
                return i;
        Log.e(TAG, "getCurIndex: id有误");
        return -1;
    }

    public void initModel() {
        patternGameInfo = new GameInfo(new PatternMap(GameInfo.DEFAULT_SIZE, GameInfo.DEFAULT_SIZE));
        patternBoardView.setGameInfo(patternGameInfo);
    }

    public void initController() {
        patternBoardView.setPatternController(new PatternController(patternBoardView, patternGameInfo));
    }

    public void loadData() {
        /*
        if(path != null) {
            String ans = FileHelper.readFileFromSD(path);
            patternGameInfo.getBaseMap().rebuild(ans);
            patternBoardView.redraw();
            //tv.setText(patternGameInfo.getBaseMap().toString());
        }else
            Log.d(TAG, "文件读取失败");
        */

        patternGameInfo.getBaseMap().rebuild(patternFile.getData());
        patternBoardView.redraw();
    }

}
