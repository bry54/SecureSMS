package com.securesms;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;
import com.securesms.contacts.ContactsActivity;
import com.securesms.main.MainActivity;
import com.securesms.settings.SettingsActivity;

/**
 * Created by admin on 2016-06-07.
 */
public class NavigationTest extends ActivityInstrumentationTestCase2<MainActivity> {


    private Solo solo;

    public NavigationTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        //setUp() is run before a test case is started.
        //This is where the solo object is created.
        solo = new Solo(getInstrumentation());
        getActivity();
    }

    @Override
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
    }

    public void testGoToContacts() throws Exception {
        //Unlock the lock screen
        solo.unlockScreen();
        //Click menu item contacts
        solo.clickOnMenuItem(solo.getString(R.string.contacts));
        //Restart activity
        getInstrumentation().callActivityOnRestart(solo.getCurrentActivity());
        //Assert that NewMessageActivity is opened
        solo.assertCurrentActivity("Expected ContactsActivity", ContactsActivity.class);
        //Click home button
        solo.clickOnActionBarHomeButton();
        //Assert that MainActivity is opened
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
    }

    public void testGoToSettings() throws Exception {
        //Unlock the lock screen
        solo.unlockScreen();
        //Click menu item contacts
        solo.clickOnMenuItem(solo.getString(R.string.action_settings));
        //Restart activity
        getInstrumentation().callActivityOnRestart(solo.getCurrentActivity());
        //Assert that NewMessageActivity is opened
        solo.assertCurrentActivity("Expected SettingsActivity", SettingsActivity.class);
        //Click home button
        solo.clickOnActionBarHomeButton();
        //Assert that MainActivity is opened
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
    }

    public void testGoToNewMessage() throws Exception {
        //Unlock the lock screen
        solo.unlockScreen();
        //Click menu item contacts
        solo.clickOnActionBarItem(R.id.action_new_message);
        //Restart activity
        getInstrumentation().callActivityOnRestart(solo.getCurrentActivity());
        //Assert that NewMessageActivity is opened
        solo.assertCurrentActivity("Expected NewMessageActivity", ContactsActivity.class);
        //Click home button
        solo.clickOnActionBarHomeButton();
        //Assert that MainActivity is opened
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
    }
}
