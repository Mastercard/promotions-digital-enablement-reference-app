package com.mastercard.developer.config;

import com.mastercard.developer.utils.AuthenticationUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.openapitools.client.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;

@Slf4j
@Component
public abstract class BaseApiClientConfiguration implements ApiClientProvider {

    @Value("${mastercard.api.base-path}")
    protected String basePath;

    @Value("${mastercard.api.keystore-alias}")
    protected String signingKeyAlias;

    @Value("${mastercard.api.keystore-password}")
    protected String signingKeyPassword;

    @Value("${mastercard.api.key-file-path:#{null}}")
    protected String signingKeyPkcs12Path;

    protected ApiClient buildBaseApiClient() {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(basePath);
        apiClient.setDebugging(true);
        apiClient.setReadTimeout(40000);
        return apiClient;
    }

    protected PrivateKey loadSigningKey() throws Exception {
        return AuthenticationUtils.loadSigningKey(signingKeyPkcs12Path, signingKeyAlias, signingKeyPassword);
    }

    protected OkHttpClient.Builder buildHttpClientBuilder(ApiClient apiClient) {
        return apiClient.getHttpClient().newBuilder();
    }
}
