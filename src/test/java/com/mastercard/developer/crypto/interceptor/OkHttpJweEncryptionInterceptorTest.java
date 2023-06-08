package com.mastercard.developer.crypto.interceptor;

import com.mastercard.developer.encryption.EncryptionException;
import com.mastercard.developer.encryption.JweConfig;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OkHttpJweEncryptionInterceptorTest {

    @Mock
    private JweConfig jweConfig;


    @Mock
    private Request.Builder builder;

    @Mock
    Response.Builder responseBuilder;

    @InjectMocks
    private OkHttpJweEncryptionInterceptor interceptor;

    @Test(expected = EncryptionException.class)
    public void testEncryptPayload() throws EncryptionException {
        String payload = "payload";
        String encryptedPayload = "encrypted-payload";
        interceptor = new OkHttpJweEncryptionInterceptor(jweConfig);
        String result = interceptor.encryptPayload(null, builder, payload);
        Assert.assertEquals(encryptedPayload, result);
    }

    @Test
    public void testDecryptPayload() throws EncryptionException {
        String payload = "payload";
        interceptor = new OkHttpJweEncryptionInterceptor(jweConfig);
        String result = interceptor.decryptPayload(null, responseBuilder, payload);
        Assert.assertEquals(payload, result);
    }
}
