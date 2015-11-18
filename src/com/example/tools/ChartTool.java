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
		//ͬ������Ҫ����dataset����ͼ��Ⱦ��renderer  
        XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();  
        XYSeries  series = new XYSeries("�¶�����");  
        for(int i = 0;i<144;i++){
        	series.add(i, Double.valueOf(list.get(i).get("value")));
        	if(min > Double.valueOf(list.get(i).get("value"))){
        		min = Double.valueOf(list.get(i).get("value"));
        	}else if(max < Double.valueOf(list.get(i).get("value"))){
        		max = Double.valueOf(list.get(i).get("value"));
        	}
        }
        context.info_chartOneWeek.setText("���: "+max+"��C"+" ,���: "+min+"��C");
        mDataset.addSeries(series);  
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();  
        //����ͼ���X��ĵ�ǰ����  
        mRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);  
        mRenderer.setXTitle("ʱ��");//����ΪX��ı���  
        mRenderer.setYTitle("�¶�(��C)");//����y��ı���  
        mRenderer.setAxisTitleTextSize(20);//����������ı���С  
        mRenderer.setChartTitle("24Сʱ���¶�����ͼ");//����ͼ�����  
        mRenderer.setChartTitleTextSize(30);//����ͼ��������ֵĴ�С  
        mRenderer.setLabelsTextSize(18);//���ñ�ǩ�����ִ�С  
        mRenderer.setLegendTextSize(20);//����ͼ���ı���С  
        mRenderer.setPointSize(4f);//���õ�Ĵ�С  
        mRenderer.setYAxisMin(0);//����y����Сֵ��0  
        mRenderer.setYAxisMax(30);  
        mRenderer.setYLabels(10);//����Y��̶ȸ�����ò�Ʋ�̫׼ȷ��  
        mRenderer.setXAxisMax(5);  
        mRenderer.setFitLegend(true);// �������ʵ�λ��
        mRenderer.setShowGrid(true);//��ʾ����  
        //��x��ǩ��Ŀ��ʾ�磺1,2,3,4�滻Ϊ��ʾ1�£�2�£�3�£�4��    
        for(int i = 0;i<144;i++){
        	mRenderer.addXTextLabel(i, list.get(i).get("key"));
        }
        mRenderer.setXLabels(0);//����ֻ��ʾ��1�£�2�µ��滻��Ķ���������ʾ1,2,3��  
        mRenderer.setBackgroundColor(Color.TRANSPARENT);
        mRenderer.setMargins(new int[] { 120, 30, 15, 20 });//������ͼλ��  
        XYSeriesRenderer r = new XYSeriesRenderer();//(������һ���߶���)  
        r.setColor(Color.RED);//������ɫ  
        r.setPointStyle(PointStyle.CIRCLE);//���õ����ʽ  
        r.setFillPoints(true);//���㣨��ʾ�ĵ��ǿ��Ļ���ʵ�ģ�  
        r.setDisplayChartValues(true);//�����ֵ��ʾ����  
        r.setChartValuesSpacing(10);//��ʾ�ĵ��ֵ��ͼ�ľ���  
        r.setChartValuesTextSize(20);//���ֵ�����ִ�С  
        r.setLineWidth(3);//�����߿�  
        mRenderer.addSeriesRenderer(r);  
        GraphicalView  view = ChartFactory.getLineChartView(context, mDataset, mRenderer);  
        view.setBackgroundColor(Color.TRANSPARENT); 
        temperautreForOneDay.addView(view, new LayoutParams(LayoutParams.FILL_PARENT, 550));
	}
	
	public void TemperautreForAll(){
		//ͬ������Ҫ����dataset����ͼ��Ⱦ��renderer  
        XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();  
        XYSeries  series = new XYSeries("�¶�����");  
        
        series.add(1, 6);  
        series.add(2, 5);  
        series.add(3, 7);  
        series.add(4, 4); 
        series.add(5, 5);  
        series.add(6, 7);  
        series.add(7, 4); 
        mDataset.addSeries(series);  
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();  
        //����ͼ���X��ĵ�ǰ����  
        mRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);  
        mRenderer.setXTitle("ʱ��");//����ΪX��ı���  
        mRenderer.setYTitle("�¶�");//����y��ı���  
        mRenderer.setAxisTitleTextSize(20);//����������ı���С  
        mRenderer.setChartTitle("15���¶�����ͼ");//����ͼ�����  
        mRenderer.setChartTitleTextSize(30);//����ͼ��������ֵĴ�С  
        mRenderer.setLabelsTextSize(18);//���ñ�ǩ�����ִ�С  
        mRenderer.setLegendTextSize(20);//����ͼ���ı���С  
        mRenderer.setPointSize(4f);//���õ�Ĵ�С  
        mRenderer.setYAxisMin(0);//����y����Сֵ��0  
        mRenderer.setYAxisMax(15);  
        mRenderer.setYLabels(10);//����Y��̶ȸ�����ò�Ʋ�̫׼ȷ��  
        mRenderer.setXAxisMax(5);  
        mRenderer.setFitLegend(true);// �������ʵ�λ��
        mRenderer.setShowGrid(true);//��ʾ����  
        mRenderer.addXTextLabel(1, "��1��");  
        mRenderer.addXTextLabel(2, "��2��");  
        mRenderer.addXTextLabel(3, "��3��");  
        mRenderer.addXTextLabel(4, "��4��"); 
        mRenderer.addXTextLabel(5, "��5��"); 
        mRenderer.addXTextLabel(6, "��6��"); 
        mRenderer.addXTextLabel(7, "��7��"); 
        mRenderer.setXLabels(0);//����ֻ��ʾ��1�£�2�µ��滻��Ķ���������ʾ1,2,3��  
        mRenderer.setMargins(new int[] { 120, 30, 15, 20 });//������ͼλ��  
        mRenderer.setBackgroundColor(Color.TRANSPARENT);
        XYSeriesRenderer r = new XYSeriesRenderer();//(������һ���߶���)  
        r.setColor(Color.YELLOW);//������ɫ  
        r.setPointStyle(PointStyle.CIRCLE);//���õ����ʽ  
        r.setFillPoints(true);//���㣨��ʾ�ĵ��ǿ��Ļ���ʵ�ģ�  
        r.setDisplayChartValues(true);//�����ֵ��ʾ����  
        r.setChartValuesSpacing(10);//��ʾ�ĵ��ֵ��ͼ�ľ���  
        r.setChartValuesTextSize(25);//���ֵ�����ִ�С  
        r.setLineWidth(3);//�����߿�  
        mRenderer.addSeriesRenderer(r);  
        GraphicalView  view = ChartFactory.getLineChartView(context, mDataset, mRenderer);  
        view.setBackgroundColor(Color.TRANSPARENT); 
        temperautreForAll.addView(view, new LayoutParams(LayoutParams.FILL_PARENT, 550));
	}
	
}
