package com.securesms.main.presenter;

import android.database.Cursor;

import com.securesms.database.DbAdapter;
import com.securesms.main.presenter.interfaces.MainPresenter;
import com.securesms.main.view.interfaces.MainView;

/**
 * Created by admin on 2016-06-01.
 */
public class MainPresenterImpl implements MainPresenter {
    private MainView view;

    private final DbAdapter dbAdapter = DbAdapter.INSTANCE;

    public MainPresenterImpl() {
    }

    @Override
    public void takeView(MainView view) {
        this.view = view;
        getAllContacts();
    }

    @Override
    public void releaseView() {
        this.view = null;
    }

    private void getAllContacts() {
        dbAdapter.open();
        Cursor cursor = dbAdapter.readListMainMessages();
        dbAdapter.close();

        if (view != null) {
            view.setContacts(cursor);
        }
    }
}
