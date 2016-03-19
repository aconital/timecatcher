package com.cpsc.timecatcher;

import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by fujiaoyang1 on 3/18/16.
 */
public class DrawPieChart {
    private String[] xData = {"School", "Work", "Housework","Family","Gym"};
    private float[] yData = { 0,0,0,0,0};
    private View view;
    private RelativeLayout relativeLayout;
    private FrameLayout frameLayout;
    private float rotationAngel=0;
    private PieChart mChart;

    DrawPieChart(View view,PieChart mChart,float[]yData,float angel) {
        this.view=view;
        this.mChart=mChart;
        this.yData=yData;
        this.rotationAngel=angel;
        frameLayout = (FrameLayout) view.findViewById(R.id.pieChartContainer);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.graphContainer);
    }

    public void setYData(float[]yData){
        this.yData=yData;
    }

    public void draw(){
        frameLayout.removeAllViews();
        frameLayout.addView(mChart);// add pie chart to main layout
        mChart.setUsePercentValues(true);
        mChart.setHoleRadius(50f);
        mChart.setTransparentCircleRadius(10f);
        mChart.setDescription("");//text in the bottom right corner. set it to "", because the default is "Description"

        // enable hole and configure
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColorTransparent(true);
        mChart.setHoleRadius(4);
        mChart.setTransparentCircleRadius(10);
        mChart.setTransparentCircleColor(0xCCCCFF);// light transparent purple
        mChart.setRotationAngle(rotationAngel);// enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        // set a chart value selected listener
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                NumberFormat formatter = new DecimalFormat("##0.0");
                if (e == null) { return; }
                TextView catetorySelectedText = (TextView) relativeLayout.findViewById(R.id.categorySelectedText);
                catetorySelectedText.setText(xData[e.getXIndex()] +
                        " = " + formatter.format(e.getVal()) + "%" );// display msg when value selected
            }
            @Override
            public void onNothingSelected() {
                TextView catetorySelectedText = (TextView) relativeLayout.findViewById(R.id.categorySelectedText);
                catetorySelectedText.setText("");
            }
        });

        addPieChartData();// add data
        Legend l = mChart.getLegend();// customize legends
        l.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
        l.setXEntrySpace(0.5f);
        l.setYEntrySpace(0.5f);
        l.setTextColor(Color.BLACK);//black
        l.setTextSize(10f);
    }

    private void addPieChartData() {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0,j=0; i < yData.length; i++){
            if(Float.compare(0, yData[i])!=0){
                yVals1.add(new Entry(yData[i], j++));
                xVals.add(xData[i]);
            }//if
        }//for

        // create pie data set
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        dataSet.setColors(new int[]
                {Color.rgb(153, 255, 255), //blue
                        Color.rgb(255, 51, 153),  //red
                        Color.rgb(255, 255, 0),//yellow
                        Color.rgb(229, 204, 255),//purple
                        Color.rgb(255, 153, 51),//orange
                });

        // instantiate pie data object now
        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        mChart.setData(data);
        mChart.highlightValues(null);// undo all highlights
        mChart.invalidate();// update pie chart
    }

}
