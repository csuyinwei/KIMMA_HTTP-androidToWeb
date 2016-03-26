package com.example.tools;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

/*
 * Http����������
 * 
 */
public class HttpTool {
	
	//��������ʱ   
	static int  timeoutConnection = 5 * 1000;
    
    //������Ӧ��ʱ   
	static int timeoutSocket = 5 * 1000; 
	
	/*
	 * ��http��������
	 * 
	 */
	public static HttpPost SengRequest(String url,Map<String, String>... arg0) throws UnsupportedEncodingException{
		
        BasicHttpParams httpParams = new BasicHttpParams(); 
        HttpConnectionParams.setConnectionTimeout(httpParams,timeoutConnection); 
        HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
	    
		HttpPost post = new HttpPost(url);
        //���������������
        post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

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
			System.out.println("��ʼHTTP����:"+System.currentTimeMillis());
			return post;
	}	
	
	


}
