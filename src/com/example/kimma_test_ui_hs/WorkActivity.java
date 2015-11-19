package com.example.kimma_test_ui_hs;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import com.example.fragment.DataFragment;
import com.example.fragment.MachineFragment;
import com.example.fragment.MyFragmentPagerAdapter;
import com.example.fragment.StorehouseFragment;
import com.example.kimma_test_ui_hs.R;
import com.example.tools.Bluetooth;
import com.example.tools.Tools;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class WorkActivity extends FragmentActivity {
	private ImageView imageBar; 
	private ViewPager mPager;  
    private ArrayList<Fragment> fragmentList;    
    public TextView tx_data, tx_storehouse, tx_tx_goods,tx_userAuthority,tx_userName,tx_logo,tx_bluetooth_connect_state,tx_http_connect_state;  
    private TextView tx_attrbution_name,tx_attrbution_startData,tx_attrbution_endData;
    private int currIndex;//当前页卡编号  
    private int bmpW;//横线图片宽度
    private int offset;//图片移动的偏移量
    private String address;
	public Bluetooth bluetoothTool = new Bluetooth(WorkActivity.this);
    private static boolean isExit = false;
    private Fragment dataFragment,storehouseFragment,machineFragment;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
    
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_work);
		Intent intent = getIntent();
		address = intent.getStringExtra("Click_Address");
		SharedPreferences mySharedPreferences = getSharedPreferences("ADDRESS", Activity.MODE_PRIVATE); 
		SharedPreferences.Editor editor = mySharedPreferences.edit(); 
		editor.putString("address", address);
		editor.commit();
		bluetoothTool.setAddress(address);
		bluetoothTool.initBluetoolh();
		bluetoothTool.startScan();
		InitTextView();  
        InitImage();  
        InitViewPager(); 
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	        if (keyCode == KeyEvent.KEYCODE_BACK) {
	            exit();
	            return false;
	        }
	        return super.onKeyDown(keyCode, event);
	    }

	    private void exit() {
	        if (!isExit) {
	            isExit = true;
	            Toast.makeText(getApplicationContext(), "再按一次断开连接",
	                    Toast.LENGTH_SHORT).show();
	            // 利用handler延迟发送更改状态信息
	            mHandler.sendEmptyMessageDelayed(0, 2000);
	        } else {
	        	close();
	            finish();
	            System.exit(0);
	        }
	    }
	
    
  //断开连接
  	public void close() {  
  	    bluetoothTool.bluetoothGatt.close();  
  	}  
	
	 public void InitTextView(){  
		    String fontPath = "fonts/font.ttf";
		    tx_logo = (TextView)findViewById(R.id.logo);
		    tx_attrbution_name = (TextView)findViewById(R.id.attrbution_name);
		    tx_attrbution_startData = (TextView)findViewById(R.id.attrbution_startData);
		    tx_attrbution_endData = (TextView)findViewById(R.id.attrbution_endData);
		    Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
		    tx_logo.setTypeface(tf);
	        tx_data = (TextView)findViewById(R.id.tv_data);  
	        tx_data.setTextColor(Color.BLUE);
	        tx_storehouse = (TextView)findViewById(R.id.tv_storehouse);  
	        tx_tx_goods = (TextView)findViewById(R.id.tv_goods);   
	        tx_data.setOnClickListener(new txListener(0));  
	        tx_storehouse.setOnClickListener(new txListener(1));  
	        tx_tx_goods.setOnClickListener(new txListener(2));   
	        tx_bluetooth_connect_state = (TextView)findViewById(R.id.bluetooth_connect_state);
	        tx_http_connect_state = (TextView)findViewById(R.id.http_connect_state);
	    }  
	 
	 public class txListener implements OnClickListener{  
	        private int index=0;  
	          
	        public txListener(int i) {  
	            index =i;  
	        }  
	        @Override  
	        public void onClick(View v) {  
	            // TODO Auto-generated method stub  
	            mPager.setCurrentItem(index);  
	            
	        }  
	    } 
	 
	 public void InitViewPager(){  
	        mPager = (ViewPager)findViewById(R.id.viewpager);  
	        fragmentList = new ArrayList<Fragment>(); 	        
	        dataFragment= new DataFragment();	        
	        storehouseFragment = new StorehouseFragment() ; 
	        machineFragment = new MachineFragment();  
	        fragmentList.add(dataFragment);  
	        fragmentList.add(storehouseFragment);  
	        fragmentList.add(machineFragment);  
	        //给ViewPager设置适配器  
	        mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));  
	        mPager.setCurrentItem(0);//设置当前显示标签页为第一页  
	        mPager.setOnPageChangeListener(new MyOnPageChangeListener());//页面变化时的监听器  
	    } 
	 
	 public void InitImage(){  
	        imageBar= (ImageView)findViewById(R.id.cursor);  
	        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.bar).getWidth();  
	        DisplayMetrics dm = new DisplayMetrics();  
	        getWindowManager().getDefaultDisplay().getMetrics(dm);  
	        int screenW = dm.widthPixels;  
	        offset = (screenW/3 - bmpW)/2;  
	        //imgageview设置平移，使下划线平移到初始位置（平移一个offset）  
	        Matrix matrix = new Matrix();  
	        matrix.postTranslate(offset, 0);  
	        imageBar.setImageMatrix(matrix);  
	    } 
	 
	 public class MyOnPageChangeListener implements OnPageChangeListener{  
	        private int one = offset *2 +bmpW;//两个相邻页面的偏移量  
	        @Override  
	        public void onPageScrolled(int arg0, float arg1, int arg2) {}  
	          
	        @Override  
	        public void onPageScrollStateChanged(int arg0) {}  
	          
	        @Override  
	        public void onPageSelected(int arg0) {  
	            Animation animation = new TranslateAnimation(currIndex*one,arg0*one,0,0);//平移动画  
	            currIndex = arg0;  
	            animation.setFillAfter(true);//动画终止时停留在最后一帧，不然会回到没有执行前的状态  
	            animation.setDuration(200);//动画持续时间0.2秒  
	            imageBar.startAnimation(animation);//是用ImageView来显示动画的  
	            if(currIndex == 0){
	            	tx_data.setTextColor(Color.BLUE);
	            	tx_storehouse.setTextColor(Color.BLACK);
		            tx_tx_goods.setTextColor(Color.BLACK);
	            }
	            else if(currIndex == 1){
	            	tx_data.setTextColor(Color.BLACK);
	            	tx_storehouse.setTextColor(Color.BLUE);
		            tx_tx_goods.setTextColor(Color.BLACK);
	            }
	            else if(currIndex == 2){
	            	tx_data.setTextColor(Color.BLACK);
	            	tx_storehouse.setTextColor(Color.BLACK);
	            	tx_tx_goods.setTextColor(Color.BLUE);
	            }
	            int i = currIndex + 1;   
	        }  
	    }
}
