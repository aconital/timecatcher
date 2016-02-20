package com.cpsc.timecatcher;


import android.app.Activity;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.CalendarView;
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
    long date;

    public CalendarFragment(){}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mListener=(OnFragmentInteractionListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    +"must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
//        fm = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_calendar,container,false);
        calendar=(CalendarView)view.findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                Calendar d= Calendar.getInstance();
                d.set(year,month,dayOfMonth,0,0,0);
                d.set(d.MILLISECOND,0);
                date=d.getTime().getTime();
//                Toast.makeText(getActivity(),""+d,Toast.LENGTH_LONG).show();
                mListener.onDateSelected(date);

            }
        });
        return view;
    }

    public interface OnFragmentInteractionListener{
        public void onDateSelected(long date);
    }

}
