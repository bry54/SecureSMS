package com.securesms.chat.presenter;

import android.database.Cursor;

import com.securesms.chat.model.MessageModel;
import com.securesms.chat.model.User;
import com.securesms.chat.presenter.interfaces.ChatPresenter;
import com.securesms.chat.view.interfaces.ChatView;
import com.securesms.database.DbAdapter;
import com.securesms.utils.AlgorithmAES;

/**
 * Created by admin on 2016-06-01.
 */
public class ChatPresenterImpl implements ChatPresenter{
    private ChatView view;
    private User user;

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

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void checkMessagesRead() {
        dbAdapter.open();
        dbAdapter.setReadMessages(user.getRecId());
        dbAdapter.close();
    }

    @Override
    public void sendMessage(String message) {
        algorithmAES.send_message(user.getNumber(), message);
        if(view != null) {
            view.successfulSendMessage();
        }

        //refresh view
        getAllMessages();
    }

    @Override
    public void removeMessage(MessageModel messageModel) {
        dbAdapter.open();
        dbAdapter.deleteRowMessage(messageModel);
        dbAdapter.close();

        //refresh view
        getAllMessages();
    }

    private void getAllMessages(){
        dbAdapter.open();
        Cursor cursor = dbAdapter.searchRowMessageRec(user.getRecId());
        dbAdapter.close();

        if(view != null) {
            view.setMessages(cursor);
        }
    }
}
