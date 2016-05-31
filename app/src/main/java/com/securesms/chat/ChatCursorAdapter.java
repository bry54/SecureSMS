package com.securesms.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.securesms.AlgoritmAES;
import com.securesms.DbAdapter;
import com.securesms.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sebastian Sokołowski on 2015-07-12.
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
        TextView tv_message = (TextView) view.findViewById(R.id.message);
        TextView tv_date = (TextView) view.findViewById(R.id.message_date);

        String date = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter.MES_DATE));
        try {
            date = parseDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //ustawianie p�l
        tv_message.setText(cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter.MES_TEXT)));
        tv_date.setText(date);

        int rec = cursor.getInt(cursor
                .getColumnIndexOrThrow(DbAdapter.MES_REC));
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv_message.getLayoutParams();


        if (rec == 0) {
            //wiadomosc odebrana
            tv_message.setBackgroundResource(R.drawable.message_receiver_text_view);
            tv_message.setGravity(Gravity.RIGHT);
            tv_date.setGravity(Gravity.RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);

            params.setMargins(75, 0, 5, 0); //substitute parameters for left, top, right, bottom
            tv_message.setLayoutParams(params);
        } else {
            //wiadomosc wyslana
            tv_message.setBackgroundResource(R.drawable.
                    message_sender_text_view);
            tv_message.setGravity(Gravity.LEFT);
            tv_date.setGravity(Gravity.LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 1);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);

            params.setMargins(5, 0, 75, 0); //substitute parameters for left, top, right, bottom
            tv_message.setLayoutParams(params);
        }
        //ukrycie pola daty gdy wiadomosc zostala teraz wyslana
        if(date.equals(""))
        {
            tv_date.setVisibility(View.GONE);
        }

        //ustawienie wielkosci czcionki
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int fontSize = Integer.parseInt(prefs.getString("font_size", "1"));
        if(fontSize==2)
        {
            tv_date.setTextAppearance(context,android.R.style.TextAppearance_Medium);
            tv_message.setTextAppearance(context,android.R.style.TextAppearance_Large);

        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.chat_list_view_item, parent, false);
    }

    public String parseDate(String date) throws ParseException {
        String wynik = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd HH");
        SimpleDateFormat sdf5 = new SimpleDateFormat("yyyy-MM-dd");

        Date tmp = sdf.parse(date);
        Date now = new Date();

        //ten sam dzien
        int ileTemu = now.getMinutes() - tmp.getMinutes();
        if (sdf4.format(tmp).equals(sdf4.format(now)) && ileTemu <= 10 && ileTemu!=0) {
            //mniej niz 10 minut
            wynik = ileTemu + " min temu";
        } else if (sdf3.format(tmp).equals(sdf3.format(now))) {
            //0 min temu
            wynik = "";
        } else if (sdf5.format(tmp).equals(sdf5.format(now))) {
            //ten sam dzien
            wynik = sdf2.format(tmp);
        } else {
            wynik = sdf3.format(tmp);
        }
        return wynik;
    }
}
