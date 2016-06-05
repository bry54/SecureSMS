package com.securesms.contacts.view.interfaces;

import android.database.Cursor;

import com.securesms.contacts.model.ReceiverUserModel;

/**
 * Created by admin on 2016-06-05.
 */
public interface ContactsView {
    void setContacts(Cursor cursor);
    void successfulAddContact();
    void failAddContact(String error);
    void showAlertView(ReceiverUserModel model);
}
