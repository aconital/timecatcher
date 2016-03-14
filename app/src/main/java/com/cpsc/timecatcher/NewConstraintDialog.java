package com.cpsc.timecatcher;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.cpsc.timecatcher.helper.Constants;
import com.cpsc.timecatcher.model.Constraint;
import com.cpsc.timecatcher.model.Day;
import com.cpsc.timecatcher.model.Operator;
import com.cpsc.timecatcher.model.Task;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by yutongluo on 2/19/16.
 */
public class NewConstraintDialog extends Dialog {

    private Button saveButton, cancelButton;
    private Spinner operatorSpinner, otherTaskSpinner;
    private Operator operator;
    private Day day;
    private Task task;
    private List<Constraint> constraintList;
    private List<Task> taskList;

    public NewConstraintDialog(Context context) {
        super(context);
    }

    public NewConstraintDialog(Context context, Day day, List<Constraint> constraintList, Task task) {
        super(context);
        this.day = day;
        this.constraintList = constraintList;
        this.task = task;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.constraint_new_dialog);
        saveButton = (Button) findViewById(R.id.button_save);
        cancelButton = (Button) findViewById(R.id.button_cancel);

        operatorSpinner = (Spinner) findViewById(R.id.constraint_operator);
        otherTaskSpinner = (Spinner) findViewById(R.id.constraint_other);

        // Populate spinner values
        final ArrayAdapter<CharSequence> operatorAdaptor = ArrayAdapter.createFromResource(
                getContext(),
                R.array.constraint_order, android.R.layout.simple_spinner_item
        );

        operatorAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        operatorSpinner.setAdapter(operatorAdaptor);

        ParseQuery<Task> taskParseQuery = Task.getQuery();
        taskParseQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        if (day != null) {
            taskParseQuery.whereEqualTo("day", day);
        } else {
            Log.d(Constants.NEW_CONSTRAINT_TAG, "Warning: null day, getting all user's tasks");
        }

        taskParseQuery.findInBackground(new FindCallback<Task>() {
            @Override
            public void done(List<Task> objects, ParseException e) {
                if (e == null) {
                    String[] taskNames;
                    if (task == null) {
                        taskNames = new String[objects.size()];
                    } else {
                        // "otherTask" cannot be task itself
                        taskNames = new String[objects.size() - 1];
                    }
                    int i = 0;
                    taskList = objects;

                    for (Task task : objects) {
                        if (NewConstraintDialog.this.task != null) {
                            if (!task.getObjectId().equals(
                                    NewConstraintDialog.this.task.getObjectId())) {
                                // "otherTask" cannot be task itself
                                taskNames[i++] = task.getTitle();
                            }
                        } else {
                            taskNames[i++] = task.getTitle();
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_spinner_item, taskNames);
                    otherTaskSpinner.setAdapter(adapter);
                } else {
                    Log.d(Constants.NEW_CONSTRAINT_TAG, "Couldn't find other tasks!");
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operator = Operator.valueOf(
                        operatorSpinner.getSelectedItem().toString().toUpperCase());

                if (otherTaskSpinner.getSelectedItem() == null) {
                    // there was no other task to select from. silently exit
                    dismiss();
                    return;
                }
                String otherTaskTitle = otherTaskSpinner.getSelectedItem().toString();
                Task other = null;
                for (Task task : taskList) {
                    if (task.getTitle().equals(otherTaskTitle)) {
                        other = task;
                    }
                }
                if (other == null) {
                    dismiss();
                    return;
                }

                boolean duplicate = false;

                for (Constraint c : constraintList) {
                    try {
                        if (c.getOperator() == operator && c.getOther() == other) {
                            duplicate = true;
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Save Constraint Error")
                                    .setMessage("You already have this constraint!")
                                    .setPositiveButton(android.R.string.ok, new OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();
                        }
                    } catch (ParseException e) {
                        Log.e(Constants.NEW_CONSTRAINT_TAG, "Duplicate check getOtherFailed");
                        Log.e(Constants.NEW_CONSTRAINT_TAG, e.getLocalizedMessage());
                    }
                }
                if (!duplicate) {
                    Constraint constraint = new Constraint();
                    constraint.setOperator(operator);
                    constraint.setOther(other);
                    constraintList.add(constraint);
                    saveButton.setEnabled(false);
                }
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
