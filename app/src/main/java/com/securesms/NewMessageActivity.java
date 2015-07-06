package com.securesms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
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
        String phoneNo = et_number.getText().toString();
        String message = et_message.getText().toString();

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getApplicationContext(), "Wys³ano SMS",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "B³¹d wysy³ania, spróbuj ponownie póŸniej", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }finally {
            dbHelper.open();
            ReceiverItem receiver_item = dbHelper.searchRowReceiverNumber(et_number.getText().toString());

            MessageItem tmp = new MessageItem();
            tmp.text=et_message.getText().toString();
            tmp.rec=1;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            tmp.date = sdf.format(new Date());
            tmp.id_receivers=receiver_item.id;
            tmp.read=0;
            dbHelper.createRowMessage(tmp);
            dbHelper.close();
            finish();
        }
    }

}
