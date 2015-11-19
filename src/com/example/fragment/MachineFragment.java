package com.example.fragment;

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

import com.example.kimma_test_ui_hs.R;
import com.example.kimma_test_ui_hs.WorkActivity;
import com.example.tools.Tools;
import com.zijunlin.Zxing.Demo.CaptureActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class MachineFragment extends Fragment{
	
	private TextView tx_BarCode,tx_lable,tx_showCode,tx_fragmentLayout;
	private EditText et_row,et_list,et_number,et_machineID;
	private Button bt_commit;
	private FrameLayout frameLayout;
	public static final int REQUSET = 1;
	private String code="";
    private static final String TAG = "MachineFragment";  
    private static String regEx1 = "^[0-9]{1,2}$";//ƥ�������� ,����ƥ���ϻ�������������
    private static String regEx2 = "^[A-Za-z0-9]{1,15}$";////ƥ��1-15�����ֻ�����ĸ������ƥ���ۻ������
    private static String regEx3 = "^[A-Za-z]{1}$";//����ƥ�����̺�
    private String address = "";
    private HttpClient httpClient;
    private Tools tool = new Tools();
    private String url = Tools.getUrl()+"work.do";
    private WorkActivity activity = (WorkActivity)getActivity();
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
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.d(TAG, "MachineFragment-----onCreateView");   
        View view = inflater.inflate(R.layout.fragment_machine, container, false);  
        httpClient = tool.initHttp();
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("ADDRESS",Activity.MODE_PRIVATE); 
		address = sharedPreferences.getString("address", "");
        tx_BarCode = (TextView)view.findViewById(R.id.tx_BarCode);
        tx_showCode = (TextView)view.findViewById(R.id.tx_showCode);
        tx_lable = (TextView)view.findViewById(R.id.tx_lable);
        et_row = (EditText)view.findViewById(R.id.et_row);
        et_list = (EditText)view.findViewById(R.id.et_list);
        et_number = (EditText)view.findViewById(R.id.et_number);
        et_machineID = (EditText)view.findViewById(R.id.et_machineID);
        frameLayout = (FrameLayout)view.findViewById(R.id.machine_FragmentLayout);
        tx_fragmentLayout = (TextView)view.findViewById(R.id.machine_FragmentLayout_info);
        bt_commit = (Button)view.findViewById(R.id.bt_commit);
        bt_commit.setText("�ύ");
        tx_BarCode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),CaptureActivity.class);
				startActivityForResult(intent, REQUSET);
			}
		});
        
        bt_commit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(Avalidation(et_machineID.getText().toString(),et_row.getText().toString(),et_list.getText().toString(),et_number.getText().toString())){
				   Map<String,String> map = new HashMap<String,String>();
				   map.put("venderNumber", et_machineID.getText().toString()); //�ۻ������
				   map.put("salverNumber", et_row.getText().toString());     //���̺�
				   map.put("channelNumber",et_list.getText().toString());    //������
				   map.put("inventoryQuantity",et_number.getText().toString());//�ϻ�����
				   map.put("ibNumber",address );         //MAC��ַ
				   readNet(url, map);
				}else{
					
				Toast.makeText(getActivity(), "�ύʧ�ܣ�������������", Toast.LENGTH_SHORT).show();
				et_machineID.setText("");
				et_row.setText("");
				et_list.setText("");
				et_number.setText("");
				}
			}
		});
       
        return view; 
		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		code = data.getExtras().getString(("result"));
		tx_showCode.setText(code);
		
	}  
	
	private boolean Avalidation(String et_machineID,String et_row,String et_list,String et_number){
		if(et_machineID.equals("") || et_row.equals("") || et_list.equals("") || et_number.equals("")) return false;
		Pattern pattern1 = Pattern.compile(regEx1);
		Pattern pattern2 = Pattern.compile(regEx2);
		Pattern pattern3 = Pattern.compile(regEx3);
		Matcher matcher1 = pattern2.matcher(et_machineID);
		Matcher matcher2 = pattern3.matcher(et_row);
		Matcher matcher3 = pattern1.matcher(et_list);
		Matcher matcher4 = pattern1.matcher(et_number);
		boolean b1= matcher1.matches();
		boolean b2= matcher2.matches();
		boolean b3= matcher3.matches();
		boolean b4= matcher4.matches();
		System.out.println("b1:"+b1+"b2:"+b2+"b3:"+b3+"b4:"+b4);
		return b1 && b2 && b3 && b4;
		
	}
	
	@SuppressWarnings("unchecked")
	public void readNet(final String url,Map<String,String> map){
		new AsyncTask<Map<String,String> , Void, String>() {
            @Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				//��BUG
				if(!result.isEmpty()){
					if(result.contains("success")){
	                    Toast.makeText(getActivity(),"�����ɹ�", Toast.LENGTH_SHORT).show();
	                    tx_fragmentLayout.setText("�ۻ������: "+et_machineID.getText().toString()+"\n"+"���̺�: "+et_row.getText().toString()+"   ������: "+et_list.getText().toString()+"\n"+"�ϻ���: "+et_number.getText().toString());
	                    CleanInput();
					}else if(result.contains("fail")){
						Toast.makeText(getActivity(),"����ʧ��", Toast.LENGTH_SHORT).show();
					}else {
						System.out.println("�����ݴ���");
						Toast.makeText(getActivity(),"HTTP����ʧ�ܣ���������", Toast.LENGTH_LONG).show();
					}
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
					System.out.println("��Ӧ״̬��:"+respone.getStatusLine().getStatusCode());
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
	
	private void CleanInput(){
		et_machineID.setText("");
		et_list.setText("");
		et_number.setText("");
		et_row.setText("");
	}
}
