package com.example.kimma_test_ui_hs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MainActivity extends ListActivity {

	private Button scan_bt;
    private ArrayList<HashMap<String, Object>> listItems;    //存放文字、图片信息
    private SimpleAdapter listItemAdapter;           //适配器   
	//与蓝牙有关的
	private BluetoothManager mBtManager;
	private BluetoothAdapter mBtAdapter;	
	private Handler mHandler;
	//存储蓝牙扫描结果,	key:name_address, value: iBeacon
	private Map<String,iBeacon> mapScanResult;
    @Override
    public void onCreate(Bundle icicle)   {
	    super.onCreate(icicle);
	    setContentView(R.layout.activity_main);
	 
	  //获取BluetoothAdapter
	    mBtManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
	    mBtAdapter = mBtManager.getAdapter();
  		
  		//检测蓝牙是否打开
  		if(mBtManager ==null || !mBtAdapter.isEnabled()){
  			Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
  			startActivityForResult(intent, 1);
  		}
	    
	    scan_bt = (Button)findViewById(R.id.scan_bt);
		mapScanResult = new HashMap<String,iBeacon>();
		mHandler = new Handler();
	    scan_bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//先扫描
				initParam();
				//然后初始化ListView
				while(mapScanResult==null){}
	            System.out.println("initListView");
	            for(String dev:mapScanResult.keySet()){  
	            	System.out.println(dev);
	            	System.out.println(mapScanResult.get(dev).getRSSI());
	            }
	    		initListView();
	        }
		});    
    }
    private void initListView(){   
        listItems = new ArrayList<HashMap<String, Object>>();
        for(String dev:mapScanResult.keySet()){  
            HashMap<String, Object> map = new HashMap<String, Object>();   
            map.put("address", dev);    //地址
            map.put("rssi",mapScanResult.get(dev).getRSSI());
            map.put("ItemImage",R.drawable.bluetooth);   //图片   
            listItems.add(map);
        }
        //生成适配器的Item和动态数组对应的元素   
        listItemAdapter = new SimpleAdapter(
        		this,
        		listItems,   // listItems数据源    
                R.layout.list_item,  //ListItem的XML布局实现  
                new String[] {"address","rssi","ItemImage"},     //动态数组与ImageItem对应的子项         
                new int[] {R.id.address,R.id.rssi, R.id.ItemImage}//list_item.xml布局文件里面的一个ImageView的ID,一个TextView 的ID  
        );
        MainActivity.this.setListAdapter(listItemAdapter);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)  {
        // TODO Auto-generated method stub       
        String []Click_key= (String[])mapScanResult.keySet().toArray(new String[mapScanResult.keySet().size()]);
        String Click_Address = (String)(mapScanResult.get(Click_key[position]).getAddress());
        System.out.println(Click_Address);
        Intent intent = new Intent(MainActivity.this,ShowDataActivity.class);
        intent.putExtra("Click_Address", Click_Address);
        startActivity(intent);
    }  
	
	//初始化扫描蓝牙的工作
	public void initParam(){
	    // 设备SDK版本大于17（Build.VERSION_CODES.JELLY_BEAN_MR1）才支持BLE 4.0  
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
	    	System.out.println("SDK > 17");
	    	mBtManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
	    	mBtAdapter = mBtManager.getAdapter();
	    	if(!mBtAdapter.isEnabled())
	    	{	
	    		//如果蓝牙未开启，则开启蓝牙，这种方法会弹出提示对话框
	    		Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	    		startActivityForResult(enableIntent, 1);
	    	}
	    	startScan();
	    }
	}
	
	//开始扫描蓝牙
	@SuppressLint("NewApi") 
	public void startScan()  
	{  
		
	    if (mBtAdapter != null && mBtAdapter.isEnabled()) {  
	        // 5秒后停止扫描，毕竟扫描蓝牙设备比较费电，根据定位及时性自行调整该值  
	        mHandler.postDelayed(new Runnable() { 
				@Override
	            public void run() {
	                mBtAdapter.stopLeScan(bltScanCallback);  
	            }
	        },500);
		        System.out.println("startLeScan:");

		        mBtAdapter.startLeScan(bltScanCallback); // 开始扫描	        	

	    }
	}
	private LeScanCallback bltScanCallback = new BluetoothAdapter.LeScanCallback() {  
	    @Override  
	    public void onLeScan(final BluetoothDevice device, int rssi,byte[] scanRecord)
	    {   
	        iBeacon mBeacon = new iBeacon();
            String address = device.getAddress();   // 获取Mac地址  
            String name = device.getName();         // 获取设备名称  
            String key = name + "_" + address;
            mBeacon.setAddress(address);		
            mBeacon.setRSSI(rssi);
            if (!mapScanResult.containsKey(key)){
                mapScanResult.put(key, mBeacon);
            }
	    }
	};
}
