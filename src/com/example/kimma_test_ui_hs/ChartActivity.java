package com.example.kimma_test_ui_hs;

import java.util.ArrayList;
import java.util.Map;
import com.example.tools.BluetoothChart;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChartActivity extends ActionBarActivity {
	private LinearLayout chartOneWeek,chartForAll;
	private BluetoothChart chartTool;
	public TextView info_chartOneWeek,info_chartForAll;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chart);
		Intent intent = getIntent();
		ArrayList<Map<String,String>> list = (ArrayList<Map<String,String>>)intent.getSerializableExtra("TempData");
		//输出测试
//		for(int i =0;i<list.size();i++){
//			list.get(i).get("X");
//			list.get(i).get("Y");
//			System.out.println("X:"+list.get(i).get("X")+"   Y:"+list.get(i).get("Y"));
//		}
		info_chartOneWeek = (TextView)findViewById(R.id.info_chartOneWeek);
		info_chartOneWeek.setText("表一信息");
		info_chartForAll = (TextView)findViewById(R.id.info_chartForAll);
		info_chartForAll.setText("表二信息");
		chartOneWeek = (LinearLayout)findViewById(R.id.chartOneWeek);
		chartForAll = (LinearLayout)findViewById(R.id.chartForAll);
		chartTool = new BluetoothChart(this, chartOneWeek, chartForAll,list);
		chartTool.TemperautreForOneDay();
		chartTool.TemperautreForAll();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.chart, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
