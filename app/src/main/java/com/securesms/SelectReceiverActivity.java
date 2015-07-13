package com.securesms;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.securesms.DbAdapter;
import com.securesms.R;

/**
 * Created by Sebastian Sokolowski on 2015-07-06.
 */
public class SelectReceiverActivity extends ListActivity {
    private DbAdapter dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.select_receiver_list_view);
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

    @Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);
        Cursor cursor = (Cursor) getListView().getItemAtPosition(position);
        String number = cursor.getString(cursor.getColumnIndexOrThrow("rec_number"));
        Intent returnIntent = new Intent();
        returnIntent.putExtra("number", number);
        setResult(RESULT_OK, returnIntent);
        finish();
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
        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, R.layout.select_receiver_list_view_item,
                cursor, columns, to, 0);
        // Assign adapter to ListView
        setListAdapter(dataAdapter);

    }
}
