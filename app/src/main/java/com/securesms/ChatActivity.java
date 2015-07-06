package com.securesms;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.telephony.SmsManager;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.securesms.items.MessageItem;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatActivity extends ListActivity {
    private DbAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;

    ImageButton btn_send;
    EditText et_message;
    String nick;
    String number;

    int recId;

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
            nick=bundle.getString("nick");
            number=bundle.getString("number");
            getActionBar().setTitle(nick +"\n"+number);
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

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, message, null, null);
            Toast.makeText(getApplicationContext(), "Wys³ano SMS",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "B³¹d wysy³ania, spróbuj ponownie póŸniej", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {


            MessageItem tmp = new MessageItem();
            tmp.text = et_message.getText().toString();
            tmp.rec = 1;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            tmp.date = sdf.format(new Date());
            tmp.id_receivers = recId;
            tmp.read=0;

            dbHelper.open();
            dbHelper.createRowMessage(tmp);
            dbHelper.close();
            et_message.setText("");
            displayListView();
        }
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
        dataAdapter = new SimpleCursorAdapter(this, R.layout.chat_list_view_item,
                cursor, columns, to, 0)
        {
            @Override
            public void bindView(final View view, final Context context, Cursor cursor) {
                super.bindView(view, context, cursor);
                final Context t = context;
                final Cursor c = cursor;

                TextView message = (TextView) view.findViewById(R.id.message);
                TextView date = (TextView) view.findViewById(R.id.message_date);
                int rec = cursor.getInt(cursor
                        .getColumnIndexOrThrow(DbAdapter.MES_REC));
                if (rec == 0) {
                    message.setBackgroundResource(R.drawable.message_receiver_text_view);
                    message.setGravity(Gravity.RIGHT);
                    date.setGravity(Gravity.RIGHT);
                } else {
                    message.setBackgroundResource(R.drawable.
                            message_sender_text_view);
                    message.setGravity(Gravity.LEFT);
                    date.setGravity(Gravity.LEFT);
                }

            }

            ;
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
