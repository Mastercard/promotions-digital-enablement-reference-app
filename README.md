# Promotions Digital Enablement API Reference Implementation

## Frameworks / Libraries used

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Apache Maven](https://maven.apache.org/index.html)
- [Mockito](https://site.mockito.org/)

--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

## Requirements
- JDK 17
- Set up the [JAVA_HOME](https://explainjava.com/java-path/) environment variable to match the location of your Java installation.
- Set up the [MAVEN_HOME](https://dzone.com/articles/installing-maven) environment variable to match the location of your Maven bin folder.

--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

## Authentication Profiles

This reference app supports two authentication strategies. The active profile is controlled by the `spring.profiles.active` property in `application.properties`.

### OAuth 2.0 (Default — `oauth2`)

Uses OAuth 2.0 with DPoP (Demonstrating Proof of Possession) and private key JWT authentication, following the FAPI 2.0 Security Profile. This is the **recommended and default** authentication method.

Required properties:
```properties
spring.profiles.active=oauth2

mastercard.api.key-file-path=<path-to-signing-key.p12>
mastercard.api.keystore-alias=<key-alias>
mastercard.api.keystore-password=<keystore-password>

mastercard.api.oauth2.client.id=<your-oauth2-client-id>
mastercard.api.oauth2.kid=<your-key-id>
mastercard.api.oauth2.token.url=https://sandbox.api.mastercard.com/oauth/token
mastercard.api.oauth2.issuer.url=https://sandbox.api.mastercard.com
mastercard.api.oauth2.scope=rewards-api-gateway:promotions-digital-enablement_read
```

### OAuth 1.0a (`oauth1`)

Uses OAuth 1.0a with RSA-SHA256 signatures. This is an older authentication method.

To switch to this profile, update `spring.profiles.active=oauth1` in `application.properties` and provide the OAuth 1.0a-specific keys below.

Required properties:
```properties
spring.profiles.active=oauth1

mastercard.api.key-file=<path-to-signing-key.p12>
mastercard.api.consumer-key=<your-consumer-key>
mastercard.api.keystore-alias=<key-alias>
mastercard.api.keystore-password=<keystore-password>
```

The `mastercard.api.consumer-key` is only required for OAuth 1.0a. It can be found under Sandbox/Production Keys on your project page at the [Mastercard Developer Portal](https://developer.mastercard.com).

----------------------------------------------------------------------------------------------------------------------------------------

## Common Setup (Both Profiles)
1. Open Mastercard Developers website and create an account at [Mastercard Developers](https://developer.mastercard.com).
2. Create a new project [here](https://developer.mastercard.com/dashboard).
3. Add the `Promotions Digital Enablement` API to your project and click continue.
4. Configure project and click continue.
5. Download Sandbox Signing Key.
6. A `.p12` file is downloaded automatically. **Note**: On Safari, the file name will be `Unknown`. Rename it to a `.p12` extension.
7. Copy the downloaded `.p12` file to `src/main/resources` folder in the code.
8. Download Client Encryption Key.
9. Copy the downloaded `.pem` file to `src/main/resources` folder in the code.
10. Copy Client Encryption Key Fingerprint.

### OAuth 2.0 Setup (Default)
1. Open `src/main/resources/application.properties` and configure the following (OAuth 2.0 is already set as the default profile):
    - `spring.profiles.active` = oauth2
    - `mastercard.api.key-file-path` - Path to the `.p12` keystore file.
    - `mastercard.api.keystore-alias` - Key alias. Default key alias for sandbox is `keyalias`.
    - `mastercard.api.keystore-password` - Keystore password. Default keystore password for sandbox project is `keystorepassword`.
    - `mastercard.api.oauth2.client.id` - OAuth 2.0 Client ID. Copy this from "Sandbox/Production Keys" on your project page.
    - `mastercard.api.oauth2.kid` - Key ID. This is the same as the key alias. Default key alias for sandbox is `keyalias`.
    - `mastercard.api.oauth2.token.url` - OAuth 2.0 token endpoint URL. For sandbox environment, use `https://sandbox.api.mastercard.com/oauth/token`.
    - `mastercard.api.oauth2.issuer.url` - OAuth 2.0 issuer URL. For sandbox environment, use `https://sandbox.api.mastercard.com`.
    - `mastercard.api.oauth2.scope` - OAuth 2.0 scopes. Use `rewards-api-gateway:promotions-digital-enablement_read`.
2. Run `mvn clean install` from the root of the project directory.
3. Run `mvn spring-boot:run` to start the project.
4. Navigate to [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) in your browser.

### OAuth 1.0a Setup
1. Open `src/main/resources/application.properties` and configure the following:
    - `spring.profiles.active` = oauth1
    - `mastercard.api.key-file` - Path to the `.p12` keystore file.
    - `mastercard.api.consumer-key` - OAuth 1.0a Consumer Key. Copy this from "Sandbox/Production Keys" on your project page.
    - `mastercard.api.keystore-alias` - Key alias. Default key alias for sandbox is `keyalias`.
    - `mastercard.api.keystore-password` - Keystore password. Default keystore password for sandbox project is `keystorepassword`.

    **Note**: The `mastercard.api.consumer-key` is only required for OAuth 1.0a. It can be found under Sandbox/Production Keys on your project page at the Mastercard Developer Portal.
2. Run `mvn clean install` from the root of the project directory.
3. Run `mvn spring-boot:run` to start the project.
4. Navigate to [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) in your browser.

### Encryption and Decryption Configuration

Both profiles require encryption/decryption configuration for payload security:

```properties
mastercard.api.encryption-certificate-file=<path-to-encryption.pem>
mastercard.api.encryption-key-fingerprint=<fingerprint>
mastercard.api.decryption-key-file-path=<path-to-decryption.p12>
mastercard.api.decryption-keystore-alias=<alias>
mastercard.api.decryption-keystore-password=<password>
```

**Note**: Most of the fields in swagger are populated with example data. The fields that are left unpopulated should be populated with data specific to your promotions program.

--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

## Tutorial

A tutorial can be found [here](https://developer.mastercard.com/promotions-digital-enablement/documentation/tutorials-and-guides/) for setting up and using this service.

--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

## Service Documentation

Promotions Digital Enablement service documentation can be found [here](https://developer.mastercard.com/promotions-digital-enablement/documentation/)

--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

## Contact us

Please send an email to [apisupport@mastercard.com]() with any questions or feedback you may have.
