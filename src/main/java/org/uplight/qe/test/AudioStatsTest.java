package org.uplight.qe.test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AudioStatsTest extends BaseSetup{


    private static final String INCORRECT_MESSAGE = "Incorrect authentication credentials.";

    @Test
    public void audioStatsSuccess(){
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + accessToken)
                .get(BASE_URL + AUDO_STATS);
        Assertions.assertEquals(200, response.getStatusCode());
        String firstSource = response.jsonPath().getString("[0].source_name");
        Assertions.assertEquals("jamendo", firstSource);
        String secondSource = response.jsonPath().getString("[1].source_name");
        Assertions.assertEquals( "wikimedia_audio", secondSource);
        String thirdSource = response.jsonPath().getString("[2].source_name");
        Assertions.assertEquals("freesound", thirdSource);
    }

    @Test
    public void audioStatsWithInvalidCred(){

        Response response = RestAssured.given()
                .header("Authorization", "Bearer invalidToken")
                .get(BASE_URL + AUDO_STATS);
        Assertions.assertEquals(401, response.getStatusCode());
        String msg = response.jsonPath().get("detail").toString();
        Assertions.assertEquals(INCORRECT_MESSAGE, msg);
    }
}
