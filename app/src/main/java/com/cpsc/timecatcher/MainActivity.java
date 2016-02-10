package com.cpsc.timecatcher;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.facebook.appevents.AppEventsLogger;
import com.parse.ParseFacebookUtils;
import com.parse.ui.ParseLoginBuilder;

import java.util.ArrayList;
import java.util.List;

import adapters.TaskAdapter;
import models.Task;

public class MainActivity extends AppCompatActivity {

    private List<Task> taskList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TaskAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseLoginBuilder builder = new ParseLoginBuilder(MainActivity.this);
        startActivityForResult(builder.build(), 0);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new TaskAdapter(taskList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareTaskData();
    }
    private void prepareTaskData() {
        Task task1 = new Task("Get Ready", "11:25", "15:23");
        taskList.add(task1);

        Task task2 = new Task("School", "9:25", "5:23");
        taskList.add(task2);

        Task task3 = new Task("Gym", "1:25", "7:23");
        taskList.add(task3);

        Task task4 = new Task("Piano", "14:5", "12:23");
        taskList.add(task4);

        Task task5 = new Task("dog walking", "5:25", "55:23");
        taskList.add(task5);


        mAdapter.notifyDataSetChanged();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }
    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
}
