package com.example.victor.simpletodo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SingleListItem extends Activity {

    private TextView tvLabel;
    private ArrayAdapter itemsAdapter;
    private ListView lvItems;
    private EditText etComment;
    private EditText etDate;
    private DatePickerDialog dateDialog;
    private SimpleDateFormat dateFormatter;
    private Child task;
    private ArrayList<Group> listTasks;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.single_list_item_view);

        readItems();

        setupViews();

        setupItemsAdapter();

        setupListViewListener();

        setupDate();

    }

    public void setupViews() {

        //task = getIntent().getParcelableExtra("task");
        int childPos = getIntent().getIntExtra("childPos", 0);
        int groupPos = getIntent().getIntExtra("groupPos", 0);
        //listTasks = getIntent().getParcelableArrayListExtra("listTasks");

        String groupName = listTasks.get(groupPos).getGroupName();
        task = listTasks.get(groupPos).getChildren().get(childPos);

        tvLabel = (TextView) findViewById(R.id.label);
        etComment = (EditText) findViewById(R.id.comment);
        etDate = (EditText) findViewById(R.id.etDate);

        tvLabel.setText(task.getChildName() + " (" + groupName + ")");
        etComment.setText(task.getComment());
        etDate.setInputType(InputType.TYPE_NULL);
        etDate.setText(task.getDate());
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

    }

    public void setupItemsAdapter(){
        itemsAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, task.getTasks());

        lvItems = (ListView) findViewById(R.id.subItems);
        lvItems.setAdapter(itemsAdapter);
    }

    public void setupListViewListener(){
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter,
                                           View item, int pos, long id) {

                task.getTasks().remove(pos);
                itemsAdapter.notifyDataSetChanged();

                return true;
            }
        });
    }

    private void setupDate() {

        Calendar newCalendar = Calendar.getInstance();
        dateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etDate.setText(dateFormatter.format(newDate.getTime()));
                task.setDate(dateFormatter.format(newDate.getTime()));

                writeItems();
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    public void onDateClick(View v) {
        dateDialog.show();
    }

    @Override
    public void onBackPressed(){
        saveComment();
        writeItems();
        super.onBackPressed();
    }

    public void saveComment(){
        task.setComment(etComment.getText().toString());
    }

    public void onAddSubItem(View v) {
        EditText etNewSubItem = (EditText) findViewById(R.id.etNewSubItem);
        String itemText = etNewSubItem.getText().toString();
        if (!itemText.equals("")) {
            task.getTasks().add(itemText);
            itemsAdapter.notifyDataSetChanged();
            etNewSubItem.setText("");
            writeItems();
        }
    }

    public void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo");

        FileInputStream fis;
        try {
            fis = new FileInputStream(todoFile);
            ObjectInputStream oi = new ObjectInputStream(fis);
            listTasks = (ListTasks) oi.readObject();
            /*Toast.makeText(SingleListItem.this,
                    "Items read !",
                    Toast.LENGTH_LONG).show();*/
            oi.close();
        } catch (FileNotFoundException e) {
            Log.e("InternalStorage", e.getMessage());
        } catch (IOException e) {
            Log.e("InternalStorage", e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("InternalStorage", e.getMessage());
        }
        if (listTasks==null) listTasks = new ListTasks();
    }

    public void writeItems() {

        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo");

        try {
            FileOutputStream fos = new FileOutputStream(todoFile);
            ObjectOutputStream of = new ObjectOutputStream(fos);
            of.writeObject(listTasks);
            of.flush();
            of.close();
            fos.close();
        }
        catch (Exception e) {
            Log.e("InternalStorage", e.getMessage());
        }

    }

}
