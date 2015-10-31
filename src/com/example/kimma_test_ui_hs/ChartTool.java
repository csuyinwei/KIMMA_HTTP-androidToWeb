package com.example.kimma_test_ui_hs;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class ChartTool {
	
	private Context context;
	
	private LinearLayout temperautreForOneDay;
	
	private LinearLayout temperautreForAll;


	public ChartTool(Context context, LinearLayout temperautreForOneDay,
			LinearLayout temperautreForAll) {
		super();
		this.context = context;
		this.temperautreForOneDay = temperautreForOneDay;
		this.temperautreForAll = temperautreForAll;
	}


	public void TemperautreForOneDay(){
		//同样是需要数据dataset和视图渲染器renderer  
        XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();  
        XYSeries  series = new XYSeries("第一条线");  
        series.add(1, 6);  
        series.add(2, 5);  
        series.add(3, 7);  
        series.add(4, 4); 
        mDataset.addSeries(series);  

          
          
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();  
        //设置图表的X轴的当前方向  
        mRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);  
        mRenderer.setXTitle("日期");//设置为X轴的标题  
        mRenderer.setYTitle("价格");//设置y轴的标题  
        mRenderer.setAxisTitleTextSize(20);//设置轴标题文本大小  
        mRenderer.setChartTitle("价格走势图");//设置图表标题  
        mRenderer.setChartTitleTextSize(30);//设置图表标题文字的大小  
        mRenderer.setLabelsTextSize(18);//设置标签的文字大小  
        mRenderer.setLegendTextSize(20);//设置图例文本大小  
        mRenderer.setPointSize(10f);//设置点的大小  
        mRenderer.setYAxisMin(0);//设置y轴最小值是0  
        mRenderer.setYAxisMax(15);  
        mRenderer.setYLabels(10);//设置Y轴刻度个数（貌似不太准确）  
        mRenderer.setXAxisMax(5);  
        mRenderer.setShowGrid(true);//显示网格  
        //将x标签栏目显示如：1,2,3,4替换为显示1月，2月，3月，4月  
        mRenderer.addXTextLabel(1, "1月");  
        mRenderer.addXTextLabel(2, "2月");  
        mRenderer.addXTextLabel(3, "3月");  
        mRenderer.addXTextLabel(4, "4月");  
        mRenderer.setXLabels(0);//设置只显示如1月，2月等替换后的东西，不显示1,2,3等  
        mRenderer.setMargins(new int[] { 120, 30, 15, 20 });//设置视图位置  
        
        XYSeriesRenderer r = new XYSeriesRenderer();//(类似于一条线对象)  
        r.setColor(Color.RED);//设置颜色  
        r.setPointStyle(PointStyle.CIRCLE);//设置点的样式  
        r.setFillPoints(true);//填充点（显示的点是空心还是实心）  
        r.setDisplayChartValues(true);//将点的值显示出来  
        r.setChartValuesSpacing(10);//显示的点的值与图的距离  
        r.setChartValuesTextSize(25);//点的值的文字大小  
          
      //  r.setFillBelowLine(true);//是否填充折线图的下方  
      //  r.setFillBelowLineColor(Color.GREEN);//填充的颜色，如果不设置就默认与线的颜色一致  
        r.setLineWidth(3);//设置线宽  
        mRenderer.addSeriesRenderer(r);  
        
        GraphicalView  view = ChartFactory.getLineChartView(context, mDataset, mRenderer);  
        view.setBackgroundColor(Color.BLACK); 
        
        //setContentView(view); 
        //LinearLayout layout = (LinearLayout)findViewById(R.id.linechart);
        temperautreForOneDay.addView(view, new LayoutParams(LayoutParams.FILL_PARENT, 400));
	}
	
	public void TemperautreForAll(){
		//同样是需要数据dataset和视图渲染器renderer  
        XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();  
        XYSeries  series = new XYSeries("第一条线");  
        series.add(1, 6);  
        series.add(2, 5);  
        series.add(3, 7);  
        series.add(4, 4); 
        mDataset.addSeries(series);  

          
          
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();  
        //设置图表的X轴的当前方向  
        mRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);  
        mRenderer.setXTitle("日期");//设置为X轴的标题  
        mRenderer.setYTitle("价格");//设置y轴的标题  
        mRenderer.setAxisTitleTextSize(20);//设置轴标题文本大小  
        mRenderer.setChartTitle("价格走势图");//设置图表标题  
        mRenderer.setChartTitleTextSize(30);//设置图表标题文字的大小  
        mRenderer.setLabelsTextSize(18);//设置标签的文字大小  
        mRenderer.setLegendTextSize(20);//设置图例文本大小  
        mRenderer.setPointSize(10f);//设置点的大小  
        mRenderer.setYAxisMin(0);//设置y轴最小值是0  
        mRenderer.setYAxisMax(15);  
        mRenderer.setYLabels(10);//设置Y轴刻度个数（貌似不太准确）  
        mRenderer.setXAxisMax(5);  
        mRenderer.setShowGrid(true);//显示网格  
        //将x标签栏目显示如：1,2,3,4替换为显示1月，2月，3月，4月  
        mRenderer.addXTextLabel(1, "1月");  
        mRenderer.addXTextLabel(2, "2月");  
        mRenderer.addXTextLabel(3, "3月");  
        mRenderer.addXTextLabel(4, "4月");  
        mRenderer.setXLabels(0);//设置只显示如1月，2月等替换后的东西，不显示1,2,3等  
        mRenderer.setMargins(new int[] { 120, 30, 15, 20 });//设置视图位置  
        
        XYSeriesRenderer r = new XYSeriesRenderer();//(类似于一条线对象)  
        r.setColor(Color.RED);//设置颜色  
        r.setPointStyle(PointStyle.CIRCLE);//设置点的样式  
        r.setFillPoints(true);//填充点（显示的点是空心还是实心）  
        r.setDisplayChartValues(true);//将点的值显示出来  
        r.setChartValuesSpacing(10);//显示的点的值与图的距离  
        r.setChartValuesTextSize(25);//点的值的文字大小  
          
      //  r.setFillBelowLine(true);//是否填充折线图的下方  
      //  r.setFillBelowLineColor(Color.GREEN);//填充的颜色，如果不设置就默认与线的颜色一致  
        r.setLineWidth(3);//设置线宽  
        mRenderer.addSeriesRenderer(r);  
        
        GraphicalView  view = ChartFactory.getLineChartView(context, mDataset, mRenderer);  
        view.setBackgroundColor(Color.BLACK); 
        
        //setContentView(view); 
        //LinearLayout layout = (LinearLayout)findViewById(R.id.linechart);
        temperautreForOneDay.addView(view, new LayoutParams(LayoutParams.FILL_PARENT, 400));
	}

	
	
}
