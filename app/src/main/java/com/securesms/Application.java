package com.securesms;

import com.securesms.database.DbAdapter;
import com.securesms.utils.AlgorithmAES;

/**
 * Created by admin on 2016-06-01.
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initSingletons();
    }

    private void initSingletons() {
        DbAdapter.initInstance(getApplicationContext());
        AlgorithmAES.initInstance();
    }
}
