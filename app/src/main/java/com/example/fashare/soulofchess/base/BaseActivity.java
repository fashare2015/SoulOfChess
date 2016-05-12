package com.example.fashare.soulofchess.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fashare.soulofchess.R;


public class BaseActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        initParam();

        initView();
        initModel();
        initController();

        loadData();

    }

    public void initTitleBar(String title) {
        ((TextView)findViewById(R.id.tb_title)).setText(title);
    }

    public void initTitleBar(View.OnClickListener leftListener, String title, View.OnClickListener rightListener) {
        initTitleBar(R.drawable.add, leftListener, title, R.drawable.add, rightListener);
    }

    public void initTitleBar(String title, int rightRes, View.OnClickListener rightListener) {
        initTitleBar(R.drawable.add, null, title, rightRes, rightListener);
    }

    public void initTitleBar(int leftRes, View.OnClickListener leftListener, String title) {
        initTitleBar(leftRes, leftListener, title, R.drawable.add, null);
    }

    public void initTitleBar(int leftRes, View.OnClickListener leftListener, String title, int rightRes, View.OnClickListener rightListener) {
        ((TextView)findViewById(R.id.tb_title)).setText(title);
        ImageView leftIv = (ImageView)findViewById(R.id.tb_left);
        ImageView rightIv = (ImageView)findViewById(R.id.tb_right);
        if(leftListener != null){
            leftIv.setImageResource(leftRes);
            leftIv.setVisibility(View.VISIBLE);
            leftIv.setOnClickListener(leftListener);
        }

        if(rightListener != null){
            rightIv.setImageResource(rightRes);
            rightIv.setVisibility(View.VISIBLE);
            rightIv.setOnClickListener(rightListener);
        }
    }

    public void initParam() {
    }

    public void loadData() {
    }

    public void initView() {
    }

    public void initModel() {
    }

    public void initController() {
    }

}
