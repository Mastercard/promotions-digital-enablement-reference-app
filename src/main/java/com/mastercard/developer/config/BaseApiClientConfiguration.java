package com.mastercard.developer.config;

import com.mastercard.developer.utils.AuthenticationUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.openapitools.client.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
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

    @Value("${mastercard.api.downstream-route:#{null}}")
    protected String downstreamRoute;

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
        OkHttpClient.Builder builder = apiClient.getHttpClient().newBuilder();
        if (downstreamRoute != null && !downstreamRoute.isEmpty()) {
            builder.addInterceptor(new DownstreamRouteInterceptor(downstreamRoute));
        }
        return builder;
    }

    private static class DownstreamRouteInterceptor implements Interceptor {
        private final String route;

        DownstreamRouteInterceptor(String route) {
            this.route = route;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder()
                    .addHeader("downstream-route", route)
                    .build();
            return chain.proceed(request);
        }
    }
}
