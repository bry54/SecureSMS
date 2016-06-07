package com.securesms;

import com.securesms.utils.AlgorithmAES;

import junit.framework.TestCase;

/**
 * Created by admin on 2016-06-06.
 */
public class AlgorithmAESUnitTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void test1() throws Exception {
        String message = "Takie cos z takim czym bez takiego czegos. Algo i nie";
        String password = "sdfasdfas34rawwrczekolaasdfasdfas34rawwrczeaasdfasdfas34rawwr";

        testAlgorithm(password,message);
    }

    public void test2() throws Exception {
        String message = "Takie cos z takim czym bez takiego czegos. Żar mi wpadł do wózka.";
        String password = "";

        testAlgorithm(password,message);
    }

    public void test3() throws Exception {
        String message = "";
        String password = "chocolate";

        testAlgorithm(password,message);
    }

    private void testAlgorithm(String password, String message) throws Exception {
        AlgorithmAES.initInstance();
        AlgorithmAES algorithmAES = AlgorithmAES.INSTANCE;

        algorithmAES.setPassword(password);

        String encMessage = algorithmAES.encrypt(message);
        String decMessage = algorithmAES.decrypt(encMessage);
        assertEquals(message, decMessage);
    }
}
