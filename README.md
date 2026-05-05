# Reference Implementation for Mastercard Promotions Digital Enablement

[![](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/Mastercard/promotions-digital-enablement-reference-app/blob/master/LICENSE)

## Table of Contents
- [Overview](#overview)
  * [Compatibility](#compatibility)
  * [References](#references)
- [Usage](#usage)
  * [Prerequisites](#prerequisites)
  * [Configuration](#configuration)
    - [Authentication Mode Selection](#authentication-mode-selection)
    - [OAuth 1.0a Setup](#oauth-1-setup)
    - [OAuth 2.0 Setup](#oauth-2-setup)
    - [Encryption and Decryption Configuration](#encryption-decryption-config)
  * [Integrating with OpenAPI Generator](#integrating-with-openapi-generator)
  * [Build and Execute](#build-and-execute)
  * [Testing](#testing)
- [Architecture](#architecture)
  * [Authentication Flow](#authentication-flow)
  * [Profile-Based Configuration](#profile-based-configuration)
- [Use Cases](#use-cases)
- [API Reference](#api-reference)
  * [Authorization](#authorization)
  * [Encryption and Decryption](#encryption-and-decryption)
    - [Loading Encryption Certificate](#loading-encryption-certificate)
    - [Loading Decryption Key](#loading-decryption-key)
    - [Configuring JWE Instance](#configuring-jwe-instance)
    - [Encrypting Entire Payloads](#encrypting-entire-payloads)
    - [Decrypting Entire Payloads](#decrypting-entire-payloads)
  * [Recommendation](#recommendation)
- [Support](#support)
- [License](#license)

## Overview <a name="overview"></a>
This is a reference application to demonstrate how Promotion Digital Enablement API can be used for the supported operations. Please see here for details on the API: [Mastercard Developers](https://developer.mastercard.com/rewards-progress/documentation).

This application supports **two authentication modes**:
- **OAuth 1.0a** — Signature-based authentication using a consumer key and `.p12` signing key
- **OAuth 2.0** — Token-based authentication with DPoP (Demonstrating Proof-of-Possession) using the FAPI 2.0 Security Profile

Authentication mode is controlled via Spring Profiles (`oauth1` or `oauth2`).

### Compatibility <a name="compatibility"></a>
* [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or later
* [Spring Boot 2.6+](https://spring.io/projects/spring-boot)
* [Apache Maven 3.3+](https://maven.apache.org/download.cgi)

### References <a name="references"></a>
* [Mastercard's OAuth 1.0a Signer library](https://github.com/Mastercard/oauth1-signer-java)
* [Mastercard's OAuth 2.0 Client library](https://github.com/Mastercard/oauth2-client-java)
* [Using OAuth 1.0a to Access Mastercard APIs](https://developer.mastercard.com/platform/documentation/using-oauth-1a-to-access-mastercard-apis/)
* [Using OAuth 2.0 to Access Mastercard APIs](https://developer.mastercard.com/platform/documentation/security-and-authentication/using-oauth-20/)

## Usage <a name="usage"></a>
### Prerequisites <a name="prerequisites"></a>
* [Mastercard Developers Account](https://developer.mastercard.com/dashboard) with access to Promotion Digital Enablement API
* A text editor or IDE (IntelliJ IDEA recommended)
* [Java 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
* [Apache Maven 3.3+](https://maven.apache.org/download.cgi)
* Set up the `JAVA_HOME` environment variable to match the location of your Java 17 installation.

### Configuration <a name="configuration"></a>

#### Authentication Mode Selection <a name="authentication-mode-selection"></a>

This application uses Spring Profiles to switch between OAuth 1.0a and OAuth 2.0. Set the active profile in `application.properties`:

```properties
# Options: oauth1, oauth2 (default)
spring.profiles.active=oauth2
```

---

#### OAuth 1.0a Setup <a name="oauth-1-setup"></a>

To use OAuth 1.0a authentication:

1. Set `spring.profiles.active=oauth1` in `application.properties`
2. Create a project on [Mastercard Developers](https://developer.mastercard.com/dashboard) and download the signing key (`.p12` file)
3. Copy the `.p12` file to `src/main/resources/`
4. Configure the following properties:

```properties
spring.profiles.active=oauth1

# Path to PKCS12 keystore (.p12) file
mastercard.api.key-file-path=src/main/resources/your-signing-key.p12

# Consumer key from Mastercard Developers project page
mastercard.api.consumer-key=your-consumer-key

# Key alias (default: keyalias)
mastercard.api.keystore-alias=keyalias

# Keystore password (default: keystorepassword)
mastercard.api.keystore-password=keystorepassword
```

**How it works:** The `ApiClientConfiguration` class (active with `oauth1` profile) uses `OkHttpOAuth1Interceptor` to sign each outgoing HTTP request with an OAuth 1.0a signature header.

---

#### OAuth 2.0 Setup <a name="oauth-2-setup"></a>

To use OAuth 2.0 authentication with DPoP:

1. Set `spring.profiles.active=oauth2` in `application.properties`
2. Create a project on [Mastercard Developers](https://developer.mastercard.com/dashboard) that has been upgraded to OAuth 2.0
3. Download the signing key (`.p12` file) and note the **Client ID** and **Key ID (kid)** from the portal
4. Copy the `.p12` file to `src/main/resources/`
5. Configure the following properties:

```properties
spring.profiles.active=oauth2

# Path to PKCS12 keystore (.p12) file (same key used for OAuth 1.0a signing)
mastercard.api.key-file-path=src/main/resources/your-signing-key.p12

# Key alias (default: keyalias)
mastercard.api.keystore-alias=keyalias

# Keystore password (default: keystorepassword)
mastercard.api.keystore-password=keystorepassword

# OAuth 2.0 Client ID from Mastercard Developers portal
mastercard.api.oauth2.client.id=your-client-id

# Key ID (kid) from Mastercard Developers portal
mastercard.api.oauth2.kid=your-key-id

# Token endpoint URL
mastercard.api.oauth2.token.url=https://sandbox.api.mastercard.com/oauth/token

# Issuer URL
mastercard.api.oauth2.issuer.url=https://sandbox.api.mastercard.com

# OAuth 2.0 scopes (comma-separated, from your project's scope list)
mastercard.api.oauth2.scope=rewards-api-gateway:promotions-digital-enablement_read
```

**How it works:** The `ApiClientOAuth2Configuration` class (active with `oauth2` profile) uses the `OAuth2Interceptor` from the `oauth2-client-java` library. It:
1. Creates a signed JWT client assertion using your `.p12` private key
2. Generates an ephemeral EC key pair for DPoP proof
3. Requests an access token from the token endpoint
4. Attaches both the `Authorization: DPoP <token>` and `DPoP` proof headers to each API request
5. Caches tokens in-memory and refreshes them automatically when expired

**Environment URLs:**

| Environment | Base Path | Token URL |
|-------------|-----------|-----------|
| Sandbox | `https://sandbox.api.mastercard.com/loyalty/rewards` | `https://sandbox.api.mastercard.com/oauth/token` |
| Stage | `https://stage.api.mastercard.com/loyalty/rewards` | `https://stage.api.mastercard.com/oauth/token` |
| Production | `https://api.mastercard.com/loyalty/rewards` | `https://api.mastercard.com/oauth/token` |

---

#### Encryption and Decryption Configuration <a name="encryption-decryption-config"></a>

Both OAuth 1.0a and OAuth 2.0 modes support JWE payload encryption. Configure the following:

```properties
# Encryption certificate (.pem) — for encrypting request payloads
mastercard.api.encryption-certificate-file-path=src/main/resources/encryption-key.pem

# Decryption key (.p12) — for decrypting response payloads
mastercard.api.decryption-key-file-path=src/main/resources/decryption-key.p12

# Decryption key alias
mastercard.api.decryption-key-alias=keyalias

# Decryption keystore password
mastercard.api.decryption-keystore-password=keystorepassword
```

Download these keys from the "API Keys" section of your project in the [Mastercard Developers](https://developer.mastercard.com/dashboard) portal:
- **Client Encryption Key** — `.pem` file for encrypting payloads
- **Mastercard Encryption Key** — `.p12` file for decrypting responses

---

### Integrating with OpenAPI Generator <a name="integrating-with-openapi-generator"></a>
[OpenAPI Generator](https://github.com/OpenAPITools/openapi-generator) generates API client libraries from [OpenAPI Specs](https://github.com/OAI/OpenAPI-Specification).
It provides generators and library templates for supporting multiple languages and frameworks.

See also:
* [OpenAPI Generator (maven Plugin)](https://mvnrepository.com/artifact/org.openapitools/openapi-generator-maven-plugin)
* [OpenAPI Generator (executable)](https://mvnrepository.com/artifact/org.openapitools/openapi-generator-cli)
* [CONFIG OPTIONS for java](https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/java.md)

#### OpenAPI Generator Plugin Configuration
```xml
<!-- https://mvnrepository.com/artifact/org.openapitools/openapi-generator-maven-plugin -->
<plugin>
    <groupId>org.openapitools</groupId>
    <artifactId>openapi-generator-maven-plugin</artifactId>
    <version>${openapi-generator.version}</version>
    <executions>
        <execution>
            <id>Promotions Digital Enablement API Client</id>
            <goals>
                <goal>generate</goal>
            </goals>
            <configuration>
                <inputSpec>${project.basedir}/src/main/resources/Promotions_Digital_Enablement-api-spec.yaml</inputSpec>
                <generatorName>java</generatorName>
                <library>okhttp-gson</library>
                <generateApiTests>false</generateApiTests>
                <generateModelTests>false</generateModelTests>
                <configOptions>
                    <sourceFolder>src/gen/main/java</sourceFolder>
                    <dateLibrary>java8</dateLibrary>
                </configOptions>
            </configuration>
        </execution>
    </executions>
</plugin>
```

#### Generating The API Client Sources
Now that you have all the dependencies you need, you can generate the sources. To do this, use one of the following two methods:

`Using IDE`
* **Method 1**<br/>
  In IntelliJ IDEA, open the Maven window **(View > Tool Windows > Maven)**. Click the icons `Reimport All Maven Projects` and `Generate Sources and Update Folders for All Projects`

* **Method 2**<br/>
  In the same menu, navigate to the commands **({Project name} > Lifecycle)**, select `clean` and `compile` then click the icon `Run Maven Build`.

`Using Terminal`
* Navigate to the root directory of the project within a terminal window and execute `mvn clean compile` command.

### Build and Execute <a name="build-and-execute"></a>
Once you've added the correct properties, we can build the application. We can do this by navigating to the project's base directory from the terminal and running the following command:

1. Run `mvn clean install` from the root of the project directory.
2. Run `mvn spring-boot:run` to start the project.
3. Navigate to [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) in your browser.

### Testing <a name="testing"></a>

#### Verify OAuth 2.0 Authentication

Once the application is running with `spring.profiles.active=oauth2`, test with:

```bash
# List promotions (requires a valid rewardsCompanyId, programId, promotionId, or accountId)
curl -X GET "http://localhost:8080/promotions?programId=YOUR_PROGRAM_ID" \
  -H "accept: application/json"

# Get promotion details
curl -X GET "http://localhost:8080/promotions/{promotionId}/details?include_audience=false" \
  -H "accept: application/json"

# Search accounts (requires encrypted payload)
curl -X POST "http://localhost:8080/accounts/searches" \
  -H "Content-Type: application/json" \
  -H "accept: application/json" \
  -d '{"accountId": "your-account-id"}'
```

**Expected behavior:**
- If authentication succeeds, you will receive a business-level response (200 or a business error like `INVALID_FIELD_PROMOTION_ID`)
- If authentication fails, you will see errors like `invalid_client` or `client_assertion signature couldn't be verified`

#### Verify OAuth 1.0a Authentication

Switch to `spring.profiles.active=oauth1`, configure the consumer key and `.p12` file, then test with the same curl commands above.

#### Common Errors

| Error | Cause | Fix |
|-------|-------|-----|
| `invalid_client` / `client_assertion signature couldn't be verified` | `.p12` key doesn't match the client ID/kid | Ensure all credentials are from the same project |
| `insufficient_scope` | Token scope doesn't match API requirements | Verify scopes in `application.properties` match your portal |
| `FORBIDDEN` / `User does not have required permissions` | Client not authorized for the resource | Check your project has access to the specific programId/data |
| `NoClassDefFoundError: tools/jackson/core/type/TypeReference` | Missing Jackson 3.x dependency | Ensure `jackson-databind:3.0.3` and `jackson-annotations:2.19.4` are in pom.xml |

---

## Architecture <a name="architecture"></a>

### Authentication Flow <a name="authentication-flow"></a>

```
┌─────────────────────────────────────────────────────────────────┐
│                    Profile: oauth1                                │
├─────────────────────────────────────────────────────────────────┤
│ Request → OkHttpJweEncryptionInterceptor (encrypt payload)      │
│         → OkHttpOAuth1Interceptor (sign request)                │
│         → Mastercard API                                        │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                    Profile: oauth2                                │
├─────────────────────────────────────────────────────────────────┤
│ Request → OkHttpJweEncryptionInterceptor (encrypt payload)      │
│         → OAuth2Interceptor (obtain token + DPoP proof)         │
│         → HttpLoggingInterceptor (log request/response)         │
│         → Mastercard API                                        │
└─────────────────────────────────────────────────────────────────┘
```

### Profile-Based Configuration <a name="profile-based-configuration"></a>

```
ApiClientProvider (interface)
  └── BaseApiClientConfiguration (abstract - shared key loading)
       ├── ApiClientConfiguration (@Profile("oauth1"))
       │     └── Provides: apiClient, cryptoApiClient beans
       └── ApiClientOAuth2Configuration (@Profile("oauth2"), @Primary)
             └── Provides: apiClient, cryptoApiClient beans

ApiClientResolver (resolves client based on active profile)
```

| Class | Profile | Description |
|-------|---------|-------------|
| `BaseApiClientConfiguration` | — | Abstract base; loads `.p12` signing key, builds base `ApiClient` |
| `ApiClientConfiguration` | `oauth1` | Configures `OkHttpOAuth1Interceptor` for signature-based auth |
| `ApiClientOAuth2Configuration` | `oauth2` | Configures `OAuth2Interceptor` with DPoP for token-based auth |
| `ApiClientResolver` | — | Resolves the correct `ApiClient` based on active Spring profile |
| `ApiClientProvider` | — | Interface defining `getApiClient()` and `createNewApiClient()` |

---

## Swagger <a name="Swagger"></a>
Navigate to [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) in your browser to access the following controllers:
1. **AccountController**
    - `POST /accounts/searches`
2. **OptInController**
    - `POST /promotion-activations`
    - `GET /promotions`
3. **PromotionDetailController**
    - `GET /promotions/{id}/details`
4. **ProgressController**
    - `GET /promotion-progresses`
5. **EventController**
    - `GET /events`
    - `POST /events`
6. **AudienceController**
    - `GET /audiences`
    - `POST /audiences`
    - `PUT /audiences/{id}`
    - `DELETE /audiences/{id}`
7. **TransactionController**
    - `GET /transactions`

## Use Cases <a name="use-cases"></a>
Refer [Use Cases](https://developer.mastercard.com/rewards-progress/documentation/use-cases/) for more details.

## API Reference <a name="api-reference"></a>
Refer [Api Reference](https://developer.mastercard.com/rewards-progress/documentation/api-reference/) to develop a client application that consumes a RESTful Promotion Digital Enablement API with Spring Boot.

### Authorization <a name="authorization"></a>
The `com.mastercard.developer.interceptors` package provides request interceptor classes for configuring your API client. These classes handle adding the correct `Authorization` header before sending the request.

- **OAuth 1.0a**: `OkHttpOAuth1Interceptor` — adds an `Authorization: OAuth ...` signature header
- **OAuth 2.0**: `OAuth2Interceptor` — obtains a DPoP-bound access token and adds `Authorization: DPoP <token>` + `DPoP` proof headers

### Encryption and Decryption <a name="encryption-and-decryption"></a>
The `com.mastercard.developer.crypto.interceptor` package provides `OkHttpJweEncryptionInterceptor` that encrypts request payloads and decrypts response payloads using JWE (JSON Web Encryption).

#### Loading Encryption Certificate <a name="loading-encryption-certificate"></a>

A `Certificate` object can be created from a file by calling `EncryptionUtils.loadEncryptionCertificate`:
```java
Certificate encryptionCertificate = EncryptionUtils.loadEncryptionCertificate("<insert certificate file path>");
```

Supported certificate formats: PEM, DER.

#### Loading Decryption Key <a name="loading-decryption-key"></a>

##### From a PKCS#12 Key Store

A `PrivateKey` object can be created from a PKCS#12 key store by calling `EncryptionUtils.loadDecryptionKey` the following way:
```java
PrivateKey decryptionKey = EncryptionUtils.loadDecryptionKey(
                                    "<insert PKCS#12 key file path>",
                                    "<insert key alias>",
                                    "<insert key password>");
```

#### Configuring JWE Instance <a name="configuring-jwe-instance"></a>
Use the `JweConfigBuilder` to create `JweConfig` instances. Example:
```java
JweConfig jweConfig = JweConfigBuilder.aJweEncryptionConfig()
        .withEncryptionCertificate(certificate)
        .withEncryptionPath("$", "$")
        .withEncryptedValueFieldName("encryptedPayload")
        .withDecryptionKey(decryptionKey)
        .build();
```

#### Encrypting Entire Payloads <a name="encrypting-entire-payloads"></a>

Example using the configuration [above](#configuring-jwe-instance):
```java
String payload = "{" +
    "    \"sensitiveField1\": \"sensitiveValue1\"," +
    "    \"sensitiveField2\": \"sensitiveValue2\"" +
    "}";
```

Output:
```json
{
    "encryptedPayload": "eyJhbGciOiJSU0EtT0(...)IsImVuYyI6IkEyNTifQ.OKOawDo13gRp2ojaHV7LFpZcg(...)VZKTyKOMTYUmKoTCVJRgckCL9kiMT03JGe.48V1_ALb6US04U3b.5eym8TW_c8SuK0ltJ3rpYI(...)7TALvtu6UG9oMo4vpzs9tX_EFShS8iB7j6ji.XFBoMYUZodetZdvTiFvSkQ"
}
```

#### Decrypting Entire Payloads <a name="decrypting-entire-payloads"></a>

Example using the configuration [above](#configuring-jwe-instance):
```java
String encryptedPayload = "{" +
    "  \"encryptedPayload\": \"eyHhbGciOiJSU0EtT0F(...)BiYzQyTIyNTQ1ODgzNSJ9.VkO7N6gAptqoD7IQaK(...)ptYySP_TuvERby89DY1EezAm3A.qj6ISyzq1ASDJKD0.ENF7bUfBkoWAEYvk(...)o9JGMctx-PSdeVqwCQAVRNj0pYs1WjOp4UDbE4eEZIF6YA.Wc7ARH7R6sikpKzxET3MYA\" +
    "}";
```

Output:
```json
{
    "sensitiveField1": "sensitiveValue1",
    "sensitiveField2": "sensitiveValue2"
}
```

### Recommendation <a name="recommendation"></a>
It is recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.

## Support <a name="support"></a>
If you would like further information, please send an email to apisupport@mastercard.com

## License <a name="license"></a>
Copyright 2020 Mastercard

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0
