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
		//ͬ������Ҫ����dataset����ͼ��Ⱦ��renderer  
		 XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();  
	        XYSeries  series = new XYSeries("");  
	        for(int i = 0;i<data.length;i++){
	        	series.add(i, data[i]);
	        }
	        mDataset.addSeries(series);  
	        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();  
	        //����ͼ���X��ĵ�ǰ����  
	        
	        mRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);  
	        mRenderer.setXTitle("ʱ��");//����ΪX��ı���  
	        mRenderer.setYTitle("�¶�(��C)");//����y��ı���  
	        mRenderer.setLabelsColor(Color.BLACK); // �������ǩ��ɫ
	        mRenderer.setAxesColor(Color.BLACK);
	        mRenderer.setAxisTitleTextSize(20);//����������ı���С  
	        mRenderer.setChartTitle(name);//����ͼ�����  
	        mRenderer.setChartTitleTextSize(50);//����ͼ��������ֵĴ�С  
	        mRenderer.setLabelsTextSize(30);//���ñ�ǩ�����ִ�С  
	        mRenderer.setLegendTextSize(32);//����ͼ���ı���С  
	        mRenderer.setPointSize(4f);//���õ�Ĵ�С  
	        mRenderer.setYAxisMin(0);//����y����Сֵ��0  
	        mRenderer.setYAxisMax(15);  
	        mRenderer.setYLabels(10);//����Y��̶ȸ�����ò�Ʋ�̫׼ȷ��  
	        mRenderer.setXAxisMax(5);  
	        mRenderer.setFitLegend(true);// �������ʵ�λ��
	        mRenderer.setShowGrid(true);//��ʾ����  
	        //���ñ�����ɫ
	        mRenderer.setApplyBackgroundColor(true);
	        mRenderer.setBackgroundColor(Color.GRAY);//�����ڲ���ɫ
	        mRenderer.setMarginsColor(Color.WHITE);//�����ⲿ����ɫ
	        Calendar calendar = Calendar.getInstance();
			calendar.setTime(time);
			
			for(int i = 0,n = i+1;i<data.length;i++){
				calendar.add(Calendar.MINUTE,n*(Tools.getK()));
				mRenderer.addXTextLabel(i,Tools.getSdf().format(calendar.getTime()));
			}
	        mRenderer.setXLabels(0);//����ֻ��ʾ��1�£�2�µ��滻��Ķ���������ʾ1,2,3��  
	        mRenderer.setMargins(new int[] { 120, 30, 15, 20 });//������ͼλ��  
	        mRenderer.setBackgroundColor(Color.TRANSPARENT);
	        XYSeriesRenderer r = new XYSeriesRenderer();//(������һ���߶���)  
	        r.setColor(Color.BLACK);//������ɫ  
	        r.setPointStyle(PointStyle.CIRCLE);//���õ����ʽ  
	        r.setFillPoints(true);//���㣨��ʾ�ĵ��ǿ��Ļ���ʵ�ģ�  
	        r.setDisplayChartValues(true);//�����ֵ��ʾ����  
	        r.setChartValuesSpacing(10);//��ʾ�ĵ��ֵ��ͼ�ľ���  
	        r.setChartValuesTextSize(25);//���ֵ�����ִ�С  
	        r.setLineWidth(3);//�����߿�  
	        mRenderer.addSeriesRenderer(r);  
	        GraphicalView  view = ChartFactory.getLineChartView(context, mDataset, mRenderer);  
	        view.setBackgroundColor(Color.TRANSPARENT); 
	        linearloyout.addView(view, new LayoutParams(LayoutParams.FILL_PARENT, 800));
		}

}
