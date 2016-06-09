package com.securesms.chat.presenter.interfaces;

import com.securesms.chat.model.MessageModel;
import com.securesms.chat.model.UserModel;
import com.securesms.chat.view.interfaces.ChatView;

/**
 * Created by admin on 2016-06-01.
 */
public interface ChatPresenter {
    void takeView(ChatView view);

    void releaseView();

    void setUserModel(UserModel userModel);

    void checkMessagesRead();

    void sendMessage(String message);

    void refreshMessages();
}
