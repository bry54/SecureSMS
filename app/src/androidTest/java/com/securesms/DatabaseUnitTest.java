package com.securesms;

import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;

import com.securesms.chat.model.MessageModel;
import com.securesms.contacts.model.ReceiverUserModel;
import com.securesms.database.DbAdapter;

import junit.framework.TestCase;

/**
 * Created by admin on 2016-06-06.
 */
public class DatabaseUnitTest extends TestCase {
    private String RECEIVER_NAME = "TEST_USER";
    private String RECEIVER_NUMBER = "12121212";
    private String RECEIVER_PASSWORD = "1nHASCuhsakNSchbajas";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @MediumTest
    public void testReceiverRow() {
        //Create database instance
        DbAdapter dbAdapter = DbAdapter.INSTANCE;
        //Open database connection
        dbAdapter.open();
        //Create model to insert
        ReceiverUserModel receiverUserModel = new ReceiverUserModel();
        receiverUserModel.setName(RECEIVER_NAME);
        receiverUserModel.setNumber(RECEIVER_NUMBER);
        receiverUserModel.setPassword(RECEIVER_PASSWORD);
        //Insert model to database
        dbAdapter.createRowReceiver(receiverUserModel);

        //Get model from database
        ReceiverUserModel userModel = dbAdapter.searchRowReceiverNumber(RECEIVER_NUMBER);
        //Validate model
        assertEquals(RECEIVER_NAME,userModel.getName());
        assertEquals(RECEIVER_NUMBER,userModel.getNumber());
        assertEquals(RECEIVER_PASSWORD,userModel.getPassword());

        //Remove inserted row
        dbAdapter.deleteRowReceiver(userModel);

        //Check if row still exist
        assertEquals(null,dbAdapter.searchRowReceiverNumber(RECEIVER_NUMBER));

        //Close database connection
        dbAdapter.close();
    }

    @MediumTest
    public void testMessageRow(){
        //Create database instance
        DbAdapter dbAdapter = DbAdapter.INSTANCE;
        //Open database connection
        dbAdapter.open();
        //Create model to insert

        MessageModel messageModel = new MessageModel();
        messageMode
        dbAdapter.createRowMessage()
    }
}
