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
        /*this.parentName = new String();
        this.childName = new String();
        this.comment = new String();
        this.tasks = new ArrayList<>();
        this.date = new String();
        this.completed = false;*/
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

    public String getCloudChildName() {
        return getString("childName");
    }
    public void setCloudChildName(String childName) {
        put("childName", childName);
        this.childName = childName;
    }
    public String getChildName() {
        return childName;
    }
    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getCloudParentName() {
        return getString("parentName");
    }
    public void setCloudParentName(String parentName) {
        put("parentName", parentName);
    }
    public String getParentName() {
        return parentName;
    }
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getCloudComment() {
        return getString("comment");
    }
    public void setCloudComment(String comment) {
        put("comment", comment);
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<String> getCloudTasks() {
        if (getList("subTasks")==null) { return new ArrayList<>(); }
        else { return (ArrayList)getList("subTasks"); }
    }
    public void setCloudTasks(List<String> tasks) {
        addAll("subTasks", tasks);
    }
    public void addCloudTask(String task) {
        add("subTasks", task);
    }
    public List<String> getTasks() {
        return tasks;
    }
    public void setTasks(ArrayList<String> tasks) {
        this.tasks = tasks;
    }
    public void addTask(String task) {
        tasks.add(task);
    }

    public String getCloudDate() {
        return getString("date");
    }
    public void setCloudDate(String date) {
        put("date", date);
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public boolean isCloudCompleted(){
        return getBoolean("completed");
    }
    public void setCloudCompleted(boolean complete){
        put("completed", complete);
    }
    public boolean isCompleted(){
        return completed;
    }
    public void setCompleted(boolean complete){
        this.completed = complete;
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