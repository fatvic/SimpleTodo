package com.example.victor.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class Group extends Activity implements Serializable, Parcelable {

    static private int ID=0;
    static final long serialVersionUID =-1903057014897953692L;
    private int groupId;
    private String groupName;
    private ArrayList<Child> children;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.single_list_item_view);

        TextView txtLabel = (TextView) findViewById(R.id.label);
        //TextView txtComment = (TextView) findViewById(R.id.task_comment);

        Intent i = getIntent();
        // getting attached intent data
        String label = i.getStringExtra("task");
        // displaying selected product name
        txtLabel.setText(label);

    }

    public Group(){
        this.groupId = ID++;
        this.groupName = new String();
        this.children = new ArrayList<>();
    }

    public Group(String groupName,
                 ArrayList<Child> children) {
        super();
        this.groupId = ID++;
        this.groupName = groupName;
        this.children = children;
    }

    public Group(String groupName) {
        super();
        this.groupId = ID++;
        this.groupName = groupName;
        this.children = new ArrayList<>();
    }

    public int getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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
        dest.writeInt(groupId);
        dest.writeString(groupName);
        dest.writeTypedList(children);
    }

    public void readFromParcel(Parcel in){
        this.groupId = in.readInt();
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