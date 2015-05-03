package com.example.victor.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.io.Serializable;

public class Child extends Activity implements Serializable {

    private static int ID = 0;
    private int childId;
    private String childName;

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

    public Child() {
        super();
        this.childId = ID++;
        this.childName = new String();
    }

    public Child(String childName) {
        super();
        this.childId = ID++;
        this.childName = childName;
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
}