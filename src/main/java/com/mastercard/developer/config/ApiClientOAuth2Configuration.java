package com.mastercard.developer.config;

import com.mastercard.developer.crypto.interceptor.OkHttpJweEncryptionInterceptor;
import com.mastercard.developer.encryption.JweConfig;
import com.mastercard.developer.encryption.JweConfigBuilder;
import com.mastercard.developer.oauth2.config.OAuth2Config;
import com.mastercard.developer.oauth2.config.SecurityProfile;
import com.mastercard.developer.oauth2.core.access_token.InMemoryAccessTokenStore;
import com.mastercard.developer.oauth2.core.dpop.DPoPKeyProvider;
import com.mastercard.developer.oauth2.core.dpop.StaticDPoPKeyProvider;
import com.mastercard.developer.oauth2.core.scope.StaticScopeResolver;
import com.mastercard.developer.oauth2.http.okhttp3.OAuth2Interceptor;
import com.mastercard.developer.utils.EncryptionUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.openapitools.client.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.net.URL;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.spec.ECGenParameterSpec;
import java.time.Duration;
import java.util.Set;

@Primary
@Profile("oauth2")
@Slf4j
@Configuration
public class ApiClientOAuth2Configuration extends BaseApiClientConfiguration {

    @Value("${mastercard.api.oauth2.client.id}")
    private String clientId;

    @Value("${mastercard.api.oauth2.kid}")
    private String kid;

    @Value("${mastercard.api.oauth2.token.url}")
    private String tokenUrl;

    @Value("${mastercard.api.oauth2.issuer.url}")
    private String issuerUrl;

    @Value("${mastercard.api.oauth2.scope:promotions-digital-enablement:read}")
    private String scope;

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
            OAuth2Config oauth2Config = OAuth2Config.builder()
                    .securityProfile(SecurityProfile.FAPI2SP_PRIVATE_KEY_DPOP)
                    .clientId(clientId)
                    .clientKey(loadSigningKey())
                    .tokenEndpoint(new URL(tokenUrl))
                    .issuer(new URL(issuerUrl))
                    .kid(kid)
                    .accessTokenStore(new InMemoryAccessTokenStore())
                    .scopeResolver(new StaticScopeResolver(Set.of(scope.split(","))))
                    .dpopKeyProvider(initDpopKeyProvider())
                    .clockSkewTolerance(Duration.ofSeconds(60))
                    .build();

            OkHttpClient.Builder okHttpClientBuilder = buildHttpClientBuilder(apiClient);
            okHttpClientBuilder.addInterceptor(new OAuth2Interceptor(oauth2Config));

            var loggingInterceptor = new HttpLoggingInterceptor(log::info);
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClientBuilder.addInterceptor(loggingInterceptor);

            return apiClient.setHttpClient(okHttpClientBuilder.build());
        } catch (Exception e) {
            log.error("Failed to initialize OAuth2 ApiClient", e);
        }
        return apiClient;
    }

    @Bean
    @Primary
    public ApiClient apiClient() {
        return createNewApiClient();
    }

    @Bean
    public ApiClient cryptoApiClient() {
        ApiClient apiClient = buildBaseApiClient();
        try {
            OAuth2Config oauth2Config = OAuth2Config.builder()
                    .securityProfile(SecurityProfile.FAPI2SP_PRIVATE_KEY_DPOP)
                    .clientId(clientId)
                    .clientKey(loadSigningKey())
                    .tokenEndpoint(new URL(tokenUrl))
                    .issuer(new URL(issuerUrl))
                    .kid(kid)
                    .accessTokenStore(new InMemoryAccessTokenStore())
                    .scopeResolver(new StaticScopeResolver(Set.of(scope.split(","))))
                    .dpopKeyProvider(initDpopKeyProvider())
                    .clockSkewTolerance(Duration.ofSeconds(60))
                    .build();

            OkHttpClient.Builder okHttpClientBuilder = buildHttpClientBuilder(apiClient);

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

            okHttpClientBuilder.addInterceptor(new OAuth2Interceptor(oauth2Config));

            var loggingInterceptor = new HttpLoggingInterceptor(log::info);
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClientBuilder.addInterceptor(loggingInterceptor);

            return apiClient.setHttpClient(okHttpClientBuilder.build());
        } catch (Exception e) {
            log.error("Failed to initialize OAuth2 CryptoApiClient", e);
        }
        return apiClient;
    }

    private DPoPKeyProvider initDpopKeyProvider() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
        kpg.initialize(new ECGenParameterSpec("secp256r1"), new SecureRandom());
        KeyPair kp = kpg.generateKeyPair();
        return new StaticDPoPKeyProvider(kp);
    }
}
