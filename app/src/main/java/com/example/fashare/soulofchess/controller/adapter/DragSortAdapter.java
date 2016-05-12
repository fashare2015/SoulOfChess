package com.example.fashare.soulofchess.controller.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fashare.soulofchess.PatternBrowserActivity;
import com.example.fashare.soulofchess.R;
import com.example.fashare.soulofchess.SetPatternActivity;
import com.example.fashare.soulofchess.model.PatternDir;
import com.example.fashare.soulofchess.model.PatternFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DragSortAdapter extends BaseAdapter {
    private static final String TAG = "DragSortAdapter";
    private Context context;
    private List<String> datas = new ArrayList<>(); //深拷贝
    private static String rowSeparator = "\n";

    public DragSortAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
    }

    public DragSortAdapter(Context context, String datasString) {
        this.context = context;
        this.datas = stringToData(datasString);
    }

    public List<String> getDatas() {
        return datas;
    }

    public String dataToString(){
//        String str = "";
//        for(String data: datas)
//            str += data + rowSeparator;
//        return str;
        return TextUtils.join(rowSeparator, datas);
    }

    public List<String> stringToData(String datasString) {
        return new ArrayList<String>( Arrays.asList(TextUtils.split(datasString, rowSeparator)) );
    }

    public void setDatas(List<String> datas) {
        //this.datas = datas;
        //要深拷贝
        this.datas.clear();
        for(String data: datas)
            this.datas.add(data);
    }

    //删除指定位置的item
    public void remove(int pos) {
        this.datas.remove(pos);
        this.notifyDataSetChanged();    //不要忘记更新适配器
    }

    //在指定位置插入item
    public void insert(String item, int pos) {
        this.datas.add(pos, item);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public String getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //优化ListView
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.dslv_item, null);
            ViewCache viewCache = new ViewCache();
            viewCache.tv = (TextView)convertView.findViewById(R.id.tv);
            convertView.setTag(viewCache);
        }
        ViewCache cache = (ViewCache)convertView.getTag();

        cache.tv.setText(datas.get(position));

        //cache.imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.dir));
        return convertView;
    }

    //元素的缓冲类,用于优化ListView
    private static class ViewCache{
        public TextView tv;
    }
}