package com.securesms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.securesms.items.MessageItem;
import com.securesms.items.ReceiverItem;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewMessageActivity extends Activity {
    DbAdapter dbHelper;
    ImageButton btn_send;
    ImageButton btn_add_contact;
    EditText et_number;
    EditText et_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_add);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        dbHelper = new DbAdapter(this);
        btn_send = (ImageButton) findViewById(R.id.send_message);
        btn_add_contact = (ImageButton) findViewById(R.id.show_contacts);
        et_number = (EditText) findViewById(R.id.message_number);
        et_message = (EditText) findViewById(R.id.message_text);
    }

    public void selectReceive(View v) {
        Intent intent = new Intent(getApplicationContext(), SelectReceiverActivity.class);
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
        AlgoritmAES algoritmAES = new AlgoritmAES();

        String phoneNo = et_number.getText().toString();
        String message = et_message.getText().toString();


        algoritmAES.send_message(phoneNo,message,getApplicationContext());
        finish();
    }

}
