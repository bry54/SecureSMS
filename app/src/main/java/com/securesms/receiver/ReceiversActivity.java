package com.securesms.receiver;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.securesms.database.DbAdapter;
import com.securesms.R;
import com.securesms.items.ReceiverItem;

public class ReceiversActivity extends ListActivity {
    private DbAdapter dbHelper;
    private Uri uriContact;
    EditText et_nick;
    EditText et_number;
    EditText et_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.receiver_list_view);
        dbHelper = new DbAdapter(this);

        displayListView();
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

    public void add_code(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.receiver_add_dialog, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton(R.string.add,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {


                                DbAdapter dbHelper = new DbAdapter(
                                        getApplicationContext());
                                dbHelper.open();
                                ReceiverItem receiver = new ReceiverItem();
                                receiver.name = et_nick.getText().toString();
                                receiver.number = et_number
                                        .getText().toString();
                                receiver.code = et_code.getText().toString();
                                dbHelper.createRowReceiver(receiver);
                                dbHelper.close();
                                Toast.makeText(getApplicationContext(),
                                        getString(R.string.added_receiver), Toast.LENGTH_SHORT)
                                        .show();
                                displayListView();
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        builder.show();
        et_nick = (EditText) dialogView.findViewById(R.id.editTextNick);
        et_number = (EditText) dialogView.findViewById(R.id.editTextNumber);
        et_code = (EditText) dialogView.findViewById(R.id.editTextPassword);
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

                uriContact = data.getData();

                String name = retrieveContactName();
                String number = retrieveContactNumber();
                et_nick.setText(name);

                number = number.replace(" ", "");
                number = number.replace("+48", "");
                et_number.setText(number);
            }
        }
    }

    private String retrieveContactName() {

        String contactName = null;

        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        cursor.close();
        return contactName;
    }

    private String retrieveContactNumber() {

        String contactNumber = "";

        // We only need the NUMBER column, because there will be only one row in the result
        String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};

        // Perform the query on the contact to get the NUMBER column
        // We don't need a selection or sort order (there's only one result for the given URI)
        // CAUTION: The query() method should be called from a separate thread to avoid blocking
        // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
        // Consider using CursorLoader to perform the query.
        Cursor cursor = getContentResolver()
                .query(uriContact, projection, null, null, null);
        cursor.moveToFirst();

        // Retrieve the phone number from the NUMBER column
        int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        contactNumber = cursor.getString(column);
        return contactNumber;
    }

    private void displayListView() {
        dbHelper.open();
        Cursor cursor = dbHelper.readListReceivers();
        dbHelper.close();
        // The desired columns to be bound
        String[] columns = new String[]{DbAdapter.REC_NAME, DbAdapter.REC_NUMBER,DbAdapter.REC_CODE};

        // the XML defined views which the data will be bound to
        int[] to = new int[]{R.id.receiverName, R.id.receiverNumber, R.id.receiverPassword};

        // create the adapter using the cursor pointing to the desired data
        // as well as the layout information
        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, R.layout.receiver_list_view_item,
                cursor, columns, to, 0) {
            @Override
            public void bindView(final View view, final Context context, final Cursor cursor) {
                super.bindView(view, context, cursor);

                ImageButton removeReceive = (ImageButton) view.findViewById(R.id.deleteReceiver);
                ImageButton editReceive = (ImageButton) view.findViewById(R.id.editReceiver);
                final int position = cursor.getInt(cursor.getColumnIndex(DbAdapter.REC_ID));

                removeReceive.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ReceiversActivity.this);
// Add the buttons
                        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dbHelper = new DbAdapter(context);
                                dbHelper.open();
                                ReceiverItem receiverItem = new ReceiverItem();
                                receiverItem.id = position;
                                dbHelper.deleteRowReceiver(receiverItem);
                                dbHelper.close();
                                displayListView();
                            }
                        });
                        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                        builder.setMessage(getString(R.string.conf_remove_receiver));
// Set other dialog properties

// Create the AlertDialog
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });

                editReceive.setOnClickListener(new View.OnClickListener() {

                    // Get the state's capital from this row in the database.
                    String number = cursor.getString(cursor
                            .getColumnIndexOrThrow(DbAdapter.REC_NUMBER));
                    String nick = cursor.getString(cursor
                            .getColumnIndexOrThrow(DbAdapter.REC_NAME));
                    String password = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter.REC_CODE));

                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        // Get the layout inflater
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.receiver_edit_dialog, null);
                        // Inflate and set the layout for the dialog
                        // Pass null as the parent view because its going in the dialog layout
                        et_nick = (EditText) dialogView.findViewById(R.id.editTextNick);
                        et_number = (EditText) dialogView.findViewById(R.id.editTextNumber);
                        et_code = (EditText) dialogView.findViewById(R.id.editTextPassword);

                        //ustawianie pol
                        et_nick.setText(nick);
                        et_number.setText(number);
                        et_code.setText(password);

                        builder.setView(dialogView)
                                // Add action buttons
                                .setPositiveButton(R.string.save,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {


                                                DbAdapter dbHelper = new DbAdapter(
                                                        getApplicationContext());
                                                dbHelper.open();
                                                ReceiverItem receiver = new ReceiverItem();
                                                receiver.id=position;
                                                receiver.name = et_nick.getText().toString();
                                                receiver.number = et_number
                                                        .getText().toString();
                                                receiver.code = et_code.getText().toString();
                                                dbHelper.updateRowReceive(receiver);
                                                dbHelper.close();
                                                Toast.makeText(getApplicationContext(),
                                                        getString(R.string.saved_changes_receiver), Toast.LENGTH_SHORT)
                                                        .show();
                                                displayListView();
                                            }
                                        })
                                .setNegativeButton(R.string.cancel,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        builder.show();
                    }
                });
            }
        };
        // Assign adapter to ListView
        setListAdapter(dataAdapter);

    }

}
