package com.example.Splash;

import com.example.kimma_test_ui_hs.LoginActivity;
import com.example.kimma_test_ui_hs.MainActivity;
import com.example.kimma_test_ui_hs.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.TextView;

public class SplashActivity extends Activity {
	
    private TextView tx_1,tx_2,tx_3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        tx_1 =(TextView)findViewById(R.id.StartText1);
        tx_2 =(TextView)findViewById(R.id.StartText2);
        tx_3 =(TextView)findViewById(R.id.StartText3);
        String fontPath = "fonts/font2.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        tx_1.setTypeface(tf);
        tx_2.setTypeface(tf);
        tx_3.setTypeface(tf);
        Handler x = new Handler();
        x.postDelayed(new splashhandler(), 2000);
        
    }
   
	 class splashhandler implements Runnable{
	        public void run() {
	            startActivity(new Intent(getApplication(),LoginActivity.class));
	            SplashActivity.this.finish();
	        }
	        
	    }

    

}
