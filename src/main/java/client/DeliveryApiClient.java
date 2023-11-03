package client;

import io.restassured.response.Response;
import model.customers.PostAgreement;
import model.delivery.Address;
import model.delivery.CheckDay;

import java.util.HashMap;

import static client.BaseHttpClient.doGetRequest;
import static client.BaseHttpClient.doPostRequest;
import static data.TestParams.*;

public class DeliveryApiClient {
    private static final String BASE_URL = dotenv.get("DELIVERY_API_URL");

    private static String getToken(String token) {
        if (token.equals("correct_token")) {
            return getDeliveryToken();
        } else {
            return getIncorrectToken();
        }
    }

    public static Response postDeliveryCheckAddress(String token, Address body) {
        return doPostRequest(getToken(token), BASE_URL + "/api/delivery/check/address", body);
    }
    public static Response getDeliveryApplication(String token, HashMap<String, Object> queryParams) {
        return doGetRequest(getToken(token), BASE_URL + "/api/delivery/application", queryParams);
    }
    public static Response getDeliveryApplications(String token, HashMap<String, Object> queryParams) {
        return doGetRequest(getToken(token), BASE_URL + "/api/delivery/applications", queryParams);
    }
    public static Response postDeliveryCheckDate(String token, Object body) {
        return doPostRequest(getToken(token), BASE_URL + "/api/delivery/check/date", body);
    }
    public static Response postDeliveryApplication(String token, Object body) {
        return doPostRequest(getToken(token), BASE_URL + "/api/delivery/application", body);
    }
    public static Response postDeliveryCheckDay(String token, CheckDay body) {
        return doPostRequest(getToken(token), BASE_URL + "/api/delivery/holidays/check_day", body);
    }
    public static Response getDeliveryPoints(String token, HashMap<String, Object> queryParams) {
        return doGetRequest(getToken(token), BASE_URL + "/api/delivery/points", queryParams);
    }
    public static Response getCustomersHoldingsSearch(String token, HashMap<String, Object> queryParams) {
        return doGetRequest(getToken(token), BASE_URL + "/api/customers/holdings/search", queryParams);
    }
    public static Response getCustomersContracts(String token, Object corr_id_holding) {
        return doGetRequest(getToken(token), BASE_URL + "/api/customers/" + corr_id_holding + "/contracts");
    }
    public static Response postCustomersAgreement(String token, PostAgreement body) {
        return doPostRequest(getToken(token), BASE_URL + "/api/customers/agreement", body);
    }
    public static Response postCustomersCheck(String token, PostAgreement body) {
        return doPostRequest(getToken(token), BASE_URL + "/api/customers/check", body);
    }
    public static Response postCustomersSearch(String token, PostAgreement body) {
        return doPostRequest(getToken(token), BASE_URL + "/api/customers/search", body);
    }
}