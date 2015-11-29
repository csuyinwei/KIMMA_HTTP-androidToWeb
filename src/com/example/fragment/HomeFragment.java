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
 * ��¼ģ��
 * ���ܣ�У���û��������Ч�Կ���ݵĺϷ���
 */
@SuppressLint("HandlerLeak")
public class HomeFragment<OnArticleSelectedListener_HomeFregment> extends Fragment {

	private View parentView;
    private ResideMenu resideMenu;
    private Button loginButton;
    private EditText username;
    private EditText password;
    private OnArticleSelectedListener_HomeFregment mListener;//Fragment��Activity���εļ�����
    private HttpClient httpClient;
    private Tools tool = new Tools();
    //private String url = Tools.getUrl()+"login.do";
    private String url = Tools.getUrl()+"admin/doLogin";
    
    private Handler handler = new Handler(){//����UI�߳�
    	@Override
        public void handleMessage(Message msg) {
    		try {
    			String s = msg.obj.toString();
    			LoginActivity actity = (LoginActivity)getActivity();
    			actity.tx_connect_state.setText(s+" HTTP����ʧ��");
    		
    	    }catch (Exception e) {
                e.printStackTrace();
    	    }
     }
    };
   //�Զ���ص��ӿ�
    public interface OnArticleSelectedListener_HomeFregment{          
		public void onArticleSelected(String str);
	}
	
    //ϵͳ�ص�����
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
					Toast.makeText(getActivity(), "������Ϣ����Ϊ��", Toast.LENGTH_SHORT).show();
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
     * http״̬��Ӧ��
     * http״̬���ش��� 1xx����ʱ��Ӧ��
     * http״̬���ش��� 2xx ���ɹ���
     *   200  �ɹ�
     * http״̬���ش��� 3xx ���ض���
     * http״̬���ش��� 4xx���������
     *   400   ���������� �����������������﷨��
     * http״̬���ش��� 5xx������������ 
     * 500   ���������ڲ�����  ���������������޷��������
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
//                Boolean Result_Login = false;//������
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
						/**
						 * ģ�����ר��
						 */
//						try {
//							JSONObject objectStr = new JSONObject(value.toString());
//							Result_Login = objectStr.getBoolean("Result_Login");
//							System.out.println("��¼���:"+Result_Login);
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
						/*
						 * ģ�����ר��
						 * 
						 */
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
				return value.toString();
//                return Result_Login.toString();//������
			}
		}.execute(map_data);
	}
    
    
  
    
   
	
	private void UpdateUI(){
		username.setText("");
		password.setText("");
		Toast.makeText(getActivity(), "��¼ʧ��", Toast.LENGTH_SHORT).show();
	}

    private void setUpViews() {
        LoginActivity parentActivity = (LoginActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();
        FrameLayout ignored_view = (FrameLayout) parentView.findViewById(R.id.ignored_view);//���ò��ɻ�������
        resideMenu.addIgnoredView(ignored_view);
    }
    /*
     * ���û�����������зǿ�У��
     */
    private boolean Avalidation(String name,String password){
    	if(name.equals("") || password.equals("")){
    		return false;
    	}
		return true;
    	
    }

}
