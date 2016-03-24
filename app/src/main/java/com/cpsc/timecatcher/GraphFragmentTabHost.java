package com.cpsc.timecatcher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by fujiaoyang1 on 3/15/16.
 */
public class GraphFragmentTabHost extends Fragment {
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

        mTabHost.getTabWidget().setBackgroundColor(getResources().getColor(R.color.pale_purple));


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
}
