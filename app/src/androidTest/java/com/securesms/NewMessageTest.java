package com.securesms;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;
import com.securesms.newMessage.NewMessageActivity;

/**
 * Created by admin on 2016-06-06.
 */
public class NewMessageTest extends ActivityInstrumentationTestCase2<NewMessageActivity> {
    private Solo solo;

    public NewMessageTest() {
        super(NewMessageActivity.class);
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


    public void testNewMessage() throws Exception {
        //Unlock the lock screen
        solo.unlockScreen();
        //Assert that NoteEditor activity is opened
        solo.assertCurrentActivity("Expected NewMessageActivity", NewMessageActivity.class);
    }
}
