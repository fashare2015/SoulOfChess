package com.example.fashare.soulofchess;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fashare.soulofchess.base.BaseActivity;
import com.example.fashare.soulofchess.component.file.FileHelper;
import com.example.fashare.soulofchess.component.http.HttpHelper;
import com.example.fashare.soulofchess.component.http.Person;
import com.example.fashare.soulofchess.config.UrlConstant;
import com.example.fashare.soulofchess.controller.adapter.base.PatternAdapter;
import com.example.fashare.soulofchess.model.PatternDir;
import com.example.fashare.soulofchess.model.PatternFile;
import com.example.fashare.soulofchess.model.User;
import com.example.fashare.soulofchess.model.UserCenter;
import com.example.fashare.soulofchess.view.PatternGridView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PatternBrowserActivity extends BaseActivity {
    private static final String TAG = "PatternBrowserActivity";
    public static final String IS_FILE = "isFile";
    public static final String POS = "pos";

    private GridView gridView;
    //private static final String[] strings=new String[]{"中国","俄罗斯","英国","法国"};
    private boolean isFile;
    private int pos;

    private List<PatternDir> datas = new ArrayList<>();
    private PatternAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pattern_browser);
        super.onCreate(savedInstanceState);
        initTitleBar(null, !isFile? "策 略 目 录": "策 略 文 件", v -> showAddDlg());
    }

    private void showAddDlg() {
        String dlgTitle = isFile? "新建策略": "新建目录";
        EditText et = new EditText(this);
        et.setHint("请输入名称");
        new AlertDialog.Builder(this).setTitle(dlgTitle)
                .setView(et)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialogInterface, i) -> {
                    editStrategy(et.getText().toString(), -1, false);
                }).show();
    }

    public boolean showModDlg(String oldName, int strategyId){
        LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.mod_dlg, null);
        AlertDialog dlg = new AlertDialog.Builder(this).setView(layout).show();

        layout.findViewById(R.id.edit).setOnClickListener(v -> {
            showEditDlg(oldName, strategyId);
            dlg.dismiss();
        });
        layout.findViewById(R.id.remove).setOnClickListener(v -> {
            showRemoveDlg(oldName, strategyId);
            dlg.dismiss();
        });
        return true;
    }

    private void showEditDlg(String oldName, int strategyId) {
        String dlgTitle = "编辑名称";
        EditText et = new EditText(this);
        et.setText(oldName);
        //et.setHint("请输入名称");
        new AlertDialog.Builder(this).setTitle(dlgTitle)
                .setView(et)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialogInterface, i) -> {
                    editStrategy(et.getText().toString(), strategyId, false);
                }).show();
    }

    private void showRemoveDlg(String oldName, int strategyId) {
        String dlgTitle = "删除策略";
        new AlertDialog.Builder(this).setTitle(dlgTitle)
                .setMessage(String.format("确定要移除\"%s\"吗", oldName))
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialogInterface, i) -> {
                    editStrategy("", strategyId, true);
                }).show();
    }

    private void editStrategy(String strategyName, int strategyId, boolean remove) {
        RequestParams params = new RequestParams();
        params.put("remove", remove? 1: 0);
        params.put("is_file", isFile ? 1 : 0);
        params.put("username", UserCenter.getInstance().getUser().getUsername());
        if(!isFile) {
            params.put("dir_id", strategyId);
            params.put("dir_name", strategyName);
        }else{
            params.put("dir_id", datas.get(pos).getDirId());
            params.put("dir_name", datas.get(pos).getName());
            params.put("pattern_id", strategyId);
            params.put("pattern_name", strategyName);
        }
        new HttpHelper(params).setCallback((response) -> {
            String flag = "";
            try {
                flag = response.getString("flag");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(flag.equals("success")){
                Toast.makeText(PatternBrowserActivity.this, "操作成功", Toast.LENGTH_LONG).show();
                loadData();
            }
        }).post(UrlConstant.ADD_STRATEGY);
    }

    public void initParam() {
        Intent intent = getIntent();
        isFile = intent.getBooleanExtra(IS_FILE, false);
        pos = intent.getIntExtra(POS, -1);
    }

    public void loadData() {
        //loadFromSdcard();

        //if(UserCenter.getInstance().getUser().getPatternDirList() == null) {
            String get_all_pattern_url = "http://fashare.host87.abc162.com/soul_of_chess/get_all_pattern.php";
            RequestParams params = new RequestParams();
            params.put("username", UserCenter.getInstance().getUser().getUsername());
            new HttpHelper(params).setArrayCallback((response) -> {
                Log.d(TAG, "----------------------begin-----------------------");
                //datas = HttpHelper.jsonToDirList(response);
                //datas = HttpHelper.<PatternDir>jsonToList(response);
                datas = new Gson().fromJson(response.toString(), new TypeToken<List<PatternDir>>() {
                }.getType());
                //TODO
                if (datas.get(0) instanceof PatternDir)
                    Log.d(TAG, datas.get(0).toString());
                else
                    Log.d(TAG, "不合法的类型, data.get(0):" + datas.get(0).getClass());
                Log.d(TAG, "----------------------end-----------------------");

                UserCenter.getInstance().getUser().setPatternDirList(datas);
                setDatas();
            }).post(get_all_pattern_url);
//        }else {
//            datas = UserCenter.getInstance().getUser().getPatternDirList();
//            setDatas();
//        }
    }

    private void setDatas() {
        if (!isFile)
            adapter.setDatas(datas);
        else
            adapter.setDatas(datas.get(pos).getFileList());
        //gridView.onRefreshComplete();
    }

    private void loadFromSdcard() {
        datas = FileHelper.readPatternDirs();
        UserCenter.getInstance().getUser().setPatternDirList(datas);
        if(!isFile)
            gridView.setAdapter(new PatternAdapter<PatternDir>(this, datas));
        else
            gridView.setAdapter(new PatternAdapter<PatternFile>(this, datas.get(pos).getFileList()));
    }

    private void jsonTest(){
        List<PatternFile> fileList = new ArrayList<PatternFile>();
        fileList.add(new PatternFile("1.txt"));
        fileList.add(new PatternFile("2.txt"));

        PatternDir in = new PatternDir("test", fileList), out;
        String str = new Gson().toJson(in, PatternDir.class);
        Log.d(TAG, "str = " + str);

        out = HttpHelper.jsonToObject(str, PatternDir.class);

        if(out instanceof PatternDir)
            Log.d(TAG, out.toString());
        else
            Log.d(TAG, "不合法的类型");
    }

    public void initView() {
        gridView = (GridView)findViewById(R.id.pgv);
        if(!isFile)
            adapter = new PatternAdapter<PatternDir>(this, new ArrayList<>());
        else
            adapter = new PatternAdapter<PatternFile>(this, new ArrayList<>());
        gridView.setAdapter(adapter);
        //gridView.setOnRefreshListener(base -> loadData());
//        gridView.setOnItemLongClickListener((parent, view, pos, id) -> {
//            if(adapter.getDatas().get(pos) instanceof PatternDir){
//                PatternDir item = (PatternDir)adapter.getDatas().get(pos);
//                showModDlg(item.getName(), item.getDirId());
//            }else if(adapter.getDatas().get(pos) instanceof PatternFile){
//                PatternFile item = (PatternFile)adapter.getDatas().get(pos);
//                showModDlg(item.getName(), item.getPatternId());
//            }else
//                ;//TODO
//            return true;
//        });

        //gridView.setAdapter(new ArrayAdapter<PatternDir>(this, android.R.layout.simple_list_item_1, datas));
    }

    public void initModel() {
    }

    public void initController() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
            loadData();
    }
}
