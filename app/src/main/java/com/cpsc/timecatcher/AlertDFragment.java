package com.cpsc.timecatcher;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.cpsc.timecatcher.helper.Utility;
import com.cpsc.timecatcher.model.Task;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.Calendar;
import java.util.List;

/**
 * Created by hroshandel on 3/17/2016.
 */
public class AlertDFragment extends DialogFragment {
    private String objId;
    public static AlertDFragment newInstance(String objId) {
        AlertDFragment fragment = new AlertDFragment();
        Bundle args = new Bundle();
        args.putString("id", objId
        );
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null)
        {   //convert long to Date
            objId=getArguments().getString("id");

        }
        final StructureActivity activity = (StructureActivity) getActivity();
        return new AlertDialog.Builder(activity)

                // Set Dialog Title
                .setTitle("Delete Task")
                // Set Dialog Message
                .setMessage("Are you sure you want to delete this task?")

                // Positive button
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        ParseQuery<Task> query = new ParseQuery<Task>("Task");
                        query.whereEqualTo("objectId", objId);
                        query.getFirstInBackground(new GetCallback<Task>() {
                            @Override
                            public void done(Task object, ParseException e) {
                                    Calendar c= Calendar.getInstance();
                                    long dateLong = Utility.getTodayLong();
                                try {
                                    c.setTime(object.getDay().getDate());
                                    c.set(Calendar.HOUR_OF_DAY, 0);
                                    c.set(Calendar.MINUTE, 0);
                                    c.set(Calendar.SECOND,0);
                                    c.set(Calendar.MILLISECOND, 0);
                                     dateLong= c.getTime().getTime();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
                                object.deleteInBackground();
                                Fragment scheduleFragment= TasklistFragment.newInstance(dateLong);
                                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                        .replace(R.id.frame_container, scheduleFragment).commit();


                            }
                        });
                    }
                })

                // Negative Button
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,	int which) {
                        dialog.dismiss();
                    }
                }).create();
    }
}