package com.securesms;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.robotium.solo.Solo;
import com.securesms.chat.ChatActivity;
import com.securesms.contacts.ContactsActivity;
import com.securesms.main.MainActivity;

/**
 * Created by admin on 2016-06-06.
 */
public class NewMessageTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private final String CONTACT_NAME = "I";
    private final String CONTACT_NUMBER = "";
    private final String CONTACT_PASSWORD = "takie_haslo";
    private final String MESSAGE = "Taka sobie wiadomosc z takim czyms";

    private Solo solo;

    public NewMessageTest() {
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

    public void testGlobal() throws Exception {
        /*
        1-create contact with my number
        2-create new message
        3-send to my number
        4-check validate message
        5-remove added contact
         */
        //Unlock the lock screen
        solo.unlockScreen();

        createContact();
        createNewMessage();
        checkMessage();
        removeContact();
    }

    private void removeContact() throws Exception {
        //Click menu item contacts
        solo.clickOnMenuItem(solo.getString(R.string.contacts));
        //Assert that NoteEditor activity is opened
        solo.assertCurrentActivity("Expected ContactsActivity", ContactsActivity.class);
        ListView ListView = (ListView) solo.getView(R.id.list_contacts);
        View view = ContactsTest.findStringInView(CONTACT_NAME, ListView);
        //Delete test contact
        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.deleteReceiver);
        solo.clickOnView(deleteButton);
        //Click yes
        solo.clickOnView(solo.getView(android.R.id.button1));
        //Check if item still exist
        ListView = (ListView) solo.getView(R.id.list_contacts);
        assertEquals(null, ContactsTest.findStringInView(CONTACT_NAME, ListView));
    }

    private void checkMessage() throws Exception {
        //Find added contact
        ListView ListView = (ListView) solo.getView(R.id.list_message);
        View view = findReceiveMessageInView(MESSAGE, ListView);
        //Assert that message exist
        assertNotNull(view);
        //Click home button
        solo.clickOnActionBarHomeButton();
        //Assert that MainActivity is opened
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);

    }

    private View findReceiveMessageInView(String text, ListView ListView) {
        boolean myMessage = false;
        for (int i = 0; i != ListView.getChildCount(); i++) {
            View view = ListView.getChildAt(i);
            TextView tv = (TextView) view
                    .findViewById(R.id.message);
            String findText = tv.getText().toString();
            if (findText.equals(text)) {
                if (myMessage) {
                    return view;
                }
                myMessage = true;
            }
        }
        return null;
    }

    private void createNewMessage() throws Exception {
        //Click menu item contacts
        solo.clickOnActionBarItem(R.id.action_new_message);
        //Restart activity
        getInstrumentation().callActivityOnRestart(solo.getCurrentActivity());
        //Assert that ContactsActivity is opened
        solo.assertCurrentActivity("Expected ContactsActivity", ContactsActivity.class);
        //Find added contact
        ListView ListView = (ListView) solo.getView(R.id.list_contacts);
        View view = ContactsTest.findStringInView(CONTACT_NAME, ListView);
        //Select contact added
        solo.clickOnView(view);
        //Restart activity
        getInstrumentation().callActivityOnRestart(solo.getCurrentActivity());
        //Assert that ChatActivity activity is opened
        solo.assertCurrentActivity("Expected ChatActivity", ChatActivity.class);
        //In text field 0, enter message
        solo.enterText(0, MESSAGE);
        //Click send message
        solo.clickOnView(solo.getView(R.id.send_message));
        //wait 2 sec
        solo.sleep(10000);
    }

    private void createContact() throws Exception {
        //Click menu item contacts
        solo.clickOnMenuItem(solo.getString(R.string.contacts));
        //Assert that NoteEditor activity is opened
        solo.assertCurrentActivity("Expected ContactsActivity", ContactsActivity.class);
        //Click add contact
        solo.clickOnView(solo.getView(com.securesms.R.id.buttonReceiveAdd));
        //In text field 0, enter contact name
        solo.enterText(0, CONTACT_NAME);
        //In text field 1, contact number
        solo.enterText(1, CONTACT_NUMBER);
        //In text field 2, contact password
        solo.enterText(2, CONTACT_PASSWORD);
        //Click add contact
        solo.clickOnView(solo.getView(android.R.id.button1));
        //Click home button
        solo.clickOnActionBarHomeButton();
        //Assert that MainActivity is opened
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
    }

}
