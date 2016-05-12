package com.example.fashare.soulofchess.component.http;

import android.util.Log;
import android.view.SurfaceHolder;

import org.apache.http.Header;

import com.example.fashare.soulofchess.model.AIInfo;
import com.example.fashare.soulofchess.model.PatternDir;
import com.example.fashare.soulofchess.model.PatternFile;
import com.example.fashare.soulofchess.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HttpHelper {
	private static final String TAG = "HttpHelper";
	private AsyncHttpClient client;
	private RequestParams params;
	private String ans=null;
	private Callback callback;
	private ArrayCallback arrayCallback;
	private StringCallback stringCallback;


	public HttpHelper setParams(RequestParams params) {
		this.params = params;
		return this;
	}

	public HttpHelper setCallback(Callback callback) {
		this.callback = callback;
		return this;
	}

	public HttpHelper setArrayCallback(ArrayCallback arrayCallback) {
		this.arrayCallback = arrayCallback;
		return this;
	}

	public HttpHelper setStringCallback(StringCallback stringCallback) {
		this.stringCallback = stringCallback;
		return this;
	}

	public HttpHelper(RequestParams params) {
		this.client = new AsyncHttpClient();
		this.params = params;
	}

	public HttpHelper() {
		this.client = new AsyncHttpClient();
		this.params = new RequestParams();
	}

	public void post(String url){
        client.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
				//super.onSuccess(statusCode, headers, response);
				//if (statusCode == 200) {
				ans = response.toString();
				Log.d("from php:", response.toString());
				Log.d("statusCode :", statusCode+" ");
				//Toast.makeText(getApplicationContext(), "�ϴγɹ�", 1)  .show();
				try {
					//if (response.get("flag").equals("success")){
						if(callback != null)
							callback.onSuccess(response);
						else{
							//Log.e();
						}
					//}else{
						//tudo
						;
					//}

				}catch (Exception e){
					e.printStackTrace();
				}
			}

			@Override
			public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
				//super.onSuccess(statusCode, headers, response);
				//if (statusCode == 200) {
				ans = response.toString();
				Log.d("from php:", response.toString());
				Log.d("statusCode :", statusCode+" ");
				//Toast.makeText(getApplicationContext(), "�ϴγɹ�", 1)  .show();
				try {
					//if (response.get("flag").equals("success")){
						if(arrayCallback != null)
							arrayCallback.onSuccess(response);
						else{
							//Log.e();
						}


				}catch (Exception e){
					e.printStackTrace();
				}
			}

			@Override
			public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
				Log.d("from php:", responseString);
				Log.d("statusCode :", statusCode+" ");

				if(stringCallback != null) {
					Log.e(TAG, "stringCallback.onSuccess is invoked");
					stringCallback.onSuccess(responseString);
				}else{
					Log.e(TAG, "stringCallback is null");
				}
			}

			@Override
			public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
				//super.onFailure(statusCode, headers, throwable, errorResponse);
				if(errorResponse != null)
					Log.d("from php:", errorResponse.toString());
				Log.d("statusCode :", statusCode+" ");
			}

			@Override
			public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
				//super.onFailure(statusCode, headers, throwable, errorResponse);
				Log.d("from php:", errorResponse.toString());
				Log.d("statusCode :", statusCode+" ");
			}

			@Override
			public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
				//super.onFailure(statusCode, headers, responseString, throwable);
				Log.d("from php:", responseString);
				Log.d("statusCode :", statusCode+" ");
			}
		});

	}


	public static List<PatternDir> jsonToDirList(JSONArray dirArray) {
		List<PatternDir> patternDirList = new ArrayList<>();
		try {
			int i=0;
			while(i < dirArray.length()){
				JSONObject dirObj = dirArray.getJSONObject(i ++);
				String dirName = dirObj.getString("dir_name");
				JSONArray patArray = dirObj.getJSONArray("pat_list");

				List<PatternFile> patternFileList = new ArrayList<>();
				int j=0;
				while(j < patArray.length()){
					JSONObject patObj = patArray.getJSONObject(j ++);
					String patName = patObj.getString("pat_name");
					String data = patObj.getString("data");
					patternFileList.add(new PatternFile(patName, null, dirName, data));
				}

				patternDirList.add(new PatternDir(dirName, patternFileList));
			}

		}catch (Exception e){
			e.printStackTrace();
		}

		Log.d("HttpHelper", "patternDirList 加载完成");
		if(patternDirList == null)
			Log.e("HttpHelper", "patternDirList is null");
		return patternDirList;
	}

	public static List<AIInfo> jsonToAiList(JSONArray aiArray) {
		List<AIInfo> aiInfoList = new ArrayList<>();
		try {
			int i=0;
			while(i < aiArray.length()){
				JSONObject dirObj = aiArray.getJSONObject(i++);
				String aiName = dirObj.getString("ai_name");
				String data = dirObj.getString("data");

				aiInfoList.add(new AIInfo(aiName, data));
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		Log.d("HttpHelper", "aiInfoList 加载完成");
		if(aiInfoList == null)
			Log.e("HttpHelper", "aiInfoList is null");
		return aiInfoList;
	}

	public static <T> List<T> jsonToList(JSONArray jsonArray) {
		return jsonToList(jsonArray.toString());
	}

	public static <T> List<T> jsonToList(String jsonString) {
		//new Gson().fromJson(response.toString(), new TypeToken<List<PatternDir>>(){}.getType());

		return new Gson().fromJson(jsonString, new TypeToken<List<T>>() {
		}.getType());
	}

	public static <T> T jsonToObject(JSONObject jsonObject, Class<T> cls) {
		return jsonToObject(jsonObject.toString(), cls);
	}

	public static <T> T jsonToObject(String jsonString, Class<T> cls) {
		return new Gson().fromJson(jsonString, cls);
	}


	public interface Callback{
		void onSuccess(JSONObject response);
	}

	public interface ArrayCallback{
		void onSuccess(JSONArray response);
	}

	public interface StringCallback{
		void onSuccess(String responseString);
	}
}
