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

import com.example.kimma_test_ui_hs.MipcaActivityCapture;
import com.example.kimma_test_ui_hs.R;
import com.example.tools.HttpTool;
import com.example.tools.Tools;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 上货模块
 * 功能：连接智能箱，获得芯片MAC地址，输入售货机编号，托盘号，货道号，上货数量，提交至服务器
 * 
 */
@SuppressLint("HandlerLeak")
public class MachineFragment extends Fragment{
	
	private final static int SCANNIN_GREQUEST_CODE = 3;
	
	private TextView tx_fragmentLayout, tx_TwoBarCode;
	private EditText et_row,et_list,et_number,et_machineID;
	private Button bt_commit;
	private FrameLayout frameLayout;
    private static final String TAG = "MachineFragment";  
    private static String regEx1 = "^[0-9]{1,2}$";//匹配正整数 ,用于匹配上货数量，货道号
    private static String regEx2 = "^[A-Za-z0-9]{1,15}$";////匹配1-15个数字或者字母，用于匹配售货机编号
    private static String regEx3 = "^[A-Z]{1}$";//用于匹配托盘号
    private String address = "";
    private HttpClient httpClient;
    private Tools tool = new Tools();
    //private String url = Tools.getUrl()+"work.do";
    private String url = Tools.getUrl()+"vender_inv/doSupply";

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.d(TAG, "MachineFragment-----onCreateView");   
        View view = inflater.inflate(R.layout.fragment_machine, container, false);  
        httpClient = tool.initHttp();
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("ADDRESS",Activity.MODE_PRIVATE); 
		address = sharedPreferences.getString("address", "");
        
        
        et_row = (EditText)view.findViewById(R.id.et_row);
        et_list = (EditText)view.findViewById(R.id.et_list);
        et_number = (EditText)view.findViewById(R.id.et_number);
        et_machineID = (EditText)view.findViewById(R.id.et_machineID);
        frameLayout = (FrameLayout)view.findViewById(R.id.machine_FragmentLayout);
        tx_fragmentLayout = (TextView)view.findViewById(R.id.machine_FragmentLayout_info);
        bt_commit = (Button)view.findViewById(R.id.bt_commit);
        bt_commit.setText("提交");
        tx_TwoBarCode = (TextView)view.findViewById(R.id.tx_TwoBarCodeIcon);
        
        
        bt_commit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(Avalidation(et_machineID.getText().toString(),et_row.getText().toString(),et_list.getText().toString(),et_number.getText().toString())){
				   Map<String,String> map = new HashMap<String,String>();
				   map.put("venderNumber", et_machineID.getText().toString()); //售货机编号
				   map.put("salver", et_row.getText().toString());     //托盘号
				   map.put("channelNumber",et_list.getText().toString());    //货道号
				   map.put("inventoryQuantity",et_number.getText().toString());//上货数量
				   map.put("ibNumber",address );         //MAC地址
				   readNet(url, map);
				}else{
				Toast.makeText(getActivity(), "提交失败，输入数据有误", Toast.LENGTH_SHORT).show();
				et_machineID.setText("");
				et_row.setText("");
				et_list.setText("");
				et_number.setText("");
				}
			}
		});
        
        tx_TwoBarCode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getActivity(), MipcaActivityCapture.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
			}
		});
       
        return view; 
		
	}
	
	
	
	

  
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("requestCode:"+requestCode);
		System.out.println("resultCode:"+resultCode);
		switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if(resultCode == -1){
				Bundle bundle = data.getExtras();
				//显示扫描到的内容
				et_machineID.setText(bundle.getString("result"));
				
			}
			break;
		}
    }	

	






	/*
	 * 利用正则表达式校验输入参数
	 */
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
				//有BUG
				if(!result.isEmpty()){
					if(result.contains("success")){
	                    Toast.makeText(getActivity(),"操作成功", Toast.LENGTH_SHORT).show();
	                    tx_fragmentLayout.setText("售货机编号: "+et_machineID.getText().toString()+"\n"+"托盘号: "+et_row.getText().toString()+"   货道号: "+et_list.getText().toString()+"\n"+"上货量: "+et_number.getText().toString());
	                    CleanInput();
					}else if(result.contains("illegal_vender_number")){
						Toast.makeText(getActivity(),"无法根据输入的venderNumber查到对应的售货机", Toast.LENGTH_SHORT).show();
					}else if(result.contains("illegal_ib_number")){
						Toast.makeText(getActivity(),"无法根据输入的智能箱编号查到相应的配送批次信息", Toast.LENGTH_SHORT).show();
					}else if(result.contains("illegal_salver_number")){
						Toast.makeText(getActivity(),"操作失败", Toast.LENGTH_SHORT).show();
					}else if(result.contains("illegal_channel_number ")){
						Toast.makeText(getActivity(),"输入的货道号超出该售货机的货道数量", Toast.LENGTH_SHORT).show();
					}else {
						System.out.println("无数据传回");
						Toast.makeText(getActivity(),"HTTP请求失败，请检查网络", Toast.LENGTH_LONG).show();
					}
				}
			}
			@Override
			protected String doInBackground(Map<String,String>... arg0) {
                String value = "null";
                HttpPost post = null;
                try {
					post = HttpTool.SengRequest(url, arg0);
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                try {
					HttpResponse respone = httpClient.execute(post);
					int state = respone.getStatusLine().getStatusCode()/100;
					System.out.println("响应状态码:"+respone.getStatusLine().getStatusCode());
					if(state == 1){
						System.out.println("临时响应");
					}else if(state == 2){
						value = EntityUtils.toString(respone.getEntity());
//						System.out.println("value:"+value);
						System.out.println("补货模块接收到正确的HTTP请求:"+ System.currentTimeMillis());
					}else if(state == 3){
						System.out.println("重定向");
					}else if(state == 4){
						System.out.println("请求错误");
					}else if(state == 5){
						System.out.println("服务器错误");
					}
				}  catch (Exception e) {
					e.printStackTrace();
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
