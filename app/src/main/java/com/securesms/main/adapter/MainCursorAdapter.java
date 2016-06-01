package com.securesms.main.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.securesms.database.DbAdapter;
import com.securesms.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sebastian Soko�owski on 2015-07-12.
 */
public class MainCursorAdapter extends CursorAdapter {
    DbAdapter dbHelper;
    private LayoutInflater mInflater;

    public MainCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        dbHelper = new DbAdapter(context);
        mInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv_name = (TextView) view.findViewById(R.id.receiver_name);
        TextView tv_date = (TextView) view.findViewById(R.id.message_date);
        TextView tv_count = (TextView) view.findViewById(R.id.tv_count_message);
        TextView tv_lastMessage = (TextView) view.findViewById(R.id.tv_last_message);
        ImageView image = (ImageView) view.findViewById(R.id.iv_is_read);

        int receiverId = cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter.MES_REC_ID));
        String data=cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter.MES_DATE));
        try {
            data=parseDate(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //pobieranie danych z bazy danych

        dbHelper.open();
        boolean isRead = dbHelper.isReadMessageReceiver(receiverId);
        int count = dbHelper.getCountMessageReceiver(receiverId);
        //String lastMessage = dbHelper.get
        dbHelper.close();

        //ustawianie zdjecia wiadomosci (czy odebrana czy nie)

        if(!isRead)
        {
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_unread));
        }

        //ustawianie pol
        tv_name.setText(cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter.REC_NAME)));
        tv_date.setText(data);
        tv_count.setText("("+count+")");
        tv_lastMessage.setText(cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter.MES_TEXT)));


    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.main_list_view_item, parent, false);
    }
    public String parseDate(String date) throws ParseException {
        String wynik = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd");

        Date tmp = sdf.parse(date);
        Date now = new Date();

        //ten sam dzien
        if (sdf4.format(tmp).equals(sdf4.format(now))) {
            wynik = sdf2.format(tmp);
        } else {
            wynik = sdf3.format(tmp);
        }
        return wynik;
    }
}
