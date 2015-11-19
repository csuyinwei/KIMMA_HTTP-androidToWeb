package com.example.kimma_test_ui_hs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.modle.TempPoint;
import com.example.tools.SourceChart;
import com.example.tools.Tools;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SourceChartActivity extends ActionBarActivity {
	
	private Button bt_forword,bt_next;
	private TextView tx_PageInfo;
	private LinearLayout layout;
	private SourceChart sourceChart;
	private static int CurrentIndex = 0;
	private Tools tool = new Tools();
	private ArrayList<TempPoint> points;
	

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_source_chart);
		layout = (LinearLayout)findViewById(R.id.Source_Chart);
		sourceChart = new SourceChart(this,layout);
		tx_PageInfo = (TextView)findViewById(R.id.PageInfo);
		Intent intent = getIntent();
		List<Map<String,String>> list = (ArrayList<Map<String,String>>)intent.getSerializableExtra("SourceTempData");
		System.out.println("���յ���list:"+list.size()+" "+list.get(1).get("process"));
		try {
			points = (ArrayList<TempPoint>) tool.Souce_data_handle(list);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sourceChart.DispatchDetaile(points.get(CurrentIndex).getProcess(), points.get(CurrentIndex).getDate(), points.get(CurrentIndex).getData());
		tx_PageInfo.setText("��  "+points.size()+" ҳ,"+"��ǰΪ "+(CurrentIndex+1)+" ҳ");
		bt_forword = (Button)findViewById(R.id.forword);
		bt_forword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				System.out.println("��һҳ");
				if((--CurrentIndex)<0){
					System.out.println("�Ѿ��ǵ�һҳ");
					toast(0);
					CurrentIndex++;
				}else{
					layout.removeViewAt(0);
					sourceChart.DispatchDetaile(points.get(CurrentIndex).getProcess(), points.get(CurrentIndex).getDate(), points.get(CurrentIndex).getData());
					tx_PageInfo.setText("��  "+points.size()+" ҳ,"+"��ǰΪ "+(CurrentIndex+1)+" ҳ");
				}
			}
		});
		bt_next  = (Button)findViewById(R.id.next);
		bt_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				System.out.println("��һ��");
				if((++CurrentIndex)>points.size()-1){
					toast(1);
					System.out.println("�Ѿ������һҳ");
					CurrentIndex--;
				}else{
					layout.removeViewAt(0);
					sourceChart.DispatchDetaile(points.get(CurrentIndex).getProcess(), points.get(CurrentIndex).getDate(), points.get(CurrentIndex).getData());
					tx_PageInfo.setText("��  "+points.size()+" ҳ,"+"��ǰΪ "+(CurrentIndex+1)+" ҳ");
				}
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.source_chart, menu);
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
	
	private void toast(int n){
		if(n == 0){
			Toast.makeText(this, "�Ѿ��ǵ�һҳ", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(this, "�Ѿ������һҳ", Toast.LENGTH_SHORT).show();
		}
	}
}
