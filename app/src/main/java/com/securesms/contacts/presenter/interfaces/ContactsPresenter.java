package com.securesms.contacts.presenter.interfaces;

import android.content.Context;
import android.net.Uri;

import com.securesms.contacts.model.ContactModel;
import com.securesms.contacts.model.ReceiverUserModel;
import com.securesms.contacts.view.interfaces.ContactsView;

/**
 * Created by admin on 2016-06-05.
 */
public interface ContactsPresenter {
    void takeView(ContactsView view);

    void releaseView();

    ContactModel getContact(Context context, Uri uri);

    void saveContact(ReceiverUserModel model);

    void removeContact(ReceiverUserModel model);

    void updateContact(ReceiverUserModel model);
}
