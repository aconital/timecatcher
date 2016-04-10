package com.cpsc.timecatcher;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cengalabs.flatui.views.FlatTextView;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.cpsc.timecatcher.algorithm.TimeUtils;
import com.cpsc.timecatcher.gui.NoScrollListView;
import com.cpsc.timecatcher.helper.Constants;
import com.cpsc.timecatcher.model.Category;
import com.cpsc.timecatcher.model.Constraint;
import com.cpsc.timecatcher.model.Day;
import com.cpsc.timecatcher.model.Task;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import adapters.ConstraintAdapter;

import static com.cpsc.timecatcher.algorithm.TimeUtils.addMinutesToDate;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewEditTaskFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewEditTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class NewEditTaskFragment extends Fragment implements
        RadialTimePickerDialogFragment.OnTimeSetListener {
    private Date date;
    private boolean newDate = false;
    private Day day;
    private final static String DATE_TAG = "DATE";
    private final static String TASK_TAG = "TASK";

    private boolean fixed;
    private boolean newTask = true;
    private Date startTime;
    private Date endTime;
    private int totalTimeMinutes = 0;
    private int totalTimeHours = 0;

    private TextView totalTimeTextView;

    private final DateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.CANADA);
    private final DateFormat dateFormat = new SimpleDateFormat("EEE, MMM d", Locale.CANADA);
    private OnFragmentInteractionListener mListener;
    private List<String> categories;
    private Task task;
    private final Calendar calendar = Calendar.getInstance();
    private List<Constraint> constraints;

    public NewEditTaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param date MAKE SURE THIS DATE IS THE STRIPED DATE WITH 0h:0m:0s:00
     * @return A new instance of fragment NewEditTaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewEditTaskFragment newInstance(long date) {
        NewEditTaskFragment fragment = new NewEditTaskFragment();
        Bundle args = new Bundle();
        args.putLong(DATE_TAG, date);
        fragment.setArguments(args);
        return fragment;
    }

    public static NewEditTaskFragment newInstance(String taskId) {
        NewEditTaskFragment fragment = new NewEditTaskFragment();
        Bundle args = new Bundle();
        args.putString(TASK_TAG, taskId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            date = new Date(getArguments().getLong(DATE_TAG));
            String taskString = getArguments().getString(TASK_TAG);
            if (taskString != null) {
                Log.d(Constants.NEW_EDIT_TASK_TAG, "Not a new task! setting newTask to false");
                newTask = false; // not a new task
                ParseQuery<Task> taskParseQuery = Task.getQuery();
                taskParseQuery.whereEqualTo("objectId", getArguments().getString(TASK_TAG));
                try {
                    task = taskParseQuery.getFirst();
                } catch (Exception e) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Error")
                            .setMessage("Could not find task!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // close fragment
                                    getActivity().getSupportFragmentManager().beginTransaction().remove(
                                            NewEditTaskFragment.this).commit();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        }
    }

    private void populateCategoriesSpinner(final Spinner spinner) {
        final String[] defaultCategories = getResources().getStringArray(
                R.array.new_task_default_categories_array);

        final Set<String> allCategories = new HashSet<>(Arrays.asList(defaultCategories));
        final ParseQuery<Category> categoryQuery = Category.getQuery();
        categoryQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        categoryQuery.findInBackground(new FindCallback<Category>() {
            @Override
            public void done(List<Category> objects, ParseException e) {
                if (e == null) {
                    for (Category category : objects) {
                        allCategories.add(category.getTitle());
                    }
                } else {
                    Log.e(Constants.NEW_EDIT_TASK_TAG, "Could not fetch categories!");
                }
                NewEditTaskFragment.this.categories = new ArrayList<>(allCategories);
                NewEditTaskFragment.this.categories.add(0, "Select Category");
                if (task != null) {
                    // Select categories
                    try {
                        Category selectedCategory = task.getCategory();
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                getContext(),
                                android.R.layout.simple_spinner_item,
                                new ArrayList<>(NewEditTaskFragment.this.categories));
                        spinner.setAdapter(adapter);
                        if (selectedCategory != null) {
                            int selectedIndex = NewEditTaskFragment.this.categories.indexOf(
                                    selectedCategory.getTitle());
                            spinner.setSelection(selectedIndex);
                        }
                    } catch (ParseException e1) {
                        Log.d(Constants.NEW_EDIT_TASK_TAG, "Could not query selected category");
                    } catch (NullPointerException npe) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                getContext(),
                                android.R.layout.simple_spinner_item,
                                new ArrayList<>(NewEditTaskFragment.this.categories));
                        spinner.setAdapter(adapter);
                    }
                } else {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            getContext(),
                            android.R.layout.simple_spinner_item,
                            new ArrayList<>(NewEditTaskFragment.this.categories));
                    spinner.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_new_edit_task, container, false);
        final FlatTextView taskPageTitle = (FlatTextView) view.findViewById(R.id.title_newtask);
        final EditText title = (EditText) view.findViewById(R.id.edit_task_name);
        final EditText description = (EditText) view.findViewById(R.id.edit_task_description);
        final TextView startTime = (TextView) view.findViewById(R.id.startTime);
        final TextView endTime = (TextView) view.findViewById(R.id.endTime);
        final TextView dateTextView = (TextView) view.findViewById(R.id.title_date);
        final Spinner categorySpinner = (Spinner) view.findViewById(R.id.categories);
        final Switch fixedSwitch = (Switch) view.findViewById(R.id.fixed);
        final RelativeLayout startTimeLayout = (RelativeLayout) view.findViewById(R.id.startTimeLayout);
        final RelativeLayout endTimeLayout = (RelativeLayout) view.findViewById(R.id.endTimeLayout);
        final RelativeLayout totalTimeLayout = (RelativeLayout) view.findViewById(R.id.totalTimeLayout);
        final Button saveButton = (Button) view.findViewById(R.id.save_button);
        final Button newConstraintButton = (Button) view.findViewById(R.id.add_constraint_button);
        totalTimeTextView = (TextView) view.findViewById(R.id.totalTime);

        if (task != null) {
            title.setText(task.getTitle());
            description.setText(task.getDescription());
            taskPageTitle.setText(R.string.edit_task_title);
            // start time, end time, dateTextView, categorySpinner are initiated later.
            fixed = task.getFixed();
            fixedSwitch.setChecked(task.getFixed());
            if (task.getFixed()) {
                startTimeLayout.setVisibility(View.VISIBLE);
                endTimeLayout.setVisibility(View.VISIBLE);
                totalTimeLayout.setVisibility(View.GONE);
            } else {
                startTimeLayout.setVisibility(View.GONE);
                endTimeLayout.setVisibility(View.GONE);
                totalTimeLayout.setVisibility(View.VISIBLE);
                totalTimeMinutes = task.getTotalTime() % 60;
                totalTimeHours = task.getTotalTime() / 60;
            }
        }

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                NewEditTaskFragment.this.getActivity().onTouchEvent(event);
                return false;
            }
        });

        // Check if user is logged in
        if (ParseUser.getCurrentUser() == null) {
            Log.e(Constants.NEW_EDIT_TASK_TAG, "USER IS NULL! SAVING WILL NOT WORK");
        }

        // Day
        if (task == null) {
            day = getOrCreateDay(date, calendar);
        } else {
            try {
                day = task.getDay();
            } catch (Exception e) {
                Log.e(Constants.NEW_EDIT_TASK_TAG, "Could not find existing day!");
                new AlertDialog.Builder(getContext())
                        .setTitle("Error")
                        .setMessage("Something went wrong. Please try again!")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
            date = day.getDate();
        }
        if (day == null || date == null) throw new AssertionError("Day or date is null!");

        calendar.setTime(date);
        if (task == null) {
            dateTextView.setText(dateFormat.format(calendar.getTime()));
        } else {
            dateTextView.setText(dateFormat.format(day.getDate()));
        }

        // Initialize Texts

        final Calendar currentTimeCalendar = Calendar.getInstance();
        currentTimeCalendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        if (task == null) {
            this.startTime = currentTimeCalendar.getTime();
            this.endTime = addMinutesToDate(currentTimeCalendar, 30).getTime();
        } else {
            this.startTime = task.getStartTime();
            this.endTime = task.getEndTime();
        }
        if (this.startTime != null) {
            startTime.setText(timeFormat.format(this.startTime));
        }

        if (this.endTime != null) {
            endTime.setText(timeFormat.format(this.endTime));
        }

        // Validate field inputs
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    title.setError("Name can't be empty");
                }
            }
        });

        // initialize spinner items
        totalTimeTextView.setText(
                getString(R.string.totalTimeString, totalTimeHours, totalTimeMinutes));
        totalTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                        .setForced24hFormat()
                        .setOnTimeSetListener(NewEditTaskFragment.this)
                        .setStartTime(totalTimeHours, totalTimeMinutes);
                rtpd.show(getChildFragmentManager(), "FRAG");
            }
        });

        populateCategoriesSpinner(categorySpinner);

        // Initialize Constraints List View
        ListView constraintsListView = (NoScrollListView) view.findViewById(R.id.constraints_list);
        if (task != null) {
            ParseQuery<Constraint> taskConstraintsQuery = task.getConstraints();
            constraints = new ArrayList<>();
            try {
                for (Constraint constraint : taskConstraintsQuery.find()) {
                    // A deep copy is required here, since when we edit constraints, we delete
                    // all previous constraints and add the constraints again for simplicity.
                    // If we do not deep copy, when we delete the constraints, existing constraints
                    // will not be around anymore to be added.

                    // In this method, a clone of each previous constraint is created. When we
                    // delete all the previous constraints these are left, and is then saved and
                    // associated with the current task by a ParseRelation.
                    constraints.add(constraint.clone());
                }
            } catch (Exception e) {
                Log.d(Constants.NEW_EDIT_TASK_TAG, "Could not find constraints! Assume no constraints");
            }
        } else {
            constraints = new ArrayList<>();
        }
        final ConstraintAdapter constraintAdapter = new ConstraintAdapter(
                getContext(),
                R.layout.constraint_list_row,
                constraints
        );
        constraintsListView.setAdapter(constraintAdapter);
        constraintsListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        // Listeners
        fixedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fixed = isChecked;
                if (isChecked) {
                    startTimeLayout.setVisibility(View.VISIBLE);
                    endTimeLayout.setVisibility(View.VISIBLE);
                    totalTimeLayout.setVisibility(View.GONE);

                } else {
                    startTimeLayout.setVisibility(View.GONE);
                    endTimeLayout.setVisibility(View.GONE);
                    totalTimeLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                c.setTime(NewEditTaskFragment.this.startTime);
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog startTimePicker;
                startTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        c.set(Calendar.HOUR_OF_DAY, selectedHour);
                        c.set(Calendar.MINUTE, selectedMinute);
                        NewEditTaskFragment.this.startTime = c.getTime();
                        startTime.setText(timeFormat.format(c.getTime()));
                    }
                }, hour, minute, android.text.format.DateFormat.is24HourFormat(getActivity()));
                startTimePicker.show();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                c.setTime(NewEditTaskFragment.this.endTime);
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog endTimePicker;
                endTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        c.set(Calendar.HOUR_OF_DAY, selectedHour);
                        c.set(Calendar.MINUTE, selectedMinute);
                        NewEditTaskFragment.this.endTime = c.getTime();
                        endTime.setText(timeFormat.format(c.getTime()));
                    }
                }, hour, minute, android.text.format.DateFormat.is24HourFormat(getActivity()));
                endTimePicker.show();
            }
        });

        newConstraintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NewConstraintDialog constraintDialog = new NewConstraintDialog(
                        getActivity(), day, constraints, task);
                // Constraint needs to be based on current day only
                // pass in day so it knows which day.
                constraintDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Log.d(Constants.NEW_EDIT_TASK_TAG,
                                "Dialog dismissed, notifying data change: " + constraints.size());
                        constraintAdapter.notifyDataSetChanged();
                    }
                });
                constraintDialog.show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence error = title.getError();
//                final int totalHours = Integer.parseInt(totalTimeHour.getSelectedItem().toString());
//                final int totalMinutes = Integer.parseInt(totalTimeMinute.getSelectedItem().toString());
                if (error != null) {
                    // If title is empty
                    title.requestFocus();
                } else if (title.getText().toString().matches("")) {
                    // The title error only shows if user edits the field
                    // this check is still necessary
                    title.setError("Task name cannot be empty!");
                    title.requestFocus();
                } else if (totalTimeHours == 0 && totalTimeMinutes == 0 && !fixed) {
                    // Total time shouldn't be zero
                    new AlertDialog.Builder(getContext())
                            .setTitle("Save Task Error")
                            .setMessage("Can't create a task with no total time!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                } else {
                    // Check if other task with same name in the same day
                    String taskTitle = title.getText().toString();
                    ParseQuery<Task> query = Task.getQuery();
                    query.whereEqualTo("user", ParseUser.getCurrentUser());
                    query.whereEqualTo("title", taskTitle);
                    // TODO: avoid this call completely if newDate
                    if (!newDate) {
                        query.whereEqualTo("day", day);
                    }
                    query.findInBackground(new FindCallback<Task>() {
                        @Override
                        public void done(List<Task> objects, ParseException e) {
                            if (e == null) {
                                Log.d(Constants.NEW_EDIT_TASK_TAG, "# tasks with same name: " + objects.size());
                                if (objects.size() > 0 && !newDate && task == null) {
                                    // trying to create a task with the same name
                                    title.setError("Already have a task with the same name!");
                                } else if (NewEditTaskFragment.this.fixed &&
                                        NewEditTaskFragment.this.endTime.before(
                                                NewEditTaskFragment.this.startTime
                                        )) {
                                    new AlertDialog.Builder(getContext())
                                            .setTitle("Error")
                                            .setMessage("Start time cannot be after end time!")
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                } else {
                                    if (newTask) {
                                        task = new Task();
                                    }
                                    saveButton.setEnabled(false);
                                    saveButton.setText(R.string.button_saving);
                                    task.setTitle(title.getText().toString());
                                    task.setDescription(description.getText().toString());
                                    task.setFixed(fixed);
                                    task.setUser(ParseUser.getCurrentUser());

                                    // Time

                                    final int totalTime;
                                    int previousTotalTime = 0;
                                    if (!newTask) {
                                        previousTotalTime = task.getTotalTime();
                                    }

                                    if (fixed) {
                                        task.setStartTime(NewEditTaskFragment.this.startTime);
                                        task.setEndTime(NewEditTaskFragment.this.endTime);
                                        totalTime = TimeUtils.getMinutesDiff(
                                                NewEditTaskFragment.this.startTime,
                                                NewEditTaskFragment.this.endTime);
                                        task.setTotalTime(totalTime);
                                    } else {
                                        totalTime = totalTimeHours * 60 + totalTimeMinutes;
                                        task.setTotalTime(totalTime);
                                        task.setStartTime(day.getDate());
                                    }

                                    // category

                                    // remove previous time spent on category
                                    if (!newTask) {
                                        try {
                                            String previousCategoryName = task.getCategory().getTitle();
                                            int prevTimeSpentOn = day.getTimeSpentOn(previousCategoryName);
                                            prevTimeSpentOn -= previousTotalTime;
                                            day.setTimeSpent(
                                                    previousCategoryName,
                                                    prevTimeSpentOn
                                            );
                                        } catch (ParseException | NullPointerException e1) {
                                            Log.d(Constants.NEW_EDIT_TASK_TAG, "No previous category found!");
                                        }
                                    }
                                    if (categorySpinner.getSelectedItemPosition() == 0) {
                                        // user unset category
                                        task.remove("category");
                                        task.saveEventually();
                                    } else if (categorySpinner.getSelectedItemPosition() > 0) {
                                        int pos = categorySpinner.getSelectedItemPosition();
                                        ParseQuery<Category> categoryParseQuery = Category.getQuery();
                                        categoryParseQuery.whereEqualTo("user", ParseUser.getCurrentUser());
                                        categoryParseQuery.whereEqualTo("title", categories.get(pos));
                                        final String title = categories.get(pos);
                                        final int previousTimeSpent = day.getTimeSpentOn(title);
                                        categoryParseQuery.findInBackground(new FindCallback<Category>() {
                                            @Override
                                            public void done(List<Category> categories, ParseException e) {
                                                if (e == null) {
                                                    if (categories.size() == 0) {
                                                        // no objects fetched, create category
                                                        // This is for one of the 5 default categories each
                                                        // user has.
                                                        final Category c = new Category();
                                                        c.setTitle(title);
                                                        c.setUser(ParseUser.getCurrentUser());
                                                        c.saveEventually(new SaveCallback() {
                                                            @Override
                                                            public void done(ParseException e) {
                                                                task.setCategory(c);
                                                                day.setTimeSpent(title, previousTimeSpent + totalTime);
                                                            }
                                                        });
                                                    } else if (categories.size() == 1) {
                                                        task.setCategory(categories.get(0));
                                                        day.setTimeSpent(title, previousTimeSpent + totalTime);
                                                    } else {
                                                        Log.e(Constants.NEW_EDIT_TASK_TAG, "Multiple categories returned!");
                                                    }
                                                    task.saveEventually();
                                                } else {
                                                    Log.e(Constants.NEW_EDIT_TASK_TAG, "Could not fetch categories!");
                                                }
                                            }
                                        });

                                    }

                                    // save constraints
                                    try {
                                        if (!newTask) {
                                            // If not a newTask, remove all previous constraints
                                            // and add them again to the task. This is not an
                                            // efficient approach, but it's very simple. The
                                            // alternative is cross check which ones are gone and
                                            // only delete those.
                                            Log.d(Constants.NEW_EDIT_TASK_TAG,
                                                    "Not a new task! Removing all previous constraints");
                                            task.removeAllConstraints();
                                        }
                                    } catch (ParseException e1) {
                                        new AlertDialog.Builder(getActivity().getBaseContext())
                                                .setTitle("Error")
                                                .setMessage("Something went wrong. Please try again!")
                                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        saveButton.setEnabled(true);
                                                    }
                                                })
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();
                                    }

                                    for (final Constraint c : constraints) {
                                        Log.d(Constants.NEW_EDIT_TASK_TAG, "Adding constraint: " + c.toString());
                                        try {
                                            c.save();
                                        } catch (ParseException e1) {
                                            Log.e(Constants.NEW_EDIT_TASK_TAG, "Could not save constraint: " + e1.getMessage());
                                            new AlertDialog.Builder(getActivity())
                                                    .setTitle("Error")
                                                    .setMessage("Something went wrong. Please try again!")
                                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            saveButton.setEnabled(true);
                                                        }
                                                    })
                                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                                    .show();
                                        }
                                        task.addConstraint(c);
                                    }

                                    day.saveInBackground(new SaveCallback() {
                                        // save day first
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                task.setDay(day);
                                                task.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if (e == null) {
                                                            // TODO: Close view
                                                            saveButton.setText("Saved!");
                                                            Toast.makeText(
                                                                    getActivity(),
                                                                    "Saved",
                                                                    Toast.LENGTH_SHORT).show();
                                                            getFragmentManager().popBackStackImmediate();
                                                            getFragmentManager().beginTransaction().commit();

                                                        } else {
                                                            Log.e(Constants.NEW_EDIT_TASK_TAG + "SaveTask", e.getMessage());
                                                        }
                                                    }
                                                });
                                            } else {
                                                Log.e(Constants.NEW_EDIT_TASK_TAG + "SaveDay", e.getMessage());
                                            }
                                        }
                                    });
                                }
                            } else {
                                Log.e(Constants.NEW_EDIT_TASK_TAG, e.getMessage());
                            }
                        }
                    });
                }
            }
        });
        return view;
    }

    private Day getOrCreateDay(Date date, Calendar calendar) {
        ParseQuery<Day> dayParseQuery = Day.getQuery();
        dayParseQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        dayParseQuery.whereEqualTo("date", date);
        Day day;
        try {
            List<Day> days = dayParseQuery.find();
            Log.d(Constants.NEW_EDIT_TASK_TAG, "# of day with same date: " + days.size());
            if (days.size() == 0) {
                // no such day, create it
                newDate = true;
                day = new Day();
                day.initialize();
                day.setUser(ParseUser.getCurrentUser());
                day.setDate(date);

                // reasonable default: 8:30 AM to 23:59PM
                calendar.setTime(date);
                calendar.set(Calendar.HOUR_OF_DAY, 8);
                calendar.set(Calendar.MINUTE, 30);

                day.setDayStart(calendar.getTime());

                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                day.setDayEnd(calendar.getTime());

                // reset time
                calendar.setTime(new Date());

                day.pin();
            } else {
                day = days.get(0);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            // Couldn't save Day!
            // show error
            new AlertDialog.Builder(getActivity())
                    .setTitle("Error")
                    .setMessage("Something went wrong. Please try again!")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // close fragment
                            getActivity().getSupportFragmentManager().beginTransaction().remove(
                                    NewEditTaskFragment.this).commit();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return null;
        }
        return day;
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
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        totalTimeHours = hourOfDay;
        totalTimeMinutes = minute;
        totalTimeTextView.setText(getString(R.string.totalTimeString, hourOfDay, minute));
    }
}
