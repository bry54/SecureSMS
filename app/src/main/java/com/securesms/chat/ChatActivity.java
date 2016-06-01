package com.securesms.chat;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;

import com.securesms.utils.AlgoritmAES;
import com.securesms.database.DbAdapter;
import com.securesms.R;
import com.securesms.items.MessageItem;

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
 
        // create the adapter using the cursor pointing to the desired data
        // as well as the layout information
        ChatCursorAdapter dataAdapter = new ChatCursorAdapter(ChatActivity.this,cursor,0);
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
