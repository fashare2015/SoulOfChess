package com.example.fashare.soulofchess;

import android.content.Intent;
import android.os.Bundle;

import com.example.fashare.soulofchess.base.BaseActivity;
import com.example.fashare.soulofchess.component.http.HttpHelper;
import com.example.fashare.soulofchess.controller.adapter.base.PatternAdapter;
import com.example.fashare.soulofchess.model.AIInfo;
import com.example.fashare.soulofchess.model.PatternDir;
import com.example.fashare.soulofchess.model.PatternFile;
import com.example.fashare.soulofchess.model.UserCenter;
import com.loopj.android.http.RequestParams;

import java.util.List;


public class AIBrowserActivity extends BaseActivity {

    private List<AIInfo> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.ai_browser);
        super.onCreate(savedInstanceState);
        initTitleBar("我 的 A I");
    }

    @Override
    public void loadData() {
        if(UserCenter.getInstance().getUser().getAiInfoList() == null){
            String get_all_ai_url = "http://fashare.host87.abc162.com/soul_of_chess/get_all_ai.php";
            RequestParams params = new RequestParams();
            params.put("username", UserCenter.getInstance().getUser().getUsername());
            new HttpHelper(params).setArrayCallback((response) -> {
                datas = HttpHelper.jsonToAiList(response);
                UserCenter.getInstance().getUser().setAiInfoList(datas);
            }).post(get_all_ai_url);
        }else {
            datas = UserCenter.getInstance().getUser().getAiInfoList();

        }
    }

    public void initView() {
        findViewById(R.id.btn_my_ai).setOnClickListener(v -> setAI());
        findViewById(R.id.ai_test).setOnClickListener(v -> TestAI());
    }

    private void setAI() {
        startActivity(new Intent(this, SetAIActivity.class));
    }

    private void TestAI() {
        startActivity(new Intent(this, PkActivity.class));
    }

    public void initModel() {
    }

    public void initController() {
    }

}
