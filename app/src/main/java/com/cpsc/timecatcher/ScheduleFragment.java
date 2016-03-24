package com.cpsc.timecatcher;

import android.content.Context;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.cpsc.timecatcher.helper.Constants;
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

import java.util.Calendar;
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
public class ScheduleFragment extends Fragment implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener {
    private  long longDate;
    private   WeekView mWeekView;
    private Date date;
    private final static String DATE_TAG="DATE";
    private FloatingActionButton fab;
    private List<Task> taskList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TaskAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private OnFragmentInteractionListener mListener;
    private List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
    public ScheduleFragment() {}
    private boolean fetched =false;
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
        setTitle(date);
        mWeekView = (WeekView) view.findViewById(R.id.weekView);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        mWeekView.goToDate(calendar);
        mWeekView.setOnEventClickListener(this);
        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);
        mWeekView.setScrollListener(new WeekView.ScrollListener() {
            @Override
            public void onFirstVisibleDayChanged(Calendar newFirstVisibleDay, Calendar oldFirstVisibleDay) {
                newFirstVisibleDay.set(Calendar.MINUTE,0);
                newFirstVisibleDay.set(Calendar.HOUR,0);
                newFirstVisibleDay.set(Calendar.SECOND,0);
                newFirstVisibleDay.set(Calendar.MILLISECOND,0);
                longDate=newFirstVisibleDay.getTime().getTime();
                setTitle(newFirstVisibleDay.getTime());
            }
        });

        fab= (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment scheduleFragment= TasklistFragment.newInstance(longDate);
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                        .replace(R.id.frame_container, scheduleFragment).addToBackStack(Constants.SCHEDULE_TAG).commit();

            }
        });
     /*   recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);



        mAdapter = new TaskAdapter(taskList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);*/

        return view;
    }
    @Override
    public void onResume()
    {
        super.onResume();

    }
    private void setTitle(Date date)
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
    {
        if(!fetched){
            fetched=true;
        ParseQuery<Day> query = new ParseQuery<Day>("Day");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Day>() {
            @Override
            public void done(List<Day> objects, com.parse.ParseException e) {
                if (objects.size()>0 ) {
                    for(Day d:objects) {
                        Day day = d;
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
                                        Calendar startTime = Calendar.getInstance();
                                        startTime.setTime(t.getStartTime());
                                        Calendar endTime = Calendar.getInstance();
                                        endTime.setTime(t.getEndTime());
                                        WeekViewEvent event = new WeekViewEvent(1, t.getTitle(), startTime, endTime);
                                        event.setColor(getResources().getColor(R.color.grape_light));
                                        events.add(event);
                                    }

                                    // This line will trigger the method 'onMonthChange()' again.
                                    mWeekView.notifyDatasetChanged();

                                } else
                                    Log.e("Parse", "No tasks found");
                            }
                        });
                    }
                    } else
                    Log.e("Parse", "No object returned");
            }
        });

        }
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


    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        if (events.size() <= 0 ) {
            updateSchedule();
            return new ArrayList<WeekViewEvent>();
        }

        List<WeekViewEvent> matchedEvents = new ArrayList<WeekViewEvent>();
        for (WeekViewEvent event : events) {
            if (eventMatches(event, newYear, newMonth)) {
                matchedEvents.add(event);
            }
        }
        return matchedEvents;

    }
    private boolean eventMatches(WeekViewEvent event, int year, int month) {
        return (event.getStartTime().get(Calendar.YEAR) == year && event.getStartTime().get(Calendar.MONTH) == month - 1) || (event.getEndTime().get(Calendar.YEAR) == year && event.getEndTime().get(Calendar.MONTH) == month - 1);
    }
    private String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(getActivity(), "Clicked " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(getActivity(), "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {

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
