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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cpsc.timecatcher.gui.MultiSpinner;
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
 * {@link NewTaskFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class NewTaskFragment extends Fragment implements MultiSpinner.MultiSpinnerListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private Date date;
    private Day day;
    private final static String DATE_TAG="DATE";

    private boolean fixed;
    private Date startTime;
    private Date endTime;

    private final DateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.CANADA);
    private final DateFormat dateFormat = new SimpleDateFormat("EEE, MMM d", Locale.CANADA);
    private OnFragmentInteractionListener mListener;

    private List<String> categories;

    private boolean[] selected;

    private Task task;

    private final Calendar calendar = Calendar.getInstance();

    private List<Constraint> constraints;

    public NewTaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param long Date MAKE SURE THIS DATE IS THE STRIPED DATE WITH 0h:0m:0s:00
     * @return A new instance of fragment NewTaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewTaskFragment newInstance(long date) {
        NewTaskFragment fragment = new NewTaskFragment();
        Bundle args = new Bundle();
        args.putLong(DATE_TAG, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            date = new Date(getArguments().getLong(DATE_TAG));
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(Calendar.HOUR, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            date = c.getTime();
        }
    }

    private void populateCategoriesSpinner(final MultiSpinner spinner) {
        final String[] defaultCategories = getResources().getStringArray(R.array.new_task_default_categories_array);

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
                    Log.e(Constants.NEW_TASK_TAG, "Could not fetch categories!");
                }
                NewTaskFragment.this.categories = new ArrayList<>(allCategories);
                spinner.setItems(new ArrayList<>(NewTaskFragment.this.categories),
                        getResources().getString(R.string.new_task_category_hint), NewTaskFragment.this);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_new_task, container, false);
        final EditText title = (EditText) view.findViewById(R.id.edit_task_name);
        final EditText description = (EditText) view.findViewById(R.id.edit_task_description);
        final TextView startTime = (TextView) view.findViewById(R.id.startTime);
        final TextView endTime = (TextView) view.findViewById(R.id.endTime);
        final TextView dateTextView = (TextView) view.findViewById(R.id.title_date);
        final MultiSpinner multiSpinner = (MultiSpinner) view.findViewById(R.id.categories);
        final Switch fixedSwitch = (Switch) view.findViewById(R.id.fixed);
        final RelativeLayout startTimeLayout = (RelativeLayout) view.findViewById(R.id.startTimeLayout);
        final RelativeLayout endTimeLayout = (RelativeLayout) view.findViewById(R.id.endTimeLayout);
        final LinearLayout totalTimeLayout = (LinearLayout) view.findViewById(R.id.totalTimeLayout);
        final Spinner totalTimeHour = (Spinner) view.findViewById(R.id.totalTimeHour);
        final Spinner totalTimeMinute = (Spinner) view.findViewById(R.id.totalTimeHourMinute);
        final Button saveButton = (Button) view.findViewById(R.id.save_button);
        final Button newConstraintButton = (Button) view.findViewById(R.id.add_constraint_button);

        // Day
        getOrCreateDay();

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
                Log.d(Constants.NEW_TASK_TAG, "text changed: " + s.toString().length());
                if (s.toString().length() == 0) {
                    title.setError("Name can't be empty");
                } else {
                    String taskTitle = s.toString();
                    ParseQuery<Task> query= Task.getQuery();
                    query.whereEqualTo("user", ParseUser.getCurrentUser());
                    query.whereEqualTo("title", taskTitle);
                    query.findInBackground(new FindCallback<Task>() {
                        @Override
                        public void done(List<Task> objects, ParseException e) {
                            if (e == null) {
                                Log.d(Constants.NEW_TASK_TAG, "# tasks with same name: " + objects.size());

                                if (objects.size() > 0) {
                                    title.setError("Already have a task with the same name!");
                                }
                            } else {
                                Log.e(Constants.NEW_TASK_TAG, e.getMessage());
                            }

                        }
                    });
                }
            }
        });

        // Initialize Texts
        startTime.setText(timeFormat.format(calendar.getTime()));
        this.startTime = calendar.getTime();
        endTime.setText(timeFormat.format(addMinutesToDate(calendar, 30).getTime()));
        this.endTime = addMinutesToDate(calendar, 30).getTime();

        calendar.setTime(date);
        dateTextView.setText(dateFormat.format(calendar.getTime()));


        // initialize spinner items
        ArrayAdapter<CharSequence> totalTimeHourAdapter = ArrayAdapter.createFromResource(
                getActivity().getBaseContext(),
                R.array.new_task_set_time_hours, android.R.layout.simple_spinner_item
        );
        totalTimeHourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        totalTimeHour.setAdapter(totalTimeHourAdapter);

        ArrayAdapter<CharSequence> totalTimeMinuteAdapter = ArrayAdapter.createFromResource(
                getActivity().getBaseContext(),
                R.array.set_time_minutes, android.R.layout.simple_spinner_item
        );

        totalTimeMinuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        totalTimeMinute.setAdapter(totalTimeMinuteAdapter);

        populateCategoriesSpinner(multiSpinner);

        // Initialize Constraints List View
        ListView constraintsListView = (ListView) view.findViewById(R.id.constraints_list);
        constraints = new ArrayList<>();
        final ConstraintAdapter constraintAdapter = new ConstraintAdapter(
                getContext(),
                R.layout.constraint_list_row,
                constraints
        );
        constraintsListView.setAdapter(constraintAdapter);

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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence error = title.getError();
                int totalHours = Integer.parseInt(totalTimeHour.getSelectedItem().toString());
                int totalMinutes = Integer.parseInt(totalTimeMinute.getSelectedItem().toString());
                int totalTime = totalHours * 60 + totalMinutes;
                if (error != null) {
                    // If title is empty
                    title.requestFocus();
                } else if (title.getText().toString().matches("")) {
                    // The title error only shows if user edits the field
                    // this check is still necessary
                    title.setError("Task name cannot be empty!");
                    title.requestFocus();
                } else if (totalTime == 0 && !fixed) {
                    // Total time shouldn't be zero
                    new AlertDialog.Builder(getContext())
                            .setTitle("Save Task Error")
                            .setMessage("Can't create a task with no total time!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    totalTimeMinute.requestFocus();
                                }
                            }).show();
                } else {
                    task = new Task();
                    saveButton.setEnabled(false);
                    saveButton.setText("Saving...");
                    task.setTitle(title.getText().toString());
                    task.setDescription(description.getText().toString());
                    task.setFixed(fixed);
                    task.setUser(ParseUser.getCurrentUser());

                    // Time
                    if (fixed) {
                        task.setStartTime(NewTaskFragment.this.startTime);
                        task.setEndTime(NewTaskFragment.this.endTime);
                    } else {
                        task.setTotalTime(totalTime);
                    }
                    // Categories
                    if (selected != null) {
                        for (int i = 0; i < selected.length; i++) {
                            if (selected[i]) {
                                // this category is selected, look it up on Parse
                                ParseQuery<Category> categoryParseQuery = Category.getQuery();
                                categoryParseQuery.whereEqualTo("user", ParseUser.getCurrentUser());
                                categoryParseQuery.whereEqualTo("title", categories.get(i));
                                final String title = categories.get(i);
                                categoryParseQuery.findInBackground(new FindCallback<Category>() {
                                    @Override
                                    public void done(List<Category> objects, ParseException e) {
                                        if (e == null) {
                                            if (objects.size() == 0) {
                                                // no objects fetched, create category
                                                // This is for one of the 5 default categories each
                                                // user has.
                                                final Category c = new Category();
                                                c.setTitle(title);
                                                c.setUser(ParseUser.getCurrentUser());
                                                c.saveEventually(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        task.addCategory(c);
                                                    }
                                                });
                                            } else if (objects.size() == 1) {
                                                task.addCategory(objects.get(0));
                                            } else {
                                                Log.e(Constants.NEW_TASK_TAG, "Multiple categories returned!");
                                            }
                                        } else {
                                            Log.e(Constants.NEW_TASK_TAG, "Could not fetch categories!");
                                        }
                                    }
                                });
                            }
                        }
                    }

                    // save constraints
                    for (final Constraint c : constraints) {
                        Log.d(Constants.NEW_TASK_TAG, "Adding constraint: " + c.toString());
                        c.saveInBackground();
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
                                        } else {
                                            Log.e(Constants.NEW_TASK_TAG, e.getMessage());
                                        }
                                    }
                                });
                            } else {
                                Log.e(Constants.NEW_TASK_TAG, e.getMessage());
                            }

                        }
                    });
                }
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog startTimePicker;
                startTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        c.set(Calendar.HOUR_OF_DAY, selectedHour);
                        c.set(Calendar.MINUTE, selectedMinute);
                        if (NewTaskFragment.this.endTime != null) {
                            if (NewTaskFragment.this.endTime.before(c.getTime())) {
                                // endTime < startTime!
                                // silent fail the change
                                return;
                            }
                        }
                        NewTaskFragment.this.startTime = c.getTime();
                        startTime.setText(timeFormat.format(c.getTime()));
                    }
                }, hour, minute, android.text.format.DateFormat.is24HourFormat(getActivity()));
                startTimePicker.show();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = addMinutesToDate(calendar, 30);
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog endTimePicker;
                endTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        c.set(Calendar.HOUR_OF_DAY, selectedHour);
                        c.set(Calendar.MINUTE, selectedMinute);
                        if (NewTaskFragment.this.startTime != null) {
                            if (NewTaskFragment.this.startTime.after(c.getTime())) {
                                // startTime > endTime !
                                // silent fail the change
                                return;
                            }
                        }
                        NewTaskFragment.this.endTime = c.getTime();
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
                        getActivity(), day, constraints);
                // Constraint needs to be based on current day only
                // pass in day so it knows which day.
                constraintDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Log.d(Constants.NEW_TASK_TAG,
                                "Dialog dismissed, notifying data change: " + constraints.size());
                        constraintAdapter.notifyDataSetChanged();
                    }
                });
                constraintDialog.show();

            }
        });

        return view;

    }

    public void getOrCreateDay(){
        ParseQuery<Day> dayParseQuery = Day.getQuery();
        dayParseQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        dayParseQuery.whereEqualTo("date", date);
        try{
            List<Day> days = dayParseQuery.find();
            Log.d(Constants.NEW_TASK_TAG, "# of day with same date: " + days.size());
            if (days.size() == 0) {
                // no such day, create it
                day = new Day();
                day.setUser(ParseUser.getCurrentUser());
                day.setDate(date);

                // reasonable default: 8:30 AM to 9PM
                calendar.setTime(date);
                calendar.set(Calendar.HOUR_OF_DAY, 8);
                calendar.set(Calendar.MINUTE, 30);

                day.setDayStart(calendar.getTime());

                calendar.set(Calendar.HOUR_OF_DAY, 20);
                day.setDayEnd(calendar.getTime());

                try {
                    day.pin();
                } catch (ParseException e) {
                    // Couldn't save Day!

                    // show error
                    new AlertDialog.Builder(getActivity().getBaseContext())
                            .setTitle("Error")
                            .setMessage("Something went wrong. Please try again!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // close fragment
                                    getActivity().getSupportFragmentManager().beginTransaction().remove(
                                            NewTaskFragment.this).commit();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    e.printStackTrace();
                }
            } else {
                day = days.get(0);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    public void onItemsSelected(boolean[] selected) {
        this.selected = selected;
    }
}
