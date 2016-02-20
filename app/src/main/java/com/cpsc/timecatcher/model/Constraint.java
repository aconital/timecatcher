package com.cpsc.timecatcher.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by yutongluo on 2/7/16.
 */
@ParseClassName("Constraint")
public class Constraint extends ParseObject {
    public static final String TAG = "Constraint";

    public Operator getOperator() {
        String operator = getString("operator");
        return Operator.valueOf(operator);
    }

    public void setOperator(Operator operator) {
        put("operator", operator.toString());
    }

    public Task getOther() {
        return (Task) getParseObject("other");
    }

    public void setOther(Task other) {
        put("other", other);
    }

    public void setOther(String taskId) {
        ParseObject task = ParseObject.createWithoutData("Task", taskId);
        put("other", task);
    }
}
