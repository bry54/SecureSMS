package com.securesms.contacts.presenter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.securesms.contacts.model.ContactModel;
import com.securesms.contacts.presenter.interfaces.ContactsPresenter;
import com.securesms.contacts.view.interfaces.ContactsView;
import com.securesms.database.DbAdapter;
import com.securesms.contacts.model.ReceiverUserModel;

/**
 * Created by admin on 2016-06-05.
 */
public class ContactsPresenterImpl implements ContactsPresenter {
    private ContactsView view;

    private final DbAdapter dbAdapter = DbAdapter.INSTANCE;


    public ContactsPresenterImpl() {
    }

    @Override
    public void takeView(ContactsView view) {
        this.view = view;
        displayListView();
    }

    @Override
    public void releaseView() {
        this.view = null;
    }

    private void displayListView() {
        dbAdapter.open();
        Cursor cursor = dbAdapter.readListReceivers();
        dbAdapter.close();

        if (view != null) {
            view.setContacts(cursor);
        }
    }

    public ContactModel getContact(Context context, Uri uri) {
        ContactModel contactModel = new ContactModel();
        contactModel.setNumber(retrieveContactNumber(context, uri));
        contactModel.setName(retrieveContactName(context, uri));
        return contactModel;
    }

    @Override
    public void saveContact(ReceiverUserModel model) {
        if (validateModel(model)) {
            dbAdapter.open();
            dbAdapter.createRowReceiver(model);
            dbAdapter.close();

            displayListView();
        }
    }

    @Override
    public void removeContact(ReceiverUserModel model) {
        if (model.getId() != null) {
            dbAdapter.open();
            dbAdapter.deleteRowReceiver(model);
            dbAdapter.close();

            displayListView();
        }
    }

    @Override
    public void updateContact(ReceiverUserModel model) {
        if (validateModel(model)) {
            dbAdapter.open();
            dbAdapter.updateRowReceive(model);
            dbAdapter.close();

            displayListView();
        }
    }

    private boolean validateModel(ReceiverUserModel model) {
        if (model.getName().length() == 0) {
            view.failAddContact("Nieprawidłowa nazwa");
            return false;
        }
        if (model.getPassword().length() == 0) {
            view.failAddContact("Nieprawidłowy kod szyfrowania");
            return false;
        }
        if (model.getNumber().length() <= 8) {
            view.failAddContact("Nieprawidłowy numer");
            return false;
        }
        return true;
    }

    private String retrieveContactNumber(Context context, Uri uriContact) {
        String contactNumber = "";

        // We only need the NUMBER column, because there will be only one row in the result
        String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};

        // Perform the query on the contact to get the NUMBER column
        // We don't need a selection or sort order (there's only one result for the given URI)
        // CAUTION: The query() method should be called from a separate thread to avoid blocking
        // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
        // Consider using CursorLoader to perform the query.
        Cursor cursor = context.getContentResolver()
                .query(uriContact, null, null, null, null);
        cursor.moveToFirst();

        // Retrieve the phone number from the NUMBER column
        int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        contactNumber = cursor.getString(column);

        contactNumber = contactNumber.replace(" ", "");
        contactNumber = contactNumber.replace("+48", "");
        return contactNumber;
    }


    private String retrieveContactName(Context context, Uri uriContact) {
        String contactName = null;

        // querying contact data store
        Cursor cursor = context.getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        cursor.close();
        return contactName;
    }
}
