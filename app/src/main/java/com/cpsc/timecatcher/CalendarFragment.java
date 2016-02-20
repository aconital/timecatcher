package com.cpsc.timecatcher;


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

/**
 * Created by junyishen on 2016-02-19.
 */
public class CalendarFragment extends Fragment {
    private CalendarView calendar;
    private OnFragmentInteractionListener mListener;
    private FragmentManager fm;
    private FragmentTransaction ft;
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
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        fm = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_calendar,container,false);
        calendar=(CalendarView)view.findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                ft=fm.beginTransaction();
                ScheduleFragment sf= new ScheduleFragment();
                Bundle bundle=new Bundle();
                bundle.putLong("date", calendar.getDate());
                sf.setArguments(bundle);
                ft.replace(R.id.calendarView, sf);
                ft.addToBackStack(null);
                ft.commit();


            }
        });
        return view;
    }

    public interface OnFragmentInteractionListener{
        void onFragmentInteractionListener(Uri uri);
    }

}
