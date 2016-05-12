package com.example.fashare.soulofchess.component.file;

import android.os.Environment;
import android.util.Log;

import com.example.fashare.soulofchess.model.GameInfo;
import com.example.fashare.soulofchess.model.PatternDir;
import com.example.fashare.soulofchess.model.PatternFile;
import com.example.fashare.soulofchess.model.PatternMap;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by apple on 2016/2/14.
 */
public class FileHelper {
    private static final String TAG = "FileHelper";
    public static String[] patternOrders = {"五连珠", "活四", "活三", "活二", "活一"};

    public static String basePath;
    public static String baseAIPath;

    static{
        baseAIPath = basePath = Environment.getExternalStorageDirectory().toString() + "/soulOfChess";
        File destDir = new File(basePath);
        if(!destDir.exists())   destDir.mkdir();

        basePath += "/pattern/";
        destDir = new File(basePath);
        if(!destDir.exists())   destDir.mkdir();

        baseAIPath += "/AI/";
        destDir = new File(baseAIPath);
        if(!destDir.exists())   destDir.mkdir();
        /*
        basePath += "/活四/";
        destDir = new File(basePath);
        if(!destDir.exists())   destDir.mkdir();
        */

    }

    //写数据到SD中的文件
    /*
    public static void writeFileToSD(String fileName, String str){
        try {
            fileName = basePath + fileName;
            checkFile(fileName, true);
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(str.getBytes());
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    */

    public static void writeFileToSD(String absolutePath, String data){
        try {
            //checkFile(absolutePath, true);
            File file = new File(absolutePath);
            if(!file.exists())  file.createNewFile();

            FileOutputStream fos = new FileOutputStream(absolutePath);
            fos.write(data.getBytes());
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
    private static void checkFile(String absolutePath, boolean isWrite) {
        File file = new File(absolutePath);
        if(!file.exists()) {
            try {
                file.createNewFile();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    */

    //读SD中的文件
    /*
    public static String readFileFromSD(String fileName){
        fileName = basePath + fileName;
        String res = "";
        try {
            FileInputStream fis = new FileInputStream(fileName);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");
            fis.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }
    */
    public static String readFileFromSD(String absolutePath){
        String data = "";
        try {
            File file = new File(absolutePath);
            if(!file.exists())  return null;

            FileInputStream fis = new FileInputStream(absolutePath);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            data = EncodingUtils.getString(buffer, "UTF-8");
            fis.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }

    // todu
    public static List< List<PatternMap> > readPatterns(){
        List< List<PatternMap> > patternMaps = new ArrayList<>();

        for(int i=0; i<patternOrders.length; i++){
            String destPath = basePath + "/" + patternOrders[i];
            File destDir = new File(destPath);
            if(!destDir.exists())   continue;

            List<PatternMap> tmpMapList = new ArrayList<>();
            File[] files = destDir.listFiles();
            Arrays.sort(files, (a, b) -> getOrder(a.getName()) - getOrder(b.getName()));
            //事先排好序
            for(File file: files){
                PatternMap tmpMap = new PatternMap(GameInfo.DEFAULT_SIZE, GameInfo.DEFAULT_SIZE);
                tmpMap.rebuild( readFileFromSD(file.getPath()) );
                //String filePath = patternOrders[i] + "/" + file.getName();
                //tmpMap.rebuild(readFileFromSD(filePath));
                tmpMap.cut();
                tmpMapList.add(tmpMap);

                /*
                Log.d(file.getName(), "-----------------------------");
                //Log.d("chessMap: i", chessMap.getStartXY().y + " - " + chessMap.getEndXY().y);
                //Log.d("chessMap: j", chessMap.getStartXY().x + " - " + chessMap.getEndXY().x);
                Log.d("map: i*j", tmpMap.getEndXY().y-tmpMap.getStartXY().y + " * "
                        + (tmpMap.getEndXY().x-tmpMap.getStartXY().x+""));
                Log.d(file.getName(), "-----------------------------");
                */
            }
            patternMaps.add(tmpMapList);
        }

        return patternMaps;
    }

    private static int getOrder(String name) {
        for(int i=0; i<patternOrders.length; i++)
            if(name.equals(patternOrders[i]))
                return i;
        return -1;
    }

    public static List<PatternDir> readPatternDirs(){
        String destPath = basePath;
        File destDir = new File(destPath);
        if(!destDir.exists()){
            Log.e(TAG, "未找到pattern文件夹");
            return null;
        }

        List<PatternDir> dirList = new ArrayList<>();
        File[] dirs = destDir.listFiles();
        for(File dir: dirs)
            if(dir.isDirectory()) {
                List<PatternFile> fileList = new ArrayList<>();
                File[] files = dir.listFiles();
                for (File file : files)
                    fileList.add(new PatternFile(file.getName(), file.getPath(), dir.getName(), readFileFromSD(file.getPath())));
                dirList.add(new PatternDir(dir.getName(), fileList));
            }
        return dirList;
    }

}
