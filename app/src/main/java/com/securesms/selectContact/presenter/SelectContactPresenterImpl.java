package com.securesms.selectContact.presenter;

import android.database.Cursor;

import com.securesms.database.DbAdapter;
import com.securesms.selectContact.presenter.interfaces.SelectContactPresenter;
import com.securesms.selectContact.view.interfaces.SelectContactView;

/**
 * Created by admin on 2016-06-05.
 */
public class SelectContactPresenterImpl implements SelectContactPresenter {
    private SelectContactView view;
    private final DbAdapter dbAdapter = DbAdapter.INSTANCE;

    @Override
    public void takeView(SelectContactView view) {
        this.view = view;
        getAllContacts();
    }

    @Override
    public void releaseView() {
        this.view = null;
    }


    private void getAllContacts() {
        dbAdapter.open();
        Cursor cursor = dbAdapter.readListReceivers();
        dbAdapter.close();

        if (view != null) {
            view.setContacts(cursor);
        }
    }
}
