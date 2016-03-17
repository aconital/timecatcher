package com.cpsc.timecatcher;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.cpsc.timecatcher.helper.SimpleItemTouchHelperCallback;
import com.cpsc.timecatcher.model.Day;
import com.cpsc.timecatcher.model.Task;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import adapters.TaskAdapter;

/**
 * Created by hroshandel on 2016-02-23.
 */
public class TasklistFragment extends Fragment {

    private  long longDate;
    private Date date;
    private final static String DATE_TAG="DATE";
    private FloatingActionButton fab,schedule_fab;
    private List<Task> taskList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TaskAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private OnFragmentInteractionListener mListener;

    public TasklistFragment(){}
    public static TasklistFragment newInstance(long date) {
        TasklistFragment fragment = new TasklistFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_tasklist, container, false);


       recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        schedule_fab=(FloatingActionButton) view.findViewById(R.id.fab_schedule);
        schedule_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO YUTONG DO WHAT YOU WANT HERE
            }
        });
        fab= (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment scheduleFragment= NewEditTaskFragment.newInstance(longDate);
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                        .replace(R.id.frame_container, scheduleFragment).commit();

            }
        });

        mAdapter = new TaskAdapter(taskList,getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Task task= taskList.get(position);

                Fragment scheduleFragment= NewEditTaskFragment.newInstance(task.getObjectId().toString());
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                        .replace(R.id.frame_container, scheduleFragment).addToBackStack("AE_FRAGMENT").commit();
            }

            @Override
            public void onLongClick(View view, int position) {
                Task task= taskList.get(position);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                AlertDFragment alertdFragment = AlertDFragment.newInstance(task.getObjectId());
                alertdFragment.show(ft, null);
            }
        }));

     /*   ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
*/
        return view;
    }
    private  void loadTasks()
    {
        taskList.clear();
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
    public void onResume()
    {
        super.onResume();
       loadTasks();
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void addTask(long date);
    }


    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;

        public interface OnItemClickListener {
            public void onItemClick(View view, int position);
        }

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private TasklistFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final TasklistFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
