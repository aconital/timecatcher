package com.cpsc.timecatcher;

import com.parse.ui.ParseLoginDispatchActivity;

/**
 * Created by hroshandel on 2016-02-15.
 */
public class DispatchActivity extends ParseLoginDispatchActivity {
    @Override
    protected Class<?> getTargetClass() {
        return StructureActivity.class;
    }
}
