package com.android.ld.jni_html_aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ${lida} on 2017/3/4.
 */
public class Book implements Parcelable{
    private String name;
    private int id;

    public Book(String name, int id) {
        this.name = name;
        this.id = id;
    }

    protected Book(Parcel in) {
        name = in.readString();
        id = in.readInt();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(id);
    }
}
