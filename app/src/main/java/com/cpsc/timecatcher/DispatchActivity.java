package com.cpsc.timecatcher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.parse.ParseFacebookUtils;
import com.parse.ui.ParseLoginBuilder;
import com.parse.ui.ParseLoginDispatchActivity;
import com.vungle.publisher.VunglePub;

/**
 * Created by hroshandel on 2016-02-15.
 */
public class DispatchActivity extends ParseLoginDispatchActivity {
    final VunglePub vunglePub = VunglePub.getInstance();

    @Override
    protected Class<?> getTargetClass() {
        return StructureActivity.class;
    }
    @Override
    protected void onPause() {
        super.onPause();
        vunglePub.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vunglePub.onResume();
    }
}