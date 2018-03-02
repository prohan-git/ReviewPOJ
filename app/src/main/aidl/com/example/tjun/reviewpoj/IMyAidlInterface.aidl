// IMyAidlInterface.aidl
package com.example.tjun.reviewpoj;
import com.example.tjun.reviewpoj.Book;
// Declare any non-default types here with import statements

interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);


     void addBook(in Book book);
     List<Book> getBoolList();
}
