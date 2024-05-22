package org.uplight.qe.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.uplight.qe.util.RequestBodyBuildUtil;
import org.uplight.qe.util.EmailServiceUtil;

public abstract class BaseSetup {

    protected static String clientId;
    protected static String clientSecret;
    protected static String accessToken;
    protected static final String BASE_URL = "https://api.openverse.engineering";
    protected static final String REGISTER = "/v1/auth_tokens/register/";
    protected static final String ACCESS_TOKEN = "/v1/auth_tokens/token/";
    protected static final String AUDIO_SEARCH = "/v1/audio/";
    protected static final String AUDO_STATS = "/v1/audio/stats/";
    protected static final String TOKEN_GRANT_TYPE = "client_credentials";
    protected static final String REGISTER_MSG = "Check your email for a verification link.";
    protected static final String VERIFY_MSG = "Successfully verified email. Your OAuth2 credentials are now active.";

    @BeforeAll
    public void setup(){

        registerValidApp();
        String emailId = EmailServiceUtil.fetchLatestUnReadEmailIdFromOpenverse();
        String verifyURL = EmailServiceUtil.fetchVerifyLinkFromEmailMessageWithId(emailId);
        clickVerifyLinkFromEmail(verifyURL);
        getAccessToken();
    }

    public static void registerValidApp(){
        // Register an application to access to API via OAuth2.
        String appMetaData = RequestBodyBuildUtil.createValidAppMetaData();
        System.out.println("... App Meata Data created as: " + appMetaData);

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .and()
                .body(appMetaData)
                .when()
                .post(BASE_URL + REGISTER);

        Assertions.assertEquals(201, response.getStatusCode());

        clientId = response.jsonPath().get("client_id");
        Assertions.assertNotNull(clientId);

        clientSecret = response.jsonPath().get("client_secret");
        Assertions.assertNotNull(clientSecret);

        String msg = response.jsonPath().get("msg");
        Assertions.assertEquals(REGISTER_MSG, msg);

        System.out.printf("... Client ID: %s%n... Client Secret: %s%n", clientId, clientSecret);

    }

    public static void clickVerifyLinkFromEmail(String verifyURL){

        Response response = RestAssured.get(verifyURL);
        Assertions.assertEquals(200, response.getStatusCode());
        String msg = response.jsonPath().get("msg");
        Assertions.assertEquals(VERIFY_MSG, msg);
        System.out.println("... Verify Message: " + msg);
    }

    public static void getAccessToken(){

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .formParam("grant_type", TOKEN_GRANT_TYPE)
                .formParam("client_id", clientId)
                .formParam("client_secret", clientSecret)
                .when()
                .post(BASE_URL + ACCESS_TOKEN);
        Assertions.assertEquals(200, response.getStatusCode());
        accessToken = response.jsonPath().get("access_token");
        Assertions.assertNotNull(accessToken);
        System.out.println("... Access Token: " + accessToken);
    }
}
