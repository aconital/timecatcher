package com.cpsc.timecatcher.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by yutongluo on 2/5/16.
 */

@ParseClassName("Category")
public class Category extends ParseObject {
    public String getTitle() {
        return getString("title");
    }

    public void setTitle(String title) {
        put("title", title);
    }

    public String getIconPath() {
        return getString("iconPath");
    }

    public void setIconPath(String iconPath) {
        put("iconPath", iconPath);
    }

    public ParseUser getUser() {
        return getParseUser("user");
    }

    public void setUser(ParseUser value) {
        if (value != null)
            put("user", value);
    }

    public static ParseQuery<Category> getQuery() {
        return ParseQuery.getQuery(Category.class);
    }
}
