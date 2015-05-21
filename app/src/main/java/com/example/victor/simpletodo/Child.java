package com.example.victor.simpletodo;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@ParseClassName("Tasks")
public class Child extends ParseObject implements Serializable, Parcelable {

    static final long serialVersionUID =-1903057014897953592L;
    private String parentName;
    private String childName;
    private String comment;
    private List<String> tasks;
    private String date;
    private boolean completed;

    public Child() {
        super();
        this.parentName = new String();
        this.childName = new String();
        this.comment = new String();
        this.tasks = new ArrayList<>();
        this.date = new String();
        this.completed = false;
    }

    public Child(String childName, String parentName) {
        super();
        this.parentName = parentName;
        this.childName = childName;
        this.comment = new String();
        this.tasks = new ArrayList<>();
        this.date = new String();
        this.completed = false;
    }

    public String getChildName(Boolean online) {
        if (online) return getString("childName");
        else return childName;
    }
    public void setChildName(String childName, Boolean online) {
        if (online) put("childName", childName);
        else this.childName = childName;
    }
    public String getParentName(Boolean online) {
        if (online) return getString("parentName");
        else return parentName;
    }
    public void setParentName(String parentName, Boolean online) {
        if (online) put("parentName", parentName);
        else this.parentName = parentName;
    }
    public String getComment(Boolean online) {
        if (online) return getString("comment");
        else return comment;
    }
    public void setComment(String comment, Boolean online) {
        if (online) put("comment", comment);
        else this.comment = comment;
    }
    public List<String> getTasks(Boolean online) {
        if (online) return getList("subTasks");
        else return tasks;
    }
    public void setTasks(List<String> tasks, Boolean online) {
        if (online) put("subTasks", tasks);
        else this.tasks = tasks;
    }
    public void addTask(String task, Boolean online) {
        if (online) addUnique("subTasks", task);
        else tasks.add(task);
    }
    public String getDate(Boolean online) {
        if (online) return getString("date");
        else return date;
    }
    public void setDate(String date, Boolean online) {
        if (online) put("date", date);
        else this.date = date;
    }
    public boolean isCompleted(Boolean online) {
        if (online) return getBoolean("completed");
        else return completed;
    }
    public void setCompleted(Boolean completed, Boolean online) {
        if (online) put("completed", completed);
        else this.completed = completed;
    }


    public Child(Parcel in){
        this();
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(childName);
        dest.writeString(comment);
        dest.writeStringList(tasks);
    }

    public void readFromParcel(Parcel in){
        this.childName = in.readString();
        this.comment = in.readString();
        in.readStringList(this.tasks);
    }

    public static final Parcelable.Creator<Child> CREATOR = new Creator<Child>(){

        @Override
        public Child createFromParcel(Parcel source) {
            return new Child(source);
        }

        @Override
        public Child[] newArray(int size) {
            return new Child[size];
        }
    };

}