package com.example.tools;

import java.util.Calendar;
import java.util.Date;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class SourceChart {
	
	private Context context;
	private LinearLayout linearloyout;
	public SourceChart(Context context, LinearLayout linearloyout) {
		super();
		this.context = context;
		this.linearloyout = linearloyout;
	}
	
	public void DispatchDetaile(String name,Date time,int[] data){
		//同样是需要数据dataset和视图渲染器renderer  
		 XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();  
	        XYSeries  series = new XYSeries("");  
	        for(int i = 0;i<data.length;i++){
	        	series.add(i, data[i]);
	        }
	        mDataset.addSeries(series);  
	        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();  
	        //设置图表的X轴的当前方向  
	        
	        mRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);  
	        mRenderer.setXTitle("时间");//设置为X轴的标题  
	        mRenderer.setYTitle("温度(°C)");//设置y轴的标题  
	        mRenderer.setLabelsColor(Color.BLACK); // 设置轴标签颜色
	        mRenderer.setAxesColor(Color.BLACK);
	        mRenderer.setAxisTitleTextSize(20);//设置轴标题文本大小  
	        mRenderer.setChartTitle(name);//设置图表标题  
	        mRenderer.setChartTitleTextSize(50);//设置图表标题文字的大小  
	        mRenderer.setLabelsTextSize(30);//设置标签的文字大小  
	        mRenderer.setLegendTextSize(32);//设置图例文本大小  
	        mRenderer.setPointSize(4f);//设置点的大小  
	        mRenderer.setYAxisMin(0);//设置y轴最小值是0  
	        mRenderer.setYAxisMax(15);  
	        mRenderer.setYLabels(10);//设置Y轴刻度个数（貌似不太准确）  
	        mRenderer.setXAxisMax(5);  
	        mRenderer.setFitLegend(true);// 调整合适的位置
	        mRenderer.setShowGrid(true);//显示网格  
	        //设置背景颜色
	        mRenderer.setApplyBackgroundColor(true);
	        mRenderer.setBackgroundColor(Color.GRAY);//设置内部颜色
	        mRenderer.setMarginsColor(Color.WHITE);//设置外部背景色
	        Calendar calendar = Calendar.getInstance();
			calendar.setTime(time);
			
			for(int i = 0,n = i+1;i<data.length;i++){
				calendar.add(Calendar.MINUTE,n*(Tools.getK()));
				mRenderer.addXTextLabel(i,Tools.getSdf().format(calendar.getTime()));
			}
	        mRenderer.setXLabels(0);//设置只显示如1月，2月等替换后的东西，不显示1,2,3等  
	        mRenderer.setMargins(new int[] { 120, 30, 15, 20 });//设置视图位置  
	        mRenderer.setBackgroundColor(Color.TRANSPARENT);
	        XYSeriesRenderer r = new XYSeriesRenderer();//(类似于一条线对象)  
	        r.setColor(Color.BLACK);//设置颜色  
	        r.setPointStyle(PointStyle.CIRCLE);//设置点的样式  
	        r.setFillPoints(true);//填充点（显示的点是空心还是实心）  
	        r.setDisplayChartValues(true);//将点的值显示出来  
	        r.setChartValuesSpacing(10);//显示的点的值与图的距离  
	        r.setChartValuesTextSize(25);//点的值的文字大小  
	        r.setLineWidth(3);//设置线宽  
	        mRenderer.addSeriesRenderer(r);  
	        GraphicalView  view = ChartFactory.getLineChartView(context, mDataset, mRenderer);  
	        view.setBackgroundColor(Color.TRANSPARENT); 
	        linearloyout.addView(view, new LayoutParams(LayoutParams.FILL_PARENT, 800));
		}

}
