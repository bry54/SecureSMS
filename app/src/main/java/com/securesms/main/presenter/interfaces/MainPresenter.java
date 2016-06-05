package com.securesms.main.presenter.interfaces;

import com.securesms.main.view.interfaces.MainView;

/**
 * Created by admin on 2016-06-01.
 */
public interface MainPresenter {
    void takeView(MainView view);
    void releaseView();
}
