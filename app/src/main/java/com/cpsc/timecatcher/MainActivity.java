package com.cpsc.timecatcher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.cpsc.timecatcher.model.Category;
import com.cpsc.timecatcher.model.Task;
import com.facebook.appevents.AppEventsLogger;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseLoginBuilder builder = new ParseLoginBuilder(MainActivity.this);
        startActivityForResult(builder.build(), 0);


        Category c = new Category();
        c.setTitle("test category");
        c.setUser(ParseUser.getCurrentUser());
        c.saveInBackground();

        Task t = new Task();
        t.setTitle("test task");
        t.setDescription("simple test");
        t.saveInBackground();
        t.setUser(ParseUser.getCurrentUser());
        t.addCategory(c);
        t.saveInBackground();
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
