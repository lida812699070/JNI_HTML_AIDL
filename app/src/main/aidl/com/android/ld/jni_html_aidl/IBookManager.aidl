// IBookManager.aidl
package com.android.ld.jni_html_aidl;
import com.android.ld.jni_html_aidl.Book;
// Declare any non-default types here with import statements

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
}
