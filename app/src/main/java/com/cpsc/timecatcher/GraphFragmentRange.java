package com.cpsc.timecatcher;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpsc.timecatcher.model.Day;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by fujiaoyang1 on 3/13/16.
 */
public class GraphFragmentRange extends Fragment {
    private RelativeLayout relativeLayout;
    private FrameLayout frameLayout;
    private DatePicker datePicker;
    private PieChart mChart;
    private String[] xData = { "School", "Work", "Housework","Family","Gym"};
    //private float[] yData = { 30,20,10,30,10};// corresponds to  "School", "Work", "HouseWork","Family","Gym" respectively
    private float[] yData = { 0,0,0,0,0};// corresponds to  "School", "Work", "HouseWork","Family","Gym" respectively
    private float rotationAngel=0;
    private Date startDate;
    private Date endDate;
    private static int Year=-1, Month=-1, Day=-1;
    private TextView textViewStart,textViewEnd;

    public GraphFragmentRange(){}
    private void dataInitialization(){
        // used to initialize   float[] yData
        long timeSpentOnCategory[]={0,0,0,0,0};
        long total=0;

        ParseQuery<com.cpsc.timecatcher.model.Day> dayParseQuery = com.cpsc.timecatcher.model.Day.getQuery();
        dayParseQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        if(startDate.compareTo(endDate)==0){
            dayParseQuery.whereEqualTo("date", startDate);
        }
        else{
            dayParseQuery.whereGreaterThanOrEqualTo("date", startDate);
            dayParseQuery.whereLessThanOrEqualTo("date", endDate);
        }
        try{
            List<Day> days = dayParseQuery.find();
            for (Day day : days) {
                timeSpentOnCategory[0]+=day.getTimeSpentOn(xData[0]);
                timeSpentOnCategory[1]+=day.getTimeSpentOn(xData[1]);
                timeSpentOnCategory[2]+=day.getTimeSpentOn(xData[2]);
                timeSpentOnCategory[3]+=day.getTimeSpentOn(xData[3]);
                timeSpentOnCategory[4]+=day.getTimeSpentOn(xData[4]);
            }

            for(int i=0;i<5;i++) {
                total+=timeSpentOnCategory[i];
            }

            if(Float.compare(0, total)!=0){
                for(int i=0;i<5;i++) {
                    yData[i]=100* ((float)timeSpentOnCategory[i]/(float)total);
                }
            }

        }catch(Exception e) {
            System.out.println("Exception thrown  :" + e);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Time Spent Distribution Chart");
        Calendar currentTime = Calendar.getInstance();
        currentTime.set(Calendar.HOUR_OF_DAY, 0);
        currentTime.set(Calendar.MINUTE, 0);
        currentTime.set(Calendar.SECOND, 0);
        currentTime.set(Calendar.MILLISECOND, 0);
        Date today=currentTime.getTime();
        endDate=startDate= today;
        dataInitialization();
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //icepick.Icepick.saveInstanceState(this, outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment_graph_day, container, false);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.graphContainer);
        frameLayout = (FrameLayout) view.findViewById(R.id.pieChartContainer);
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
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker dp, int year,
                                                  int month, int dayOfMonth) {
                                Date specifiedDate=new Date(year-1900,month,dayOfMonth);
                                if(specifiedDate.compareTo(startDate)!=0){// date changed
                                    startDate=specifiedDate;
                                    checkDateOrder();
                                    textViewStart.setText(getDateString(specifiedDate));
                                    dataInitialization();
                                    drawPieChart();// add data
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
                                    dataInitialization();
                                    drawPieChart();// add data
                                }
                            }
                        }
                        //initial day
                        , c.get(Calendar.YEAR)
                        , c.get(Calendar.MONTH)
                        , c.get(Calendar.DAY_OF_MONTH)).show();
            }//onClick
        });

        // check if data available
        if(IsEmptyChart()==true){//no data to show
            return view;
        }
        drawPieChart();
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
        float sum=0;
        for (int i = 0; i < yData.length; i++) {
            sum+=yData[i];
        }//for
        if(Float.compare(0, sum)==0) {
            return true;// no data to show
        }
        else{
            return false;// has data to show
        }
    }

    private void checkDateOrder(){
        if(startDate.compareTo(endDate)>0){
            //AlertingDialogFragment newFragment = new AlertingDialogFragment();
            //newFragment.show(getActivity().getSupportFragmentManager(), "alertDialog");
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

    private void drawPieChart(){
        frameLayout.removeAllViews();
        mChart = new PieChart(getActivity());
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
        l.setTextColor(getResources().getColor(R.color.black));//black
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


