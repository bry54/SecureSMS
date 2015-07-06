package com.securesms;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.securesms.items.ReceiverItem;

public class ReceiversActivity extends ListActivity {
    private DbAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;

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

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.receiver_add_dialog, null))
                // Add action buttons
                .setPositiveButton(R.string.add,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Dialog dialogView = (Dialog) dialog;
                                EditText et_nick = (EditText) dialogView.findViewById(R.id.editTextNick);
                                EditText et_number = (EditText) dialogView.findViewById(R.id.editTextNumber);
                                EditText et_code = (EditText) dialogView.findViewById(R.id.editTextPassword);
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
                                        "Dodano kontakt", Toast.LENGTH_SHORT)
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

    private void displayListView() {
        dbHelper.open();
        Cursor cursor = dbHelper.readListReceivers();
        dbHelper.close();
        // The desired columns to be bound
        String[] columns = new String[]{DbAdapter.REC_NAME, DbAdapter.REC_NUMBER};

        // the XML defined views which the data will be bound to
        int[] to = new int[]{R.id.receiverName, R.id.receiverNumber};

        // create the adapter using the cursor pointing to the desired data
        // as well as the layout information
        dataAdapter = new SimpleCursorAdapter(this, R.layout.receiver_list_view_item,
                cursor, columns, to, 0) {
            @Override
            public void bindView(final View view, final Context context, Cursor cursor) {
                super.bindView(view, context, cursor);
                final Context t = context;
                final Cursor c = cursor;

                ImageButton removeReceive = (ImageButton) view.findViewById(R.id.deleteReceiver);

                removeReceive.setOnClickListener(new View.OnClickListener() {
                    private int position = c.getInt(c.getColumnIndex(DbAdapter.REC_ID));

                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ReceiversActivity.this);
// Add the buttons
                        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dbHelper = new DbAdapter(t);
                                dbHelper.open();
                                ReceiverItem receiverItem = new ReceiverItem();
                                receiverItem.id = position;
                                dbHelper.deleteRowReceiver(receiverItem);
                                dbHelper.close();
                                displayListView();
                            }
                        });
                        builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                        builder.setMessage("Czy na pewno chcesz usunac kontakt?");
// Set other dialog properties

// Create the AlertDialog
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }
        };
        final ImageButton removeReceive = (ImageButton) findViewById(R.id.deleteReceiver);
        // Assign adapter to ListView
        setListAdapter(dataAdapter);

    }

}
