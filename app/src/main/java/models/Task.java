package models;

/**
 * Created by hroshandel on 2016-02-09.
 */
public class Task {
    private String title, start, end;

    public Task() {
    }

    public Task(String title, String start, String end) {
        this.title = title;
        this.start = start;
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setGenre(String end) {
        this.end = end;
    }
}