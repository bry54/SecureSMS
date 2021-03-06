package com.securesms;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.robotium.solo.Solo;
import com.securesms.contacts.ContactsActivity;

/**
 * Created by admin on 2016-06-06.
 */
public class ContactsTest extends ActivityInstrumentationTestCase2<ContactsActivity> {
    private Solo solo;

    public ContactsTest() {
        super(ContactsActivity.class);
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
        final String CONTACT_NAME = "TEST_USER";
        final String CONTACT_NUMBER = "500000000";
        final String CONTACT_PASSWORD = "HASLO";

        final String CONTACT_NAME2 = "TEST_USER2";
        final String CONTACT_NUMBER2 = "500000001";
        final String CONTACT_PASSWORD2 = "HASLO2";
        /*
        Add new Contact
         */

        //Unlock the lock screen
        solo.unlockScreen();
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
        assertEquals(CONTACT_NAME, solo.getEditText(0).getText().toString());
        assertEquals(CONTACT_NUMBER, solo.getEditText(1).getText().toString());
        assertEquals(CONTACT_PASSWORD, solo.getEditText(2).getText().toString());
        //Click on cancel button
        solo.clickOnView(solo.getView(android.R.id.button2));

        /*
        Edit contact
         */
        //Click on edit button
        ListView = (ListView) solo.getView(R.id.list_contacts);
        view = findStringInView(CONTACT_NAME, ListView);
        editButton = (ImageButton) view.findViewById(R.id.editReceiver);
        solo.clickOnView(editButton);
        //Clear entered values
        solo.clearEditText(0);
        solo.clearEditText(1);
        solo.clearEditText(2);
        //Enter new values
        solo.enterText(0, CONTACT_NAME2);
        solo.enterText(1, CONTACT_NUMBER2);
        solo.enterText(2, CONTACT_PASSWORD2);
        //Click save edit contact
        solo.clickOnView(solo.getView(android.R.id.button1));
        //Click on edit button
        ListView = (ListView) solo.getView(R.id.list_contacts);
        view = findStringInView(CONTACT_NAME2, ListView);
        editButton = (ImageButton) view.findViewById(R.id.editReceiver);
        solo.clickOnView(editButton);
        //Check values
        assertEquals(CONTACT_NAME2, solo.getEditText(0).getText().toString());
        assertEquals(CONTACT_NUMBER2, solo.getEditText(1).getText().toString());
        assertEquals(CONTACT_PASSWORD2, solo.getEditText(2).getText().toString());
        //Click on cancel button
        solo.clickOnView(solo.getView(android.R.id.button2));

        //Delete contact
        deleteContact(CONTACT_NAME2);

        //Check if item still exist
        ListView = (ListView) solo.getView(R.id.list_contacts);
        assertEquals(null, findStringInView(CONTACT_NAME2, ListView));
    }

    private void deleteContact(String contactName) throws Exception{
        ListView listView= (ListView) solo.getView(R.id.list_contacts);
        View view = findStringInView(contactName, listView);
        //Delete test contact
        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.deleteReceiver);
        solo.clickOnView(deleteButton);
        //Click yes
        solo.clickOnView(solo.getView(android.R.id.button1));
    }

    public void testAddInvalidNameContact() throws Exception{
        final String CONTACT_NAME = "";
        final String CONTACT_NUMBER = "500000000";
        final String CONTACT_PASSWORD = "password";

        solo.clickOnView(solo.getView(com.securesms.R.id.buttonReceiveAdd));
        //In text field 0, enter contact name
        solo.enterText(0, CONTACT_NAME);
        //In text field 1, contact number
        solo.enterText(1, CONTACT_NUMBER);
        //In text field 2, contact password
        solo.enterText(2, CONTACT_PASSWORD);
        //Click add contact
        solo.clickOnView(solo.getView(android.R.id.button1));
        //Check toast message
        assertTrue(solo.waitForText(solo.getString(R.string.add_contact_invalid_username)));
    }

    public void testAddValidNameContact() throws Exception{
        final String CONTACT_NAME = "1";
        final String CONTACT_NUMBER = "500000000";
        final String CONTACT_PASSWORD = "password";

        solo.clickOnView(solo.getView(com.securesms.R.id.buttonReceiveAdd));
        //In text field 0, enter contact name
        solo.enterText(0, CONTACT_NAME);
        //In text field 1, contact number
        solo.enterText(1, CONTACT_NUMBER);
        //In text field 2, contact password
        solo.enterText(2, CONTACT_PASSWORD);
        //Click add contact
        solo.clickOnView(solo.getView(android.R.id.button1));
        //Check toast message
        assertTrue(solo.waitForText(solo.getString(R.string.add_contact_successful)));

        //Delete contact
        deleteContact(CONTACT_NAME);
    }

    public void testAddValidNameContact2() throws Exception{
        final String CONTACT_NAME = "12345678901234567890";
        final String CONTACT_NUMBER = "500000000";
        final String CONTACT_PASSWORD = "password";

        solo.clickOnView(solo.getView(com.securesms.R.id.buttonReceiveAdd));
        //In text field 0, enter contact name
        solo.enterText(0, CONTACT_NAME);
        //In text field 1, contact number
        solo.enterText(1, CONTACT_NUMBER);
        //In text field 2, contact password
        solo.enterText(2, CONTACT_PASSWORD);
        //Click add contact
        solo.clickOnView(solo.getView(android.R.id.button1));
        //Check toast message
        assertTrue(solo.waitForText(solo.getString(R.string.add_contact_successful)));

        //Delete contact
        deleteContact(CONTACT_NAME);
    }

    public void testAddInvalidNameContact2() throws Exception{
        final String CONTACT_NAME = "123456789012345678901";
        final String CONTACT_NUMBER = "500000000";
        final String CONTACT_PASSWORD = "password";

        solo.clickOnView(solo.getView(com.securesms.R.id.buttonReceiveAdd));
        //In text field 0, enter contact name
        solo.enterText(0, CONTACT_NAME);
        //In text field 1, contact number
        solo.enterText(1, CONTACT_NUMBER);
        //In text field 2, contact password
        solo.enterText(2, CONTACT_PASSWORD);
        //Click add contact
        solo.clickOnView(solo.getView(android.R.id.button1));
        //Check toast message
        assertTrue(solo.waitForText(solo.getString(R.string.add_contact_invalid_username)));
    }

    public void testAddInvalidNumberContact() throws Exception{
        final String CONTACT_NAME = "user1212";
        final String CONTACT_NUMBER = "12345678";
        final String CONTACT_PASSWORD = "password";

        solo.clickOnView(solo.getView(com.securesms.R.id.buttonReceiveAdd));
        //In text field 0, enter contact name
        solo.enterText(0, CONTACT_NAME);
        //In text field 1, contact number
        solo.enterText(1, CONTACT_NUMBER);
        //In text field 2, contact password
        solo.enterText(2, CONTACT_PASSWORD);
        //Click add contact
        solo.clickOnView(solo.getView(android.R.id.button1));
        //Check toast message
        assertTrue(solo.waitForText(solo.getString(R.string.add_contact_invalid_number)));
    }
    public void testAddValidNumberContact() throws Exception{
        final String CONTACT_NAME = "user1212";
        final String CONTACT_NUMBER = "123456789";
        final String CONTACT_PASSWORD = "password";

        solo.clickOnView(solo.getView(com.securesms.R.id.buttonReceiveAdd));
        //In text field 0, enter contact name
        solo.enterText(0, CONTACT_NAME);
        //In text field 1, contact number
        solo.enterText(1, CONTACT_NUMBER);
        //In text field 2, contact password
        solo.enterText(2, CONTACT_PASSWORD);
        //Click add contact
        solo.clickOnView(solo.getView(android.R.id.button1));
        //Check toast message
        assertTrue(solo.waitForText(solo.getString(R.string.add_contact_successful)));

        //Delete contact
        deleteContact(CONTACT_NAME);
    }
    public void testAddValidNumberContact2() throws Exception{
        final String CONTACT_NAME = "user1212";
        final String CONTACT_NUMBER = "1234567890";
        final String CONTACT_PASSWORD = "password";

        solo.clickOnView(solo.getView(com.securesms.R.id.buttonReceiveAdd));
        //In text field 0, enter contact name
        solo.enterText(0, CONTACT_NAME);
        //In text field 1, contact number
        solo.enterText(1, CONTACT_NUMBER);
        //In text field 2, contact password
        solo.enterText(2, CONTACT_PASSWORD);
        //Click add contact
        solo.clickOnView(solo.getView(android.R.id.button1));
        //Check toast message
        assertTrue(solo.waitForText(solo.getString(R.string.add_contact_successful)));

        //Delete contact
        deleteContact(CONTACT_NAME);
    }
    public void testAddInvalidNumberContact2() throws Exception{
        final String CONTACT_NAME = "user1212";
        final String CONTACT_NUMBER = "12345678901";
        final String CONTACT_PASSWORD = "password";

        solo.clickOnView(solo.getView(com.securesms.R.id.buttonReceiveAdd));
        //In text field 0, enter contact name
        solo.enterText(0, CONTACT_NAME);
        //In text field 1, contact number
        solo.enterText(1, CONTACT_NUMBER);
        //In text field 2, contact password
        solo.enterText(2, CONTACT_PASSWORD);
        //Click add contact
        solo.clickOnView(solo.getView(android.R.id.button1));
        //Check toast message
        assertTrue(solo.waitForText(solo.getString(R.string.add_contact_invalid_number)));
    }

    public void testAddInvalidPasswordContact() throws Exception{
        final String CONTACT_NAME = "user1212";
        final String CONTACT_NUMBER = "500000000";
        final String CONTACT_PASSWORD = "";

        solo.clickOnView(solo.getView(com.securesms.R.id.buttonReceiveAdd));
        //In text field 0, enter contact name
        solo.enterText(0, CONTACT_NAME);
        //In text field 1, contact number
        solo.enterText(1, CONTACT_NUMBER);
        //In text field 2, contact password
        solo.enterText(2, CONTACT_PASSWORD);
        //Click add contact
        solo.clickOnView(solo.getView(android.R.id.button1));
        //Check toast message
        assertTrue(solo.waitForText(solo.getString(R.string.add_contact_invalid_password)));
    }
    public void testAddValidPasswordContact() throws Exception{
        final String CONTACT_NAME = "user1212";
        final String CONTACT_NUMBER = "500000000";
        final String CONTACT_PASSWORD = "1";

        solo.clickOnView(solo.getView(com.securesms.R.id.buttonReceiveAdd));
        //In text field 0, enter contact name
        solo.enterText(0, CONTACT_NAME);
        //In text field 1, contact number
        solo.enterText(1, CONTACT_NUMBER);
        //In text field 2, contact password
        solo.enterText(2, CONTACT_PASSWORD);
        //Click add contact
        solo.clickOnView(solo.getView(android.R.id.button1));
        //Check toast message
        assertTrue(solo.waitForText(solo.getString(R.string.add_contact_successful)));

        //Delete contact
        deleteContact(CONTACT_NAME);
    }
    public void testAddValidPasswordContact2() throws Exception{
        final String CONTACT_NAME = "user1212";
        final String CONTACT_NUMBER = "500000000";
        final String CONTACT_PASSWORD = "12345678901234567890";

        solo.clickOnView(solo.getView(com.securesms.R.id.buttonReceiveAdd));
        //In text field 0, enter contact name
        solo.enterText(0, CONTACT_NAME);
        //In text field 1, contact number
        solo.enterText(1, CONTACT_NUMBER);
        //In text field 2, contact password
        solo.enterText(2, CONTACT_PASSWORD);
        //Click add contact
        solo.clickOnView(solo.getView(android.R.id.button1));
        //Check toast message
        assertTrue(solo.waitForText(solo.getString(R.string.add_contact_successful)));

        //Delete contact
        deleteContact(CONTACT_NAME);
    }

    public void testAddInvalidPasswordContact2() throws Exception{
        final String CONTACT_NAME = "user1212";
        final String CONTACT_NUMBER = "500000000";
        final String CONTACT_PASSWORD = "123456789012345678901";

        solo.clickOnView(solo.getView(com.securesms.R.id.buttonReceiveAdd));
        //In text field 0, enter contact name
        solo.enterText(0, CONTACT_NAME);
        //In text field 1, contact number
        solo.enterText(1, CONTACT_NUMBER);
        //In text field 2, contact password
        solo.enterText(2, CONTACT_PASSWORD);
        //Click add contact
        solo.clickOnView(solo.getView(android.R.id.button1));
        //Check toast message
        assertTrue(solo.waitForText(solo.getString(R.string.add_contact_invalid_password)));
    }

    public static View findStringInView(String text, ListView ListView) {
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
}
