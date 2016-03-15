package com.cpsc.timecatcher;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

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
 * Created by fujiaoyang1 on 3/13/16.
 */
public class GraphFragment extends Fragment {
    //private RelativeLayout mainLayout;
    private FrameLayout mainLayout;
    private PieChart mChart;
    private Toast toast;
    private TextView txtView;

    private String[] xData = { "School", "Work", "HouseWork","Family","Gym"};
    private float[] yData = { 30,20,10,30,10};// corresponds to  "School", "Work", "HouseWork","Family","Gym" respectively


    public GraphFragment(){}

    private void dataInitialization(){
        // used to initialize   float[] yData
        /*
        ParseQuery<Task> taskParseQuery = Task.getQuery();
        taskParseQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        if (day != null) {
            taskParseQuery.whereEqualTo("day", day);
        } else {
            Log.d(Constants.NEW_CONSTRAINT_TAG, "Warning: null day, getting all user's tasks");
        }
       */
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Time Spent Distribution Chart");
        dataInitialization();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_graph, container, false);
        mainLayout=(FrameLayout) view.findViewById(R.id.graphContainer);
        mChart = new PieChart(getActivity());

        // add pie chart to main layout
        mainLayout.addView(mChart);
       // mainLayout.setBackgroundColor(Color.parseColor("#330000"));//set to black

        mChart.setUsePercentValues(true);
        mChart.setHoleRadius(50f);
        mChart.setTransparentCircleRadius(10f);
        mChart.setDescription("Daily Time Cost Distribution");
        mChart.setDescriptionTextSize(14f);
        mChart.setDescriptionPosition(800f, 1550f);
        mChart.setDescriptionColor(Color.parseColor("#FF3399"));

        // enable hole and configure
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColorTransparent(true);
        mChart.setHoleRadius(4);
        mChart.setTransparentCircleRadius(10);
        mChart.setTransparentCircleColor(Color.parseColor("#CCCCFF"));

        // enable rotation of the chart by touch
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);
      // set a chart value selected listener
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                // display msg when value selected
                NumberFormat formatter = new DecimalFormat("#00.0");
                if (e == null)
                    return;

                toast = Toast.makeText(getActivity(),
                        xData[e.getXIndex()] + " = " + formatter.format(e.getVal()) + "%", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 145);
                toast.show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        // add data
        addData();

        // customize legends
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(0.5f);
        l.setYEntrySpace(0.5f);
        l.setTextColor(Color.parseColor("#000000"));//black
        //l.setTextColor(Color.parseColor(Integer.toString(getResources().getColor(R.color.black))));//black
        l.setTextSize(10f);
        return view;
    }
    private void addData() {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < yData.length; i++)
            yVals1.add(new Entry(yData[i], i));

        for (int i = 0; i < xData.length; i++)
            xVals.add(xData[i]);

        // create pie data set
        //PieDataSet dataSet = new PieDataSet(yVals1, "Importance Priority");
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        dataSet.setColors(new int[]
                {Color.rgb(51,255,255), //blue
                        Color.rgb(255,51,153),  //red
                        Color.rgb(255,255,0),//yellow
                        Color.rgb(229,204,255),//purple
                        Color.rgb(255,153,51),//orange
                });

        // instantiate pie data object now
        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(14f);
        data.setValueTextColor(Color.BLACK);

        mChart.setData(data);
        // undo all highlights
        mChart.highlightValues(null);

        // update pie chart
        mChart.invalidate();
    }

}
