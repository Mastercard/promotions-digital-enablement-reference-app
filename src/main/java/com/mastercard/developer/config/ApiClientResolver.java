package com.mastercard.developer.config;

import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
public class ApiClientResolver {

    @Autowired
    private ApiClientProvider apiClientProvider;

    @Autowired
    private Environment environment;

    public ApiClient resolveApiClient() {
        String[] activeProfiles = environment.getActiveProfiles();
        log.info("Active profiles: {}", Arrays.toString(activeProfiles));

        if (Arrays.asList(activeProfiles).contains("oauth2")) {
            log.info("Resolving ApiClient using OAuth2 configuration");
        } else if (Arrays.asList(activeProfiles).contains("oauth1")) {
            log.info("Resolving ApiClient using OAuth1 configuration");
        } else {
            log.warn("No recognized auth profile active. Defaulting to available ApiClientProvider.");
        }

        return apiClientProvider.createNewApiClient();
    }
}
