package com.example.kimma_test_ui_hs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.fragment.DataFragment;
import com.example.fragment.DespatchRegisterFragment;
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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class WorkActivity extends FragmentActivity {
	private ImageView imageBar; 
	private ViewPager mPager;  
    private ArrayList<Fragment> fragmentList;    
    public TextView tv_despatch,tx_data, tx_storehouse, tx_tx_goods,tx_userAuthority,tx_userName,tx_logo,tx_bluetooth_connect_state,tx_http_connect_state;  
    public Button bt_flash;
    private int currIndex;//当前页卡编号  
    private int bmpW;//横线图片宽度
    private int offset;//图片移动的偏移量
    private String address;
	public Bluetooth bluetoothTool = new Bluetooth(WorkActivity.this);
    private static boolean isExit = false;
    private Fragment dataFragment,storehouseFragment,machineFragment,despatchRegisterFragment;
    private Tools tool = new Tools();
    private List<Map<String,String>> list = null;
//    private static final String MyAction_startCash = "com.example.WorkAActivity.startCash";
//    private static final String MyAction_endCash = "com.example.WorkAActivity.endCash";
    
    private boolean isCash = false;


	public boolean isCash() {
		return isCash;
	}


	private boolean isRead = false;
    
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
    
    public List<Map<String, String>> getList() {
		return list;
	}
    
   
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
        
        IntentFilter filter_all =new IntentFilter("com.example.bluetooth_all");//注册自定义广播，用于接收历史温度数据
		registerReceiver(MyBroadeReceive_all, filter_all);
		
		
		
        bt_flash = (Button)findViewById(R.id.Flasd);
        bt_flash.setEnabled(false);
        bt_flash.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				final byte[] data = {(byte) 0xA1,0x30,0x00,0x00,0x00};
				bt_flash.setText("正在缓存数据......");
				//发送开始接受数据的广播
//				Intent intent  = new Intent();
// 				intent.setAction(MyAction_startCash);
// 				sendBroadcast(intent);
				new Thread(){ 
				     public void run(){ 
				        // do something 
				    	 int i = 1;
				    	 isRead = true;
				    	 while(isRead){
			        			System.out.println("第"+i+"次数据");
			        			bluetoothTool.writeCharacteristic(bluetoothTool.characteristic,data);
								System.out.println("开始时间:"+ System.currentTimeMillis());
								try {
									Thread.sleep(4000);//至少每次通信间隔1.5秒
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								bluetoothTool.readCharacteristic(bluetoothTool.characteristic);
								try {
									Thread.sleep(4000);//至少每次通信间隔1.5秒
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								i++;
								
			        		}
				     } 
				}.start();
			}
		});
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
		   
		    Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
		    tx_logo.setTypeface(tf);
		    tv_despatch = (TextView)findViewById(R.id.tv_despatch);
	        tx_data = (TextView)findViewById(R.id.tv_data);  
	        tv_despatch.setTextColor(Color.BLUE);
	        tx_storehouse = (TextView)findViewById(R.id.tv_storehouse);  
	        tx_tx_goods = (TextView)findViewById(R.id.tv_goods);   
	        tv_despatch.setOnClickListener(new txListener(0));
	        tx_data.setOnClickListener(new txListener(1));  
	        tx_storehouse.setOnClickListener(new txListener(2));  
	        tx_tx_goods.setOnClickListener(new txListener(3));   
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
	        despatchRegisterFragment = new DespatchRegisterFragment();
	        fragmentList.add(despatchRegisterFragment);
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
	        offset = (screenW/4 - bmpW)/2;  
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
	            	tv_despatch.setTextColor(Color.BLUE);
	            	tx_data.setTextColor(Color.BLACK);
	            	tx_storehouse.setTextColor(Color.BLACK);
		            tx_tx_goods.setTextColor(Color.BLACK);
	            }
	            else if(currIndex == 1){
	            	tv_despatch.setTextColor(Color.BLACK);
	            	tx_data.setTextColor(Color.BLUE);
	            	tx_storehouse.setTextColor(Color.BLACK);
		            tx_tx_goods.setTextColor(Color.BLACK);
	            } else if(currIndex == 2){
	            	tv_despatch.setTextColor(Color.BLACK);
	            	tx_data.setTextColor(Color.BLACK);
	            	tx_storehouse.setTextColor(Color.BLUE);
	            	tx_tx_goods.setTextColor(Color.BLACK);
	            }else if(currIndex == 3){
	            	tv_despatch.setTextColor(Color.BLACK);
	            	tx_data.setTextColor(Color.BLACK);
	            	tx_storehouse.setTextColor(Color.BLACK);
	            	tx_tx_goods.setTextColor(Color.BLUE);
	            }
	        }  
	    }
	 
	 
	 private BroadcastReceiver MyBroadeReceive_all =new  BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
	          //通知UI界面接收完成
				Log.i("历史数据已接受", "");
				isRead = false;
				byte[] msg = arg1.getByteArrayExtra("BluetoothData_all");
				System.out.println("广播接收到的历史数据长度 ：" + msg.length);
				String[] dataStr = tool.HaxToString(msg,msg.length);
				list = tool.DealData(dataStr);
				Log.i("workActivity中List数据大小", list.size()+"");
				bt_flash.setText("缓存数据完成");
				bt_flash.setEnabled(false);
//				Intent intent  = new Intent();
// 				intent.setAction(MyAction_endCash);
// 				sendBroadcast(intent);
				isCash = true;
			}
		};
		
}
