package com.example.fragment;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.kimma_test_ui_hs.LoginActivity;
import com.example.kimma_test_ui_hs.MainActivity;
import com.example.kimma_test_ui_hs.R;
import com.example.kimma_test_ui_hs.WorkActivity;
import com.example.tools.Tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HeterogeneousExpandableList;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class StorehouseFragment extends Fragment{
	private static final String TAG = "StorehouseFragment";  
	private Button bt_start,bt_end;
	private HttpClient httpClient;
	private Tools tool = new Tools();
	private String url = Tools.getUrl()+"work.do"; 
	private WorkActivity activity = (WorkActivity)getActivity();
	private static String address = "";
	private Handler handler = new Handler(){
    	@Override
        public void handleMessage(Message msg) {
    		try {
    			String s = msg.obj.toString();
    			activity.tx_http_connect_state.setText(s);
    	    }catch (Exception e) {
                e.printStackTrace();
    	    }
     }
    };

    
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Log.d(TAG, "StorehouseFragment-----onCreateView");   
        View view = inflater.inflate(R.layout.fragment_storehouse, container, false);  
        
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("ADDRESS",Activity.MODE_PRIVATE); 
		address = sharedPreferences.getString("address", "");
        httpClient = tool.initHttp();
        bt_start = (Button)view.findViewById(R.id.bt_start);
        bt_end = (Button)view.findViewById(R.id.bt_end);
        bt_start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final EditText inputServer = new EditText(getActivity());
		        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		        builder.setTitle("商品发货地信息").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
		                .setNegativeButton("取消", null);
		        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		               inputServer.getText().toString();
		               if(inputServer.getText().toString().equals("")){
		            	  Toast.makeText(getActivity(), "商品发货地不能为空!",Toast.LENGTH_SHORT).show(); 
		               }else{
		            	  //其他操作
		            	   System.out.println("获取地址:"+address);
		            	   if(!address.equals("")){
		            		   Map<String,String> map = new HashMap<String,String>();
			            	   map.put("tag", "departure");
			            	   map.put("intelligentBoxNumber", address);
			            	   map.put("departurePlace",inputServer.getText().toString());
			            	   readNet(url, map);
		            	   }else{
		            		   Toast.makeText(getActivity(), "一会再来",Toast.LENGTH_SHORT).show();
		            	   }
		            	   
		               }
		             }
		        });
		        builder.show();
			}
		});
        
        
         bt_end.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final EditText inputServer = new EditText(getActivity());
		        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		        builder.setTitle("商品收货地信息").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
		                .setNegativeButton("取消", null);
		        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		               inputServer.getText().toString();
		               if(inputServer.getText().toString().equals("")){
		            	  Toast.makeText(getActivity(), "商品收货地不能为空!",Toast.LENGTH_SHORT).show(); 
		               }else{
		            	   if(!address.equals("")){
		            		   Map<String,String> map = new HashMap<String,String>();
			            	   map.put("tag", "arrival");
			            	   map.put("intelligentBoxNumber", address);
			            	   map.put("arrivalPlace",inputServer.getText().toString());
			            	   readNet(url, map);
		            	   }else{
		            		   Toast.makeText(getActivity(), "一会再来",Toast.LENGTH_SHORT).show();
		            	   }
		               }
		             }
		        });
		        builder.show();
			}
		});
        return view; 
		
	}  
	
	@SuppressWarnings("unchecked")
	public void readNet(final String url,Map<String,String> map){
		new AsyncTask<Map<String,String> , Void, String>() {
            @Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				//有BUG
				if(result.contains("success")){
                    Toast.makeText(getActivity(),"操作成功", Toast.LENGTH_SHORT).show();					
				}else if(result.contains("fail")){
					Toast.makeText(getActivity(),"操作失败", Toast.LENGTH_SHORT).show();
				}else {
					System.out.println("无数据传回");
					Toast.makeText(getActivity(),"HTTP请求失败，请检查网络", Toast.LENGTH_LONG).show();
				}
			}
			@Override
			protected String doInBackground(Map<String,String>... arg0) {
                String value = "null";
                HttpPost post = new HttpPost(url);
              //解决中文乱码问题
                post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                try {
					List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>(); 
					int size = arg0[0].size();
					System.out.println("size:"+size);
					int i = 0;
					while(i<size){
						list.add(new BasicNameValuePair(arg0[0].keySet().toArray()[i].toString(), arg0[0].get(arg0[0].keySet().toArray()[i].toString())));
						System.out.println("Key:"+arg0[0].keySet().toArray()[i].toString()+" "+"value:"+arg0[0].get(arg0[0].keySet().toArray()[i].toString()));
						i++;
					}		
					HttpEntity entity = new UrlEncodedFormEntity(list,HTTP.UTF_8);
					post.setEntity(entity);
				} catch (UnsupportedEncodingException e1) {}
                try {
					HttpResponse respone = httpClient.execute(post);
					int state = respone.getStatusLine().getStatusCode()/100;
					//System.out.println("响应状态码:"+respone.getStatusLine().getStatusCode());
					if(state == 1){
						System.out.println("临时响应");
					}else if(state == 2){
						value = EntityUtils.toString(respone.getEntity());
						System.out.println("value:"+value);
					}else if(state == 3){
						System.out.println("重定向");
					}else if(state == 4){
						System.out.println("请求错误");
					}else if(state == 5){
						System.out.println("服务器错误");
					}
				}  catch (Exception e) {
					System.out.println("IOException");
					String exception = tool.ExceptionCode(e);
					System.out.println(exception);
					e.printStackTrace();
					Message msg = handler.obtainMessage();
					msg.obj = exception;
					handler.sendMessage(msg);					
				}
				return value;
			}
		}.execute(map);
	}
	
 
}
