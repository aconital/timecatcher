package com.cpsc.timecatcher;

import android.app.Application;

import com.cpsc.timecatcher.model.Category;
import com.cpsc.timecatcher.model.Task;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private static AppController mInstance;


    /**
     * Enum used to identify the tracker that needs to be used for tracking.
     * <p/>
     * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
     * storing them; all in Application object helps ensure that they are created only once per
     * application instance.
     */


    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Task.class);
        ParseObject.registerSubclass(Category.class);

// [Optional] Power your app with Local Datastore. For more info, go to
// https://parse.com/docs/android/guide#local-datastore
        Parse.enableLocalDatastore(this);
        Parse.initialize(this);
        ParseFacebookUtils.initialize(this);
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

}