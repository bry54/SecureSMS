package com.securesms.contacts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.securesms.R;
import com.securesms.chat.ChatActivity;
import com.securesms.chat.model.UserModel;
import com.securesms.contacts.adapter.ContactsCursorAdapter;
import com.securesms.contacts.model.ContactModel;
import com.securesms.contacts.model.ReceiverUserModel;
import com.securesms.contacts.presenter.ContactsPresenterImpl;
import com.securesms.contacts.presenter.interfaces.ContactsPresenter;
import com.securesms.contacts.view.interfaces.ContactsView;
import com.securesms.database.DbAdapter;
import com.securesms.main.MainActivity;

public class ContactsActivity extends Activity implements ContactsView {
    private ListView mListView;
    private TextView mEmpty;
    private ImageButton mButtonAdd;

    private EditText mDialogNick;
    private EditText mDialogNumber;
    private EditText mDialogPassword;

    private boolean mResultNumber = false;

    private final ContactsPresenter mPresenter = new ContactsPresenterImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.receiver_list_view);
        mListView = (ListView) findViewById(R.id.list_contacts);
        mEmpty = (TextView) findViewById(R.id.empty);
        mButtonAdd = (ImageButton) findViewById(R.id.buttonReceiveAdd);
        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogAdd(null);
            }
        });

        mResultNumber = getIntent().getBooleanExtra(MainActivity.USER_SELECT, false);
        if (mResultNumber) {
            mButtonAdd.setVisibility(View.GONE);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Cursor cursor = (Cursor) mListView.getItemAtPosition(i);
                    int recId= cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter.REC_ID));
                    String nick = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter.REC_NAME));
                    String number = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter.REC_NUMBER));
                    UserModel userModel = new UserModel(recId,nick,number);

                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                    intent.putExtra(MainActivity.USER_EXTRA, userModel);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.takeView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.releaseView();
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                    NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void alertDialogAdd(final ReceiverUserModel model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.receiver_add_dialog, null);
        mDialogNick = (EditText) dialogView.findViewById(R.id.editTextNick);
        mDialogNumber = (EditText) dialogView.findViewById(R.id.editTextNumber);
        mDialogPassword = (EditText) dialogView.findViewById(R.id.editTextPassword);

        if (model != null) {
            mDialogNick.setText(model.getName());
            mDialogNumber.setText(model.getNumber());
            mDialogPassword.setText(model.getPassword());
        }

        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton(R.string.add,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                ReceiverUserModel receiver = new ReceiverUserModel();
                                receiver.setId(model != null ? model.getId() : null);
                                receiver.setName(mDialogNick.getText().toString());
                                receiver.setNumber(mDialogNumber
                                        .getText().toString());
                                receiver.setPassword(mDialogPassword.getText().toString());

                                if (model != null) {
                                    mPresenter.updateContact(receiver);
                                } else {
                                    mPresenter.saveContact(receiver);
                                }
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
        builder.show();
    }

    public void selectContact(View v) {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, 12);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if (requestCode == 12) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                ContactModel contactModel = mPresenter.getContact(this, data.getData());

                mDialogNick.setText(contactModel.getName());
                mDialogNumber.setText(contactModel.getNumber());
            }
        }
    }

    @Override
    public void setContacts(Cursor cursor) {
        // The desired columns to be bound
        String[] columns = new String[]{DbAdapter.REC_NAME, DbAdapter.REC_NUMBER, DbAdapter.REC_CODE};

        // the XML defined views which the data will be bound to
        int[] to = new int[]{R.id.receiverName, R.id.receiverNumber, R.id.receiverPassword};

        ContactsCursorAdapter dataAdapter = new ContactsCursorAdapter(this, R.layout.receiver_list_view_item,
                cursor, columns, to, 0, mPresenter, this, mResultNumber);

        mListView.setAdapter(dataAdapter);
        if (dataAdapter.getCount() > 0) {
            mEmpty.setVisibility(View.GONE);
        } else {
            mEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void successfulAddContact() {
        Toast.makeText(getApplicationContext(),
                getString(R.string.added_receiver), Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void failAddContactInvalidUsername() {
        Toast.makeText(getApplicationContext(),
                getString(R.string.add_contact_invalid_username), Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void failAddContactInvalidPassword() {
        Toast.makeText(getApplicationContext(),
                getString(R.string.add_contact_invalid_password), Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void failAddContactInvalidNumber() {
        Toast.makeText(getApplicationContext(),
                getString(R.string.add_contact_invalid_number), Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void showAlertView(ReceiverUserModel model) {
        alertDialogAdd(model);
    }
}
