package org.uplight.qe.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.uplight.qe.util.RequestBodyBuildUtil;

import static io.restassured.RestAssured.given;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthRegisterTest extends BaseSetup {

    private final static String INVALID_APP_NAME_MSG = "Ensure this field has no more than 150 characters.";
    private final static String INVALID_EMAIL_MSG = "Enter a valid email address.";


    private String validAppMetaData;
    private String invalidAppNameData;
    private String invalidAppDescData;
    private String invalidAppWithBadEmail;
    private String invalidAppNameAndDescData;

    @BeforeAll
    @Override
    public void setup(){

        // override super setup so we need customerized the setup for the register api

        // init valid app data
        validAppMetaData = RequestBodyBuildUtil.createValidAppMetaData();

        // init  invalid app data name is more than 150 characters
        invalidAppNameData = RequestBodyBuildUtil.createInvalidAppNameData();

        // init  invalid app data with descrip is more than 10000 characters
        invalidAppDescData = RequestBodyBuildUtil.createInvalidAppDescData();

        // init invalid app data with bad email address
        invalidAppWithBadEmail = RequestBodyBuildUtil.createInvalidAppEmail();

        // init invalid app data with both bad name and bad description
        invalidAppNameAndDescData = RequestBodyBuildUtil.createInvalidAppNameAndDescData();

    }

    @Test
    public void registerAppWithValidData() {

        // Register an application to access to API via OAuth2.
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .and()
                .body(validAppMetaData)
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

    @Test
    public void registerAppWithInvalidName() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .and()
                .body(invalidAppNameData)
                .when()
                .post(BASE_URL + REGISTER);

        Assertions.assertEquals(400, response.getStatusCode());

        String msg = response.jsonPath().get("name").toString();
        Assertions.assertTrue(msg.contains(INVALID_APP_NAME_MSG));
    }

    //todo
    @Test
    public void registerAppWithInvalidDescription() {
    }

    //todo
    @Test
    public void registerAppWithInvalidAppNameAndDescData() {
    }


    @Test
    public void registerAppWithInvalidEmailAddress() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .and()
                .body(invalidAppWithBadEmail)
                .when()
                .post(BASE_URL + REGISTER);
        Assertions.assertEquals(400, response.getStatusCode());
        String msg = response.jsonPath().get("email").toString();
        Assertions.assertTrue(msg.contains(INVALID_EMAIL_MSG));
    }


    // this test will be disabled for now because it will cause the api not accepting any new request for cool down time
    @Test
    @Disabled
    public void registerAppWithTooManyRequest() {

        for (int i = 0; i < 9; i++) {
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .and()
                    .body(validAppMetaData)
                    .when()
                    .post(BASE_URL + REGISTER);
        }
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .and()
                .body(validAppMetaData)
                .when()
                .post(BASE_URL + REGISTER);

        Assertions.assertEquals(429, response.getStatusCode());
    }

}
