package com.securesms.newMessage.presenter.interfaces;

import com.securesms.newMessage.model.NewMessageModel;
import com.securesms.newMessage.view.interfaces.NewMessageView;

/**
 * Created by admin on 2016-06-03.
 */
public interface NewMessagePresenter {
    void takeView(NewMessageView view);

    void releaseView();

    void sentMessage(NewMessageModel newMessageModel);
}
