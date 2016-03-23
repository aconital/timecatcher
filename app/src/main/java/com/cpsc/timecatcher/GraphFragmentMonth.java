package com.cpsc.timecatcher;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by fujiaoyang1 on 3/13/16.
 */
public class GraphFragmentMonth extends Fragment {
    private PieChart mChart;
    private float[] yData = { 0,0,0,0,0};// corresponds to  "School", "Work", "HouseWork","Family","Gym" respectively
    private float rotationAngel=0;
    private DrawPieChart drawPieChart;
    private PieChartDataInitialization  dataInit;
    private Date today;
    private Date aMonthAgo;//30 days

    public GraphFragmentMonth(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getActivity().setTitle("Time Spent Distribution Chart");
        Calendar currentTime = Calendar.getInstance();
        currentTime.set(Calendar.HOUR_OF_DAY, 0);
        currentTime.set(Calendar.MINUTE, 0);
        currentTime.set(Calendar.SECOND, 0);
        currentTime.set(Calendar.MILLISECOND, 0);
        today=currentTime.getTime();
        currentTime.add(Calendar.DAY_OF_MONTH, -29);
        aMonthAgo = currentTime.getTime();//seven Days Ago
        dataInit =new PieChartDataInitialization(aMonthAgo,today);
        yData=dataInit.getYData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment_graph_day, container, false);
        mChart = new PieChart(getActivity());
        drawPieChart= new DrawPieChart(view, mChart,yData,rotationAngel);

        TextView startTime = (TextView) view.findViewById(R.id.startDateGraph);
        TextView endTime = (TextView) view.findViewById(R.id.endDateGraph);
        startTime.setText(getDateString(aMonthAgo));
        endTime.setText(getDateString(today));

        if(IsEmptyChart()!=true) {
            drawPieChart.draw();
        }
        return view;
    }
    @Override
    public void onPause() {
        super.onPause();
        if(IsEmptyChart()==false) {
            rotationAngel=mChart.getRotationAngle();
        }
    }

    private boolean IsEmptyChart() {
        for (int i = 0; i < yData.length; i++) {
            if(Float.compare(0, yData[i])!=0){
                return false;
            }
        }//for
        return true;
    }

    // return the string  format like Mar 15,2016
    private String getDateString(Date date) {
        String Datetime;
        SimpleDateFormat dateformat = new SimpleDateFormat("MMM dd,' 'yyyy");//Mar 15,2016
        Datetime = dateformat.format(date);// the argument is an date object Date(int year, int month, int day)
        return Datetime;
    }
}


