package com.example.kimma_test_ui_hs;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;

public class CalendarActivity extends ActionBarActivity {
	
	private Button bt_submit;
	private String calendar = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_calendar);
		bt_submit = (Button)findViewById(R.id.bt_Subimt);
		CalendarView calendarView=(CalendarView)findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                    int dayOfMonth) {
                // TODO Auto-generated method stub
                String date = year + "-" + (month+1) + "-" + dayOfMonth;
                System.out.println("ÈÕÆÚ£º"+date);
                calendar = date;
            }
        });
        bt_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				System.out.println(calendar);
				Intent intent = new Intent();
				intent.putExtra("calendar", calendar);
				CalendarActivity.this.setResult(2,intent);
				CalendarActivity.this.finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calendar, menu);
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
}
