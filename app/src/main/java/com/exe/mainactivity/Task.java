package com.exe.mainactivity;

import java.util.Date;
import java.util.UUID;

public class Task {
    private UUID id;
    private String name;
    private Date date;
    private boolean done;
    private Category category;

    public Task (){
        id = UUID.randomUUID();
        date = new Date();
        category = Category.HOME;
    }

    public UUID getId(){
        return id;
    }
    public void setTaskName(String taskName) {

        name = taskName;

    }
    public String getName() { return name; }

    public Date getDate() {
        return date;
    }


    public boolean isDone() {
        return done;
    }

    public void setDone(boolean isChecked) {
        done = isChecked;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
