# AuthenticationApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**apiV1AuthRefreshPost**](AuthenticationApi.md#apiV1AuthRefreshPost) | **POST** /api/v1/auth/refresh | Refresh JWT token |
| [**apiV1AuthSigninPost**](AuthenticationApi.md#apiV1AuthSigninPost) | **POST** /api/v1/auth/signin | Authenticate user and return JWT |
| [**apiV1AuthSignupPost**](AuthenticationApi.md#apiV1AuthSignupPost) | **POST** /api/v1/auth/signup | Register a new user |


<a id="apiV1AuthRefreshPost"></a>
# **apiV1AuthRefreshPost**
> RefreshResponse apiV1AuthRefreshPost(refreshRequest)

Refresh JWT token

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.AuthenticationApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");

    AuthenticationApi apiInstance = new AuthenticationApi(defaultClient);
    RefreshRequest refreshRequest = new RefreshRequest(); // RefreshRequest | Refresh token payload
    try {
      RefreshResponse result = apiInstance.apiV1AuthRefreshPost(refreshRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling AuthenticationApi#apiV1AuthRefreshPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **refreshRequest** | [**RefreshRequest**](RefreshRequest.md)| Refresh token payload | |

### Return type

[**RefreshResponse**](RefreshResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | New JWT returned |  -  |
| **401** | Invalid refresh token |  -  |

<a id="apiV1AuthSigninPost"></a>
# **apiV1AuthSigninPost**
> SigninResponse apiV1AuthSigninPost(signinRequest)

Authenticate user and return JWT

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.AuthenticationApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");

    AuthenticationApi apiInstance = new AuthenticationApi(defaultClient);
    SigninRequest signinRequest = new SigninRequest(); // SigninRequest | Signin payload
    try {
      SigninResponse result = apiInstance.apiV1AuthSigninPost(signinRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling AuthenticationApi#apiV1AuthSigninPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **signinRequest** | [**SigninRequest**](SigninRequest.md)| Signin payload | |

### Return type

[**SigninResponse**](SigninResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | JWT returned |  -  |
| **401** | Unauthorized |  -  |

<a id="apiV1AuthSignupPost"></a>
# **apiV1AuthSignupPost**
> SignupResponse apiV1AuthSignupPost(signupRequest)

Register a new user

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.AuthenticationApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:8080");

    AuthenticationApi apiInstance = new AuthenticationApi(defaultClient);
    SignupRequest signupRequest = new SignupRequest(); // SignupRequest | Signup payload
    try {
      SignupResponse result = apiInstance.apiV1AuthSignupPost(signupRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling AuthenticationApi#apiV1AuthSignupPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **signupRequest** | [**SignupRequest**](SignupRequest.md)| Signup payload | |

### Return type

[**SignupResponse**](SignupResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Successfully registered |  -  |
| **400** | Bad request |  -  |

