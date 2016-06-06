package com.securesms.selectContact.presenter.interfaces;

import com.securesms.selectContact.view.interfaces.SelectContactView;

/**
 * Created by admin on 2016-06-05.
 */
public interface SelectContactPresenter {
    void takeView(SelectContactView view);

    void releaseView();
}
