package com.securesms;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.securesms.items.MessageItem;
import com.securesms.items.ReceiverItem;

public class SmsReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// ---get the SMS message passed in---
		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		String messageReceived = "";
		if (bundle != null) {
			// ---retrieve the SMS message received---
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			for (int i = 0; i < msgs.length; i++)

			{
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				messageReceived += msgs[i].getMessageBody().toString();
			}

			// ---display the new SMS message---

			String senderPhoneNumber = msgs[0].getOriginatingAddress();
			// add message to db
			DbAdapter dbHelper = new DbAdapter(context);
			dbHelper.open();
			
			ReceiverItem receiver_item = dbHelper.searchRowReceiverNumber(senderPhoneNumber.substring(3));
			MessageItem tmp = new MessageItem();
			tmp.text = messageReceived;
			tmp.rec = 0;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			tmp.date = sdf.format(new Date());
			tmp.id_receivers=receiver_item.id;
			
			dbHelper.createRowMessage(tmp);
			dbHelper.close();
		}
	}
}