package com.securesms.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.SmsMessage;


import com.securesms.chat.model.User;
import com.securesms.utils.AlgorithmAES;
import com.securesms.database.DbAdapter;
import com.securesms.main.MainActivity;
import com.securesms.R;
import com.securesms.chat.ChatActivity;
import com.securesms.contacts.model.ReceiverUserModel;

public class SmsReceiver extends BroadcastReceiver {
    private final AlgorithmAES algorithmAES = AlgorithmAES.INSTANCE;
    private final DbAdapter dbAdapter = DbAdapter.INSTANCE;

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
                messageReceived += msgs[i].getMessageBody();
            }

            // ---display the new SMS message---

            String senderPhoneNumber = msgs[0].getOriginatingAddress();
            // decode and add message to db

            //sprawdzenie czy ma zostac wyswietlone powiadomienie
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            boolean showNotyfications = prefs.getBoolean("showNotyfications", true);


            //sprawdzenie czy wiadomosc jest zakodowana
            if (messageReceived.startsWith("!encsms")) {
                //sprawdzenie czy numer jest w bazie
                dbAdapter.open();
                ReceiverUserModel receiver_item = dbAdapter.searchRowReceiverNumber(senderPhoneNumber.substring(3));
                dbAdapter.close();

                if (receiver_item == null) {
                    return;
                }

                String message = algorithmAES.receive_message(receiver_item, messageReceived.substring(7), context);
                if (showNotyfications) {
                    notify(context, senderPhoneNumber.substring(3), message);
                }

            }

        }
    }

    public void notify(Context context, String number, String message) {
        dbAdapter.open();
        ReceiverUserModel receiver_item = dbAdapter.searchRowReceiverNumber(number);
        dbAdapter.close();

        //pobranie ustawien i ustalenie czy wiadomosc ma byc pokazana na pasku
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean showMessage = prefs.getBoolean("showMessage", false);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(receiver_item.getName()).setAutoCancel(true);

        //ustawienie wibracji i dzwieku
        if (prefs.getBoolean("cbp_sound", false)) {
            mBuilder.setSound(Uri.parse(prefs.getString("rp_ringtone", null)));
        }
        if (prefs.getBoolean("cbp_vibration", false)) {
            long[] steps = {0, 500, 100, 200, 100, 200};
            mBuilder.setVibrate(steps);
        }

        if (showMessage) {
            //pokazanie wiadomosci
            mBuilder.setContentText(message);
        }
        int recId = receiver_item.getId();
        String nick = receiver_item.getName();

        Intent resultIntent = new Intent(context, ChatActivity.class);

        User user = new User(recId, nick, number);
        resultIntent.putExtra(MainActivity.USER_EXTRA, user);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(ChatActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_ONE_SHOT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(MainActivity.mId, mBuilder.build());
    }
}