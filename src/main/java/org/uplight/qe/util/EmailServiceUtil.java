package org.uplight.qe.util;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.Data;
import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

@Data
public class EmailServiceUtil {

    // Using MailSlurp as Email Agency to handle Email related services
    @Getter
    private static final String EMAIL_ADDRESS = "d5dbe9e1-ae0e-470d-af17-7423f8e158bf@mailslurp.net";

    @Getter
    private static final String API_ACCESS_KEY = "aa354d689c046ca3d09a539194c279f98233a3b38b512b559f9475cb271a66dd";

    @Getter
    private static final String INBOX_ID = "d5dbe9e1-ae0e-470d-af17-7423f8e158bf";


    public static final String BASE_URL = "https://api.mailslurp.com";
    public static final int TIMEOUT = 30000;
    public static final String UNREAD_ONLY = "true";
    public static final String DESCENDING = "DESC";
    public static final String ASCENDING = "ASC";

    public static String fetchLatestUnReadEmailIdFromOpenverse(){
        String emailId;
        Response response = RestAssured.given()
                .header("x-api-key", API_ACCESS_KEY)
                .param("inboxId", INBOX_ID)
                .param("timeout", TIMEOUT)
                .param("unreadOnly", UNREAD_ONLY)
                .param("sort", DESCENDING)
                .when()
                .get(BASE_URL + "/waitForLatestEmail");
        emailId = response.jsonPath().get("id");
        System.out.println("... Email Id: " + emailId);
        Assertions.assertNotNull(emailId);
        return emailId;
    }

    public static String fetchVerifyLinkFromEmailMessageWithId(String emailId){
        String url;
        Response response = RestAssured.given()
                .header("x-api-key", API_ACCESS_KEY)
                .when()
                .get(BASE_URL + "/emails/" + emailId + "/body");

        url = extractUrlHelper(response.getBody().asString());
        System.out.println("... Verify Link from Email: " + url);
        Assertions.assertNotNull(url);
        return url;
    }

    public static String extractUrlHelper(String content) {
        // Regular expression pattern to match URLs
        String regex = "https?://[^\\s/$.?#].[^\\s]*";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(regex);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(content);

        // Find the URL in the content
        if (matcher.find()) {
            // Return the first URL found
            return matcher.group();
        } else {
            // Return null if no URL found
            return null;
        }
    }



}
