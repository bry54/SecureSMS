package com.securesms;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddMessage extends Activity {
	Button btn_send;
	Button btn_add_contact;
	EditText et_number;
	EditText et_message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_message);

		btn_send = (Button) findViewById(R.id.send_message);
		btn_add_contact = (Button) findViewById(R.id.show_contacts);
		et_number = (EditText) findViewById(R.id.message_number);
		et_message = (EditText) findViewById(R.id.message_text);
	}

	public void show_contects(View v) {

	}

	public void send_message(View v) {
		String phoneNo = et_number.getText().toString();
		String message = et_message.getText().toString();

		try {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phoneNo, null, message, null, null);
			Toast.makeText(getApplicationContext(), "SMS sent.",
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"SMS faild, please try again.", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

}
