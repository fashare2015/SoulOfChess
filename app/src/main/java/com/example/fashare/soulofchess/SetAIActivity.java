package com.example.fashare.soulofchess;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;


import com.example.fashare.soulofchess.base.BaseActivity;
import com.example.fashare.soulofchess.component.http.HttpHelper;
import com.example.fashare.soulofchess.controller.adapter.DragSortAdapter;
import com.example.fashare.soulofchess.model.PatternDir;
import com.example.fashare.soulofchess.model.UserCenter;
import com.loopj.android.http.RequestParams;
import com.mobeta.android.dslv.DragSortListView;

import java.util.ArrayList;
import java.util.List;


public class SetAIActivity extends BaseActivity {
    private static final String TAG = "SetAIActivity";
    public static final String PATH = "PATH";

    private DragSortListView dslv;
    private DragSortAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.set_ai);
        super.onCreate(savedInstanceState);
        initTitleBar(R.drawable.edit, v -> newSelectDlg(), "所 选 策 略", R.drawable.confirm, v -> saveAI());
    }

    public void initParam() {
        //path = getIntent().getStringExtra(PATH);
    }

    public void loadData() {
        String datas = UserCenter.getInstance().getUser().getAiInfoList().get(0).getData();
        adapter = new DragSortAdapter(this, datas);
        dslv.setAdapter(adapter);
    }

    public void initView() {
        dslv = (DragSortListView)findViewById(R.id.dslv);
        dslv.setDropListener((from, to) -> {
            String item = adapter.getItem(from);
            adapter.remove(from);
            adapter.insert(item, to);
        });
        dslv.setDragEnabled(true);

        //findViewById(R.id.load).setOnClickListener(v -> newSelectDlg());
        //findViewById(R.id.save).setOnClickListener(v -> saveAI());
                //FileHelper.writeFileToSD(FileHelper.baseAIPath + "ai.txt", adapter.dataToString()));
    }

    private void newSelectDlg() {
        List<PatternDir> dirList = UserCenter.getInstance().getUser().getPatternDirList();
        String[] names = new String[dirList.size()];
        for(int i=0; i<dirList.size(); i++)
            names[i] = dirList.get(i).getName();

        //复选框默认值：false=未选; true=选中 ,各自对应items[i]
        final boolean[] selected = new boolean[names.length];
        for(int i=0; i<selected.length; i++)
            selected[i] = false;

        //创建对话框
        new AlertDialog.Builder(this).setTitle("选择策略")
                .setMultiChoiceItems(names, selected, (dialog, pos, isChecked) -> selected[pos] = isChecked)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialogInterface, which) -> {
                    List<String> tmpList = new ArrayList<>();
                    for (int i = 0; i < selected.length; i++)
                        if (selected[i])
                            tmpList.add(names[i]);
                    adapter.setDatas(new ArrayList<>(tmpList));
                    adapter.notifyDataSetChanged();
                }).show();
    }

    private void saveAI() {
        String save_ai = "http://fashare.host87.abc162.com/soul_of_chess/save_ai.php";
        //startActivity(new Intent(this, IndexActivity.class));
        RequestParams params = new RequestParams();
        params.put("username", UserCenter.getInstance().getUser().getUsername());
        params.put("name", "AI一号");
        params.put("data", adapter.dataToString());
        new HttpHelper(params).setCallback((response) -> {

            Toast.makeText(this, "保存成功", Toast.LENGTH_LONG).show();
            finish();

        }).post(save_ai);
    }

    public void initModel() {
    }

    public void initController() {
    }



}
