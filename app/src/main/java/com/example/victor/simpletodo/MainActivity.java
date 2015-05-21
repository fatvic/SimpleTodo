package com.example.victor.simpletodo;

import android.app.Activity;
import android.app.Dialog;
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

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends Activity {



    private ExpandableListAdapter itemsAdapter;
    private ExpandableListView elvItems;
    private ArrayList<Group> listTasks = new ArrayList<>();

    static final int CUSTOM_DIALOG_ID = 0;
    private Dialog dialog;

    private Button buttonCats;
    private Button buttonAddCat;

    ArrayAdapter<String> catsAdapter;
    ListView lvCats;
    ArrayList<String> categories = new ArrayList<>();
    private String catText = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "R4QGlKuCnu2T6hJz32bgYewRF3VPNtrJAFfSk8sp", "p5F7ZtkFXRlanjmcfJfvuui0QASzg8am2N237TRV");
        ParseObject.registerSubclass(Child.class);
        ParseObject.registerSubclass(Group.class);

        //readItems();
        //loadCat();

        setupItemsAdapter();

        setupCategoriesDialog();

        loadTasks();
        loadCats();

        setupCategorieButtons();


        setupExpandableListViewListener();

    }

    public void setupItemsAdapter(){
        itemsAdapter = new ExpandableListAdapter(this, listTasks);

        elvItems = (ExpandableListView) findViewById(R.id.expandableListView);
        elvItems.setAdapter(itemsAdapter);
    }

    public void setupCategoriesDialog(){
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.categories);
        dialog.setTitle("Choisir une catégorie");

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
    }

    public void onCatClick(View v) {
        showDialog(CUSTOM_DIALOG_ID);
    }

    /*public void onAddCat(View v) {
        boolean isAdded = addCat(v);
        if (isAdded) dismissDialog(CUSTOM_DIALOG_ID);
    }*/

    public void setupCategorieButtons(){
        buttonCats = (Button)findViewById(R.id.categories);
        buttonAddCat = (Button)dialog.findViewById(R.id.btnAddCat);
        buttonAddCat.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean isAdded = addCat();
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
                        String catName = categories.get(position);

                        deleteCat(catName);
                        //deleteTasks(catName);

                        categories.remove(position);
                        catsAdapter.notifyDataSetChanged();
                        //saveCat();
                        return true;
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

    private void setupExpandableListViewListener() {
        elvItems.setOnGroupClickListener(
                new ExpandableListView.OnGroupClickListener() {

                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v,
                                                int groupPosition, long id) {

                        catText = listTasks.get(groupPosition).getGroupName();
                        buttonCats.setText(catText);

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

                        Child child = listTasks.get(groupPosition).getChildren().get(childPosition);
                        child.getObjectId();

                        Intent i = new Intent(getApplicationContext(), SingleListItem.class);
                        i.putExtra("objectId", child.getObjectId());

                        startActivity(i);

                        return false;
                    }
                }
        );

        elvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View view, int pos, long id) {
                        int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                        int childPosition = ExpandableListView.getPackedPositionChild(id);

                        if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {

                            Group group = listTasks.get(groupPosition);
                            Child child = group.getChildren().get(childPosition);

                            group.getChildren().remove(childPosition);
                            if (group.getChildren().size()==0) {
                                deleteCat(group.getGroupName());
                                listTasks.remove(groupPosition);
                            }
                            itemsAdapter.notifyDataSetChanged();

                            child.deleteEventually();

                            //writeItems();
                            return true;
                        } else if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {

                            Group group = listTasks.get(groupPosition);
                            ArrayList<Child> children = group.getChildren();
                            listTasks.remove(groupPosition);
                            itemsAdapter.notifyDataSetChanged();

                            deleteCat(group.getGroupName());

                            for (Child child : children) {
                                child.deleteEventually();
                            }

                            //writeItems();
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
                        /*String task = ((TextView) view).getText().toString();

                        // Launching new Activity on selecting single List Item
                        Intent i = new Intent(getApplicationContext(), Group.class);
                        // sending data to new activity
                        i.putExtra("task", task);
                        startActivity(i);*/

                    }
                }
        );
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String childName = etNewItem.getText().toString();
        if (!(childName.equals("") || catText.equals(""))) {
            Child child = new Child(childName, catText);
            child.setCloudChildName(childName);
            child.setCloudParentName(catText);
            child.setCloudCompleted(false);
            child.setCloudComment(new String());
            child.setCloudDate(new String());
            child.setCloudTasks(new ArrayList<String>());
            child.saveEventually();
            etNewItem.setText("");
            itemsAdapter.addItem(child, catText);
            itemsAdapter.notifyDataSetChanged();
            //writeItems();
        }
    }

    public boolean addCat(){
        EditText catName = (EditText) dialog.findViewById(R.id.catName);
        catText = catName.getText().toString();
        if (!catText.equals("") && !categories.contains(catText)) {
            Group cat = new Group();
            cat.setGroupName(catText);
            cat.saveEventually();
            categories.add(catText);
            catName.setText("");
            //saveCat();
            catsAdapter.notifyDataSetChanged();
            buttonCats.setText(catText);
            //listTasks.add(cat);
            return true;
        } else return false;
    }

    public void deleteCat(String catName) {
        ParseQuery<Group> query = ParseQuery.getQuery(Group.class);
        query.whereEqualTo("groupName", catName);
        query.getFirstInBackground(new GetCallback<Group>() {
            public void done(Group cat, ParseException e) {
                if (cat == null) {
                    Log.d("category", "The getFirst request failed.");
                } else {
                    cat.deleteEventually();
                    Log.d("category", "Retrieved and deleted the object.");
                }
            }
        });
    }

    public void deleteTasks(String catName) {
        ParseQuery<Child> childrenQuery = ParseQuery.getQuery(Child.class);
        childrenQuery.whereEqualTo("parentName", catName);
        childrenQuery.findInBackground(new FindCallback<Child>() {
            public void done(List<Child> children, ParseException e) {
                if (e == null) {
                    for (Child child : children) {
                        child.deleteEventually();
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo");

        FileInputStream fis;
        try {
            fis = new FileInputStream(todoFile);
            ObjectInputStream oi = new ObjectInputStream(fis);
            listTasks = (ListTasks) oi.readObject();
            /*Toast.makeText(MainActivity.this,
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

        //updateData();

    }

    public boolean saveCat(){
        String[] cat = categories.toArray(new String[categories.size()]);
        return saveArray(cat, "categories", getApplicationContext());
    }

    public void loadCat(){
        categories =  new ArrayList<>(Arrays.asList(loadArray("categories", getApplicationContext())));
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

    public void loadCats() {

        ParseQuery<Group> groupsQuery = ParseQuery.getQuery(Group.class);
        groupsQuery.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> groups, ParseException error) {
                if (groups != null) {
                    for (final Group cat : groups) {
                        final String catName = cat.getGroupName();
                        categories.add(catName);
                    }
                }
            }
        });
    }

    public void loadTasks() {

        ParseQuery<Child> childrenQuery = ParseQuery.getQuery(Child.class);
        childrenQuery.findInBackground(new FindCallback<Child>() {
            public void done(List<Child> children, ParseException e) {
                if (e == null) {
                    if (children.size() > 0) {
                        for (Child child : children) {
                            itemsAdapter.addItem(child, child.getCloudParentName());
                            itemsAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void signUp(String userName, String passWord, String email) {
        ParseUser user = new ParseUser();
        user.setUsername(userName);
        user.setPassword(passWord);
        user.setEmail(email);

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });
    }

    public void logIn(String userName, String passWord){
        ParseUser.logInInBackground(userName, passWord, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                }
            }
        });
    }
}

