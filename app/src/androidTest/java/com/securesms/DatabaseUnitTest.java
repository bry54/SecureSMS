package com.securesms;

import android.test.suitebuilder.annotation.MediumTest;

import com.securesms.chat.model.MessageModel;
import com.securesms.contacts.model.ReceiverUserModel;
import com.securesms.database.DbAdapter;

import junit.framework.TestCase;

import java.util.List;

/**
 * Created by admin on 2016-06-06.
 */
public class DatabaseUnitTest extends TestCase {
    private String RECEIVER_NAME = "TEST_USER";
    private String RECEIVER_NUMBER = "12121212";
    private String RECEIVER_PASSWORD = "1nHASCuhsakNSchbajas";

    private int MESSAGE_ID_REC = 13;
    private String MESSAGE_TEXT = "TEST TEST TEST TEST TEST";
    private boolean MESSAGE_READ = false;
    private int MESSAGE_REC = 33;
    private String MESSAGE_DATE = "2016-12-12";

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
        assertEquals(RECEIVER_NAME, userModel.getName());
        assertEquals(RECEIVER_NUMBER, userModel.getNumber());
        assertEquals(RECEIVER_PASSWORD, userModel.getPassword());

        //Remove inserted row
        dbAdapter.deleteRowReceiver(userModel);

        //Check if row still exist
        assertEquals(null, dbAdapter.searchRowReceiverNumber(RECEIVER_NUMBER));

        //Close database connection
        dbAdapter.close();
    }

    @MediumTest
    public void testMessageRow() {
        //Create database instance
        DbAdapter dbAdapter = DbAdapter.INSTANCE;
        //Open database connection
        dbAdapter.open();
        //Create model to insert
        MessageModel messageModel = new MessageModel();
        messageModel.setDate(MESSAGE_DATE);
        messageModel.setId_receivers(MESSAGE_ID_REC);
        messageModel.setText(MESSAGE_TEXT);
        messageModel.setRec(MESSAGE_REC);
        messageModel.setRead(MESSAGE_READ);
        //Insert model to database
        dbAdapter.createRowMessage(messageModel);

        //Get model from database
        List<MessageModel> tmpModel = dbAdapter.getRowsMessagesRec(MESSAGE_ID_REC);
        MessageModel model = tmpModel.get(0);
        //Validate model
        assertEquals(MESSAGE_ID_REC, model.getId_receivers());
        assertEquals(MESSAGE_TEXT, model.getText());
        assertEquals(MESSAGE_DATE, model.getDate());
        assertEquals(MESSAGE_READ, model.getReadBoolean());
        assertEquals(MESSAGE_REC, model.getRec());

        //Check is message is read
        assertEquals(true,dbAdapter.isNotReadMessage(MESSAGE_ID_REC));
        //Set message read
        dbAdapter.setReadMessages(MESSAGE_ID_REC);
        assertEquals(false,dbAdapter.isNotReadMessage(MESSAGE_ID_REC));

        //Remove inserted row
        dbAdapter.deleteRowMessage(model);
        //Check if row still exist
        assertEquals(0, dbAdapter.getCountMessageReceiver(MESSAGE_ID_REC));
        //Close database connection
        dbAdapter.close();
    }
}
