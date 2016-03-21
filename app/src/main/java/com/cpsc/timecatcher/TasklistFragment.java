package com.cpsc.timecatcher;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.algorithm.CSP;
import com.algorithm.CSP_Solver;
import com.algorithm.TaskAssignment;
import com.algorithm.Time;
import com.cpsc.timecatcher.helper.Constants;
import com.cpsc.timecatcher.helper.Utility;
import com.cpsc.timecatcher.model.Constraint;
import com.cpsc.timecatcher.model.Day;
import com.cpsc.timecatcher.model.Operator;
import com.cpsc.timecatcher.model.Task;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
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
    private FloatingActionButton fab, schedule_fab, import_fab;
    private List<Task> taskList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TaskAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private OnFragmentInteractionListener mListener;
    private Day day;

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
                // SCHEDULE
                if ( taskList.size() == 0 ) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Error")
                            .setMessage("No tasks to schedule!")
                            .setPositiveButton(android.R.string.ok,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {}})
                            .show();
                }
                else if (day != null) {
                    ArrayList<Pair> constraintsInt = new ArrayList<>();
                    CSP problem = new CSP(Utility.dateToTime(day.getDayStart()),
                            Utility.dateToTime(day.getDayEnd()));

                    boolean overtime = false;
                    for (int i = 0; i < taskList.size(); i++) {
                        Task currentTask = taskList.get(i);
                        if (currentTask.getFixed()) {
                            overtime = overtime || problem.addFixedTask(
                                    Utility.dateToTime(currentTask.getStartTime()),
                                    Utility.dateToTime(currentTask.getEndTime()));
                        } else {
                            int hours = currentTask.getTotalTime() / 60;
                            int minutes = currentTask.getTotalTime() % 60;
                            overtime = overtime || problem.addFlexibleTask(new Time(hours, minutes));
                        }
                        if (overtime) {
                            Log.d("Algorithm", "Could not add flexible task: not enough time");
                        }
                        List<Constraint> constraints;
                        ParseQuery<Constraint> taskConstraintsQuery = currentTask.getConstraints();
                        try {
                            constraints = taskConstraintsQuery.find();
                            for (Constraint constraint : constraints) {
                                if (constraint.getOperator() == Operator.BEFORE) {
                                    constraintsInt.add(
                                            Pair.create(i, taskList.indexOf(constraint.getOther())));
                                } else {
                                    constraintsInt.add(
                                            Pair.create(taskList.indexOf(constraint.getOther()), i));
                                }

                            }
                        } catch (Exception e) {
                            Log.d(Constants.NEW_EDIT_TASK_TAG,
                                    "Could not find constraints! Assume no constraints");
                        }
                    }
                    problem.createConstraintGraph();
                    for (Pair constraintPair : constraintsInt) {
                        problem.addConstraint((int) constraintPair.first,
                                (int) constraintPair.second, 0);
                    }

                    if (problem.isConstraintsConflict()) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Error")
                                .setMessage("You have conflicting constraints!" +
                                        " Please edit your tasks and try again.")
                                .setPositiveButton(android.R.string.ok,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                .show();
                        return;
                    }

                    CSP_Solver csp_solver = new CSP_Solver(problem);
                    List<ArrayList<TaskAssignment>> solutions = csp_solver.getSolutions();
                    if (solutions.size() == 0) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Error")
                                .setMessage("Could not schedule your day!" +
                                        " Please edit your tasks and try again.")
                                .setPositiveButton(android.R.string.ok,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                .show();
                        return;
                    }
                    else {
                    ArrayList<TaskAssignment> solution = solutions.get(0);
                    for (TaskAssignment taskAssignment : solution) {
                        Task task = taskList.get(taskAssignment.getTaskId());
                        if (!task.getFixed()) {
                            // assign task to slot
                            // start time
                            task.setStartTime(Utility.timeToDate(day.getDate(),
                                    taskAssignment.getAssignment().getStartTime()));

                            // end time
                            task.setEndTime(Utility.timeToDate(day.getDate(),
                                    taskAssignment.getAssignment().getEndTime()));
                            try {
                                AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                                Intent i = new Intent(getActivity(), AlarmReceiver.class);
                                i.putExtra("id", task.getObjectId());
                                i.putExtra("msg", task.getTitle());
                                PendingIntent pi = PendingIntent.getBroadcast(getActivity(), 0, i, PendingIntent.FLAG_CANCEL_CURRENT);

                                Calendar c = Calendar.getInstance();
                                c.setTime(Utility.timeToDate(day.getDate(),
                                        taskAssignment.getAssignment().getStartTime()));
                                c.add(Calendar.MINUTE, -15);
                                c.set(Calendar.SECOND, 0);
                                c.set(Calendar.MILLISECOND, 0);

                                long time = c.getTime().getTime();
                                am.set(AlarmManager.RTC_WAKEUP, time, pi);
                                task.save();
                            } catch (ParseException e) {
                                Log.e("Algorithm", "Could not save task: " + task.getObjectId());
                                e.printStackTrace();
                            }
                        } else {
                            AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                            Intent i = new Intent(getActivity(), AlarmReceiver.class);
                            i.putExtra("id", task.getObjectId());
                            i.putExtra("msg", task.getTitle());
                            PendingIntent pi = PendingIntent.getBroadcast(getActivity(), 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
                            //15 min before
                            Calendar c = Calendar.getInstance();
                            c.setTime(task.getStartTime());
                            c.add(Calendar.MINUTE, -15);
                            c.set(Calendar.SECOND, 0);
                            c.set(Calendar.MILLISECOND, 0);

                            long time = c.getTime().getTime();
                            am.set(AlarmManager.RTC_WAKEUP, time, pi);
                        }


                    }
                }
                    mAdapter.notifyDataSetChanged();

                    Log.d("Algorithm", csp_solver.solutionsString());
                    Toast.makeText(getActivity(), "Tasks Scheduled!", Toast.LENGTH_SHORT).show();
                }
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
                    day = object;
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
