package com.example.victor.simpletodo;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.io.Serializable;
import java.util.ArrayList;

//Annotation qui défini le nom de la table dans Parse
@ParseClassName("Categories")
public class Group extends ParseObject implements Serializable, Parcelable {

    static final long serialVersionUID =-1903057014897953692L;
    private String groupName;
    private ArrayList<Child> children;

    public Group(){
        this.groupName = new String();
        this.children = new ArrayList<>();
    }

    public Group(String groupName) {
        super();
        this.groupName = groupName;
        this.children = new ArrayList<>();
    }

    public Group(String groupName, ArrayList<Child >children) {
        super();
        this.groupName = groupName;
        this.children = children;
    }

    public String getGroupName(Boolean online) {
        if (online) return getString("groupName");
        else return groupName;
    }
    public void setGroupName(String groupName, Boolean online) {
        if (online) put("groupName", groupName);
        else this.groupName = groupName;
    }

    public ArrayList<Child> getChildren() {
        return children;
    }
    public void setChildren(ArrayList<Child> children) {
        this.children = children;
    }

    public void addChild(Child child){
        this.children.add(child);
    }

    public Group(Parcel in){
        this();
        readFromParcel(in);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(groupName);
        dest.writeTypedList(children);
    }

    public void readFromParcel(Parcel in){
        this.groupName = in.readString();
        in.readTypedList(children, Child.CREATOR);
    }

    public static final Parcelable.Creator<Group> CREATOR = new Creator<Group>(){

        @Override
        public Group createFromParcel(Parcel source) {
            return new Group(source);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };
}