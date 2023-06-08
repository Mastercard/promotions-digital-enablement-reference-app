package com.mastercard.developer.config;

import com.mastercard.developer.crypto.interceptor.OkHttpJweEncryptionInterceptor;
import com.mastercard.developer.encryption.JweConfig;
import com.mastercard.developer.encryption.JweConfigBuilder;
import com.mastercard.developer.interceptors.OkHttpOAuth1Interceptor;
import com.mastercard.developer.utils.AuthenticationUtils;
import com.mastercard.developer.utils.EncryptionUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.PrivateKey;
import java.security.cert.Certificate;

/**
 * This is ApiClient configuration, it will read properties from application.properties and create instance of ApiClient.
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(MastercardProperties.class)
public class ApiClientConfiguration {

    private final MastercardProperties mcProperties;
    private final OkHttpOAuth1Interceptor okHttpOAuth1Interceptor;

    @Autowired
    @SneakyThrows
    public ApiClientConfiguration(MastercardProperties mcProperties) {
        this.mcProperties = mcProperties;
        PrivateKey signingKey = AuthenticationUtils.loadSigningKey(mcProperties.getKeyFile().getFile().getAbsolutePath(), mcProperties.getKeystoreAlias(), mcProperties.getKeystorePassword());
        this.okHttpOAuth1Interceptor = new OkHttpOAuth1Interceptor(mcProperties.getConsumerKey(), signingKey);
    }

    @Bean
    public ApiClient apiClient() {
        ApiClient apiClient = newApiClient();
        return apiClient.setHttpClient(apiClient.getHttpClient()
                .newBuilder()
                .addInterceptor(okHttpOAuth1Interceptor)
                .build()
        );
    }

    @Bean
    @SneakyThrows
    public ApiClient cryptoApiClient() {
        ApiClient cryptoApiClient = newApiClient();

        Certificate certificate = EncryptionUtils.loadEncryptionCertificate(mcProperties.getEncryptionCertificateFile().getFile().getAbsolutePath());
        PrivateKey decryptionKey = EncryptionUtils.loadDecryptionKey(mcProperties.getDecryptionKeyFile().getFile().getAbsolutePath(), mcProperties.getDecryptionKeyAlias(), mcProperties.getDecryptionKeystorePassword());

        JweConfig jweConfig = JweConfigBuilder.aJweEncryptionConfig()
                .withEncryptionCertificate(certificate)
                .withEncryptionPath("$", "$")
                .withEncryptedValueFieldName("encryptedPayload")
                .withDecryptionKey(decryptionKey)
                .build();

        return cryptoApiClient.setHttpClient(cryptoApiClient.getHttpClient()
                .newBuilder()
                .addInterceptor(new OkHttpJweEncryptionInterceptor(jweConfig))
                .addInterceptor(okHttpOAuth1Interceptor)
                .build()
        );
    }

    private ApiClient newApiClient() {
        ApiClient client = new ApiClient();
        client.setBasePath(mcProperties.getBasePath());
        client.setDebugging(true);
        client.setReadTimeout(40000);
        return client;
    }
}
