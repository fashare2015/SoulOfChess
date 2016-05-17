package com.example.fashare.soulofchess.controller.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fashare.soulofchess.PkActivity;
import com.example.fashare.soulofchess.R;
import com.example.fashare.soulofchess.component.http.HttpHelper;
import com.example.fashare.soulofchess.config.UrlConstant;
import com.example.fashare.soulofchess.model.RankCenter;
import com.example.fashare.soulofchess.model.User;
import com.loopj.android.http.RequestParams;

import java.util.List;

public class PullToRefreshAdapter extends BaseAdapter {
    private static final String TAG = "PullToRefreshAdapter";
    private Context context;
    private List<String> usernames;

    public PullToRefreshAdapter(Context context, List<String> datas) {
        this.context = context;
        this.usernames = datas;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return usernames.size();
    }

    @Override
    public String getItem(int i) {
        return usernames.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //优化ListView
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.rank_item, null);
            ViewCache viewCache = new ViewCache();
            viewCache.layout = (LinearLayout)convertView.findViewById(R.id.layout_rank_item);
            viewCache.rank = (TextView)convertView.findViewById(R.id.rank);
            viewCache.nickname = (TextView)convertView.findViewById(R.id.tv);
            convertView.setTag(viewCache);
        }
        ViewCache cache = (ViewCache)convertView.getTag();

        cache.rank.setText(String.format("第%d名:", position+1));
        cache.nickname.setText(usernames.get(position));
        cache.layout.setOnClickListener(v -> {
            loadPlayerInfo(usernames.get(position));
        });

        return convertView;
    }

    private void loadPlayerInfo(String playerName) {
        RequestParams params = new RequestParams();
        params.put("username", playerName);
        new HttpHelper(params).setCallback(response -> {
            User player = HttpHelper.jsonToObject(response, User.class);
            RankCenter.getInstance().setCurPlayerName(playerName);
            RankCenter.getInstance().putPlayer(playerName, player);
            showDlg(context, player);
        }).post(UrlConstant.GET_USER_BY_USERNAME);
    }

    private void showDlg(Context context, User player) {
        new AlertDialog.Builder(context).setTitle(player.getUsername())
                .setNegativeButton("取消", null)
                .setPositiveButton("挑战", (dialogInterface, i) -> {
                    ((Activity)context).startActivityForResult(
                            new Intent(context, PkActivity.class)
                                    .putExtra("hasTwoAI", true),
                            0);
                }).show();
    }

    //元素的缓冲类,用于优化ListView
    private static class ViewCache{
        private LinearLayout layout;
        public TextView rank, nickname;
    }
}