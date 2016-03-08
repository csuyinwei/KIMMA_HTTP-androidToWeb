package com.example.fragment;



import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.kimma_test_ui_hs.LoginActivity;
import com.example.kimma_test_ui_hs.R;
import com.example.kimma_test_ui_hs.SourceChartActivity;
import com.example.tools.Tools;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/*
 * 查询模块
 * 功能：溯源某一个售货机的商品历史数据，可模糊查询
 *     进入查村模块，向服务器提交申请，获得所有收货机编号并在ListView中显示，
 *     点击摸一个Item,弹出输入托盘号和货道号的输入框，将数据封装传回服务器
 *     服务器传回该商品的各个阶段历史数据，分阶段显示
 */
@SuppressLint({ "InflateParams", "HandlerLeak" })
public class QueryFragment extends ListFragment {	
	
	private static String regEx1 = "^[0-9]{1,2}$";//匹配正整数 ,用于匹配上货数量，货道号
    private static String regEx3 = "^[A-Za-z]{1}$";//用于匹配托盘号
    
	
	OnArticleSelectedListener_QueryFregment mListener;
	private ArrayAdapter<String> adapter;
	private List<String> lists = new ArrayList<String>();
	private EditText seach;
	private String [] Items;
	private String [] values = null;
	private HttpClient httpClient;
	private Tools tool = new Tools();
//	private String url = Tools.getUrl()+"login.do";
	private String url_0 = Tools.getUrl()+"vender/doListAll";
	private String url_1 = Tools.getUrl()+"temperature/getProcess";
	private List<Map<String,String>> list = new ArrayList<Map<String,String>>();
	
	public interface OnArticleSelectedListener_QueryFregment{          
		public void onArticleSelected(String str);
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try{               
			mListener =(OnArticleSelectedListener_QueryFregment)activity;
	    }catch(Exception e){
	    	e.setStackTrace(null);
	    }
	}
	
    private Handler handler = new Handler(){
    	@Override
        public void handleMessage(Message msg) {
    		try {
    			String s = msg.obj.toString();
    			LoginActivity actity = (LoginActivity)getActivity();
    			actity.tx_connect_state.setText(s+" HTTP请求失败");
    		
    	    }catch (Exception e) {
                e.printStackTrace();
    	    }
     }
    };

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		final View rootView = inflater.inflate(R.layout.fragment_query, container,false);
		
		httpClient = tool.initHttp();
		readNet(url_0, new HashMap<String,String>());
//		readNet(url, new HashMap<String,String>());
		lists.clear();
		seach = (EditText)rootView.findViewById(R.id.query_input);
		seach.addTextChangedListener(watcher);
        return rootView;
    }
	
	TextWatcher watcher = new TextWatcher(){

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			lists.clear();
			System.out.println(arg0.toString());
			ArrayList<String> list = new ArrayList<String>();
			for(int i = 0;i<Items.length;i++){
				if(Items[i].contains(arg0.toString())){
					list.add(Items[i]);
				}
				continue;
			}
			int size = list.size();
			String[] temp = new String[size];
			for(int j = 0;j<size;j++){
				temp[j] = (String)list.get(j);
			}
			System.out.println("size:"+temp.length);
			
			initListView(temp);
			
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}
		
	};

	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		final String vender_num = lists.get(position).toString();
		//弹出对话框
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View layout = inflater.inflate(R.layout.dialog, (ViewGroup)getActivity().findViewById(R.id.layout_dialog));
		final EditText salver = (EditText)layout.findViewById(R.id.et_salver);
		final EditText channe = (EditText)layout.findViewById(R.id.et_channe);
		new AlertDialog.Builder(getActivity()).setTitle("商品详细信息").setView(layout)
	     .setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					if(Avalidation(salver.getText().toString(),channe.getText().toString())){
						//向服务器发送请求数据
						Map<String,String> map = new HashMap<String,String>();
						map.put("venderNumber", vender_num);
						map.put("salverNumber", salver.getText().toString());
						map.put("channelNumber", channe.getText().toString());
						readNet(url_1, map);
//						readNet(url, map);
					}else{
						Toast.makeText(getActivity(), "输入格式有误", Toast.LENGTH_SHORT).show();
					}
				}
			})
	     .setNegativeButton("取消", null).show();
	}
	
	private void initListView(String[] Items){
		for(int i = 0;i<Items.length;i++){
			lists.add(Items[i]);
		}
		adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,lists);
        setListAdapter(adapter);
	}
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	private boolean Avalidation(String salver,String channe){
		if(salver.equals("") || channe.equals("")) return false;
		Pattern pattern1 = Pattern.compile(regEx1);
		Pattern pattern3 = Pattern.compile(regEx3);
		Matcher matcher1 = pattern1.matcher(channe);
		Matcher matcher3 = pattern3.matcher(salver);
		boolean b1= matcher1.matches();
		boolean b3= matcher3.matches();
		return b1 && b3 ;
		
	}
	
	/*
     * http状态响应码
     * http状态返回代码 1xx（临时响应）
     * http状态返回代码 2xx （成功）
     *   200  成功
     * http状态返回代码 3xx （重定向
     * http状态返回代码 4xx（请求错误）
     *   400   （错误请求） 服务器不理解请求的语法。
     * http状态返回代码 5xx（服务器错误） 
     * 500   （服务器内部错误）  服务器遇到错误，无法完成请求。
     * 
     */
    @SuppressWarnings("unchecked")
	public void readNet(final String url,Map<String,String> map_data){
    	
    	if(map_data.size() != 0){
    		new AsyncTask<Map<String,String>, Void, List<Map<String,String>>>() {
                @Override
    			protected void onPostExecute(List<Map<String,String>> result) {
    				// TODO Auto-generated method stub
    				super.onPostExecute(result);
                    //数据解析
    				if(result.get(0).get("Result_Source_time").toString().contains("illegal_vender_number")){
    					System.out.println("输入的venderNumber查不到对应的售货机");
    					Toast.makeText(getActivity(), "输入的venderNumber查不到对应的售货机",Toast.LENGTH_SHORT).show();
    				}else if(result.get(0).get("Result_Source_time").toString().contains("illegal_salver_number")){
    					System.out.println("输入的venderNumber查不到对应的售货机");
    					Toast.makeText(getActivity(), "输入的venderNumber查不到对应的售货机",Toast.LENGTH_SHORT).show();
    				}else if(result.get(0).get("Result_Source_time").toString().contains("illegal_channel_number ")){
    					System.out.println("输入的货道号超出该售货机的货道数量");
    					Toast.makeText(getActivity(), "输入的货道号超出该售货机的货道数量",Toast.LENGTH_SHORT).show();
    				}else if(result.get(0).get("Result_Source_time").toString().contains("none_commodity")){
    					System.out.println("该货道上没有货物了");
    					Toast.makeText(getActivity(), "输入的venderNumber查不到对应的售货机",Toast.LENGTH_SHORT).show();
    				}else if(result.get(0).get("Result_Source_time").toString().contains("none_temperature_process")){
    					System.out.println("没有查到该货物的温度与过程信息");
    					Toast.makeText(getActivity(), "没有查到该货物的温度与过程信息",Toast.LENGTH_SHORT).show();
    				}else{
	    				Intent intent = new Intent(getActivity(),SourceChartActivity.class);
	    				intent.putExtra("SourceTempData", (Serializable)list);
	    				startActivity(intent);
    				}
    				
    			}
				@Override
    			protected List<Map<String,String>> doInBackground(Map<String, String>... arg0) {
                    String value = null;
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
    					System.out.println("响应状态码:"+respone.getStatusLine().getStatusCode());
    					if(state == 1){
    						System.out.println("临时响应");
    					}else if(state == 2){
    						value = EntityUtils.toString(respone.getEntity());
    						try {
    							JSONObject objectStr = new JSONObject(value.toString());
    							Map<String,String> map_time = new HashMap<String,String>();
    							map_time.put("Result_Source_time",objectStr.getString("lastSubmitTimeOrFailInfo"));
    							list.add(map_time);
    							JSONArray arrayJson = objectStr .getJSONArray("temperatureProcessList");
    							System.out.println("size:"+arrayJson.length());
    							for(int i = 0;i<arrayJson.length();i++){
    								Map<String,String> map = new HashMap<String,String>();
    								JSONObject tempJson = arrayJson.optJSONObject(i);
    								map.put("process", tempJson.getString("process"));
    								map.put("data",tempJson.getString("temperature"));
    								list.add(map);
    								System.out.println("process:"+tempJson.getString("process"));
    								System.out.println("data:"+tempJson.getString("temperature"));	
    							}
    							
    						} catch (JSONException e) {
    							e.printStackTrace();
    						}
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
    				return list;
    			}
    		}.execute(map_data);
    	}else{
    		new AsyncTask<Map<String,String>, Void, String []>() {
                @Override
    			protected void onPostExecute(String[] result) {
    				// TODO Auto-generated method stub
    				super.onPostExecute(result);
    				//Log.i("result",result.toString());
    				if(result==null){
    					Toast.makeText(getActivity(), "没有获得任何数据",Toast.LENGTH_SHORT).show();
    				}else{
    					lists.clear();
        				Items = values;
        				initListView(Items);
    				}
    				
    			}
    			@Override
    			protected String[] doInBackground(Map<String, String>... arg0) {
                    String value = "null";
                    HttpPost post = new HttpPost(url);
                    try {
    					HttpResponse respone = httpClient.execute(post);
    					int state = respone.getStatusLine().getStatusCode()/100;
    					System.out.println("响应状态码:"+respone.getStatusLine().getStatusCode());
    					
    					if(state == 1){
    						System.out.println("临时响应");
    					}else if(state == 2){
    						value = EntityUtils.toString(respone.getEntity());
    						Log.i("value", value);
    						try {
    							JSONObject objectStr = new JSONObject(value.toString());
    							JSONArray arrayJson = objectStr .getJSONArray("venderNumbers");
    						    values = new String [arrayJson.length()];
    							for(int i=0;i<arrayJson.length();i++) {  
    				                 values[i] = (String) arrayJson.get(i);
    				                 System.out.println(arrayJson.get(i).toString());
    						    } 
    							
    						} catch (JSONException e) {
    							e.printStackTrace();
    						}
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
    				return values;
    			}
    		}.execute(map_data);
    		
    	}
    	 
		
	}

	
}
