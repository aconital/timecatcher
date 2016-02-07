package com.cpsc.timecatcher.model;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by yutongluo on 2/5/16.
 */
public interface ITask {

    // Mutators
    void setTitle(String title);
    void setDescription(String description);
    void addCategory(Category category);
    void removeCategory(Category category);

    // Accessors
    String getTitle();
    String getDescription();
    ParseQuery<ParseObject> getCategories();
}
