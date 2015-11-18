package com.example.tools;

import java.util.ArrayList;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.example.kimma_test_ui_hs.ChartActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class ChartTool {
	
	private ChartActivity context;
	
	private LinearLayout temperautreForOneDay;
	
	private LinearLayout temperautreForAll;
	
	private ArrayList<Map<String,String>> list ;


	public ChartTool(ChartActivity context, LinearLayout temperautreForOneDay,LinearLayout temperautreForAll,ArrayList<Map<String,String>> list) {
		super();
		this.context = context;
		this.temperautreForOneDay = temperautreForOneDay;
		this.temperautreForAll = temperautreForAll;
		this.list = list;
	}


	public void TemperautreForOneDay(){
		double max=-1,min=30;
		//同样是需要数据dataset和视图渲染器renderer  
        XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();  
        XYSeries  series = new XYSeries("温度曲线");  
        for(int i = 0;i<144;i++){
        	series.add(i, Double.valueOf(list.get(i).get("value")));
        	if(min > Double.valueOf(list.get(i).get("value"))){
        		min = Double.valueOf(list.get(i).get("value"));
        	}else if(max < Double.valueOf(list.get(i).get("value"))){
        		max = Double.valueOf(list.get(i).get("value"));
        	}
        }
        context.info_chartOneWeek.setText("最高: "+max+"°C"+" ,最低: "+min+"°C");
        mDataset.addSeries(series);  
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();  
        //设置图表的X轴的当前方向  
        mRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);  
        mRenderer.setXTitle("时间");//设置为X轴的标题  
        mRenderer.setYTitle("温度(°C)");//设置y轴的标题  
        mRenderer.setAxisTitleTextSize(20);//设置轴标题文本大小  
        mRenderer.setChartTitle("24小时内温度走势图");//设置图表标题  
        mRenderer.setChartTitleTextSize(30);//设置图表标题文字的大小  
        mRenderer.setLabelsTextSize(18);//设置标签的文字大小  
        mRenderer.setLegendTextSize(20);//设置图例文本大小  
        mRenderer.setPointSize(4f);//设置点的大小  
        mRenderer.setYAxisMin(0);//设置y轴最小值是0  
        mRenderer.setYAxisMax(30);  
        mRenderer.setYLabels(10);//设置Y轴刻度个数（貌似不太准确）  
        mRenderer.setXAxisMax(5);  
        mRenderer.setFitLegend(true);// 调整合适的位置
        mRenderer.setShowGrid(true);//显示网格  
        //将x标签栏目显示如：1,2,3,4替换为显示1月，2月，3月，4月    
        for(int i = 0;i<144;i++){
        	mRenderer.addXTextLabel(i, list.get(i).get("key"));
        }
        mRenderer.setXLabels(0);//设置只显示如1月，2月等替换后的东西，不显示1,2,3等  
        mRenderer.setBackgroundColor(Color.TRANSPARENT);
        mRenderer.setMargins(new int[] { 120, 30, 15, 20 });//设置视图位置  
        XYSeriesRenderer r = new XYSeriesRenderer();//(类似于一条线对象)  
        r.setColor(Color.RED);//设置颜色  
        r.setPointStyle(PointStyle.CIRCLE);//设置点的样式  
        r.setFillPoints(true);//填充点（显示的点是空心还是实心）  
        r.setDisplayChartValues(true);//将点的值显示出来  
        r.setChartValuesSpacing(10);//显示的点的值与图的距离  
        r.setChartValuesTextSize(20);//点的值的文字大小  
        r.setLineWidth(3);//设置线宽  
        mRenderer.addSeriesRenderer(r);  
        GraphicalView  view = ChartFactory.getLineChartView(context, mDataset, mRenderer);  
        view.setBackgroundColor(Color.TRANSPARENT); 
        temperautreForOneDay.addView(view, new LayoutParams(LayoutParams.FILL_PARENT, 550));
	}
	
	public void TemperautreForAll(){
		//同样是需要数据dataset和视图渲染器renderer  
        XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();  
        XYSeries  series = new XYSeries("温度曲线");  
        
        series.add(1, 6);  
        series.add(2, 5);  
        series.add(3, 7);  
        series.add(4, 4); 
        series.add(5, 5);  
        series.add(6, 7);  
        series.add(7, 4); 
        mDataset.addSeries(series);  
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();  
        //设置图表的X轴的当前方向  
        mRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);  
        mRenderer.setXTitle("时间");//设置为X轴的标题  
        mRenderer.setYTitle("温度");//设置y轴的标题  
        mRenderer.setAxisTitleTextSize(20);//设置轴标题文本大小  
        mRenderer.setChartTitle("15天温度走势图");//设置图表标题  
        mRenderer.setChartTitleTextSize(30);//设置图表标题文字的大小  
        mRenderer.setLabelsTextSize(18);//设置标签的文字大小  
        mRenderer.setLegendTextSize(20);//设置图例文本大小  
        mRenderer.setPointSize(4f);//设置点的大小  
        mRenderer.setYAxisMin(0);//设置y轴最小值是0  
        mRenderer.setYAxisMax(15);  
        mRenderer.setYLabels(10);//设置Y轴刻度个数（貌似不太准确）  
        mRenderer.setXAxisMax(5);  
        mRenderer.setFitLegend(true);// 调整合适的位置
        mRenderer.setShowGrid(true);//显示网格  
        mRenderer.addXTextLabel(1, "第1天");  
        mRenderer.addXTextLabel(2, "第2天");  
        mRenderer.addXTextLabel(3, "第3天");  
        mRenderer.addXTextLabel(4, "第4天"); 
        mRenderer.addXTextLabel(5, "第5天"); 
        mRenderer.addXTextLabel(6, "第6天"); 
        mRenderer.addXTextLabel(7, "第7天"); 
        mRenderer.setXLabels(0);//设置只显示如1月，2月等替换后的东西，不显示1,2,3等  
        mRenderer.setMargins(new int[] { 120, 30, 15, 20 });//设置视图位置  
        mRenderer.setBackgroundColor(Color.TRANSPARENT);
        XYSeriesRenderer r = new XYSeriesRenderer();//(类似于一条线对象)  
        r.setColor(Color.YELLOW);//设置颜色  
        r.setPointStyle(PointStyle.CIRCLE);//设置点的样式  
        r.setFillPoints(true);//填充点（显示的点是空心还是实心）  
        r.setDisplayChartValues(true);//将点的值显示出来  
        r.setChartValuesSpacing(10);//显示的点的值与图的距离  
        r.setChartValuesTextSize(25);//点的值的文字大小  
        r.setLineWidth(3);//设置线宽  
        mRenderer.addSeriesRenderer(r);  
        GraphicalView  view = ChartFactory.getLineChartView(context, mDataset, mRenderer);  
        view.setBackgroundColor(Color.TRANSPARENT); 
        temperautreForAll.addView(view, new LayoutParams(LayoutParams.FILL_PARENT, 550));
	}
	
}
