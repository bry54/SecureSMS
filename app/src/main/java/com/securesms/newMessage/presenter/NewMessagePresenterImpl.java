package com.securesms.newMessage.presenter;

import com.securesms.newMessage.model.NewMessageModel;
import com.securesms.newMessage.presenter.interfaces.NewMessagePresenter;
import com.securesms.newMessage.view.interfaces.NewMessageView;
import com.securesms.utils.AlgorithmAES;

/**
 * Created by admin on 2016-06-03.
 */
public class NewMessagePresenterImpl implements NewMessagePresenter {
    private NewMessageView view;

    private final AlgorithmAES algorithmAES = AlgorithmAES.INSTANCE;

    public NewMessagePresenterImpl() {
    }

    @Override
    public void takeView(NewMessageView view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        this.view = null;
    }

    @Override
    public void sentMessage(NewMessageModel newMessageModel) {
        if (newMessageModel.getPhoneNo().length() <= 8) {
            if (view != null) {
                view.noPhoneNumber();
            }
        } else {
            algorithmAES.send_message(newMessageModel.getPhoneNo(), newMessageModel.getMessage());
        }
    }
}
