package com.securesms.main;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.securesms.DbAdapter;
import com.securesms.NewMessageActivity;
import com.securesms.R;
import com.securesms.SettingsActivity;
import com.securesms.chat.ChatActivity;
import com.securesms.chat.ChatCursorAdapter;
import com.securesms.receiver.ReceiversActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends ListActivity {
    public static int mId = 1921;
    private DbAdapter dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list_view);
        dbHelper = new DbAdapter(this);
        displayListView();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_contacts) {
            Intent intent = new Intent(getApplicationContext(), ReceiversActivity.class);
            startActivity(intent);

        }
        if (id == R.id.action_new_message) {
            Intent intent = new Intent(getApplicationContext(), NewMessageActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
        Cursor cursor = (Cursor) list.getItemAtPosition(position);

        // Get the state's capital from this row in the database.
        int recId = cursor.getInt(cursor
                .getColumnIndexOrThrow("mes_rec_id"));
        String nick = cursor.getString(cursor
                .getColumnIndexOrThrow("rec_name"));
        String number = cursor.getString(cursor
                .getColumnIndexOrThrow("rec_number"));
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra("recId", recId);
        intent.putExtra("nick", nick);
        intent.putExtra("number", number);


        startActivity(intent);
    }

    private void displayListView() {
        dbHelper.open();
        Cursor cursor = dbHelper.readListMainMessages();
        dbHelper.close();

        // create the adapter using the cursor pointing to the desired data
        // as well as the layout information
        MainCursorAdapter dataAdapter = new MainCursorAdapter(MainActivity.this, cursor, 0);
        // Assign adapter to ListView
        setListAdapter(dataAdapter);

    }

}
