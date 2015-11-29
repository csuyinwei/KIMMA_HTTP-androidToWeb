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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*
 * �ֿ�ģ��
 * ���ܣ������Ǽǣ��ջ��Ǽ�
 */
@SuppressLint("HandlerLeak")
public class StorehouseFragment extends Fragment{
	private static final String TAG = "StorehouseFragment";  
	private Button bt_start,bt_end;
	private HttpClient httpClient;
	private Tools tool = new Tools();
	//private String url = Tools.getUrl()+"work.do";
	private String url_depart = Tools.getUrl()+"trans_proc/doDepart";
	private String url_arrive = Tools.getUrl()+"trans_proc/doArrive";
	private static String address = "";
	
    
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Log.d(TAG, "StorehouseFragment-----onCreateView");   
        View view = inflater.inflate(R.layout.fragment_storehouse, container, false);  
        
        IntentFilter filter_endCash =new IntentFilter("com.example.WorkAActivity.endCash");//ע���Զ���㲥
		getActivity().registerReceiver(MyBroadeReceive_endCash, filter_endCash);
        
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("ADDRESS",Activity.MODE_PRIVATE); 
		address = sharedPreferences.getString("address", "");
        httpClient = tool.initHttp();
        bt_start = (Button)view.findViewById(R.id.bt_start);
        bt_end = (Button)view.findViewById(R.id.bt_end);
        bt_end.setEnabled(false);
        bt_start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final EditText inputServer = new EditText(getActivity());
		        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		        builder.setTitle("��Ʒ��������Ϣ").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
		                .setNegativeButton("ȡ��", null);
		        builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		               inputServer.getText().toString();
		               if(inputServer.getText().toString().equals("")){
		            	  Toast.makeText(getActivity(), "��Ʒ�����ز���Ϊ��!",Toast.LENGTH_SHORT).show(); 
		               }else{
		            	  //��������
		            	   System.out.println("��ȡ��ַ:"+address);
		            	   if(!address.equals("")){
		            		   Map<String,String> map = new HashMap<String,String>();
			            	   map.put("intelligentBoxNumber", address);
			            	   map.put("departurePlace",inputServer.getText().toString());
			            	   readNet(url_depart, map);
		            	   }else{
		            		   Toast.makeText(getActivity(), "һ������",Toast.LENGTH_SHORT).show();
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
		        builder.setTitle("��Ʒ�ջ�����Ϣ").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
		                .setNegativeButton("ȡ��", null);
		        builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		               inputServer.getText().toString();
		               if(inputServer.getText().toString().equals("")){
		            	  Toast.makeText(getActivity(), "��Ʒ�ջ��ز���Ϊ��!",Toast.LENGTH_SHORT).show(); 
		               }else{
		            	   if(!address.equals("")){
		            		   Map<String,String> map = new HashMap<String,String>();
			            	   map.put("intelligentBoxNumber", address);
			            	   map.put("arrivalPlace",inputServer.getText().toString());
			            	   List<Map<String,String>> list = ((WorkActivity) getActivity()).getList();
			            	   StringBuilder sb = new StringBuilder();
			            	   for(int i =0;i<list.size()-1;i++){
			            		   String str = list.get(i).get("value");
			            		   sb.append(str+",");
			            	   }
			            	   sb.append(list.get(list.size()-1).get("value"));
			            	   System.out.println(sb);
			            	   map.put("temperature", sb.toString());
			            	   System.out.println(sb.toString());
			            	   readNet(url_arrive, map);
		            	   }else{
		            		   Toast.makeText(getActivity(), "һ������",Toast.LENGTH_SHORT).show();
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
				//��BUG
				if(result.contains("success")){
                    Toast.makeText(getActivity(),"�����ɹ�", Toast.LENGTH_SHORT).show();					
				}else if(result.contains("duplicate_depart")){
					Toast.makeText(getActivity(),"�ظ����뿪վ��Ǽ�", Toast.LENGTH_SHORT).show();
				}else if(result.contains("illegal_ib_number")){
					Toast.makeText(getActivity(),"�޷�����������������Ų鵽��Ӧ������������Ϣ", Toast.LENGTH_SHORT).show();
				}else if(result.contains("duplicate_arrive")){
					Toast.makeText(getActivity(),"�ظ��ĵ���վ��Ǽ�", Toast.LENGTH_SHORT).show();
				}else {
					System.out.println("�����ݴ���");
					Toast.makeText(getActivity(),"����ʧ�ܣ�������������", Toast.LENGTH_LONG).show();
				}
			}
			@Override
			protected String doInBackground(Map<String,String>... arg0) {
                String value = "null";
                HttpPost post = new HttpPost(url);
              //���������������
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
					//System.out.println("��Ӧ״̬��:"+respone.getStatusLine().getStatusCode());
					if(state == 1){
						System.out.println("��ʱ��Ӧ");
					}else if(state == 2){
						value = EntityUtils.toString(respone.getEntity());
						System.out.println("value:"+value);
					}else if(state == 3){
						System.out.println("�ض���");
					}else if(state == 4){
						System.out.println("�������");
					}else if(state == 5){
						System.out.println("����������");
					}
				}  catch (Exception e) {
					e.printStackTrace();
				}
				return value;
			}
		}.execute(map);
	}
	
	private BroadcastReceiver MyBroadeReceive_endCash =new  BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			bt_end.setEnabled(true);
		}
		
	};
}
