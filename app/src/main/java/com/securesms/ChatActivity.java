package com.securesms;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.telephony.SmsManager;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.securesms.items.MessageItem;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatActivity extends ListActivity {
    private DbAdapter dbHelper;

    ImageButton btn_send;
    EditText et_message;
    String nick;
    String number;

    long recId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        registerForContextMenu(getListView());

        dbHelper = new DbAdapter(this);
        btn_send = (ImageButton) findViewById(R.id.send_message);
        et_message = (EditText) findViewById(R.id.message_text);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            recId = bundle.getInt("recId");
            nick = bundle.getString("nick");
            number = bundle.getString("number");
            getActionBar().setTitle(nick + "\n" + number);

            //zaznaczenie wiadomości jako odczytanej
            dbHelper.open();
            dbHelper.setReadMessages(recId);
            dbHelper.close();
        }
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

    public void send_message(View v) {
        String message = et_message.getText().toString();
        AlgoritmAES algoritmAES = new AlgoritmAES();

        algoritmAES.send_message(number, message, getApplicationContext());
        et_message.setText("");
        displayListView();
    }

    private void displayListView() {
        dbHelper.open();
        Cursor cursor = dbHelper.searchRowMessageRec(recId);
        dbHelper.close();
        // The desired columns to be bound
        String[] columns = new String[]{DbAdapter.MES_TEXT, DbAdapter.MES_DATE};

        // the XML defined views which the data will be bound to
        int[] to = new int[]{R.id.message, R.id.message_date};

        // create the adapter using the cursor pointing to the desired data
        // as well as the layout information
        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, R.layout.chat_list_view_item,
                cursor, columns, to, 0) {
            @Override
            public void bindView(final View view, final Context context, Cursor cursor) {
                super.bindView(view, context, cursor);

                TextView message = (TextView) view.findViewById(R.id.message);
                TextView date = (TextView) view.findViewById(R.id.message_date);
                int rec = cursor.getInt(cursor
                        .getColumnIndexOrThrow(DbAdapter.MES_REC));
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)message.getLayoutParams();

                if (rec == 0) {
                    message.setBackgroundResource(R.drawable.message_receiver_text_view);
                    message.setGravity(Gravity.RIGHT);
                    date.setGravity(Gravity.RIGHT);

                    params.setMargins(75, 0, 5, 0); //substitute parameters for left, top, right, bottom
                    message.setLayoutParams(params);
                } else {
                    message.setBackgroundResource(R.drawable.
                            message_sender_text_view);
                    message.setGravity(Gravity.LEFT);
                    date.setGravity(Gravity.LEFT);

                    params.setMargins(5, 0, 75, 0); //substitute parameters for left, top, right, bottom
                    message.setLayoutParams(params);
                }

            }

        };
        // Assign adapter to ListView
        setListAdapter(dataAdapter);


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.remove_message:
                MessageItem tmp = new MessageItem();
                tmp.id = info.id;
                dbHelper.open();
                dbHelper.deleteRowMessage(tmp);
                dbHelper.close();
                displayListView();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
