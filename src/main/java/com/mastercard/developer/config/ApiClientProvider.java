package com.mastercard.developer.config;

import org.openapitools.client.ApiClient;

public interface ApiClientProvider {
    ApiClient getApiClient();
    ApiClient createNewApiClient();
}
