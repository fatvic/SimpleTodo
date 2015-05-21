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
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

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
import java.util.List;
import java.util.Locale;

public class SingleListItem extends Activity {

    private TextView tvLabel;
    private ArrayAdapter itemsAdapter;
    private ListView lvItems;
    private EditText etComment;
    private EditText etDate;
    private DatePickerDialog dateDialog;
    private SimpleDateFormat dateFormatter;
    private List<String> subTasks;
    private ArrayList<Group> listTasks;
    private Boolean online;
    private Child task;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.single_list_item_view);

        online = getIntent().getBooleanExtra("online", false);

        if (online) {
            ParseQuery<Child> query = ParseQuery.getQuery(Child.class);
            query.getInBackground(getIntent().getStringExtra("objectId"), new GetCallback<Child>() {
                public void done(Child child, ParseException e) {
                    if (e == null) {
                        setupViews(child);
                        setupItemsAdapter(child);
                        setupListViewListener();
                        setupDate();
                    } else {
                        Toast.makeText(SingleListItem.this,
                                "ERROR PARSE",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            loadOfflineTasks();
            loadCurrentTask();
            setupViews(task);
            setupItemsAdapter(task);
            setupListViewListener();
            setupDate();
        }
    }

    public void loadCurrentTask() {
        //task = getIntent().getParcelableExtra("child");
        int childPos = getIntent().getIntExtra("childPos", 0);
        int groupPos = getIntent().getIntExtra("groupPos", 0);
        //listTasks = getIntent().getParcelableArrayListExtra("listTasks");

        //String groupName = listTasks.get(groupPos).getGroupName();
        task = listTasks.get(groupPos).getChildren().get(childPos);
    }

    public void setupViews(Child task) {

        tvLabel = (TextView) findViewById(R.id.label);
        etComment = (EditText) findViewById(R.id.comment);
        etDate = (EditText) findViewById(R.id.etDate);

        tvLabel.setText(task.getChildName(online) + " (" + task.getParentName(online) + ")");
        etComment.setText(task.getComment(online));
        etDate.setInputType(InputType.TYPE_NULL);
        etDate.setText(task.getDate(online));
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

    }

    public void setupItemsAdapter(Child task){

        //subTasks = task.getTasks(online);


        itemsAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, task.getTasks(online));

        lvItems = (ListView) findViewById(R.id.subItems);
        lvItems.setAdapter(itemsAdapter);

        //itemsAdapter.addAll(subTasks);
        itemsAdapter.notifyDataSetChanged();
    }

    public void setupListViewListener(){
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter,
                                           View item, final int pos, long id) {
            if (online) {
                ParseQuery<Child> query = ParseQuery.getQuery(Child.class);
                query.getInBackground(getIntent().getStringExtra("objectId"), new GetCallback<Child>() {
                    public void done(Child task, ParseException e) {
                        if (e == null) {
                            String subTaskName = task.getTasks(online).get(pos);
                            itemsAdapter.remove(subTaskName);
                            itemsAdapter.notifyDataSetChanged();

                            List<String> subTask = new ArrayList<>();
                            subTask.add(subTaskName);
                            task.removeAll("subTasks", subTask);
                            task.saveEventually();


                        } else {

                            Toast.makeText(SingleListItem.this,
                                    "ERROR PARSE",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                task.getTasks(online).remove(pos);
                itemsAdapter.notifyDataSetChanged();
            }
                return true;
            }
        });
    }

    private void setupDate() {

        Calendar newCalendar = Calendar.getInstance();
        dateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
            if (online) {
                ParseQuery<Child> query = ParseQuery.getQuery(Child.class);
                query.getInBackground(getIntent().getStringExtra("objectId"), new GetCallback<Child>() {
                    public void done(Child task, ParseException e) {
                        if (e == null) {

                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, monthOfYear, dayOfMonth);
                            etDate.setText(dateFormatter.format(newDate.getTime()));
                            task.setDate(dateFormatter.format(newDate.getTime()), online);
                            task.saveEventually();

                        } else {

                            Toast.makeText(SingleListItem.this,
                                    "ERROR PARSE",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etDate.setText(dateFormatter.format(newDate.getTime()));
                task.setDate(dateFormatter.format(newDate.getTime()), online);

                writeItems();
            }

            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    public void onDateClick(View v) {
        dateDialog.show();
    }

    @Override
    public void onBackPressed(){
        saveComment();
        if (!online) writeItems();
        super.onBackPressed();
    }

    public void saveComment(){
        if (online) {
            ParseQuery<Child> query = ParseQuery.getQuery(Child.class);
            query.getInBackground(getIntent().getStringExtra("objectId"), new GetCallback<Child>() {
                public void done(Child task, ParseException e) {
                    if (e == null) {

                        task.setComment(etComment.getText().toString(), online);
                        task.saveEventually();

                    } else {

                        Toast.makeText(SingleListItem.this,
                                "ERROR PARSE",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            task.setComment(etComment.getText().toString(), online);
        }
    }

    public void onAddSubItem(View v) {
        if (online) {
            ParseQuery<Child> query = ParseQuery.getQuery(Child.class);
            query.getInBackground(getIntent().getStringExtra("objectId"), new GetCallback<Child>() {
                public void done(Child task, ParseException e) {
                    if (e == null) {

                        EditText etNewSubItem = (EditText) findViewById(R.id.etNewSubItem);
                        String itemText = etNewSubItem.getText().toString();
                        if (!itemText.equals("")) {

                            task.addUnique("subTasks", itemText);
                            task.saveEventually();
                            itemsAdapter.clear();
                            itemsAdapter.addAll(task.getTasks(online));
                            itemsAdapter.notifyDataSetChanged();
                            etNewSubItem.setText("");

                        }

                    } else {

                        Toast.makeText(SingleListItem.this,
                                "ERROR PARSE",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            EditText etNewSubItem = (EditText) findViewById(R.id.etNewSubItem);
            String itemText = etNewSubItem.getText().toString();
            if (!itemText.equals("")) {
                task.getTasks(online).add(itemText);
                itemsAdapter.notifyDataSetChanged();
                etNewSubItem.setText("");
                writeItems();
            }

        }


    }

    public void loadOfflineTasks() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo");

        FileInputStream fis;
        try {
            fis = new FileInputStream(todoFile);
            ObjectInputStream oi = new ObjectInputStream(fis);
            listTasks = (ArrayList) oi.readObject();
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
        if (listTasks==null) listTasks = new ArrayList<>();
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
