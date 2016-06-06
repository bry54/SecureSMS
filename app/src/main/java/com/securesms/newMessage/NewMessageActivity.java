package com.securesms.newMessage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.securesms.R;
import com.securesms.newMessage.model.NewMessageModel;
import com.securesms.newMessage.presenter.NewMessagePresenterImpl;
import com.securesms.newMessage.presenter.interfaces.NewMessagePresenter;
import com.securesms.newMessage.view.interfaces.NewMessageView;
import com.securesms.selectContact.SelectContactActivity;

public class NewMessageActivity extends Activity implements NewMessageView {
    private ImageButton btn_send;
    private ImageButton btn_add_contact;
    private EditText et_number;
    private EditText et_message;

    private final NewMessagePresenter mPresenter = new NewMessagePresenterImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        btn_send = (ImageButton) findViewById(R.id.send_message);
        btn_add_contact = (ImageButton) findViewById(R.id.show_contacts);
        et_number = (EditText) findViewById(R.id.message_number);
        et_message = (EditText) findViewById(R.id.message_text);
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

    public void selectReceive(View v) {
        Intent intent = new Intent(getApplicationContext(), SelectContactActivity.class);
        startActivityForResult(intent, 1);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                et_number.setText(data.getStringExtra("number"));
            }
        }
    }

    public void send_message(View v) {
        String phoneNo = et_number.getText().toString();
        String message = et_message.getText().toString();

        NewMessageModel newMessageModel = new NewMessageModel(phoneNo, message);
        mPresenter.sentMessage(newMessageModel);
    }

    @Override
    public void successfulSendMessage() {
        Toast.makeText(this, getString(R.string.send_sms), Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void failSendMessage() {
        Toast.makeText(this, getString(R.string.error_send_sms), Toast.LENGTH_LONG).show();
    }

    @Override
    public void noPhoneNumber() {
        et_number.setError("No phone number");
    }
}
