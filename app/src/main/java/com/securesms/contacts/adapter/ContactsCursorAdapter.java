package com.securesms.contacts.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;

import com.securesms.R;
import com.securesms.contacts.presenter.interfaces.ContactsPresenter;
import com.securesms.contacts.view.interfaces.ContactsView;
import com.securesms.database.DbAdapter;
import com.securesms.contacts.model.ReceiverUserModel;

/**
 * Created by admin on 2016-06-05.
 */
public class ContactsCursorAdapter extends SimpleCursorAdapter {
    private final Context mContext;
    private final ContactsPresenter mPresenter;
    private final ContactsView mView;

    public ContactsCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, ContactsPresenter contactsPresenter, ContactsView view) {
        super(context, layout, c, from, to, flags);
        this.mContext = context;
        this.mPresenter = contactsPresenter;
        this.mView = view;
    }

    @Override
    public void bindView(final View view, Context context, final Cursor cursor) {
        super.bindView(view, context, cursor);

        ImageButton removeReceive = (ImageButton) view.findViewById(R.id.deleteReceiver);
        ImageButton editReceive = (ImageButton) view.findViewById(R.id.editReceiver);
        final int position = cursor.getInt(cursor.getColumnIndex(DbAdapter.REC_ID));

        removeReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setPositiveButton(mContext.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ReceiverUserModel receiverUserModel = new ReceiverUserModel();
                        receiverUserModel.setId(position);

                        mPresenter.removeContact(receiverUserModel);
                    }
                });
                builder.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder.setMessage(mContext.getString(R.string.conf_remove_receiver));

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        editReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the state's capital from this row in the database.
                String number = cursor.getString(cursor
                        .getColumnIndexOrThrow(DbAdapter.REC_NUMBER));
                String nick = cursor.getString(cursor
                        .getColumnIndexOrThrow(DbAdapter.REC_NAME));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter.REC_CODE));

                ReceiverUserModel receiverUserModel = new ReceiverUserModel();
                receiverUserModel.setNumber(number);
                receiverUserModel.setName(nick);
                receiverUserModel.setPassword(password);

                mView.showAlertView(receiverUserModel);
            }
        });
    }
}
