package com.example.victor.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class Parent extends Activity implements Serializable {
    static private int ID=0;
    static final long serialVersionUID =-1903057014897953692L;
    private int groupId;
    private String groupName;
    private ArrayList<Child> children;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.single_list_item_view);

        TextView txtLabel = (TextView) findViewById(R.id.task_label);
        //TextView txtComment = (TextView) findViewById(R.id.task_comment);

        Intent i = getIntent();
        // getting attached intent data
        String label = i.getStringExtra("task");
        // displaying selected product name
        txtLabel.setText(label);

    }

    public Parent(){
        this.groupId = ID++;
        this.groupName = new String();
        this.children = new ArrayList<>();
    }

    public Parent(String groupName,
                  ArrayList<Child> children) {
        super();
        this.groupId = ID++;
        this.groupName = groupName;
        this.children = children;
    }

    public Parent(String groupName) {
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


}