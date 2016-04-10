package com.cpsc.timecatcher.importer;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.cpsc.timecatcher.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.everything.providers.android.calendar.CalendarProvider;
import me.everything.providers.android.calendar.Event;
import me.everything.providers.android.calendar.Instance;

/**
 * Created by yutongluo on 3/21/16.
 */
public class EventPickerDialog extends Dialog {

    public interface EventPickerDialogListener {
        public void userPickedEvents(SparseBooleanArray selected);
    }

    private final String TAG = "EventPickerDialog";
    final private List<Instance> instances;

    private ListView eventsListView;
    private List<String> taskNames = new ArrayList<>();
    final private CalendarProvider calendarProvider;
    private final DateFormat dateFormat = new SimpleDateFormat("EEE, h:mm a", Locale.CANADA);
    private EventPickerDialogListener listener;
    private CheckBox checkAll;
    private Button done;

    public EventPickerDialog(Context context) {
        super(context);
        instances = new ArrayList<>();
        calendarProvider = new CalendarProvider(getContext());
        eventsListView = null;
    }

    public void setPickerDialogListener(EventPickerDialogListener listener) {
        this.listener = listener;
    }

    public EventPickerDialog(
            Context context,
            List<Instance> instances,
            CalendarProvider calendarProvider) {
        super(context);
        this.instances = instances;
        this.calendarProvider = calendarProvider;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.event_picker);

        eventsListView = (ListView) findViewById(R.id.eventsList);
        final ArrayAdapter<String> eventsAdaptor = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_multiple_choice,
                taskNames);
        eventsListView.setAdapter(eventsAdaptor);
        eventsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        eventsListView.setItemsCanFocus(false);

        for (Instance i : instances) {
            Event e = calendarProvider.getEvent(i.eventId);
            taskNames.add(e.title + " at " + dateFormat.format(new Date(i.begin)));
        }
        Log.d(TAG, "taskNames now has " + taskNames.size() + " names, notifying adaptor..");
        eventsAdaptor.notifyDataSetChanged();

        checkAll = (CheckBox) findViewById(R.id.selectAllCheckBox);
        checkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i <= eventsAdaptor.getCount(); i++)
                    eventsListView.setItemChecked(i, isChecked);
            }
        });
        checkAll.setChecked(true);

        done = (Button) findViewById(R.id.done_button);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Bug with android: for some reason it considered the select all checkbox
                // as one of the listview ones... despite the fact that the select all checkbox
                // is completely seperate of the listview.
                listener.userPickedEvents(
                        eventsListView.getCheckedItemPositions()
                );
                EventPickerDialog.this.dismiss();
            }
        });
    }
}
