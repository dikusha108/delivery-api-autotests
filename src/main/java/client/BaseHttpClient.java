package client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RedirectConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static io.restassured.mapper.ObjectMapperType.GSON;

public class BaseHttpClient {
    private static final String AUTHORIZATION = "Authorization";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String JSON = "application/json";

    private static final RestAssuredConfig config = RestAssuredConfig.newConfig()
            .objectMapperConfig(new ObjectMapperConfig(GSON))
            .sslConfig(new SSLConfig().relaxedHTTPSValidation())
            .redirect(new RedirectConfig().followRedirects(true));

    public static Response doGetRequest(String token, String uri) {
        return given().config(config)
                .filter(new AllureRestAssured())
                .header(AUTHORIZATION, token)
                .get(uri);
    }
    public static Response doGetRequest(String token, String uri, HashMap<String, Object> queryParams) {
        return given().config(config)
                .filter(new AllureRestAssured())
                .header(AUTHORIZATION, token)
                .queryParams(queryParams)
                .get(uri);
    }
    public static Response doPostRequest(String token, String uri, HashMap<String, Object> queryParams) {
        return given().config(config)
                .filter(new AllureRestAssured())
                .header(CONTENT_TYPE, JSON)
                .header(AUTHORIZATION, token)
                .queryParams(queryParams)
                .post(uri);
    }
    public static Response doPostRequest(String token, String uri, Object body) {
        return given().config(config)
                .filter(new AllureRestAssured())
                .header(CONTENT_TYPE, JSON)
                .header(AUTHORIZATION, token)
                .body(body)
                .post(uri);
    }
}