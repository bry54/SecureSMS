package com.securesms.chat.view.interfaces;

import android.database.Cursor;

/**
 * Created by admin on 2016-06-01.
 */
public interface ChatView {
    void successfulSendMessage();

    void failSendMessage();

    void setMessages(Cursor cursor);
}
