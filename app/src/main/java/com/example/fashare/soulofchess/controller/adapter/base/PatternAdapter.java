package com.example.fashare.soulofchess.controller.adapter.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fashare.soulofchess.PatternBrowserActivity;
import com.example.fashare.soulofchess.R;
import com.example.fashare.soulofchess.SetPatternActivity;
import com.example.fashare.soulofchess.component.http.HttpHelper;
import com.example.fashare.soulofchess.config.UrlConstant;
import com.example.fashare.soulofchess.model.PatternDir;
import com.example.fashare.soulofchess.model.PatternFile;
import com.example.fashare.soulofchess.model.UserCenter;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;

import java.util.List;

/**
 * Created by apple on 2016/2/28.
 */
public class PatternAdapter<T> extends BaseAdapter{
    private static final String TAG = "PatternAdapter<T>";
    private Context context;
    //private List<PatternDir> datas;
    private List<T> datas; //类型擦除后, 相当于List<Object>

    public List<T> getDatas() {
        return datas;
    }

    public PatternAdapter(Context context, List<T> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas==null? 0: datas.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //优化ListView
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.pattern_dir_item, null);
            ViewCache viewCache = new ViewCache();
            viewCache.dirItemLayout = (LinearLayout)convertView.findViewById(R.id.layout_dir_item);
            viewCache.imageView = (ImageView)convertView.findViewById(R.id.iv_img);
            viewCache.textView = (TextView)convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewCache);
        }
        ViewCache cache = (ViewCache)convertView.getTag();

        //设置文本和图片，然后返回这个View，用于ListView的Item的展示
        if(datas.get(position) instanceof PatternDir){
            PatternDir item = (PatternDir)datas.get(position);
            cache.textView.setText(item.getName());
            cache.textView.setBackgroundResource(R.drawable.dir_bg);
            //cache.imageView.setImageResource(R.drawable.dir_bg);
            cache.dirItemLayout.setOnClickListener(v -> context.startActivity(
                new Intent(context, context.getClass())
                .putExtra(PatternBrowserActivity.IS_FILE, true)
                .putExtra(PatternBrowserActivity.POS, position)
            ));

            cache.dirItemLayout.setOnLongClickListener(v ->
                ((PatternBrowserActivity)context).showModDlg(item.getName(), item.getDirId())
            );

        }else if(datas.get(position) instanceof PatternFile){
            PatternFile item = (PatternFile)datas.get(position);
            cache.textView.setText(item.getName());
            cache.textView.setBackgroundResource(R.drawable.pattern_bg);
            //cache.imageView.setImageResource(R.drawable.pattern_bg);
            cache.dirItemLayout.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putSerializable(SetPatternActivity.PATTERN_FILE, item);
                context.startActivity(
                        new Intent(context, SetPatternActivity.class)
                                .putExtra(SetPatternActivity.PATH, item.getPath())
                                .putExtras(bundle));
            });

            cache.dirItemLayout.setOnLongClickListener(v ->
                            ((PatternBrowserActivity) context).showModDlg(item.getName(), item.getPatternId())
            );

        }else
            Log.d(TAG, "不合法的类型T");

        //cache.imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.dir));
        return convertView;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    //元素的缓冲类,用于优化ListView
    private static class ViewCache{
        public LinearLayout dirItemLayout;
        public ImageView imageView;
        public TextView textView;
    }
}



