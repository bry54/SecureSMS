package com.securesms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.securesms.chat.model.MessageModel;
import com.securesms.contacts.model.ReceiverUserModel;

import java.util.ArrayList;


public class DbAdapter {
    public static final String DATABASE_NAME = "data.db";

    public static final String SQLITE_TABLE_RECEIVERS = "Receivers";
    public static final String REC_ID = "_id";
    public static final String REC_NAME = "rec_name";
    public static final String REC_NUMBER = "rec_number";
    public static final String REC_CODE = "rec_code";

    public static final String SQLITE_TABLE_MESSAGES = "Messages";
    public static final String MES_ID = "_id";
    public static final String MES_REC_ID = "mes_rec_id";
    public static final String MES_DATE = "mes_date";
    public static final String MES_TEXT = "mes_text";
    public static final String MES_REC = "mes_rec";
    public static final String MES_READ = "mes_read";

    public static boolean is_open = false;
    public static int DATABASE_VERSION = 7;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private final Context dbContext;

    public static DbAdapter INSTANCE;

    private static final String DATABASE_RECEIVERS_CREATE = "CREATE TABLE if not exists "
            + SQLITE_TABLE_RECEIVERS
            + " ("
            + REC_ID
            + " INTEGER PRIMARY KEY autoincrement,"
            + REC_NAME
            + " TEXT NOT NULL UNIQUE,"
            + REC_NUMBER
            + " STRING NOT NULL UNIQUE,"
            + REC_CODE
            + " TEXT NOT NULL);";
    private static final String DATABASE_MESSAGES_CREATE = "CREATE TABLE if not exists "
            + SQLITE_TABLE_MESSAGES
            + " ("
            + MES_ID
            + " INTEGER PRIMARY KEY autoincrement,"
            + MES_REC_ID
            + " INTEGER,"
            + MES_DATE
            + " TEXT,"
            + MES_TEXT
            + " TEXT,"
            + MES_REC
            + " INTEGER,"
            + MES_READ
            + " INTEGER);";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_RECEIVERS_CREATE);
            db.execSQL(DATABASE_MESSAGES_CREATE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE_RECEIVERS);
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE_MESSAGES);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    private DbAdapter(Context ctx) {
        this.dbContext = ctx;
    }

    public static void initInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DbAdapter(context);
        }
    }

    public DbAdapter open() throws SQLException {
        dbHelper = new DatabaseHelper(dbContext);
        db = dbHelper.getWritableDatabase();
        is_open = true;
        return this;
    }

    public void close() {
        if (dbHelper != null) {
            is_open = false;
            dbHelper.close();
        }
    }

    public long createRowReceiver(ReceiverUserModel t) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(REC_NAME, t.getName());
        initialValues.put(REC_NUMBER, t.getNumber());
        initialValues.put(REC_CODE, t.getPassword());
        return db.insert(SQLITE_TABLE_RECEIVERS, null, initialValues);
    }

    public long deleteRowReceiver(ReceiverUserModel t) {
        return db.delete(SQLITE_TABLE_RECEIVERS, REC_ID + " =?",
                new String[]{t.getId() + ""});
    }

    public long updateRowReceive(ReceiverUserModel t) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(REC_NAME, t.getName());
        initialValues.put(REC_NUMBER, t.getNumber());
        initialValues.put(REC_CODE, t.getPassword());
        return db.update(SQLITE_TABLE_RECEIVERS, initialValues, REC_ID + " =?",
                new String[]{t.getId() + ""});
    }

    public ReceiverUserModel searchRowReceiverNumber(String number) {
        ReceiverUserModel result = null;
        String[] allColumns = new String[]{REC_ID, REC_NAME,
                REC_NUMBER, REC_CODE};
        Cursor c = db.query(SQLITE_TABLE_RECEIVERS, allColumns, REC_NUMBER + "= ?",
                new String[]{number}, null, null, null);
        if (c != null && c.moveToFirst()) {
            result = new ReceiverUserModel(c.getInt(0), c.getString(1), c.getString(2),
                    c.getString(3));
            c.close();
        }

        return result;
    }

    public boolean isNotReadMessage(int id) {
        boolean result = false;
        String[] allColumns = new String[]{MES_REC_ID, MES_READ};
        Cursor c = db.query(SQLITE_TABLE_MESSAGES, allColumns, MES_REC_ID + " = ? AND " + MES_READ + " = ?",
                new String[]{id + "", 1 + ""}, null, null, null);
        if (c != null && c.moveToFirst()) {
            if (c.getCount() != 0) {
                result = true;
            }
            c.close();
        }
        return result;
    }

    public int getCountMessageReceiver(long id) {
        int result = 0;
        String[] allColumns = new String[]{MES_REC_ID, MES_READ};
        Cursor c = db.rawQuery("select count(*) from " + SQLITE_TABLE_MESSAGES + " where " + MES_REC_ID + "='" + id + "'", null);
        if (c != null && c.moveToFirst()) {
            result = c.getInt(0);
            c.close();
        }
        return result;
    }

    // zadania do tabeli Receiver
    public long createRowMessage(MessageModel t) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(MES_REC_ID, t.getId_receivers());
        initialValues.put(MES_DATE, t.getDate());
        initialValues.put(MES_TEXT, t.getText());
        initialValues.put(MES_REC, t.getRec());
        initialValues.put(MES_READ, t.getReadInt());
        return db.insert(SQLITE_TABLE_MESSAGES, null, initialValues);
    }

    // zadania do tabeli Receiver
    public long setReadMessages(long id) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(MES_READ, 0);
        return db.update(SQLITE_TABLE_MESSAGES, initialValues, MES_REC_ID + " =? AND " + MES_READ + " =?",
                new String[]{id + "", 1 + ""});
    }

    public long deleteRowMessage(MessageModel t) {
        return db.delete(SQLITE_TABLE_MESSAGES, REC_ID + " =?",
                new String[]{t.getId() + ""});
    }

    public Cursor searchRowMessageRec(long id) {
        String[] allColumns = new String[]{MES_ID, MES_TEXT, MES_DATE, MES_REC};
        Cursor c = db.query(SQLITE_TABLE_MESSAGES, allColumns, MES_REC_ID + "= ?", new String[]{id + ""}, null,
                null, MES_DATE);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public ArrayList<MessageModel> getRowsMessagesRec(long id) {
        ArrayList<MessageModel> list = new ArrayList<MessageModel>();
        Cursor c = db.query(SQLITE_TABLE_MESSAGES, null, MES_REC_ID + "= ?", new String[]{id + ""}, null,
                null, MES_DATE);
        if (c != null && c.moveToFirst()) {
            MessageModel item = new MessageModel();
            item.setId(c.getInt(c.getColumnIndexOrThrow(MES_ID)));
            item.setId_receivers(c.getInt(c.getColumnIndexOrThrow(MES_REC_ID)));
            item.setDate(c.getString(c.getColumnIndexOrThrow(MES_DATE)));
            item.setText(c.getString(c.getColumnIndexOrThrow(MES_TEXT)));
            item.setRec(c.getInt(c.getColumnIndexOrThrow(MES_REC)));
            item.setRead(c.getInt(c.getColumnIndexOrThrow(MES_READ)));

            list.add(item);
            while (c.moveToNext()) {
                item = new MessageModel(c.getString(0), c.getString(1), c.getInt(2));
                list.add(item);
            }
        }
        return list;
    }

    public Cursor readListMainMessages() {
        String query = "SELECT * FROM Messages m INNER JOIN Receivers r ON r._id=m.mes_rec_id GROUP BY m.mes_rec_id";
        Cursor c = db.rawQuery(query, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor readListReceivers() {
        String[] allColumns = new String[]{REC_ID, REC_NAME, REC_NUMBER, REC_CODE};
        Cursor c = db.query(SQLITE_TABLE_RECEIVERS, allColumns, null, null, null,
                null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
}
