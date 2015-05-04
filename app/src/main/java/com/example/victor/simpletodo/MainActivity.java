package com.example.victor.simpletodo;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends Activity {

    private ExpandableListAdapter itemsAdapter;
    private ExpandableListView elvItems;
    private ArrayList<Parent> listTasks;

    private Button buttonCats;
    private Button buttonAddCat;
    static final int CUSTOM_DIALOG_ID = 0;

    private Dialog dialog;

    ListView lvCats;
    ArrayAdapter<String> catsAdapter;
    String[] cats = {
            "January", "February", "March", "April",
            "May", "June", "July", "August", "September",
            "October", "November", "December"};

    ArrayList<String> categories = new ArrayList<>(Arrays.asList(cats));


    //Spinner spinCategories;
    //private ArrayAdapter<String> spinAdapter;
    private String catText = "";
    //private ArrayList<String> categories;// = {"Course", "Travail", "Shopping", "Banque", "Nouvelle catégorie"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listTasks = new ArrayList<>();

        readItems();

        setupItemsAdapter();

        setupCategoriesDialog();

        setupCategoriesButton();

        setupCategorieAddButton();

        setupListViewListener();

    }

    public void setupItemsAdapter(){
        itemsAdapter = new ExpandableListAdapter(this, listTasks);

        elvItems = (ExpandableListView) findViewById(R.id.expandableListView);
        elvItems.setAdapter(itemsAdapter);
    }

    /*public void setupCategoriesButton(){
        categories = loadCat();
        spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinCategories = (Spinner) findViewById(R.id.categories);
        spinCategories.setAdapter(spinAdapter);
        spinCategories.setOnItemSelectedListener(this);
    }*/

    public void setupCategoriesDialog(){
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.categories);
        dialog.setTitle("Choisir une catégorie");

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
    }

    public void setupCategoriesButton(){
        categories = loadCat();
        buttonCats = (Button)findViewById(R.id.categories);
        buttonCats.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showDialog(CUSTOM_DIALOG_ID);
            }
        });
    }

    public void setupCategorieAddButton(){
        categories = loadCat();
        buttonAddCat = (Button)dialog.findViewById(R.id.btnAddCat);
        buttonAddCat.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean isAdded = onAddCat(v);
                if (isAdded) dismissDialog(CUSTOM_DIALOG_ID);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        switch(id) {
            case CUSTOM_DIALOG_ID:

                dialog.setOnCancelListener(new DialogInterface.OnCancelListener(){

                    @Override
                    public void onCancel(DialogInterface d) {
                        EditText catName = (EditText) dialog.findViewById(R.id.catName);
                        catName.setText("");

                        /*Toast.makeText(MainActivity.this,
                                "OnCancelListener",
                                Toast.LENGTH_LONG).show();*/
                    }
                });

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener(){

                    @Override
                    public void onDismiss(DialogInterface d) {
                        EditText catName = (EditText) dialog.findViewById(R.id.catName);
                        catName.setText("");

                        /*Toast.makeText(MainActivity.this,
                                "OnDismissListener",
                                Toast.LENGTH_LONG).show();*/
                    }
                });

                //Prepare ListView in dialog
                lvCats = (ListView)dialog.findViewById(R.id.categoriesList);
                catsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
                lvCats.setAdapter(catsAdapter);
                lvCats.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        /*Toast.makeText(MainActivity.this,
                                parent.getItemAtPosition(position).toString() + " clicked",
                                Toast.LENGTH_LONG).show();*/
                        catText = parent.getItemAtPosition(position).toString();
                        buttonCats.setText(catText);
                        dismissDialog(CUSTOM_DIALOG_ID);
                    }
                });
                lvCats.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                                   int position, long id) {

                        /*Toast.makeText(MainActivity.this,
                                parent.getItemAtPosition(position).toString() + " long clicked",
                                Toast.LENGTH_LONG).show();*/

                        catText = "";
                        buttonCats.setText("Catégorie");
                        categories.remove(position);
                        catsAdapter.notifyDataSetChanged();
                        saveCat();
                        return false;
                    }
                });

                break;
        }

        return dialog;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog, Bundle bundle) {
        super.onPrepareDialog(id, dialog, bundle);

        switch(id) {
            case CUSTOM_DIALOG_ID:
                //
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupListViewListener() {
        elvItems.setOnGroupClickListener(
                new ExpandableListView.OnGroupClickListener() {

                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v,
                                                int groupPosition, long id) {
                    /* Toast.makeText(getApplicationContext(),
                     "Group Clicked " + listDataHeader.get(groupPosition),
                     Toast.LENGTH_SHORT).show();*/
                        return false;
                    }
                }
        );

        // Listview Group expanded listener
        elvItems.setOnGroupExpandListener(
                new ExpandableListView.OnGroupExpandListener() {

                    @Override
                    public void onGroupExpand(int groupPosition) {
                    /*Toast.makeText(getApplicationContext(),
                            listTasks.get(groupPosition).getGroupName() + " Expanded",
                            Toast.LENGTH_SHORT).show();*/
                    }
                }
        );

        // Listview Group collasped listener
        elvItems.setOnGroupCollapseListener(
                new ExpandableListView.OnGroupCollapseListener() {

                    @Override
                    public void onGroupCollapse(int groupPosition) {
                    /*Toast.makeText(getApplicationContext(),
                            listTasks.get(groupPosition).getGroupName() + " Collapsed",
                            Toast.LENGTH_SHORT).show();*/

                    }
                }
        );

        // Listview on child click listener
        elvItems.setOnChildClickListener(
                new ExpandableListView.OnChildClickListener() {

                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                        String groupName = listTasks.get(groupPosition).getGroupName();
                        String childName = listTasks.get(groupPosition).getChildren().get(childPosition).getChildName();

                    /*Toast.makeText(
                            getApplicationContext(),
                            groupName + " : " + childName, Toast.LENGTH_SHORT)
                            .show();*/

                        // Launching new Activity on selecting single List Item
                        Intent i = new Intent(getApplicationContext(), Child.class);
                        // sending data to new activity
                        i.putExtra("task", childName);
                        startActivity(i);

                        return false;
                    }
                }
        );

        elvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View view, int pos, long id) {

                        if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                            int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                            int childPosition = ExpandableListView.getPackedPositionChild(id);
                            listTasks.get(groupPosition).getChildren().remove(childPosition);
                            if (listTasks.get(groupPosition).getChildren().size()==0) listTasks.remove(groupPosition);
                            itemsAdapter.notifyDataSetChanged();

                            writeItems();
                            return true;
                        } else if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                            int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                            listTasks.remove(groupPosition);
                            itemsAdapter.notifyDataSetChanged();

                            writeItems();
                            return true;
                        }

                        return false;
                    }
                }
        );

        // listening to single list item on click
        elvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        // selected item
                        String task = ((TextView) view).getText().toString();

                        // Launching new Activity on selecting single List Item
                        Intent i = new Intent(getApplicationContext(), Group.class);
                        // sending data to new activity
                        i.putExtra("task", task);
                        startActivity(i);

                    }
                }
        );
    }

    /*private void setupSpinnerListener() {
        spinCategories.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View view, int pos, long id) {

                        categories.remove(pos);
                        spinAdapter.notifyDataSetChanged();

                        return true;
                    }
                }
        );
    }*/

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        Child child = new Child(itemText);
        if (!(itemText.equals("") || catText.equals(""))) {
            itemsAdapter.addItem(child, catText);
            itemsAdapter.notifyDataSetChanged();
            etNewItem.setText("");
            writeItems();
        }
    }

    public boolean onAddCat(View v){
        EditText catName = (EditText) dialog.findViewById(R.id.catName);
        catText = catName.getText().toString();
        if (!catText.equals("") && !categories.contains(catText)) {
            categories.add(catText);
            catName.setText("");
            saveCat();
            catsAdapter.notifyDataSetChanged();
            buttonCats.setText(catText);
            return true;
        } else return false;
    }


    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager().beginTransaction(), "datePicker");
    }

    public void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo");

        FileInputStream fis;
        try {
            fis = new FileInputStream(todoFile);
            ObjectInputStream oi = new ObjectInputStream(fis);
            listTasks = (ArrayList<Parent>) oi.readObject();
            oi.close();
        } catch (FileNotFoundException e) {
            Log.e("InternalStorage", e.getMessage());
        } catch (IOException e) {
            Log.e("InternalStorage", e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("InternalStorage", e.getMessage());
        }
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

    public boolean saveCat(){
        String[] cat = categories.toArray(new String[categories.size()]);
        return saveArray(cat, "categories", getApplicationContext());
    }

    public ArrayList<String> loadCat(){
        return new ArrayList<>(Arrays.asList(loadArray("categories", getApplicationContext())));
    }

    public boolean saveArray(String[] array, String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("categories", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.putInt(arrayName +"_size", array.length);
        for(int i=0;i<array.length;i++)
            editor.putString(arrayName + "_" + i, array[i]);
        return editor.commit();
    }

    public String[] loadArray(String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("categories", 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        String array[] = new String[size];
        for(int i=0;i<size;i++)
            array[i] = prefs.getString(arrayName + "_" + i, null);
        return array;
    }

    /*@Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        spinCategories.setSelection(pos);
        selCat = (String) spinCategories.getSelectedItem();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }*/
}
