package com.cpsc.timecatcher.importer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.cpsc.timecatcher.R;
import com.cpsc.timecatcher.algorithm.TimeUtils;
import com.cpsc.timecatcher.model.Day;
import com.cpsc.timecatcher.model.Task;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import me.everything.providers.android.calendar.CalendarProvider;
import me.everything.providers.android.calendar.Event;
import me.everything.providers.android.calendar.Instance;

/**
 * Created by yutongluo on 3/21/16.
 */
public class ImporterDialog extends Dialog implements EventPickerDialog.EventPickerDialogListener{

    private final String TAG = "ImportDialog";

    private Spinner importSpinner;
    private Button importButton, selectButton;
    private List<Instance> instances = new ArrayList<>();
    private List<Instance> selectedInstances = new ArrayList<>();

    private TextView countLabel;
    private CalendarProvider calendarProvider;
    private final java.util.Calendar c = java.util.Calendar.getInstance();

    private final Map<Long, Event> instanceToEventMap = new HashMap<>();


    public ImporterDialog(Context context) {
        super(context);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.import_layout);

        calendarProvider = new CalendarProvider(getContext());

        countLabel = (TextView) findViewById(R.id.importCountLabel);

        importSpinner = (Spinner) findViewById(R.id.importSpinner);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.import_picker_values, android.R.layout.simple_spinner_item);
        importSpinner.setAdapter(adapter);

        importSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Date start, end;
                instances.clear();
                selectedInstances.clear();
                if (position == 0) {
                    // today

                    // 00:00
                    c.setTime(new Date());
                    c.set(java.util.Calendar.HOUR_OF_DAY, 0);
                    c.set(java.util.Calendar.MINUTE, 0);
                    c.set(java.util.Calendar.SECOND, 0);
                    c.set(java.util.Calendar.MILLISECOND, 0);
                    start = c.getTime();

                    // 23:59
                    c.set(java.util.Calendar.HOUR_OF_DAY, 23);
                    c.set(java.util.Calendar.MINUTE, 59);
                    c.set(java.util.Calendar.SECOND, 59);
                    c.set(java.util.Calendar.MILLISECOND, 999);
                    end = c.getTime();
                } else {
                    // week start

                    // Sunday 00:00
                    c.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.SUNDAY);
                    c.set(java.util.Calendar.HOUR_OF_DAY, 0);
                    c.set(java.util.Calendar.MINUTE, 0);
                    c.set(java.util.Calendar.SECOND, 0);
                    c.set(java.util.Calendar.MILLISECOND, 0);
                    start = c.getTime();

                    // Saturday 23:59
                    c.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.SATURDAY);
                    c.set(java.util.Calendar.HOUR_OF_DAY, 23);
                    c.set(java.util.Calendar.MINUTE, 59);
                    c.set(java.util.Calendar.SECOND, 59);
                    c.set(java.util.Calendar.MILLISECOND, 999);
                    end = c.getTime();
                }
                List<Instance> instancesList = calendarProvider.getInstances(
                        start.getTime(),
                        end.getTime()
                ).getList();


                Iterator<Instance> instanceIterator = instancesList.iterator();
                while (instanceIterator.hasNext()) {
                    Instance i = instanceIterator.next();
                    Event e = calendarProvider.getEvent(i.eventId);
                    if (e.allDay) {
                        // filter allDay events
                        instanceIterator.remove();
                    } else {
                        instanceToEventMap.put(i.id, e);
                    }
                }


                Log.d(TAG, "Found " + instancesList.size() + " events");
                countLabel.setText(
                        String.format(getContext().getResources().getString(R.string.import_count),
                                instancesList.size()));

                instances.addAll(instancesList);

                // selectedInstances == instances on spinner picks.
                selectedInstances.addAll(instancesList);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        selectButton = (Button) findViewById(R.id.select_button);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventPickerDialog eventPickerDialog = new EventPickerDialog(
                        getContext(),
                        instances,
                        calendarProvider);
                eventPickerDialog.setPickerDialogListener(ImporterDialog.this);
                eventPickerDialog.show();
            }
        });

        importButton = (Button) findViewById(R.id.import_button);
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Instance i : selectedInstances) {
                    Log.d(TAG, "User selected: " + calendarProvider.getEvent(i.eventId).title);
                }
                if (selectedInstances.size() == 0) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Error")
                            .setMessage("No tasks to import!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                } else {
                    importButton.setEnabled(false);
                    importButton.setText(R.string.button_saving);
                    final List<Task> tasks = new LinkedList<>();
                    for (final Instance i : selectedInstances) {
                        // check if day exists
                        c.setTimeInMillis(i.begin);
                        c.set(java.util.Calendar.HOUR_OF_DAY, 0);
                        c.set(java.util.Calendar.MINUTE, 0);
                        c.set(java.util.Calendar.SECOND, 0);
                        c.set(java.util.Calendar.MILLISECOND, 0);

                        ParseQuery<Day> dayParseQuery = Day.getQuery();
                        dayParseQuery.whereEqualTo("user", ParseUser.getCurrentUser());
                        dayParseQuery.whereEqualTo("date", c.getTime());
                        Day day = new Day();
                        day.initialize();
                        try {
                            day = dayParseQuery.getFirst();
                        } catch (Exception e) {
                            if (e instanceof ParseException) {
                                if (((ParseException) e).getCode() == ParseException.OBJECT_NOT_FOUND) {
                                    // no such day, create it
                                    day = new Day();
                                    day.initialize();
                                    day.setUser(ParseUser.getCurrentUser());
                                    day.setDate(c.getTime());


                                    c.set(Calendar.HOUR_OF_DAY, 8);
                                    c.set(Calendar.MINUTE, 30);
                                    day.setDayStart(c.getTime());

                                    c.set(Calendar.HOUR_OF_DAY, 23);
                                    c.set(Calendar.MINUTE, 59);
                                    day.setDayEnd(c.getTime());
                                    try {
                                        day.save();
                                    } catch (ParseException e1) {
                                        // Couldn't save Day!
                                        new AlertDialog.Builder(getContext())
                                                .setTitle("Error")
                                                .setMessage("Something went wrong. Please try again!")
                                                .setPositiveButton(android.R.string.ok,
                                                        new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        importButton.setText(R.string.import_button);
                                                    }
                                                })
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();
                                    }
                                }
                            }
                        }
                        // final instance of day to use in the next callback
                        final Day finalDay = day;

                        // Date:
                        final Date startTime = new Date(i.begin);
                        final Date endTime = new Date(i.end);
                        // check if duplicate:
                        // in the case that event already exists, update it
                        ParseQuery<Task> duplicateQuery = Task.getQuery();
                        duplicateQuery.whereEqualTo("user", ParseUser.getCurrentUser());
                        duplicateQuery.whereEqualTo("eventId", i.eventId);
                        duplicateQuery.whereEqualTo("startTime", startTime);
                        duplicateQuery.whereEqualTo("endTime", endTime);
                        duplicateQuery.findInBackground(new FindCallback<Task>() {
                            @Override
                            public void done(List<Task> objects, ParseException e) {
                                final Task task;
                                Event event = instanceToEventMap.get(i.id);
                                // check for duplicates in current import:
                                if (e != null || objects.size() == 0) {
                                    // If we couldn't query for duplicates, or there are no
                                    // duplicates, simply add the item
                                    if (e != null) {
                                        Log.d(TAG, "Could not query in duplicate check! " +
                                                "Assume no duplicates");
                                    }
                                    task = new Task();

                                    // settings unique to new task
                                    task.setFixed(true);
                                    task.setEventId(i.eventId);
                                    task.setUser(ParseUser.getCurrentUser());

                                } else {
                                    // Duplicate. Simply update
                                    // This code assumes objects.size() is never greater than one
                                    task = objects.get(0);
                                }
                                task.setTitle(event.title);
                                if(event.description == null) {
                                    task.setDescription("");
                                } else {
                                    task.setDescription(event.description);
                                }
                                task.setDay(finalDay);
                                task.setStartTime(startTime);
                                task.setEndTime(endTime);

                                int totalTime = TimeUtils.getMinutesDiff(startTime, endTime);
                                task.setTotalTime(totalTime);
                                tasks.add(task);
                                if (tasks.size() == selectedInstances.size()) {
                                    // we've added all the instances, save
                                    // god damn async is a pain some times
                                    ParseObject.saveAllInBackground(tasks, new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            ImporterDialog.this.dismiss();
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void userPickedEvents(SparseBooleanArray selected) {
        int count = 0;

        selectedInstances.clear();

        // explanation for the "selected.size() - 1"
        // Bug with android: for some reason it considered the select all checkbox
        // as one of the listview ones... despite the fact that the select all checkbox
        // is completely seperate of the listview.
        for (int i = 0; i < selected.size() - 1; i++) {
            if(selected.get(i)) {
                selectedInstances.add(instances.get(i));
                count++;
            }
        }

        countLabel.setText(
                String.format(getContext().getResources().getString(R.string.import_count),
                        count));
    }
}
