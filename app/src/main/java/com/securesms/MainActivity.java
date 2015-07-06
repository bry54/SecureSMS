package com.securesms;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class MainActivity extends ListActivity {

	private DbAdapter dbHelper;
	private SimpleCursorAdapter dataAdapter;
	
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
        if (id == R.id.action_contacts)
        {
        	Intent intent = new Intent(getApplicationContext(), ReceiversActivity.class);
        	startActivity(intent);
			
        }
        if (id == R.id.action_new_message)
        {
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
		intent.putExtra("recId",recId);
		intent.putExtra("nick", nick);
		intent.putExtra("number", number);
		startActivity(intent);
	}
    private void displayListView() {
		dbHelper.open();
		Cursor cursor = dbHelper.readListMainMessages();
		dbHelper.close();
		// The desired columns to be bound
		String[] columns = new String[] { DbAdapter.REC_NAME ,DbAdapter.MES_DATE};

		// the XML defined views which the data will be bound to
		int[] to = new int[] { R.id.receiver_name,R.id.message_date };

		// create the adapter using the cursor pointing to the desired data
		// as well as the layout information
		dataAdapter = new SimpleCursorAdapter(this, R.layout.main_list_view_item,
				cursor, columns, to, 0);

		// Assign adapter to ListView
		setListAdapter(dataAdapter);

	}
}
