package com.example.tjun.reviewpoj;

import android.os.Parcel;
import android.os.Parcelable;

/****
 * </pre> 
 *  Project_Name:    ReviewPOJ
 *  Copyright: 
 *  Version:         1.0.0.1
 *  Created:         Tijun on 2018/2/28 0028 11:15.
 *  E-mail:          prohankj@outlook.com
 *  Desc: 
 * </pre>            
 ****/
public class Book implements Parcelable {
    public int bookId;
    public String bookName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.bookId);
        dest.writeString(this.bookName);
    }

    public Book() {
    }

    protected Book(Parcel in) {
        this.bookId = in.readInt();
        this.bookName = in.readString();
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
