package com.securesms.main;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.securesms.R;
import com.securesms.chat.ChatActivity;
import com.securesms.chat.model.UserModel;
import com.securesms.contacts.ContactsActivity;
import com.securesms.database.DbAdapter;
import com.securesms.main.presenter.MainPresenterImpl;
import com.securesms.main.presenter.interfaces.MainPresenter;
import com.securesms.main.view.adapter.MainCursorAdapter;
import com.securesms.main.view.interfaces.MainView;
import com.securesms.settings.SettingsActivity;


public class MainActivity extends Activity implements MainView {
    public static String USER_EXTRA = "USER_EXTRA";
    public static String USER_SELECT = "USER_SELECT";
    public static int mId = 1921;

    private ListView mListView;
    private TextView mEmpty;

    private final MainPresenter mPresenter = new MainPresenterImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list_view);

        mListView = (ListView) findViewById(R.id.list_contacts);
        mEmpty = (TextView) findViewById(R.id.empty);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) mListView.getItemAtPosition(i);

                // Get the state's capital from this row in the database.
                int recId = cursor.getInt(cursor
                        .getColumnIndexOrThrow(DbAdapter.MES_REC_ID));
                String nick = cursor.getString(cursor
                        .getColumnIndexOrThrow(DbAdapter.REC_NAME));
                String number = cursor.getString(cursor
                        .getColumnIndexOrThrow(DbAdapter.REC_NUMBER));

                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                UserModel userModel = new UserModel(recId, nick, number);
                intent.putExtra(USER_EXTRA, userModel);
                startActivity(intent);
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
            Intent intent = new Intent(getApplicationContext(), ContactsActivity.class);
            startActivity(intent);

        }
        if (id == R.id.action_new_message) {
            Intent intent = new Intent(getApplicationContext(), ContactsActivity.class);
            intent.putExtra(USER_SELECT,true);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setContacts(Cursor cursor) {
        MainCursorAdapter dataAdapter = new MainCursorAdapter(this, cursor, 0);
        // Assign adapter to ListView
        mListView.setAdapter(dataAdapter);
        if (dataAdapter.getCount() > 0) {
            mEmpty.setVisibility(View.GONE);
        } else {
            mEmpty.setVisibility(View.VISIBLE);
        }
    }
}
