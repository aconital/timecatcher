package com.cpsc.timecatcher.importer;

import android.app.Dialog;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.everything.providers.android.calendar.CalendarProvider;
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
                final java.util.Calendar c = java.util.Calendar.getInstance();
                Date start, end;
                instances.clear();
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

        for(Instance i : selectedInstances) {
            Log.d(TAG, "User selected: " + calendarProvider.getEvent(i.eventId).title);
        }
    }
}
