package com.securesms.settings;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.app.NavUtils;

import com.securesms.R;

/**
 * Created by Sebastian Sokolowski on 2015-07-06.
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        addPreferencesFromResource(R.layout.activity_settings);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}