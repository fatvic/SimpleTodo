package com.example.victor.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SingleListItem extends Activity {
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
}
