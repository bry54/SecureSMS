package com.securesms.selectContact;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.securesms.chat.presenter.ChatPresenterImpl;
import com.securesms.chat.presenter.interfaces.ChatPresenter;
import com.securesms.database.DbAdapter;
import com.securesms.R;
import com.securesms.selectContact.presenter.SelectContactPresenterImpl;
import com.securesms.selectContact.presenter.interfaces.SelectContactPresenter;
import com.securesms.selectContact.view.interfaces.SelectContactView;

/**
 * Created by Sebastian Sokolowski on 2015-07-06.
 */
public class SelectReceiverActivity extends Activity implements SelectContactView{
    private ListView mListContact;

    private final SelectContactPresenter mPresenter = new SelectContactPresenterImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.select_receiver_list_view);

        mListContact = (ListView) findViewById(R.id.list_contacts);
        mListContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) mListContact.getItemAtPosition(i);
                String number = cursor.getString(cursor.getColumnIndexOrThrow("rec_number"));
                Intent returnIntent = new Intent();
                returnIntent.putExtra("number", number);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.takeView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.releaseView();
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
    public void setContacts(Cursor cursor) {
        // The desired columns to be bound
        String[] columns = new String[]{DbAdapter.REC_NAME, DbAdapter.REC_NUMBER};

        // the XML defined views which the data will be bound to
        int[] to = new int[]{R.id.receiverName, R.id.receiverNumber};

        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, R.layout.select_receiver_list_view_item,
                cursor, columns, to, 0);
        mListContact.setAdapter(dataAdapter);
    }
}
