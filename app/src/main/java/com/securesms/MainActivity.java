package com.securesms;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.securesms.items.ReceiverItem;


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
        // The desired columns to be bound
        String[] columns = new String[]{DbAdapter.REC_NAME, DbAdapter.MES_DATE};

        // the XML defined views which the data will be bound to
        int[] to = new int[]{R.id.receiver_name, R.id.message_date};

        // create the adapter using the cursor pointing to the desired data
        // as well as the layout information
        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, R.layout.main_list_view_item,
                cursor, columns, to, 0) {

            @Override
            public void bindView(final View view, final Context context, final Cursor cursor) {
                super.bindView(view, context, cursor);

                ImageView image = (ImageView) view.findViewById(R.id.iv_is_read);
                int receiverId = cursor.getInt(cursor.getColumnIndex(DbAdapter.MES_REC_ID));
                dbHelper.open();
                boolean isRead = dbHelper.isReadMessageReceiver(receiverId);
                dbHelper.close();
            if(!isRead)
                {
                    image.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_unread));
                }
            };
        };

        // Assign adapter to ListView
        setListAdapter(dataAdapter);

    }
}
