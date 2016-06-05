package com.securesms;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.robotium.solo.Solo;
import com.securesms.contacts.ContactsActivity;
import com.securesms.main.MainActivity;
import com.securesms.newMessage.NewMessageActivity;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private static final String CONTACT_NAME = "TEST_USER";
    private static final String CONTACT_NUMBER = "530787034";
    private static final String CONTACT_PASSWORD = "HASLO";


    private Solo solo;

    public ApplicationTest() {
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

    public void testAddContact() throws Exception {
        //Unlock the lock screen
        solo.unlockScreen();
        //Click on action menu contacts
        solo.clickOnMenuItem("Kontakty");
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

        //Click on edit button
        ListView ListView = (ListView) solo.getView(R.id.list_contacts);
        View view = findStringInView(CONTACT_NAME, ListView);
        ImageButton editButton = (ImageButton) view.findViewById(R.id.editReceiver);
        solo.clickOnView(editButton);

        //Check values
        assertEquals(CONTACT_NAME,solo.getEditText(0).getText().toString());
        assertEquals(CONTACT_NUMBER,solo.getEditText(1).getText().toString());
        assertEquals(CONTACT_PASSWORD,solo.getEditText(2).getText().toString());

        //Click cancel edit contact
        solo.clickOnView(solo.getView(android.R.id.button2));

        //Delete test contact
        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.deleteReceiver);
        solo.clickOnView(deleteButton);
        //Click yes
        solo.clickOnView(solo.getView(android.R.id.button1));

        //Check if item still exist
        ListView = (ListView) solo.getView(R.id.list_contacts);
        assertEquals(null,findStringInView(CONTACT_NAME, ListView));

//        TextView toast = (TextView) solo.getView(android.R.id.message);
//        assertEquals("toast is not showing", toast.getText().toString(), solo.getString(R.string.error_invalid_phone));
    }

    private View findStringInView(String text, ListView ListView) {
        for (int i = 0; i != ListView.getChildCount(); i++) {
            View view = ListView.getChildAt(i);
            TextView tv = (TextView) view
                    .findViewById(R.id.receiverName);
            String findText = tv.getText().toString();
            if (findText.equals(text)) {
                return view;
            }
        }
        return null;
    }

    public void testNewMessage() throws Exception {
        //Unlock the lock screen
        solo.unlockScreen();
        //Click on action menu contacts
        solo.clickOnView(solo.getView(com.securesms.R.id.action_new_message));
        //Assert that NoteEditor activity is opened
        solo.assertCurrentActivity("Expected NewMessageActivity", NewMessageActivity.class);
    }
}