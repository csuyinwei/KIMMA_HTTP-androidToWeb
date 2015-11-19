package com.example.kimma_test_ui_hs;




import android.annotation.SuppressLint;

import android.content.IntentFilter;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Conn.ConnectionChangeReceiver;
import com.example.fragment.HomeFragment;
import com.example.fragment.HomeFragment.OnArticleSelectedListener_HomeFregment;
import com.example.fragment.QueryFragment;
import com.example.fragment.QueryFragment.OnArticleSelectedListener_QueryFregment;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

@SuppressLint("HandlerLeak")
public class LoginActivity extends FragmentActivity implements View.OnClickListener,OnArticleSelectedListener_QueryFregment,OnArticleSelectedListener_HomeFregment {
	private LoginActivity mContext;
	private ResideMenu resideMenu;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemQuery;
    private Button bt_left;
    public  TextView tx_connect_state;
    private Fragment homeFragment = new HomeFragment();
    private Fragment queryFragment = new QueryFragment();
    boolean isRun = true;
    private static boolean isExit = false;

    private ConnectionChangeReceiver myReceiver;
    
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
		setContentView(R.layout.activity_login);
		registerReceiver_conn();
		tx_connect_state = (TextView)findViewById(R.id.tx_connect_state_login);
		bt_left = (Button)findViewById(R.id.title_bar_left_menu);
		mContext = this;
        setUpMenu();
        if( savedInstanceState == null )
            changeFragment(homeFragment); 
	}
	
	
	
	 @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver_Conn();
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
	            Toast.makeText(getApplicationContext(), "再按一次退出程序",
	                    Toast.LENGTH_SHORT).show();
	            // 利用handler延迟发送更改状态信息
	            mHandler.sendEmptyMessageDelayed(0, 2000);
	        } else {
	            finish();
	            System.exit(0);
	        }
	    }


	private void setUpMenu() {

	        // attach to current activity;
	        resideMenu = new ResideMenu(this);
	        resideMenu.setUse3D(true);
	        resideMenu.setBackground(R.drawable.menu_background);
	        resideMenu.attachToActivity(this);
	        resideMenu.setMenuListener(menuListener);
	        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip. 
	        resideMenu.setScaleValue(0.9f);
	        // create menu items;
	        itemHome     = new ResideMenuItem(this, R.drawable.icon_home,"主页");
	        itemQuery  = new ResideMenuItem(this, R.drawable.icon_settings,"溯源");

	        itemHome.setOnClickListener(this);
	        itemQuery.setOnClickListener(this);


	        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
	        resideMenu.addMenuItem(itemQuery, ResideMenu.DIRECTION_LEFT);
	        
	        bt_left.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
	            }
	        });
 
	    }
	
	
	 
	 @Override
	    public boolean dispatchTouchEvent(MotionEvent ev) {
		 return resideMenu.onInterceptTouchEvent(ev) || super.dispatchTouchEvent(ev); 
	    }

	    @Override
	    public void onClick(View view) {
	        if (view == itemHome){
	            changeFragment(homeFragment);
	        }else if (view == itemQuery){
	            changeFragment(queryFragment);
	            
	        }
	        resideMenu.closeMenu();
	    }

	    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {  
	        @Override  
	        public void openMenu() {  
	            Toast.makeText(mContext, "溯源商品历史温度曲线", Toast.LENGTH_SHORT).show();  
	        }  
	  
	        @Override  
	        public void closeMenu() {  
	        }  
	    };  

	    private void changeFragment(Fragment targetFragment){
	        resideMenu.clearIgnoredViewList();
	        getSupportFragmentManager()
	                .beginTransaction()
	                .replace(R.id.main_fragment, targetFragment, "fragment")
	                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
	                .commit();
	    }

	    public ResideMenu getResideMenu(){
	        return resideMenu;
	    }
	    
		@Override
		public void onArticleSelected(String str) {
			// TODO Auto-generated method stub
			//在此对Fragment中返回的的数据进行处理
			System.out.println("LoginActivity收到返回的数据为:"+str);
		}
		
		private  void registerReceiver_conn(){
	        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
	        myReceiver=new ConnectionChangeReceiver();
	        this.registerReceiver(myReceiver, filter);
	    }
		
		private  void unregisterReceiver_Conn(){
	        this.unregisterReceiver(myReceiver);
	    }

		

}
