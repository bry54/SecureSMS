package com.securesms;

import android.content.Context;
import android.database.Cursor;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Sebastian Soko³owski on 2015-07-12.
 */
public class ChatCursorAdapter extends CursorAdapter {

    private LayoutInflater mInflater;

    public ChatCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView message = (TextView) view.findViewById(R.id.message);
        TextView date = (TextView) view.findViewById(R.id.message_date);

        //ustawianie pól
        message.setText(cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter.MES_TEXT)));
        date.setText(cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter.MES_DATE)));

        int rec = cursor.getInt(cursor
                .getColumnIndexOrThrow(DbAdapter.MES_REC));
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) message.getLayoutParams();


        if (rec == 0) {
            //wiadomosc odebrana
            message.setBackgroundResource(R.drawable.message_receiver_text_view);
            message.setGravity(Gravity.RIGHT);
            date.setGravity(Gravity.RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT,0);

            params.setMargins(75, 0, 5, 0); //substitute parameters for left, top, right, bottom
            message.setLayoutParams(params);
        } else {
            //wiadomosc wyslana
            message.setBackgroundResource(R.drawable.
                    message_sender_text_view);
            message.setGravity(Gravity.LEFT);
            date.setGravity(Gravity.LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 1);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,0);

            params.setMargins(5, 0, 75, 0); //substitute parameters for left, top, right, bottom
            message.setLayoutParams(params);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.chat_list_view_item, parent, false);
    }
}
