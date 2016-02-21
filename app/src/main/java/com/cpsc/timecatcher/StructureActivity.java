package com.cpsc.timecatcher;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.cpsc.timecatcher.helper.Constants;
import com.cpsc.timecatcher.helper.Utility;
import com.facebook.appevents.AppEventsLogger;

public class StructureActivity extends AppCompatActivity
        implements ScheduleFragment.OnFragmentInteractionListener, NewTaskFragment.OnFragmentInteractionListener, CalendarFragment.OnFragmentInteractionListener {

    private Button schedule,calendar,analytics,profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.structure_activity);
        final long todayLong= Utility.getTodayLong();
        //    getActionBar().hide();
        if(null == savedInstanceState) {
            //default schedule is today

            Fragment scheduleFragment=ScheduleFragment.newInstance(todayLong);
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                    .replace(R.id.frame_container, scheduleFragment).commit();
        }


        schedule =(Button) findViewById(R.id.schedule);
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame_container);
                if (!(f instanceof ScheduleFragment))
                {
                    Fragment scheduleFragment=ScheduleFragment.newInstance(todayLong);
                    launchFragment(scheduleFragment, Constants.SCHEDULE_TAG);
                }
            }
        });
        calendar=(Button) findViewById(R.id.calendar);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame_container);
                if (!(f instanceof CalendarFragment))
                {
                    Fragment calendarFragment=new CalendarFragment();
                    launchFragment(calendarFragment,Constants.CALENDAR_TAG);
                }
            }
        });



        analytics=(Button)findViewById(R.id.analytics);
/*        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame_container);
                if (!(f instanceof ChooseAvatarFragment))
                {
                    Fragment introFragment=new ChooseAvatarFragment();
                    launchFragment(introFragment,Constants.AVATAR_TAG);
                }
            }
        });*/
        profile=(Button)findViewById(R.id.profile);


    }

    public void onDateSelected(long date){
        Fragment scheduleFragment=ScheduleFragment.newInstance(date);
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .replace(R.id.frame_container, scheduleFragment).commit();

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

    }


    private void launchFragment(Fragment f,String tag) {
        if (f != null) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                    .replace(R.id.frame_container, f).addToBackStack(tag).commit();

        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        // MenuInflater inflater = getMenuInflater();
        // inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_scan:
                Fragment scannerFragment = new ScannerFragment();
                launchFragment(scannerFragment, Constants.SCANNER_TAG);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
*/
    @Override
    public void onResume()
    {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }
    @Override
    public void onPause()
    {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
