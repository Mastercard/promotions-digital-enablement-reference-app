package com.mastercard.developer.config;

import com.mastercard.developer.crypto.interceptor.OkHttpJweEncryptionInterceptor;
import com.mastercard.developer.encryption.JweConfig;
import com.mastercard.developer.encryption.JweConfigBuilder;
import com.mastercard.developer.interceptors.OkHttpOAuth1Interceptor;
import com.mastercard.developer.utils.EncryptionUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.openapitools.client.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.security.PrivateKey;
import java.security.cert.Certificate;

/**
 * OAuth 1.0a ApiClient configuration. Active when spring.profiles.active=oauth1.
 */
@Profile("oauth1")
@Slf4j
@Configuration
public class ApiClientConfiguration extends BaseApiClientConfiguration {

    @Value("${mastercard.api.consumer-key}")
    private String consumerKey;

    @Value("${mastercard.api.encryption-certificate-file-path:#{null}}")
    private String encryptionCertificateFilePath;

    @Value("${mastercard.api.decryption-key-file-path:#{null}}")
    private String decryptionKeyFilePath;

    @Value("${mastercard.api.decryption-key-alias:#{null}}")
    private String decryptionKeyAlias;

    @Value("${mastercard.api.decryption-keystore-password:#{null}}")
    private String decryptionKeystorePassword;

    @Override
    public ApiClient getApiClient() {
        return createNewApiClient();
    }

    @Override
    public ApiClient createNewApiClient() {
        ApiClient apiClient = buildBaseApiClient();
        try {
            OkHttpClient.Builder okHttpClientBuilder = buildHttpClientBuilder(apiClient);
            okHttpClientBuilder.addInterceptor(new OkHttpOAuth1Interceptor(consumerKey, loadSigningKey()));
            return apiClient.setHttpClient(okHttpClientBuilder.build());
        } catch (Exception e) {
            log.error("Failed to initialize OAuth1 ApiClient: {}", e.getMessage());
        }
        return apiClient;
    }

    @Bean
    public ApiClient apiClient() {
        return createNewApiClient();
    }

    @Bean
    public ApiClient cryptoApiClient() {
        ApiClient cryptoApiClient = buildBaseApiClient();
        try {
            OkHttpClient.Builder okHttpClientBuilder = buildHttpClientBuilder(cryptoApiClient);

            // Add JWE encryption if configured
            if (encryptionCertificateFilePath != null && decryptionKeyFilePath != null) {
                Certificate certificate = EncryptionUtils.loadEncryptionCertificate(encryptionCertificateFilePath);
                PrivateKey decryptionKey = EncryptionUtils.loadDecryptionKey(
                        decryptionKeyFilePath, decryptionKeyAlias, decryptionKeystorePassword);

                JweConfig jweConfig = JweConfigBuilder.aJweEncryptionConfig()
                        .withEncryptionCertificate(certificate)
                        .withEncryptionPath("$", "$")
                        .withEncryptedValueFieldName("encryptedPayload")
                        .withDecryptionKey(decryptionKey)
                        .build();

                okHttpClientBuilder.addInterceptor(new OkHttpJweEncryptionInterceptor(jweConfig));
            }

            okHttpClientBuilder.addInterceptor(new OkHttpOAuth1Interceptor(consumerKey, loadSigningKey()));
            return cryptoApiClient.setHttpClient(okHttpClientBuilder.build());
        } catch (Exception e) {
            log.error("Failed to initialize OAuth1 CryptoApiClient: {}", e.getMessage());
        }
        return cryptoApiClient;
    }
}
