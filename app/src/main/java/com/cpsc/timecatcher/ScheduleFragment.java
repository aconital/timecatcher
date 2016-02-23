package com.cpsc.timecatcher;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cpsc.timecatcher.helper.SimpleItemTouchHelperCallback;
import com.cpsc.timecatcher.model.Day;
import com.cpsc.timecatcher.model.Task;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import adapters.TaskAdapter;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScheduleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {
    private  long longDate;
    private Date date;
    private final static String DATE_TAG="DATE";
    private FloatingActionButton fab;
    private List<Task> taskList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TaskAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private OnFragmentInteractionListener mListener;

    public ScheduleFragment() {}

    public static ScheduleFragment newInstance(long date) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putLong(DATE_TAG, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       if (getArguments() != null)
       {   //convert long to Date
           longDate=getArguments().getLong(DATE_TAG);
           date = new Date(longDate);

       }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_schedule, container, false);
        setTitle();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        fab= (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment scheduleFragment=NewTaskFragment.newInstance(longDate);
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                        .replace(R.id.frame_container, scheduleFragment).commit();

            }
        });

        mAdapter = new TaskAdapter(taskList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }
    @Override
    public void onResume()
    {
        super.onResume();
        updateSchedule();
    }
    private void setTitle()
    {       SimpleDateFormat newDateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
        try {
            date = newDateFormat.parse(date.toString());
            newDateFormat.applyPattern("EEEE d MMM");
            String title=newDateFormat.format(date);
            getActivity().setTitle(title);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private void updateSchedule()
    {   taskList.clear();
        ParseQuery<Day> query = new ParseQuery<Day>("Day");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.whereEqualTo("date", date);

        query.getFirstInBackground(new GetCallback<Day>() {
            @Override
            public void done(Day object, com.parse.ParseException e) {
                if (object != null) {
                    Day day = object;
                    ParseQuery<Task> query = new ParseQuery<Task>("Task");
                    query.whereEqualTo("day", day);
                    query.whereExists("startTime");
                    query.whereExists("endTime");
                    query.addAscendingOrder("startTime");
                    query.findInBackground(new FindCallback<Task>() {
                        @Override
                        public void done(List<Task> objects, com.parse.ParseException e) {
                            if (objects.size() > 0) {
                                for (Task t : objects) {
                                        taskList.add(t);
                                }
                             //   taskList=sortTasks(taskList);
                                mAdapter.notifyDataSetChanged();
                            } else
                                Log.e("Parse", "No tasks found");
                        }
                    });
                } else
                    Log.e("Parse", "No object returned");
            }
        });


    }


    private List<Task> sortTasks(List<Task> tasks) {
        int n = tasks.size();
        int k;
        for (int m = n; m >= 0; m--) {
            for (int i = 0; i < n - 1; i++) {
                k = i + 1;
                if (tasks.get(k).getStartTime().getTime() < tasks.get(i).getStartTime().getTime()) {
                    Task temp = tasks.get(i);
                    tasks.set(i, tasks.get(k));
                    tasks.set(k, temp);
                }
            }
        }
        return  tasks;
    }
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
         public void addTask(long date);
    }
}
