package com.example.victor.simpletodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    LayoutInflater inflater;

    private ArrayList<Parent> parents;
    public ExpandableListAdapter(Context context,ArrayList<Parent> parents) {
        super();
        this.parents = parents;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(Child child, String groupName){
        int size = parents.size();
        boolean found = false;
        int i;
        for (i = 0; !found && i < size; i++){
            if (parents.get(i).getGroupName().equals(groupName)) found = true;
        }
        if (found) {
            parents.get(i - 1).addChild(child);
        }
        else {
            Parent parent = new Parent(groupName);
            parent.addChild(child);
            parents.add(parent);
        }
    }

    public boolean addGroup(String groupName){
        int size = parents.size();
        boolean found = false;
        int i;
        for (i = 0; !found && i < size; i++){
            if (parents.get(i).getGroupName().equals(groupName)) found = true;
        }
        if (found) {
            return false;
        }
        else {
            Parent parent = new Parent(groupName);
            parents.add(parent);
            return true;
        }
    }

    public Child getChild(int groupPosition, int childPosition) {
        ArrayList<Child> children= parents.get(groupPosition).getChildren();
        return children.get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<Child> children= parents.get(groupPosition).getChildren();
        return children.size();
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        Child child= getChild(groupPosition,childPosition);
        TextView childName;
        if(convertView==null) {
            convertView=inflater.inflate(R.layout.child_view, null);
        }
        childName=(TextView) convertView.findViewById(R.id.textViewChildName);
        childName.setText(child.getChildName());
        return convertView;
    }

    public Parent getGroup(int groupPosition) {
        return parents.get(groupPosition);
    }

    public int getGroupCount() {
        return parents.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {
        TextView groupName;
        Parent group = getGroup(groupPosition);
        if(convertView==null) {
            convertView=inflater.inflate(R.layout.group_view, null);
        }
        groupName=(TextView) convertView.findViewById(R.id.textViewGroupName);
        groupName.setText(group.getGroupName());
        return convertView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }
}