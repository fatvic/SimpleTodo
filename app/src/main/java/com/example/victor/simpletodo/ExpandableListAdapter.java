package com.example.victor.simpletodo;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class ExpandableListAdapter extends BaseExpandableListAdapter {
    LayoutInflater inflater;

    private ArrayList<Group> groups;

    public ExpandableListAdapter(Context context,ArrayList<Group> groups) {
        super();
        this.groups = groups;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(Child child, String groupName){
        int size = groups.size();
        boolean found = false;
        int i;
        for (i = 0; !found && i < size; i++){
            if (groups.get(i).getGroupName().equals(groupName)) found = true;
        }
        if (found) {
            groups.get(i - 1).addChild(child);
        }
        else {
            Group group = new Group(groupName);
            group.addChild(child);
            groups.add(group);
        }
    }

    public boolean addGroup(String parentName){
        int size = groups.size();
        boolean found = false;
        int i;
        for (i = 0; !found && i < size; i++){
            if (groups.get(i).getGroupName().equals(parentName)) found = true;
        }
        if (found) {
            return false;
        }
        else {
            Group group = new Group(parentName);
            groups.add(group);
            return true;
        }
    }

    public boolean addGroup (Group group) {
        int size = groups.size();
        boolean found = false;
        int i;
        for (i = 0; !found && i < size; i++){
            if (groups.get(i).getGroupName().equals(group.getGroupName())) found = true;
        }
        if (found) {
            return false;
        }
        else {
            groups.add(group);
            return true;
        }
    }

    public Child getChild(int groupPosition, int childPosition) {
        ArrayList<Child> children= groups.get(groupPosition).getChildren();
        return children.get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<Child> children= groups.get(groupPosition).getChildren();
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
        childName.setText(child.getCloudChildName());

        if(child.isCompleted()){
            childName.setPaintFlags(childName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else{
            childName.setPaintFlags(childName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        return convertView;

    }

    public Group getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    public int getGroupCount() {
        return groups.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {
        TextView groupName;
        Group group = getGroup(groupPosition);
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