package com.cpsc.timecatcher;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

//implements OnTabChangeListener
/**
 * Created by fujiaoyang1 on 3/15/16.
 */
public class GraphFragmentTabHost extends Fragment  {
    private FragmentTabHost mTabHost;

    //Mandatory Constructor
    public GraphFragmentTabHost() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Analytics");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.graph_fragment_tab_host,container, false);

        mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.graphRealTabContent);

        for(int i=0;i<mTabHost.getTabWidget().getChildCount();i++)
        {
            mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark)); // unselected
            TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#ffffff"));
        }

        //mTabHost.getTabWidget().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
                    mTabHost.getTabWidget().getChildAt(i).
                            setBackgroundColor(getResources().getColor(R.color.tab_unselected_background)); // unselected
                    TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
                    tv.setTextColor(getResources().getColor(R.color.text_unselected_background));
                }

                mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab()).
                        setBackgroundColor(getResources().getColor(R.color.tab_selected_background)); // selected
                TextView tv = (TextView) mTabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
                tv.setTextColor(getResources().getColor(R.color.text_selected_background));

            }
        });

        mTabHost.addTab(mTabHost.newTabSpec("Day").setIndicator("Day"),
                GraphFragmentDay.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Week").setIndicator("Week"),
                GraphFragmentWeek.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Month").setIndicator("Month"),
                GraphFragmentMonth.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Range").setIndicator("Range"),
                GraphFragmentRange.class, null);

        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        for(int i=0;i<mTabHost.getTabWidget().getChildCount();i++)
        {
            mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.tab_unselected_background)); // unselected
            TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(getResources().getColor(R.color.text_unselected_background));
        }
    }
}
