package com.cpsc.timecatcher;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by fujiaoyang1 on 3/13/16.
 */
public class GraphFragmentRange extends Fragment {
    private PieChart mChart;
    private float[] yData = { 0,0,0,0,0};// corresponds to "School", "Work", "HouseWork","Family","Gym" respectively
    private Date startDate;
    private Date endDate;
    private float rotationAngel=0;
    private DrawPieChart drawPieChart;
    private PieChartDataInitialization  dataInit;
    private TextView textViewStart,textViewEnd;

    public GraphFragmentRange(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Calendar currentTime = Calendar.getInstance();
        currentTime.set(Calendar.HOUR_OF_DAY, 0);
        currentTime.set(Calendar.MINUTE, 0);
        currentTime.set(Calendar.SECOND, 0);
        currentTime.set(Calendar.MILLISECOND, 0);
        Date today=currentTime.getTime();
        endDate=startDate= today;
        dataInit =new PieChartDataInitialization(startDate,endDate);
        yData=dataInit.getYData();
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment_graph_day, container, false);
        mChart = new PieChart(getActivity());
        drawPieChart= new DrawPieChart(view, mChart,yData,rotationAngel);
        textViewStart=(TextView)view.findViewById(R.id.startDateGraph);
        textViewEnd=(TextView)view.findViewById(R.id.endDateGraph);
        textViewStart.setText(getDateString(startDate));
        textViewEnd.setText(getDateString(endDate));

        checkDateOrder();
        textViewStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                Calendar c = Calendar.getInstance();
                // create a new DatePickerDialog instance and show it
                new DatePickerDialog(getActivity(),
                        //R.style.DatePickerTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker dp, int year,
                                                  int month, int dayOfMonth) {
                                Date specifiedDate = new Date(year - 1900, month, dayOfMonth);
                                if (specifiedDate.compareTo(startDate) != 0) {// date changed
                                    startDate = specifiedDate;
                                    checkDateOrder();
                                    textViewStart.setText(getDateString(specifiedDate));
                                    dataInit.setDateRange(startDate, endDate);
                                    yData = dataInit.getYData();
                                    if (IsEmptyChart() != true) {
                                        drawPieChart.setYData(yData);
                                        drawPieChart.draw();
                                    }
                                }
                            }
                        }
                        //initial day
                        , c.get(Calendar.YEAR)
                        , c.get(Calendar.MONTH)
                        , c.get(Calendar.DAY_OF_MONTH)).show();
            }//onClick
        });

        textViewEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                Calendar c = Calendar.getInstance();
                // create a new DatePickerDialog instance and show it
                new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker dp, int year,
                                                  int month, int dayOfMonth) {
                                Date specifiedDate=new Date(year-1900,month,dayOfMonth);
                                if(specifiedDate.compareTo(endDate)!=0){// date changed
                                    endDate=specifiedDate;
                                    checkDateOrder();
                                    textViewEnd.setText(getDateString(specifiedDate));
                                    dataInit.setDateRange(startDate,endDate);
                                    yData=dataInit.getYData();
                                    if(IsEmptyChart()!=true){
                                        drawPieChart.setYData(yData);
                                        drawPieChart.draw();
                                    }
                                }
                            }
                        }
                        //initial day
                        , c.get(Calendar.YEAR)
                        , c.get(Calendar.MONTH)
                        , c.get(Calendar.DAY_OF_MONTH)).show();
            }//onClick
        });

        if(IsEmptyChart()!=true){
            drawPieChart.draw();
        }
        return view;
    }

    @Override
    public void onPause(){
        super.onPause();
        if(IsEmptyChart()==false){
            rotationAngel=mChart.getRotationAngle();
        }
    }

    private boolean IsEmptyChart(){
        for (int i = 0; i < yData.length; i++) {
            if(Float.compare(0, yData[i])!=0){
                return false;
            }
        }//for
        return true;
    }

    private void checkDateOrder(){
        if(startDate.compareTo(endDate)>0){
            new AlertDialog.Builder(getContext())
                    .setTitle("Error")
                    .setMessage("Start time cannot be after end time!")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    // return the string  format like Mar 15,2016
    private String getDateString(Date date){
        String Datetime;
        SimpleDateFormat dateformat = new SimpleDateFormat("MMM dd,' 'yyyy");//Mar 15,2016
        Datetime = dateformat.format(date);// the argument is an date object Date(int year, int month, int day)
        return Datetime;
    }
}