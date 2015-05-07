package com.example.victor.simpletodo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ListTasks extends ArrayList<Group> implements Parcelable{

    public ListTasks(){}

    public ListTasks(Parcel in){
        this();
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in){
        this.clear();
        int size = in.readInt();
        for (int i = 0; i < size; i++){
            Group g = new Group(in);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        int size = this.size();
        dest.writeInt(size);
        for (int i = 0; i < size; i++){
            Group g = this.get(i);
            dest.writeParcelable(g, flags);
        }
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public ListTasks createFromParcel(Parcel in) {
            return new ListTasks(in);
        }

        @Override
        public ListTasks[] newArray(int size) {
            return new ListTasks[size];
        }
    };
}
