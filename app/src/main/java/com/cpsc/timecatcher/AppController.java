package com.cpsc.timecatcher;

import android.app.Application;


import com.cpsc.timecatcher.model.Category;
import com.cpsc.timecatcher.model.Constraint;
import com.cpsc.timecatcher.model.Day;
import com.cpsc.timecatcher.model.Task;
import com.cengalabs.flatui.FlatUI;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.vungle.publisher.VunglePub;

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private static AppController mInstance;
    final VunglePub vunglePub = VunglePub.getInstance();

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

        vunglePub.init(this, "56f9cef2f6824cc70d000023");

        ParseObject.registerSubclass(Task.class);
        ParseObject.registerSubclass(Category.class);
        ParseObject.registerSubclass(Constraint.class);
        ParseObject.registerSubclass(Day.class);

// [Optional] Power your app with Local Datastore. For more info, go to
// https://parse.com/docs/android/guide#local-datastore
        Parse.enableLocalDatastore(this);
        Parse.initialize(this);
        ParseFacebookUtils.initialize(this);
        mInstance = this;
        Iconify
                .with(new FontAwesomeModule());
        //flat ui
        // Converts the default values (radius, size, border) to dp to be compatible with different
// screen sizes. If you skip this there may be problem with different screen densities
        FlatUI.initDefaultValues(this);

// Setting default theme to avoid to add the attribute "theme" to XML
// and to be able to change the whole theme at once
        FlatUI.setDefaultTheme(FlatUI.GRAPE);



    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

}