package com.example.fragment;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import com.example.kimma_test_ui_hs.LoginActivity;
import com.example.kimma_test_ui_hs.MainActivity;
import com.example.kimma_test_ui_hs.R;
import com.example.tools.HttpTool;
import com.example.tools.Tools;
import com.special.ResideMenu.ResideMenu;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;


/*
 * 登录模块
 * 功能：校验用户输入的有效性可身份的合法性
 */
@SuppressLint("HandlerLeak")
public class HomeFragment<OnArticleSelectedListener_HomeFregment> extends Fragment {

	private View parentView;
    private ResideMenu resideMenu;
    private Button loginButton;
    private EditText username;
    private EditText password;
    private OnArticleSelectedListener_HomeFregment mListener;//Fragment向Activity传参的监听器
    private HttpClient httpClient;
    private Tools tool = new Tools();
    //private String url = Tools.getUrl()+"login.do";
    private String url = Tools.getUrl()+"admin/doLogin";
    
    private Handler handler = new Handler(){//更新UI线程
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
   //自定义回调接口
    public interface OnArticleSelectedListener_HomeFregment{          
		public void onArticleSelected(String str);
	}
	
    //系统回调函数
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try{               
			mListener =(OnArticleSelectedListener_HomeFregment)activity;
	    }catch(Exception e){
	    	e.setStackTrace(null);
	    }
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_login, container, false);  
		httpClient = tool.initHttp();
        loginButton = (Button)parentView.findViewById(R.id.login_button);
        username = (EditText)parentView.findViewById(R.id.name);
        password = (EditText)parentView.findViewById(R.id.password);
        loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(!Avalidation(username.getText().toString(),password.getText().toString())){
					Toast.makeText(getActivity(), "输入信息不能为空", Toast.LENGTH_SHORT).show();
				}else{
					Map<String,String> map_data = new HashMap<String,String>();
					map_data.put("account",username.getText().toString());
					map_data.put("password",password.getText().toString());
					readNet(url,map_data);
				}
			}
		});
        setUpViews();
        return parentView;
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
     * 
     */
    @SuppressWarnings("unchecked")
	public void readNet(final String url,Map<String,String> map_data){
		new AsyncTask<Map<String,String>, Void, String>() {
            @Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if(result.equals("true")){
					Intent intene = new Intent(getActivity().getApplication(),MainActivity.class);
					startActivity(intene);
					getActivity().finish();
					System.exit(0);
				}else{
					UpdateUI();
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
					HttpResponse respone = httpClient.execute(post);
					int state = respone.getStatusLine().getStatusCode()/100;
					System.out.println("响应状态码:"+respone.getStatusLine().getStatusCode());
					if(state == 1){
						System.out.println("临时响应");
					}else if(state == 2){
						value = EntityUtils.toString(respone.getEntity());
						System.out.println("登录模块HTTP收到正确响应响应:"+System.currentTimeMillis());
						/**
						 * 模拟调试专用
						 */
//						try {
//							JSONObject objectStr = new JSONObject(value.toString());
//							Result_Login = objectStr.getBoolean("Result_Login");
//							System.out.println("登录结果:"+Result_Login);
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
						/*
						 * 模拟调试专用
						 * 
						 */
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
				return value.toString();
//                return Result_Login.toString();//调试用
			}
		}.execute(map_data);
	}
    
    
  
    
   
	
	private void UpdateUI(){
		username.setText("");
		password.setText("");
		Toast.makeText(getActivity(), "登录失败", Toast.LENGTH_SHORT).show();
	}

    private void setUpViews() {
        LoginActivity parentActivity = (LoginActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();
        FrameLayout ignored_view = (FrameLayout) parentView.findViewById(R.id.ignored_view);//设置不可滑动区域
        resideMenu.addIgnoredView(ignored_view);
    }
    /*
     * 对用户名和密码进行非空校验
     */
    private boolean Avalidation(String name,String password){
    	if(name.equals("") || password.equals("")){
    		return false;
    	}
		return true;
    	
    }

}
