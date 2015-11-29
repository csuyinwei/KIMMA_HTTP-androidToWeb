package com.example.tools;

import android.annotation.SuppressLint;
import android.util.Log;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpException;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import com.example.modle.TempPoint;

@SuppressLint("SimpleDateFormat")
public class Tools {
	
	private static final String BaseUrl = "http://115.159.23.85/cclms/phone/";
	//private static final String BaseUrl = "http://192.168.1.229:8080/MyWebTest/";
	//private static final String BaseUrl = "http://192.168.155.3:8080/cclms/phone/";
	private static int k = -10;   //��ʾ10���ӵļ��
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	public static SimpleDateFormat getSdf() {
		return sdf;
	}

	public static int getK() {
		return k;
	}

	public static String getUrl() {
		return BaseUrl;
	}
	
	public HttpClient initHttp(){
		//��������ʱ
        HttpParams httpParameters = new BasicHttpParams();
        httpParameters.setParameter("charset", HTTP.UTF_8);
        HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
        //������Ӧ��ʱ     
        HttpConnectionParams.setSoTimeout(httpParameters, 5000); 
        HttpClient httpClient = new DefaultHttpClient(httpParameters);
		return httpClient;
	}

	public String ExceptionCode(Exception e) {
		if (e instanceof HttpException) {
			    return "�����쳣";

		} else if (e instanceof SocketTimeoutException) {
			return  "��Ӧ��ʱ";

		} else if (e instanceof ConnectTimeoutException) {
			return "����ʱ";

		} else if (e instanceof UnknownHostException) { 
			return "�޷����ӷ�����";

		} else if (e instanceof IOException) {
			return "�����쳣";
		}
		return "�����쳣"; 
	}
	
	public List<TempPoint> Souce_data_handle(List<Map<String, String>> list) throws Exception{
		System.err.println("��Դ��ʷ�¶�չʾ");
		List<TempPoint> points = new ArrayList<TempPoint>();
		String timeStr = list.get(0).get("Result_Source_time").toString();
		Date nearlyTime = sdf.parse(timeStr);
		ArrayList<int[]> arraylist = CutMapStringData(list);
		ArrayList<Date> dates = CutData(nearlyTime,arraylist);
		System.out.println("dates size:"+dates.size());
		for(int i = 1;i<list.size();i++){
			TempPoint point = new TempPoint();
			Date date = dates.get(list.size()-i-1);
			System.out.println("ÿ�����̵���������:"+sdf.format(date));
			System.out.println("ÿ�����̵�����process:"+list.get(i).get("process"));
			point.setDate(date);
			point.setProcess(list.get(i).get("process"));
			point.setData(arraylist.get(i-1));
			points.add(point);
		}
		return points;
	}
	
	private ArrayList<Date> CutData(Date date,ArrayList<int[]> array){
		//System.out.println("���յ���ԭʼʱ��;"+sdf.format(date));
		ArrayList<Date> dates = new ArrayList<Date>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		dates.add(calendar.getTime());
		for(int i = array.size()-2;i>-1;i--){
			calendar.add(Calendar.MINUTE,array.get(i+1).length*k);
			//System.out.println("ÿ�����̵���������:"+sdf.format(calendar.getTime()));
			dates.add(calendar.getTime());
		}
		return dates;
		
	}
	
	private ArrayList<int[]> CutMapStringData(List<Map<String, String>> list2){
		ArrayList<int[]> list = new ArrayList<int[]>();
		for(int n =1;n<list2.size();n++){
			String[] str =  ((String) list2.get(n).get("data")).split(",");
			int[] array = new int[str.length];  
			for(int i=0;i<str.length;i++){  
			    array[i]=Integer.parseInt(str[i]);
			}
			list.add(array);
		}
		return list;
		
	}
	

	
	/*
	 * 
	 * ���ݴ���
	 * ���ַ������� ����ת���ɵ�ļ�ֵ��
	 * 
	 */
	
	public List<Map<String,String>> DealData(String[] data){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Date time = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		for(int i = 0,n=data.length-1;i<data.length;i++,n--){
			Map<String,String> map = new HashMap<String,String>();
			calendar.add(Calendar.MINUTE,n*k);
			if(!data[i].equals("-1")){
				Log.i("������Чֵ����", String.valueOf(data[i]));
				map.put("key", sdf.format(calendar.getTime()));
				map.put("value", data[i]);
				list.add(map);
			}
			continue;
		}
		return list;
	}
	
	
	
	public String[]  HaxToString(byte[] data,int n){
        String [] str = new String[n];
	    if (data == null || data.length <= 0) {   
	        return null;   
	    }   
	    for (int i = 0; i < data.length; i++) { 
	    
	    		
	    		str[i] = String.valueOf(data[i]);

	    }
	    
		return str;

	}

}
