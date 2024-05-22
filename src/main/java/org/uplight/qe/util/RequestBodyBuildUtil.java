package org.uplight.qe.util;
import com.google.gson.JsonObject;


public class RequestBodyBuildUtil {

    static String emailAddress = EmailServiceUtil.getEMAIL_ADDRESS();
    public static String createValidAppMetaData(){

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("name", GenUtil.genRandomStringWithNLength(10));
        requestBody.addProperty("description", GenUtil.genRandomStringWithNLength(20));
        requestBody.addProperty("email", emailAddress);
        return requestBody.toString();
    }

    public static String createInvalidAppNameData(){

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("name", GenUtil.genRandomStringWithNLength(151));
        requestBody.addProperty("description", GenUtil.genRandomStringWithNLength(20));
        requestBody.addProperty("email", emailAddress);
        return requestBody.toString();
    }

    public static String createInvalidAppDescData(){

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("name", GenUtil.genRandomStringWithNLength(10));
        requestBody.addProperty("description", GenUtil.genRandomStringWithNLength(10001));
        requestBody.addProperty("email", emailAddress);
        return requestBody.toString();
    }

    public static String createInvalidAppEmail(){

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("name", GenUtil.genRandomStringWithNLength(10));
        requestBody.addProperty("description", GenUtil.genRandomStringWithNLength(20));
        requestBody.addProperty("email", GenUtil.genRandomStringWithNLength(10));
        return requestBody.toString();
    }

    public static String createInvalidAppNameAndDescData(){
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("name", GenUtil.genRandomStringWithNLength(151));
        requestBody.addProperty("description", GenUtil.genRandomStringWithNLength(10001));
        requestBody.addProperty("email", emailAddress);
        return requestBody.toString();
    }
}
