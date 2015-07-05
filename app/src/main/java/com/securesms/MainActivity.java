package com.securesms;

import android.app.Activity;
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


public class MainActivity extends Activity {

	private DbAdapter dbHelper;
	private SimpleCursorAdapter dataAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list_view);
		dbHelper = new DbAdapter(this);
		dbHelper.open();
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
            return true;
        }
        if (id == R.id.new_receiver)
        {
        	Intent intent = new Intent(getApplicationContext(), AddReceiver.class);
        	startActivity(intent);
			
        }
        if (id == R.id.new_message)
        {
        	Intent intent = new Intent(getApplicationContext(), AddMessage.class);
        	startActivity(intent);
			
        }
        return super.onOptionsItemSelected(item);
    }
    private void displayListView() {

		Cursor cursor = dbHelper.readListMainMessages();

		// The desired columns to be bound
		String[] columns = new String[] { DbAdapter.REC_NAME ,DbAdapter.MES_DATE};

		// the XML defined views which the data will be bound to
		int[] to = new int[] { R.id.receiver_name,R.id.message_date };

		// create the adapter using the cursor pointing to the desired data
		// as well as the layout information
		dataAdapter = new SimpleCursorAdapter(this, R.layout.main_list_view_item,
				cursor, columns, to, 0);

		ListView listView = (ListView) findViewById(R.id.listView);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> listView, View view,
					int position, long id) {
				// Get the cursor, positioned to the corresponding row in the
				// result set
				Cursor cursor = (Cursor) listView.getItemAtPosition(position);

				// Get the state's capital from this row in the database.
				String countryCode = cursor.getString(cursor
						.getColumnIndexOrThrow("code"));
				Toast.makeText(getApplicationContext(), countryCode,
						Toast.LENGTH_SHORT).show();
				//Toast.makeText(getApplicationContext(), "dziala!",Toast.LENGTH_LONG).show();

			}
		});

	}
}
