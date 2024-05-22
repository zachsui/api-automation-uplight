package org.uplight.qe.test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.UUID;
import java.util.regex.Pattern;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AudioSearchTest extends BaseSetup{

    private static final String INCORRECT_CRED = "Incorrect authentication credentials.";
    private static final String NO_CRED_MESSAGE = "Authentication credentials were not provided.";
    private static final Pattern UUID_PATTERN = Pattern.compile(
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$");

    @Test
    public void audioSingleQSearch(){
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + accessToken)
                .param("q", "test")
                .get(BASE_URL + AUDIO_SEARCH);

        // Test the audio search call is passing
        Assertions.assertEquals(200, response.getStatusCode());
        String audioId = response.jsonPath().getString("results[0].id");
        System.out.println("... Audio Id: " + audioId);

        // Test the Audio id is a valid UUID4
        Assertions.assertTrue(isValidUUID4(audioId));
    }

    @Test
    public void audioSearchWithInvalidToken(){
        Response response = RestAssured.given()
                .header("Authorization", "Bearer invalidToken")
                .param("q", "test")
                .get(BASE_URL + AUDIO_SEARCH);

        // Test the audio search call is passing
        Assertions.assertEquals(401, response.getStatusCode());
        String errorMsg = response.jsonPath().get("detail");

        // Test the Audio id is a valid UUID4
        Assertions.assertEquals(INCORRECT_CRED, errorMsg);
    }


    @Test
    public void audioSearchWithoutToken(){
        Response response = RestAssured.given()
                .param("q", "test")
                .get(BASE_URL + AUDIO_SEARCH);

        // Test the audio search call is passing
        Assertions.assertEquals(401, response.getStatusCode());
        String errorMsg = response.jsonPath().get("detail");

        // Test the Audio id is a valid UUID4
        Assertions.assertEquals(NO_CRED_MESSAGE, errorMsg);
    }

    @Test
    public void audioMultiQSearch(){

        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + accessToken)
                .param("q", "test")
                .param("license", "pdm,by")
                .param("categories", "illustration")
                .param("page_size", "1")
                .param("page", "1")
                .get(BASE_URL + AUDIO_SEARCH);

        // Test the audio search call is passing
        Assertions.assertEquals(200, response.getStatusCode());
        String audioId = response.jsonPath().getString("results[0].id");
        System.out.println("... Audio Id: " + audioId);
        // Test the Audio id is a valid UUID4
        Assertions.assertTrue(isValidUUID4(audioId));

        String pageSize = response.jsonPath().get("page_size").toString();
        Assertions.assertEquals(pageSize, "1");

        String page = response.jsonPath().get("page").toString();
        Assertions.assertEquals(page, "1");
    }

    @Test
    public void audioExactSearch(){

        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + accessToken)
                .param("q", "%22Giacomo%20Puccini%22")
                .get(BASE_URL + AUDIO_SEARCH);

        // Test the audio search call is passing
        Assertions.assertEquals(200, response.getStatusCode());
        String audioId = response.jsonPath().getString("results[0].id");
        System.out.println("... Audio Id: " + audioId);
        // Test the Audio id is a valid UUID4
        Assertions.assertTrue(isValidUUID4(audioId));

        String pageSize = response.jsonPath().get("page_size").toString();
        Assertions.assertEquals(pageSize, "1");

        String page = response.jsonPath().get("page").toString();
        Assertions.assertEquals(page, "1");

        String creator = response.jsonPath().getString("results[0].creator");
        Assertions.assertEquals("Casonika", creator);
    }

    //todo
    @Test
    public void audioLogicAndSearch(){

    }

    //todo
    @Test
    public void audioLogicEitherSearch(){

    }

    //todo
    @Test
    public void audioLogicIncExcSearch(){

    }

    //todo
    @Test
    public void audioAdvanceLogicSearch(){

    }

    //todo
    @Test
    public void audioCloserSearch(){

    }

    public static boolean isValidUUID4(String uuidStr) {
        try {
            // Try parsing the string as a UUID
            UUID uuid = UUID.fromString(uuidStr);
            // Check if the parsed UUID is version 4
            return uuid.version() == 4;
        } catch (IllegalArgumentException e) {
            // If parsing fails, or version is not 4, return false
            return false;
        }
    }


}
