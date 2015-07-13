package com.securesms;

import android.content.Context;
import android.telephony.SmsManager;
import android.util.Base64;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.securesms.items.MessageItem;
import com.securesms.items.ReceiverItem;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Sebastian Sokolowski on 2015-07-08
 */
public class AlgoritmAES {
    private Cipher cipher;
    private SecretKeySpec key;
    private AlgorithmParameterSpec spec;

    public AlgoritmAES()
    {

    }

    public AlgoritmAES(String password) throws Exception
    {
        // hash password with SHA-256 and crop the output to 128-bit for key
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(password.getBytes("UTF-8"));
        byte[] keyBytes = new byte[32];
        System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);

        cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        key = new SecretKeySpec(keyBytes, "AES");
        spec = getIV();
    }
    public void setPassword(String password) throws Exception
    {
        // hash password with SHA-256 and crop the output to 128-bit for key
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(password.getBytes("UTF-8"));
        byte[] keyBytes = new byte[32];
        System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);

        cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        key = new SecretKeySpec(keyBytes, "AES");
        spec = getIV();
    }
    public AlgorithmParameterSpec getIV()
    {
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
        IvParameterSpec ivParameterSpec;
        ivParameterSpec = new IvParameterSpec(iv);

        return ivParameterSpec;
    }

    public String encrypt(String plainText) throws Exception
    {
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
        String encryptedText = new String(Base64.encode(encrypted, Base64.DEFAULT), "UTF-8");
        System.out.println("Encrypt Data"+ encryptedText);
        return encryptedText;
    }

    public String decrypt(String cryptedText) throws Exception
    {
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] bytes = Base64.decode(cryptedText, Base64.DEFAULT);
        byte[] decrypted = cipher.doFinal(bytes);
        String decryptedText = new String(decrypted, "UTF-8");
        System.out.println("Encrypt Data"+ decryptedText);
        return decryptedText;
    }

    public void send_message(String number, String message,Context context ) {
        DbAdapter dbHelper = new DbAdapter(context);
        dbHelper.open();
        ReceiverItem receiver_item = dbHelper.searchRowReceiverNumber(number);

        try {
            this.setPassword(receiver_item.code);

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, "!encsms"+encrypt(message), null, null);
            Toast.makeText(context,context.getString(R.string.send_sms),Toast.LENGTH_LONG).show();

            //zapisanie wiadomosci do bazy danych
            MessageItem tmp = new MessageItem();
            tmp.text=message;
            tmp.rec=1;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            tmp.date = sdf.format(new Date());
            tmp.id_receivers=receiver_item.id;
            tmp.read=0;
            dbHelper.createRowMessage(tmp);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context,context.getString(R.string.error_send_sms),Toast.LENGTH_LONG).show();
        }finally {

            dbHelper.close();
        }
    }

    public String receive_message(ReceiverItem receiverItem, String message,Context context ) {

        MessageItem tmp = new MessageItem();
        try {
            this.setPassword(receiverItem.code);
            tmp.rec = 0;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            tmp.date = sdf.format(new Date());
            tmp.id_receivers = receiverItem.id;
            tmp.read = 1;
            tmp.text = this.decrypt(message);
        } catch (Exception e) {
            tmp.text = context.getString(R.string.error_desc_sms);
            e.printStackTrace();
        }
        finally {
            DbAdapter dbHelper = new DbAdapter(context);
            dbHelper.open();
            dbHelper.createRowMessage(tmp);
            dbHelper.close();
        }
        return tmp.text;
    }
}
