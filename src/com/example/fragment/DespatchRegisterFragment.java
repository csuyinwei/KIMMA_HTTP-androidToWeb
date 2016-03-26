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
import com.example.kimma_test_ui_hs.CalendarActivity;
import com.example.kimma_test_ui_hs.R;
import com.example.tools.HttpTool;
import com.example.tools.Tools;
import com.zijunlin.Zxing.Demo.CaptureActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 配送模块
 * 功能：扫描获得商品条码，选择生产日期，填写生产地，获得智能箱MAC地址，校验后发送给服务器
 */
public class DespatchRegisterFragment extends Fragment{
	
	public static final int REQUSET_BarCode = 1;//返回确认码，用于区分Activity返回的数据
	public static final int REQUSET_Calendar = 2;
	
	private String code="00000000";
	private TextView tx_BarCode,tx_showCode;
    private Button bt_productDate,bt_despatchInfoSubmit;
    private EditText et_productOrigin;
    private String calendar = "0000-00-00";
    private String address = "";
    private String url = Tools.getUrl()+"comdty_trans/doInput";
    private Tools tool = new Tools();
    private HttpClient httpClient;
    
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_despatch, container, false);
		SharedPreferences sharedPreferences= getActivity().getSharedPreferences("ADDRESS",Activity.MODE_PRIVATE); //通过SharedPreferences获得存入的智能箱MAC地址
		address = sharedPreferences.getString("address", "");
		
		httpClient = tool.initHttp();//httpClient初始化
		
		tx_showCode = (TextView)view.findViewById(R.id.tx_showCode);
		bt_productDate = (Button)view.findViewById(R.id.bt_productDate);
		tx_BarCode = (TextView)view.findViewById(R.id.tx_BarCodeIcon);
        tx_BarCode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),CaptureActivity.class);//跳转到扫描二维码的Activity
				startActivityForResult(intent, REQUSET_BarCode);
			}
		});
        bt_productDate.setOnClickListener(new OnClickListener() {//跳转到选择日期的Activity
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),CalendarActivity.class);
				startActivityForResult(intent, REQUSET_Calendar);
			}
		});
        et_productOrigin = (EditText)view.findViewById(R.id.et_productOrigin);
        bt_despatchInfoSubmit = (Button)view.findViewById(R.id.bt_despatchInfoSubmit);
        bt_despatchInfoSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(et_productOrigin.getText().toString().equals("") || code.equals("00000000") || calendar.equals("0000-00-00")){
					Toast.makeText(getActivity(), "商品信息不能为空",Toast.LENGTH_SHORT).show();
				}else{
					//将参数封装
					Map<String,String> map = new HashMap<String,String>();
					map.put("intelligentBoxNumber", address);
					map.put("barcode", code);
					map.put("productDate", calendar);
					map.put("productOrigin", et_productOrigin.getText().toString());
					readNet(url, map);//发送
					
				}
			}
		});
		
		return view;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	/*
	 * 接收Activity返回的数据的回掉函数
	 * 
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		System.out.println("resultCode："+resultCode);
		if(resultCode == -1){
			code = data.getExtras().getString(("result"));
			tx_showCode.setText(code);
		}
		if(resultCode == 2){
			System.out.println("接收到的日期是:"+data.getExtras().getString(("calendar")));
			calendar = data.getExtras().getString(("calendar"));
			bt_productDate.setText(calendar);
		}
	}
	
	/*
	 * 向服务器发送数据
	 * 参数：url,要传递的数据封装成Map<String,String>
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void readNet(final String url,Map<String,String> map_data){
		new AsyncTask<Map<String,String>, Void, String>() {//异步执行
            @Override
			protected void onPostExecute(String result) {//接收doInBackground()传回的参数，并可以对UI线程操作
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				System.out.println("result:"+result.toString());
				if(result.contains("success")){
					Toast.makeText(getActivity(), "提交成功", Toast.LENGTH_SHORT).show();
					bt_despatchInfoSubmit.setEnabled(false);
				}else if(result.contains("illegal_barcode")){
					Toast.makeText(getActivity(), "无法根据输入的条形码查到相应的商品类型", Toast.LENGTH_SHORT).show();
				}else if(result.contains("ib_number_null")){
					Toast.makeText(getActivity(), "智能箱编号为空", Toast.LENGTH_SHORT).show();
				}else if(result.contains("production_date_null")){
					Toast.makeText(getActivity(), "生产日期未填写", Toast.LENGTH_SHORT).show();
				}else if(result.contains("production_origin_null")){
					Toast.makeText(getActivity(), "生产地未填写", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(getActivity(), "其他错误", Toast.LENGTH_SHORT).show();
				}
			}
			@Override
			protected String doInBackground(Map<String, String>... arg0) {
                String value = "null";
//                Boolean Result_Login = false;//调试用
                HttpPost post = null;
                try {
					post = HttpTool.SengRequest(url, arg0);
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                try {
					HttpResponse respone = httpClient.execute(post);//异步接收
					int state = respone.getStatusLine().getStatusCode()/100;//获得服务器发挥的状态码
					System.out.println("响应状态码:"+respone.getStatusLine().getStatusCode());
					if(state == 1){
						System.out.println("临时响应");
					}else if(state == 2){
						value = EntityUtils.toString(respone.getEntity());
//						System.out.println("配送模块接收到的数据:"+value.toString());
						System.out.println("初始化模块接收到正确的HTTP请求:"+ System.currentTimeMillis());
						
					}else if(state == 3){
						System.out.println("重定向");
					}else if(state == 4){
						System.out.println("请求错误");
					}else if(state == 5){
						System.out.println("服务器错误");
					}
				    }  catch (Exception e) {}
				return value.toString();
//                return Result_Login.toString();//调试用
			}
		}.execute(map_data);
	}
	
	

}
