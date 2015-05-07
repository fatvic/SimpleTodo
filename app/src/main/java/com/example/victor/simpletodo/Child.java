package com.example.victor.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class Child extends Activity implements Serializable, Parcelable {

    private static int ID = 0;
    static final long serialVersionUID =-1903057014897953592L;
    private int childId;
    private String childName;
    private String description;
    private ArrayList<String> tasks;
    private String date;

    public Child() {
        super();
        this.childId = ID++;
        this.childName = new String();
        this.description = new String();
        this.tasks = new ArrayList<>();
        this.date = new String();
    }

    public Child(String childName) {
        super();
        this.childId = ID++;
        this.childName = childName;
        this.description = new String();
        this.tasks = new ArrayList<>();
        this.date = new String();
    }

    public int getChildId() {
        return childId;
    }
    public void setChildId(int childId) {
        this.childId = childId;
    }
    public String getChildName() {
        return childName;
    }
    public void setChildName(String childName) {
        this.childName = childName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public ArrayList<String> getTasks() {
        return tasks;
    }
    public void setTasks(ArrayList<String> tasks) {
        this.tasks = tasks;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
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
        dest.writeInt(childId);
        dest.writeString(childName);
        dest.writeString(description);
        dest.writeStringList(tasks);
    }

    public void readFromParcel(Parcel in){
        this.childId = in.readInt();
        this.childName = in.readString();
        this.description = in.readString();
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