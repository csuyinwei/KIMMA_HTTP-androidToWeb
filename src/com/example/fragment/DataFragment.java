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
 * 对数据的处理类：获得当前温度数据和历史温度数据
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint({ "HandlerLeak", "SimpleDateFormat" })
public class DataFragment extends Fragment {
	private Button bt_getTempNow,bt_getTempAll;
	private SurfaceView mSurface;
	private SurfaceHolder mHolder;
	private TextView tx_TempNow; 
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final View rootView = inflater.inflate(R.layout.fragment_data, container,false);

		IntentFilter filter_one =new IntentFilter("com.example.bluetooth_one");//注册自定义广播，用于接收当前温度数据
		getActivity().registerReceiver(MyBroadeReceive_one, filter_one);
		
//		IntentFilter filter_startCash =new IntentFilter("com.example.WorkAActivity.startCash");//注册自定义广播
//		getActivity().registerReceiver(MyBroadeReceive_startCash, filter_startCash);
		
//		IntentFilter filter_endCash =new IntentFilter("com.example.WorkAActivity.endCash");//注册自定义广播
//		getActivity().registerReceiver(MyBroadeReceive_endCash, filter_endCash);
		
		mSurface = (SurfaceView)rootView.findViewById(R.id.thermograph);//初始化绘图配置(温度计)
		mSurface.setZOrderOnTop(true);
		mHolder = mSurface.getHolder();
		mHolder.setFormat(PixelFormat.TRANSLUCENT);
		
		tx_TempNow = (TextView)rootView.findViewById(R.id.tx_TempNow);//初始化控件
		tx_TempNow.setHint("显示当前温度(°C)");
		bt_getTempNow = (Button)rootView.findViewById(R.id.GetTempNow);
		bt_getTempNow.setText("当前温度");
		bt_getTempAll = (Button)rootView.findViewById(R.id.GetTempAll);
		bt_getTempAll.setText("历史温度曲线");
		bt_getTempNow.setOnClickListener(new View.OnClickListener() {  
			 WorkActivity workActivity = (WorkActivity)getActivity();
		     Bluetooth tool = workActivity.bluetoothTool;
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub 
				byte[] data = {(byte) 0xA2,0x30,0x00,0x00,0x00};
				tool.writeCharacteristic(tool.characteristic,data);//向芯片写入获取当前数据的指令
				tool.readCharacteristic(tool.characteristic);//从芯片度相应的数据
            }  
        });
		
		
		bt_getTempAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.i("isCash-数据模块",((WorkActivity) getActivity()).isCash()+"");
				if(((WorkActivity) getActivity()).isCash())
				{//isCash =true  表示数据缓存完成
					if(((WorkActivity) getActivity()).getList() != null){
						Log.i("点击获取历史数据",((WorkActivity) getActivity()).getList().size()+"");
						Intent intent = new Intent(getActivity(),ChartActivity.class);
						intent.putExtra("TempData", (Serializable)((WorkActivity) getActivity()).getList());
						startActivity(intent);
					}else
					{//数据为空
						Toast.makeText(getActivity(), "数据为空", Toast.LENGTH_SHORT).show();
					}
				}else
				{//还未缓存数据
					Toast.makeText(getActivity(), "请先缓存历史数据！", Toast.LENGTH_SHORT).show();
				}
				
			}
		});

		return rootView;
	}
	
	
	/*
	 * 话温度计
	 * 输入温度值，如 10
	 */
	private void drawTemp(String temp){
		
		DisplayMetrics dm = new DisplayMetrics();  //获得屏幕宽度
		this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);  
	    int screenW = (dm.widthPixels)/4;
		float tem1 = Float.parseFloat(temp);
		//13较适宜
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
		mHolder.unlockCanvasAndPost(canvas);// 更新屏幕显示内容
	}
	
	/*
	 * 转换函数
	 * 输入字节数组，将其转换成数字字符串,向drawTemp()提供参数
	 * 输入   0x0A
	 * 输出  10
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
	 * 广播接收器
	 */
	private BroadcastReceiver MyBroadeReceive_one =new  BroadcastReceiver() {
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			byte[] msg = arg1.getByteArrayExtra("BluetoothData_one");
			String time = df.format(new Date());
			tx_TempNow.setText(time+"\n"+Convent(msg)+" °C");
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
