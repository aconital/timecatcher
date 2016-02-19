package com.cpsc.timecatcher;

import android.app.TimePickerDialog;
import android.content.Context;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cpsc.timecatcher.gui.MultiSpinner;
import com.cpsc.timecatcher.model.Category;
import com.cpsc.timecatcher.model.Task;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Date startTime;
    private Date endTime;

    private final DateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.CANADA);
    private OnFragmentInteractionListener mListener;

    public NewTaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewTaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewTaskFragment newInstance(String param1, String param2) {
        NewTaskFragment fragment = new NewTaskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void populateCategoriesSpinner(final MultiSpinner spinner) {
        final String[] defaultCategories = getResources().getStringArray(R.array.new_task_default_categories_array);

        final Set<String> allCategories = new HashSet<>(Arrays.asList(defaultCategories));
        final ParseQuery<Category> categoryQuery = Category.getQuery();
        categoryQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        final MultiSpinner.MultiSpinnerListener that = this;
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

                spinner.setItems(new ArrayList<>(allCategories),
                        getResources().getString(R.string.new_task_category_hint), that);
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
        final MultiSpinner multiSpinner = (MultiSpinner) view.findViewById(R.id.categories);
        final Switch fixedSwitch = (Switch) view.findViewById(R.id.fixed);
        final RelativeLayout startTimeLayout = (RelativeLayout) view.findViewById(R.id.startTimeLayout);
        final RelativeLayout endTimeLayout = (RelativeLayout) view.findViewById(R.id.endTimeLayout);
        final LinearLayout totalTimeLayout = (LinearLayout) view.findViewById(R.id.totalTimeLayout);

        final Spinner totalTimeHour = (Spinner) view.findViewById(R.id.totalTimeHour);
        final Spinner totalTimeMinute = (Spinner) view.findViewById(R.id.totalTimeHourMinute);

        // Validate field inputs
        title.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

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
                            if (objects.size() > 0) {
                                title.setError("Already have a task with the same name!");
                            }
                        }
                    });
                }
            }
        });

        // Initialize Texts for startTime and endTime
        final Calendar calendar = Calendar.getInstance();
        startTime.setText(dateFormat.format(calendar.getTime()));
        endTime.setText(dateFormat.format(addMinutesToDate(calendar, 30).getTime()));


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

        // Listeners
        fixedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
                        startTime.setText(dateFormat.format(c.getTime()));
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
                        endTime.setText(dateFormat.format(c.getTime()));
                    }
                }, hour, minute, android.text.format.DateFormat.is24HourFormat(getActivity()));
                endTimePicker.show();
            }
        });

        return view;

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

    }
}
