package com.example.fashare.soulofchess;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.fashare.soulofchess.base.BaseActivity;
import com.example.fashare.soulofchess.component.http.HttpHelper;
import com.example.fashare.soulofchess.config.UrlConstant;
import com.example.fashare.soulofchess.controller.adapter.PullToRefreshAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2016/4/18.
 */
public class RankActivity extends BaseActivity {
    private static final String TAG = "RankActivity";

    private PullToRefreshListView plv;
    private PullToRefreshAdapter adapter;
    private List<String> usernames = new ArrayList<>();
    private TextView myRank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.rank);
        super.onCreate(savedInstanceState);

        initTitleBar("排 行 榜");
    }

    public void initParam() {
        //path = getIntent().getStringExtra(PATH);
    }

    public void loadData() {
        new HttpHelper().setArrayCallback((response) -> {
            usernames = HttpHelper.jsonToList(response);
            adapter.setUsernames(usernames);
            //myRank.setText(String.format("我的排名：第%d名", usernames.size()+1));
            myRank.setText(String.format("我的排名：第%d名", 1));
            plv.onRefreshComplete();
        }).post(UrlConstant.GET_RANK_LIST);
    }

    public void initView() {
        plv = (PullToRefreshListView) findViewById(R.id.plv);
        if(plv != null) {
            adapter = new PullToRefreshAdapter(this, usernames);
            plv.setAdapter(adapter);
            plv.setOnRefreshListener(base -> loadData());
        }else
            Log.d(TAG, "plv is null");

        myRank = (TextView)findViewById(R.id.my_rank);
    }

}
