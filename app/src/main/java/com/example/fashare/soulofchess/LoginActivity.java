package com.example.fashare.soulofchess;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fashare.soulofchess.base.BaseActivity;
import com.example.fashare.soulofchess.component.file.FileHelper;
import com.example.fashare.soulofchess.component.http.HttpHelper;
import com.example.fashare.soulofchess.config.UrlConstant;
import com.example.fashare.soulofchess.controller.GameController;
import com.example.fashare.soulofchess.model.ChessMap;
import com.example.fashare.soulofchess.model.GameInfo;
import com.example.fashare.soulofchess.model.User;
import com.example.fashare.soulofchess.model.UserCenter;
import com.example.fashare.soulofchess.view.ChessBoardView;
import com.example.fashare.soulofchess.view.PatternBoardView;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;

import java.net.URLConnection;


public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        super.onCreate(savedInstanceState);

        //UserCenter.getInstance().setUser(new User("123456"));
        //startActivity(new Intent(this, IndexActivity.class));

    }

    public void initView() {
        findViewById(R.id.login).setOnClickListener(v -> login());
        findViewById(R.id.rgst).setOnClickListener(v -> newRgstDlg());
    }

    private void login() {
        RequestParams params = new RequestParams();
        String username = ((EditText)findViewById(R.id.login_usn)).getText().toString(),
                password = ((EditText)findViewById(R.id.login_pwd)).getText().toString();
        params.put("username", username);
        params.put("password", password);
        new HttpHelper(params).setCallback((response) -> {
            String flag = "";
            try {
                flag = response.getString("flag");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(flag.equals("success")) {
                UserCenter.getInstance().setUser(new User(username));
                Toast.makeText(this, "欢迎回来", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, IndexActivity.class));
            }else
                Toast.makeText(this, "用户名或密码有误", Toast.LENGTH_LONG).show();

        }).post(UrlConstant.LOGIN);
    }

    private void newRgstDlg() {
        String rgst_url = "http://fashare.host87.abc162.com/soul_of_chess/register.php";
        View rgst_layout = (View)getLayoutInflater().inflate(R.layout.rgst_area, null);
        AlertDialog dlg = new AlertDialog.Builder(this).setTitle("用户注册")
                .setView(rgst_layout)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialogInterface, i) -> {

                    RequestParams params = new RequestParams();
                    params.put("username", ((EditText)rgst_layout.findViewById(R.id.rgst_usn)).getText().toString());
                    params.put("password", ((EditText)rgst_layout.findViewById(R.id.rgst_pwd)).getText().toString());
                    new HttpHelper(params).setCallback((response) -> {

                        try {
                            Toast.makeText(LoginActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }).post(rgst_url);

                }).show();
    }

    public void initModel() {
    }

    public void initController() {
    }

}
