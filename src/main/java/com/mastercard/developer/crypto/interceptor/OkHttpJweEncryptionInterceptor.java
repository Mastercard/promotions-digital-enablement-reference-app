package com.mastercard.developer.crypto.interceptor;

import com.mastercard.developer.encryption.EncryptionException;
import com.mastercard.developer.encryption.JweConfig;
import com.mastercard.developer.encryption.JweEncryption;
import com.mastercard.developer.interceptors.OkHttpEncryptionInterceptor;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpJweEncryptionInterceptor extends OkHttpEncryptionInterceptor {

    private final JweConfig jweConfig;

    public OkHttpJweEncryptionInterceptor(JweConfig config) {
        this.jweConfig = config;
    }

    @Override
    protected String encryptPayload(Request request, Request.Builder builder, String requestPayload) throws EncryptionException {
        return JweEncryption.encryptPayload(requestPayload, this.jweConfig);
    }

    @Override
    protected String decryptPayload(Response response, Response.Builder builder, String responsePayload) {
        return responsePayload;
    }
}
