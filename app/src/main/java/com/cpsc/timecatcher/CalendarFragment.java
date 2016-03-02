package com.cpsc.timecatcher;


import android.app.Activity;
import android.net.Uri;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.support.v4.app.FragmentManager;
import android.content.Context;

import android.os.Bundle;
import android.view.View;

import java.util.Calendar;

/**
 * Created by junyishen on 2016-02-19.
 */
public class CalendarFragment extends Fragment {
    private CalendarView calendar;
    private OnFragmentInteractionListener mListener;
//    private FragmentManager fm;
//    private FragmentTransaction ft;
    long date,currentDate;

    public CalendarFragment(){}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        getActivity().setTitle("TimeCatcher");
//        fm = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_calendar,container,false);
        LinearLayout calLayout= (LinearLayout) view.findViewById(R.id.calendarView);
        calendar=(CalendarView)calLayout.findViewById(R.id.calendar);
        currentDate=calendar.getDate();
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                if(android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    Calendar d = Calendar.getInstance();
                    d.set(year, month, dayOfMonth, 0, 0, 0);
                    d.set(d.MILLISECOND, 0);
                    date = d.getTime().getTime();
                    mListener.onDateSelected(date);
                } else {
                    if(calendar.getDate() != currentDate) {
                        currentDate=calendar.getDate();
                        Calendar d = Calendar.getInstance();
                        d.set(year, month, dayOfMonth, 0, 0, 0);
                        d.set(d.MILLISECOND, 0);
                        date = d.getTime().getTime();
                        mListener.onDateSelected(date);
                    }
                }
            }
        });
        return view;
    }

    public interface OnFragmentInteractionListener{
        public void onDateSelected(long date);
    }

}
