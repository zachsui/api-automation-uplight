package org.uplight.qe.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.uplight.qe.util.EmailServiceUtil;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthTokenTest extends BaseSetup{

    @BeforeAll
    @Override
    public void setup(){
        registerValidApp();
        String emailId = EmailServiceUtil.fetchLatestUnReadEmailIdFromOpenverse();
        String verifyURL = EmailServiceUtil.fetchVerifyLinkFromEmailMessageWithId(emailId);
        clickVerifyLinkFromEmail(verifyURL);
    }


    // happy path 200 call
    @Test
    public void getAccessTokenSuccess(){

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

    // no grant type provided 400
    @Test
    public void getAccessTokenBadRequest(){

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .formParam("client_id", clientId)
                .formParam("client_secret", clientSecret)
                .when()
                .post(BASE_URL + ACCESS_TOKEN);
        Assertions.assertEquals(400, response.getStatusCode());
        String msg = response.jsonPath().get("error").toString();
        Assertions.assertTrue(msg.contains("unsupported_grant_type"));
    }

    // bad client or bad client secret provided 401
    @Test
    public void getAccessTokenUnauthorized(){

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .formParam("client_id", "bad-client-id")
                .formParam("client_secret", clientSecret)
                .when()
                .post(BASE_URL + ACCESS_TOKEN);
        Assertions.assertEquals(401, response.getStatusCode());
        String msg = response.jsonPath().get("error").toString();
        Assertions.assertTrue(msg.contains("invalid_client"));
    }
}
