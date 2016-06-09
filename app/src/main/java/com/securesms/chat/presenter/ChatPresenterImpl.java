package com.securesms.chat.presenter;

import android.database.Cursor;

import com.securesms.chat.model.MessageModel;
import com.securesms.chat.model.UserModel;
import com.securesms.chat.presenter.interfaces.ChatPresenter;
import com.securesms.chat.view.interfaces.ChatView;
import com.securesms.database.DbAdapter;
import com.securesms.utils.AlgorithmAES;

/**
 * Created by admin on 2016-06-01.
 */
public class ChatPresenterImpl implements ChatPresenter {
    private ChatView view;
    private UserModel userModel;

    private final AlgorithmAES algorithmAES = AlgorithmAES.INSTANCE;
    private final DbAdapter dbAdapter = DbAdapter.INSTANCE;

    public ChatPresenterImpl() {
    }

    @Override
    public void takeView(ChatView view) {
        this.view = view;
        getAllMessages();
    }

    @Override
    public void releaseView() {
        this.view = null;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    @Override
    public void checkMessagesRead() {
        dbAdapter.open();
        dbAdapter.setReadMessages(userModel.getRecId());
        dbAdapter.close();
    }

    @Override
    public void sendMessage(String message) {
        boolean success = algorithmAES.send_message(userModel.getNumber(), message);
        if (view != null) {
            if (success) {
                view.successfulSendMessage();
            } else {
                view.failSendMessage();
            }
        }

        //refresh view
        getAllMessages();
    }

    @Override
    public void refreshMessages() {
        getAllMessages();
    }

    private void getAllMessages() {
        dbAdapter.open();
        Cursor cursor = dbAdapter.searchRowMessageRec(userModel.getRecId());
        dbAdapter.close();

        if (view != null) {
            view.setMessages(cursor);
        }
    }
}
