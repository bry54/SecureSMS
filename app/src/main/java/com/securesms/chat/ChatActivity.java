package com.securesms.chat;

import android.app.Activity;
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
import android.widget.ListView;
import android.widget.Toast;

import com.securesms.R;
import com.securesms.chat.model.MessageModel;
import com.securesms.chat.model.UserModel;
import com.securesms.chat.presenter.ChatPresenterImpl;
import com.securesms.chat.presenter.interfaces.ChatPresenter;
import com.securesms.chat.view.RefreshEvent;
import com.securesms.chat.view.adapter.ChatCursorAdapter;
import com.securesms.chat.view.interfaces.ChatView;
import com.securesms.main.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ChatActivity extends Activity implements ChatView {
    private ImageButton btn_send;
    private EditText et_message;
    private ListView lv_messages;

    private final ChatPresenter mPresenter = new ChatPresenterImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        btn_send = (ImageButton) findViewById(R.id.send_message);
        et_message = (EditText) findViewById(R.id.message_text);
        lv_messages = (ListView) findViewById(R.id.list_message);

        Bundle bundle = getIntent().getExtras();


        if (bundle != null) {
            UserModel userModel = (UserModel) bundle.getSerializable(MainActivity.USER_EXTRA);
            mPresenter.setUserModel(userModel);
            getActionBar().setTitle(userModel.getNick() + "\n" + userModel.getNumber());

            mPresenter.checkMessagesRead();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.takeView(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.releaseView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(RefreshEvent event) {
        mPresenter.refreshMessages();
    };

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
        mPresenter.sendMessage(message);
    }

    @Override
    public void successfulSendMessage() {
        et_message.setText("");
        Toast.makeText(this, this.getString(R.string.send_sms), Toast.LENGTH_LONG).show();
    }

    @Override
    public void failSendMessage() {
        Toast.makeText(this, this.getString(R.string.error_send_sms), Toast.LENGTH_LONG).show();
    }

    @Override
    public void setMessages(Cursor cursor) {
        ChatCursorAdapter dataAdapter = new ChatCursorAdapter(ChatActivity.this, cursor, 0);
        // Assign adapter to ListView
        lv_messages.setAdapter(dataAdapter);
    }
}
