package com.example.fragment;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.example.kimma_test_ui_hs.ChartActivity;
import com.example.kimma_test_ui_hs.R;
import com.example.kimma_test_ui_hs.WorkActivity;
import com.example.tools.Bluetooth;
import com.example.tools.Tools;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/*
 * �����ݵĴ����ࣺ��õ�ǰ�¶����ݺ���ʷ�¶�����
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint({ "HandlerLeak", "SimpleDateFormat" })
public class DataFragment extends Fragment {
	private Button bt_getTempNow,bt_getTempAll;
	private SurfaceView mSurface;
	private SurfaceHolder mHolder;
	private TextView tx_TempNow; 
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final View rootView = inflater.inflate(R.layout.fragment_data, container,false);

		IntentFilter filter_one =new IntentFilter("com.example.bluetooth_one");//ע���Զ���㲥�����ڽ��յ�ǰ�¶�����
		getActivity().registerReceiver(MyBroadeReceive_one, filter_one);
		
//		IntentFilter filter_startCash =new IntentFilter("com.example.WorkAActivity.startCash");//ע���Զ���㲥
//		getActivity().registerReceiver(MyBroadeReceive_startCash, filter_startCash);
		
//		IntentFilter filter_endCash =new IntentFilter("com.example.WorkAActivity.endCash");//ע���Զ���㲥
//		getActivity().registerReceiver(MyBroadeReceive_endCash, filter_endCash);
		
		mSurface = (SurfaceView)rootView.findViewById(R.id.thermograph);//��ʼ����ͼ����(�¶ȼ�)
		mSurface.setZOrderOnTop(true);
		mHolder = mSurface.getHolder();
		mHolder.setFormat(PixelFormat.TRANSLUCENT);
		
		tx_TempNow = (TextView)rootView.findViewById(R.id.tx_TempNow);//��ʼ���ؼ�
		tx_TempNow.setHint("��ʾ��ǰ�¶�(��C)");
		bt_getTempNow = (Button)rootView.findViewById(R.id.GetTempNow);
		bt_getTempNow.setText("��ǰ�¶�");
		bt_getTempAll = (Button)rootView.findViewById(R.id.GetTempAll);
		bt_getTempAll.setText("��ʷ�¶�����");
		bt_getTempNow.setOnClickListener(new View.OnClickListener() {  
			 WorkActivity workActivity = (WorkActivity)getActivity();
		     Bluetooth tool = workActivity.bluetoothTool;
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub 
				byte[] data = {(byte) 0xA2,0x30,0x00,0x00,0x00};
				tool.writeCharacteristic(tool.characteristic,data);//��оƬд���ȡ��ǰ���ݵ�ָ��
				tool.readCharacteristic(tool.characteristic);//��оƬ����Ӧ������
            }  
        });
		
		
		bt_getTempAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.i("isCash-����ģ��",((WorkActivity) getActivity()).isCash()+"");
				if(((WorkActivity) getActivity()).isCash())
				{//isCash =true  ��ʾ���ݻ������
					if(((WorkActivity) getActivity()).getList() != null){
						Log.i("�����ȡ��ʷ����",((WorkActivity) getActivity()).getList().size()+"");
						Intent intent = new Intent(getActivity(),ChartActivity.class);
						intent.putExtra("TempData", (Serializable)((WorkActivity) getActivity()).getList());
						startActivity(intent);
					}else
					{//����Ϊ��
						Toast.makeText(getActivity(), "����Ϊ��", Toast.LENGTH_SHORT).show();
					}
				}else
				{//��δ��������
					Toast.makeText(getActivity(), "���Ȼ�����ʷ���ݣ�", Toast.LENGTH_SHORT).show();
				}
				
			}
		});

		return rootView;
	}
	
	
	/*
	 * ���¶ȼ�
	 * �����¶�ֵ���� 10
	 */
	private void drawTemp(String temp){
		
		DisplayMetrics dm = new DisplayMetrics();  //�����Ļ���
		this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);  
	    int screenW = (dm.widthPixels)/4;
		float tem1 = Float.parseFloat(temp);
		//13������
		int y = 290 - (int) ((tem1 - 5) * 20);
		Canvas canvas = mHolder.lockCanvas();
		canvas.drawColor(Color.TRANSPARENT);
		Paint mPaint = new Paint();
		mPaint.setColor(Color.WHITE);
		Paint mText = new Paint();
		mText.setColor(Color.BLACK);
		canvas.drawRect(screenW, 10, screenW+20, 300, mPaint);
		Paint paintCircle = new Paint();
		paintCircle.setColor(Color.RED);
		Paint paintLine = new Paint();
		paintLine.setColor(Color.BLUE);
		canvas.drawRect(screenW, y, screenW+20, 300, paintCircle);
		canvas.drawCircle(screenW+10, 320, 25, paintCircle);
		int ydegree = 280;
		int tem = 5;
		while (ydegree > 10) {
			canvas.drawLine(screenW+20, ydegree, screenW+27, ydegree, mText);
			if (ydegree % 20 == 0) {
				canvas.drawLine(screenW+20, ydegree, screenW+22, ydegree, mText);
				canvas.drawText(tem + "", screenW+30, ydegree + 1, mText);
				tem++;
			}
			ydegree = ydegree - 2;
		}
		mHolder.unlockCanvasAndPost(canvas);// ������Ļ��ʾ����
	}
	
	/*
	 * ת������
	 * �����ֽ����飬����ת���������ַ���,��drawTemp()�ṩ����
	 * ����   0x0A
	 * ���  10
	 */
	private String Convent(byte[] data){
		StringBuilder stringBuilder = new StringBuilder("");   
	    if (data == null || data.length <= 0) {   
	        return null;   
	    }   
	    for (int i = 0; i < data.length; i++) {   
	        int v = data[i] & 0xFF;   
	        String hv = Integer.toHexString(v);   
	        if (hv.length() < 2) {   
	            stringBuilder.append(0);   
	        }   
	        stringBuilder.append(hv);   
	    }   
		int n = Integer.parseInt(stringBuilder.toString(), 16);
		String s = String.valueOf(n);
		return s;
		
	}
		
	/*
	 * �㲥������
	 */
	private BroadcastReceiver MyBroadeReceive_one =new  BroadcastReceiver() {
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			byte[] msg = arg1.getByteArrayExtra("BluetoothData_one");
			String time = df.format(new Date());
			tx_TempNow.setText(time+"\n"+Convent(msg)+" ��C");
			drawTemp(Convent(msg));
			
		}
	};
	
	private BroadcastReceiver MyBroadeReceive_startCash =new  BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			bt_getTempNow.setEnabled(false);
			bt_getTempAll.setEnabled(false);
			
		}
		
	};
	
//	private BroadcastReceiver MyBroadeReceive_endCash =new  BroadcastReceiver() {
//
//		@Override
//		public void onReceive(Context arg0, Intent arg1) {
//			// TODO Auto-generated method stub
//			bt_getTempNow.setEnabled(true);
//			bt_getTempAll.setEnabled(true);
//		}
//		
//	};
	
	
	
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().unregisterReceiver(MyBroadeReceive_one);
//		getActivity().unregisterReceiver(MyBroadeReceive_all);
	}
}
