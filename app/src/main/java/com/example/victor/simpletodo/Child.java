package com.example.victor.simpletodo;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.io.Serializable;
import java.util.ArrayList;

//Annotation qui défini le nom de la table dans Parse
@ParseClassName("Child")
public class Child extends ParseObject implements Serializable, Parcelable {

    private static int ID = 0;
    static final long serialVersionUID =-1903057014897953592L;
    private int childId;
    private String childName;
    private String comment;
    private ArrayList<String> tasks;
    private String date;
    private boolean isCompleted;

    public Child() {
        super();
        this.childId = ID++;
        this.childName = new String();
        this.comment = new String();
        this.tasks = new ArrayList<>();
        this.date = new String();
        this.isCompleted = false;
    }

    public Child(String childName) {
        super();
        this.childId = ID++;
        this.childName = childName;
        this.comment = new String();
        this.tasks = new ArrayList<>();
        this.date = new String();
        this.isCompleted = false;
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
        put("name", childName);
        this.childName = childName;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
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
        dest.writeString(comment);
        dest.writeStringList(tasks);
    }

    public void readFromParcel(Parcel in){
        this.childId = in.readInt();
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

    public boolean isCompleted(){
        return getBoolean("completed");
    }
    public void setCompleted(boolean complete){
        this.isCompleted = complete;
        put("completed", complete);
    }

    /*public String getName(){
        return getString("name");
    }
    public void setName(String name){
        put("name", name);
    }*/

}