package steps;

import client.DeliveryApiClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.customers.Contract;
import model.customers.Customer;
import model.customers.GetCustomersHoldingsSearchResponse;
import model.customers.PostAgreement;
import model.delivery.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static data.TestParams.*;
import static steps.BaseSteps.changeDateFormat;
import static steps.BaseSteps.checkStatusCode;

public class DeliverySteps {
    private static final Integer delivery_check_id = postDeliveryCheckAddress(
            "correct_token",
            new Address(getRandomString(getOutsideMoscowAddresses()), false),
            200,
            PostDeliveryCheckAddressResponse.class
    ).getDeliveryCheckId();

    private static final Integer point_id = getDeliveryPointsFromDb(getPecom(), true, true, true).get(0).getId();

    @Step("Проверить возможность доставки по адресу")
    public static <T> T postDeliveryCheckAddress(String token, Address body, Integer statusCode, Class<T> responseClass) {
        Response response = DeliveryApiClient.postDeliveryCheckAddress(token, body);
        checkStatusCode(response, statusCode);
        return response.body().as(responseClass);
    }
    @Step("Просмотр заявки на доставку")
    public static <T> T getDeliveryApplication(String token, HashMap<String, Object> queryParams, Integer statusCode, Class<T> responseClass) {
        Response response = DeliveryApiClient.getDeliveryApplication(token, queryParams);
        checkStatusCode(response, statusCode);
        return response.body().as(responseClass);
    }
    @Step("Просмотр заявок на доставку")
    public static List<DeliveryApplication> getDeliveryApplications(String token, HashMap<String, Object> queryParams, Integer statusCode) {
        Response response = DeliveryApiClient.getDeliveryApplications(token, queryParams);
        checkStatusCode(response, statusCode);
        return Arrays.asList(response.body().as(DeliveryApplication[].class));
    }
    @Step("Просмотр заявок на доставку")
    public static <T> T getDeliveryApplications(String token, HashMap<String, Object> queryParams, Integer statusCode, Class<T> responseClass) {
        Response response = DeliveryApiClient.getDeliveryApplications(token, queryParams);
        checkStatusCode(response, statusCode);
        return response.body().as(responseClass);
    }
    @Step("Проверка минимальной даты доставки")
    public static <T> T postDeliveryCheckDate(String token, Object body, Integer statusCode, Class<T> responseClass) {
        Response response = DeliveryApiClient.postDeliveryCheckDate(token, body);
        checkStatusCode(response, statusCode);
        return response.body().as(responseClass);
    }
    @Step("Проверка дня")
    public static <T> T postDeliveryCheckDay(String token, CheckDay body, Integer statusCode, Class<T> responseClass) {
        Response response = DeliveryApiClient.postDeliveryCheckDay(token, body);
        checkStatusCode(response, statusCode);
        return response.body().as(responseClass);
    }
    @Step("Получить delivery_check_id")
    public static Integer getDeliveryCheckId() {return delivery_check_id;}

    @Step("Получить point_id")
    public static Integer getPointId() {return point_id;}
    @Step("Создание заявки на доставку")
    public static <T> T postDeliveryApplication(String token, Object body, Integer statusCode, Class<T> responseClass) {
        Response response = DeliveryApiClient.postDeliveryApplication(token, body);
        checkStatusCode(response, statusCode);
        return response.body().as(responseClass);
    }
    @Step("Просмотр списка ПВЗ")
    public static <T> T getDeliveryPoints(String token, HashMap<String, Object> queryParams, Integer statusCode, Class<T> responseClass) {
        Response response = DeliveryApiClient.getDeliveryPoints(token, queryParams);
        checkStatusCode(response, statusCode);
        return response.body().as(responseClass);
    }
    @Step("Просмотр списка ПВЗ")
    public static List<DeliveryPoint> getDeliveryPoints(String token, HashMap<String, Object> queryParams, Integer statusCode) {
        Response response = DeliveryApiClient.getDeliveryPoints(token, queryParams);
        checkStatusCode(response, statusCode);
        return Arrays.asList(response.body().as(DeliveryPoint[].class));
    }

    @Step("Просмотр информации о холдинге")
    public static <T> T getCustomersHoldingsSearch(String token, HashMap<String, Object> queryParams, Integer statusCode, Class<T> responseClass) {
        Response response = DeliveryApiClient.getCustomersHoldingsSearch(token, queryParams);
        checkStatusCode(response, statusCode);
        return response.body().as(responseClass);
    }

    @Step("Просмотр списка договоров, требующих подписания доп. соглашения")
    public static <T> T getCustomersContracts(String token, Object corr_id_holding, Integer statusCode, Class<T> responseClass) {
        Response response = DeliveryApiClient.getCustomersContracts(token, corr_id_holding);
        checkStatusCode(response, statusCode);
        return response.body().as(responseClass);
    }

    @Step("Просмотр списка договоров, требующих подписания доп. соглашения")
    public static List<Contract>  getCustomersContracts(String token, Object corr_id_holding, Integer statusCode) {
        Response response = DeliveryApiClient.getCustomersContracts(token, corr_id_holding);
        checkStatusCode(response, statusCode);
        return Arrays.asList(response.body().as(Contract[].class));
    }

    @Step("Создание соглашения с пользователем доставки")
    public static <T> T postCustomersAgreement(String token, PostAgreement body, Integer statusCode, Class<T> responseClass) {
        Response response = DeliveryApiClient.postCustomersAgreement(token, body);
        checkStatusCode(response, statusCode);
        return response.body().as(responseClass);
    }

    @Step("Проверка возможности доставки пользователю")
    public static <T> T postCustomersCheck(String token, PostAgreement body, Integer statusCode, Class<T> responseClass) {
        Response response = DeliveryApiClient.postCustomersCheck(token, body);
        checkStatusCode(response, statusCode);
        return response.body().as(responseClass);
    }

    @Step("Получение данных по пользователю")
    public static <T> T postCustomersSearch(String token, PostAgreement body, Integer statusCode, Class<T> responseClass) {
        Response response = DeliveryApiClient.postCustomersSearch(token, body);
        checkStatusCode(response, statusCode);
        return response.body().as(responseClass);
    }

    @Step("Получить заявки на доставку из БД")
    public static List<DeliveryApplication> getDeliveryApplicationsFromDb(String system, Integer user_id, Integer corr_id, Integer corr_id_holding) {
        List<DeliveryApplication> result = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT da1.id, invoice_id, invoice_number, invoice_date, bill_id, bill_number, bill_date, \n" +
                "address_check_id, user_id , final_cargo_quantity , da1.point_id,\n" +
                "da1.transport_company_id, dd2.address, dd.delivery_date_planned, dt.\"name\", dd.track_number, dd.tracking_url \n" +
                "FROM public.delivery2_deliveryapplication da1\n" +
                "left join delivery2_addresscheck da2 on da2.id  = da1.address_check_id \n" +
                "left join delivery2_transportcompany dt on dt.id = da1.transport_company_id \n" +
                "left join delivery2_deliveryorder dd on dd.application_id = da1.id\n" +
                "left join delivery2_deliverypoint dd2 on dd2.apiship_id = da1.point_id " +
                "WHERE ");

        List<String> conditions = new ArrayList<>();

        if (system != null) {
            conditions.add("\"system\" = '" + system + "'");
        }
        if (user_id != null) {
            conditions.add("da1.user_id = " + user_id);
        }
        if (corr_id != null) {
            conditions.add("da1.corr_id = " + corr_id);
        }
        if (corr_id_holding != null) {
            conditions.add("da1.corr_id_holding = " + corr_id_holding);
        }

        String conditionString = String.join(" AND ", conditions);
        queryBuilder.append(conditionString);
        try {
            Class.forName("org.postgresql.Driver");

            Connection conn = DriverManager.getConnection(
                    getDeliveryPostgresUrl(),
                    getDeliveryPostgresUsername(),
                    getDeliveryPostgresPassword()
            );

            if (conn == null) {
                System.err.println("Could not connect to database");
            } else {
                Statement stmt1 = conn.createStatement();
                ResultSet res = stmt1.executeQuery(queryBuilder.toString() + " order by da1.id");

                while (res.next()) {
                    DeliveryApplication newItem = new DeliveryApplication(
                        res.getString("bill_date"),
                        res.getString("bill_number"),
                        res.getString("address"),
                        res.getString("delivery_date_planned"),
                        res.getString("invoice_date"),
                        res.getString("invoice_number"),
                        res.getString("name"),
                        res.getString("track_number"),
                        res.getString("tracking_url"),
                        res.getString("user_id"),
                        res.getDouble("final_cargo_quantity"),
                        res.getInt("address_check_id"),
                        res.getInt("invoice_id"),
                        res.getInt("id")
                    );
                    if (res.getObject("point_id") != null ) {
                        newItem.setPoint(getDeliveryPointFromDb(res.getInt("point_id")));
                    }
                    if (res.getObject("final_cargo_quantity") == null ) {
                        newItem.setCargoPlaces(null);
                    }
                    result.add(newItem);
                }

                stmt1.close();
                conn.close();
            }
        } catch (Exception e) {
            throw new AssertionError("Connection failed:\n" + e.getMessage());
        }
        return result;
    }

    @Step("Получить заявку на доставку из БД")
    public static DeliveryApplication getDeliveryApplicationFromDb(String condition) {
        String query = "SELECT da1.id, invoice_id, invoice_number, invoice_date, bill_id, bill_number, bill_date, \n" +
                "address_check_id, user_id , final_cargo_quantity , da1.point_id,\n" +
                "da1.transport_company_id, dd2.address, dd.delivery_date_planned, dt.\"name\", dd.track_number, dd.tracking_url \n" +
                "FROM public.delivery2_deliveryapplication da1\n" +
                "left join delivery2_addresscheck da2 on da2.id  = da1.address_check_id \n" +
                "left join delivery2_transportcompany dt on dt.id = da1.transport_company_id \n" +
                "left join delivery2_deliveryorder dd on dd.application_id = da1.id\n" +
                "left join delivery2_deliverypoint dd2 on dd2.apiship_id = da1.point_id " +
                "WHERE " + condition;

        try {
            Class.forName("org.postgresql.Driver");

            Connection conn = DriverManager.getConnection(
                    getDeliveryPostgresUrl(),
                    getDeliveryPostgresUsername(),
                    getDeliveryPostgresPassword()
            );

            if (conn == null) {
                System.err.println("Could not connect to database");
            } else {
                Statement stmt1 = conn.createStatement();
                ResultSet res = stmt1.executeQuery(query);

                while (res.next()) {
                    DeliveryApplication result = new DeliveryApplication(
                            res.getString("bill_date"),
                            res.getString("bill_number"),
                            res.getString("address"),
                            res.getString("delivery_date_planned"),
                            res.getString("invoice_date"),
                            res.getString("invoice_number"),
                            res.getString("name"),
                            res.getString("track_number"),
                            res.getString("tracking_url"),
                            res.getString("user_id"),
                            res.getInt("final_cargo_quantity"),
                            res.getInt("address_check_id"),
                            res.getInt("invoice_id"),
                            res.getInt("id")
                    );

                    if (res.getObject("point_id") != null ) {
                        result.setPoint(getDeliveryPointFromDb(res.getInt("point_id")));
                        String address = result.getPoint().getAddress();
                        result.setDeliveryAddress(address);
                    }
                    if (res.getObject("final_cargo_quantity") == null ) {
                        result.setCargoPlaces(null);
                    }

                    stmt1.close();
                    conn.close();

                    return result;
                }
                stmt1.close();
                conn.close();
            }
        } catch (Exception e) {
            throw new AssertionError("Connection failed:\n" + e.getMessage());
        }
        return null;
    }

    @Step("Получить ПВЗ из БД")
    public static DeliveryPoint getDeliveryPointFromDb(Integer point_id) {
        DeliveryPoint result = null;
        String query = "select dd.*, dt.\"name\" as transport_company, dt.is_active as transport_company_is_active \n" +
                "from delivery2_deliverypoint dd \n" +
                "join delivery2_transportcompany dt on dt.id = dd.transport_company_id \n" +
                "where apiship_id = " + point_id;
        try {
            Class.forName("org.postgresql.Driver");

            Connection conn = DriverManager.getConnection(
                    getDeliveryPostgresUrl(),
                    getDeliveryPostgresUsername(),
                    getDeliveryPostgresPassword()
            );

            if (conn == null) {
                System.err.println("Could not connect to database");
            } else {
                Statement stmt1 = conn.createStatement();
                ResultSet res = stmt1.executeQuery(query);

                while (res.next()) {
                    result = new DeliveryPoint(
                            res.getInt("apiship_id"),
                            res.getString("name"),
                            res.getString("address"),
                            res.getString("lat"),
                            res.getString("lng"),
                            res.getString("url"),
                            res.getString("phone"),
                            res.getString("description"),
                            res.getString("city"),
                            res.getString("streetType"),
                            res.getString("street"),
                            res.getString("house"),
                            res.getString("transport_company"),
                            res.getBoolean("transport_company_is_active"),
                            res.getBoolean("no_weight_limits"),
                            res.getBoolean("multiplace"),
                            res.getBoolean("city_fias_id_is_valid")
                    );
                }

                stmt1.close();
                conn.close();
            }
        } catch (Exception e) {
            throw new AssertionError("Connection failed:\n" + e.getMessage());
        }
        return result;
    }

    @Step("Получить список ПВЗ из БД")
    public static List<DeliveryPoint> getDeliveryPointsFromDb(String transport_company, Boolean available_for_delivery, Boolean no_weight_limits, Boolean multiplace) {
        StringBuilder queryBuilder = new StringBuilder("select dd.*, dt.\"name\" as transport_company, dt.is_active as transport_company_is_active\n" +
                "from delivery2_deliverypoint dd \n" +
                "left join delivery2_transportcompany dt on dt.id = dd.transport_company_id \n" +
                "left join delivery2_federaldistrictsettings df on df.id = dd.federal_district_id\n" +
                "left join delivery2_federalsubject df2 on df2.id = dd.federal_subject_id " +
                "where ");
        List<DeliveryPoint> result = new ArrayList<>();

        List<String> conditions = new ArrayList<>();

        if (transport_company != null) {
            conditions.add("dt.\"name\" = '" + transport_company + "'");
        }
        if (available_for_delivery != null) {
            if (available_for_delivery) {
                conditions.add(" city_fias_id_is_valid = true and dt.is_active = true and dd.federal_district_id is not null\n" +
                        "and df.is_active = true and (df2.is_active is null or df2.is_active = true) ");
            }
            else {
                conditions.add(" city_fias_id_is_valid != true OR dt.is_active != true or dd.federal_district_id is  null\n" +
                        "or df.is_active != true or (df2.is_active is not null and df2.is_active != true) ");
            }
        }
        if (no_weight_limits != null) {
            conditions.add("no_weight_limits = " + no_weight_limits);
        }
        if (multiplace != null) {
            conditions.add("multiplace = " + multiplace);
        }

        String conditionString = String.join(" AND ", conditions);
        queryBuilder.append(conditionString);

        try {
            Class.forName("org.postgresql.Driver");

            Connection conn = DriverManager.getConnection(
                    getDeliveryPostgresUrl(),
                    getDeliveryPostgresUsername(),
                    getDeliveryPostgresPassword()
            );

            if (conn == null) {
                System.err.println("Could not connect to database");
            } else {
                Statement stmt1 = conn.createStatement();
                ResultSet res = stmt1.executeQuery(queryBuilder.toString());

                while (res.next()) {
                    result.add(new DeliveryPoint(
                            res.getInt("apiship_id"),
                        res.getString("name"),
                        res.getString("address"),
                        res.getString("lat"),
                        res.getString("lng"),
                        res.getString("url"),
                        res.getString("phone"),
                        res.getString("description"),
                        res.getString("city"),
                        res.getString("streetType"),
                        res.getString("street"),
                        res.getString("house"),
                        res.getString("transport_company"),
                        res.getBoolean("transport_company_is_active"),
                        res.getBoolean("no_weight_limits"),
                        res.getBoolean("multiplace"),
                        res.getBoolean("city_fias_id_is_valid")
                    ));
                }

                stmt1.close();
                conn.close();
            }
        } catch (Exception e) {
            throw new AssertionError("Connection failed:\n" + e.getMessage());
        }
        return result;
    }

    @Step("Получить информацию о холдинге из БД")
    public static GetCustomersHoldingsSearchResponse getCustomersHoldingsSearchFromDb(Integer corr_id, Boolean has_agreement) {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT user_id, corr_id_holding, delivery_is_available " +
                "FROM public.customers_customer where corr_id_holding = " + corr_id;

        try {
            Class.forName("org.postgresql.Driver");

            Connection conn = DriverManager.getConnection(
                    getDeliveryPostgresUrl(),
                    getDeliveryPostgresUsername(),
                    getDeliveryPostgresPassword()
            );

            if (conn == null) {
                System.err.println("Could not connect to database");
            } else {
                Statement stmt1 = conn.createStatement();
                ResultSet res = stmt1.executeQuery(query);

                while (res.next()) {
                    Customer newItem = new Customer(
                            "B2B",
                            res.getString("user_id"),
                            res.getString("corr_id_holding"),
                            res.getString("delivery_is_available")
                    );
                    customers.add(newItem);
                }

                stmt1.close();
                conn.close();
            }
        } catch (Exception e) {
            throw new AssertionError("Connection failed:\n" + e.getMessage());
        }

        return new GetCustomersHoldingsSearchResponse(has_agreement, customers);
    }

    @Step("Получить информацию о холдинге из БД")
    public static List<Contract> getCustomersContractsFromDb(Integer corr_id) {
        List<Contract> contracts = new ArrayList<>();
        String query = "SELECT c.CNT_ID, c.CNT_NO, MAX(c.CNT_DATE) AS CNT_DATE, c.CNTT_ID, c.CNT_NAME, " +
                "MAX(c2.CORR_ID) AS CORR_ID, MAX(c2.CORR_PARENT_ID) AS CORR_PARENT_ID " +
                "FROM CONTRACT c " +
                "JOIN CORR c2 ON c.CORR_ID = c2.CORR_ID " +
                "WHERE (c2.CORR_PARENT_ID = " + corr_id + " OR c2.CORR_ID = " + corr_id +
                ") AND c.CNTT_ID IN (1, 2)  " +
                "    AND c.CNT_ISACTIVE = 1 " +
                "GROUP BY c.CNT_ID, c.CNT_NO, c.CNTT_ID, c.CNT_NAME";

        try {
            Connection conn = DriverManager.getConnection(
                    getFirebirdUrl(),
                    getFirebirdUsername(),
                    getFirebirdPassword()
            );

            if (conn == null) {
                System.err.println("Could not connect to database");
            } else {
                Statement stmt1 = conn.createStatement();
                ResultSet res = stmt1.executeQuery(query);

                while (res.next()) {
                    Contract newItem = new Contract(
                            res.getInt("CNT_ID"),
                            res.getString("CNT_NO"),
                            changeDateFormat(res.getString("CNT_DATE"), "yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd"),
                            res.getInt("CNTT_ID"),
                            res.getString("CNT_NAME")
                    );
                    contracts.add(newItem);
                }

                stmt1.close();
                conn.close();
            }
        } catch (Exception e) {
            throw new AssertionError("Connection failed:\n" + e.getMessage());
        }

        return contracts;
    }

    @Step("Найти нового пользователя, подходящего для заключения ДС")
    public static PostAgreement findNewUserForAgreementInDb(String condition) {
        PostAgreement customer = new PostAgreement();
        String queryForFB = getQueryString(condition);
        String queryForPSQL = "SELECT id FROM public.customers_customer where \"system\" = 'B2B' and user_id = '";

        try {
            Connection connFB = DriverManager.getConnection(getFirebirdUrl(), getFirebirdUsername(), getFirebirdPassword());
            Connection connPSQL = DriverManager.getConnection(getDeliveryPostgresUrl(), getDeliveryPostgresUsername(), getDeliveryPostgresPassword());

            if (connFB == null) {
                System.err.println("Could not connect to firebird database");
            } else if (connPSQL == null) {
                System.err.println("Could not connect to PSQL database");
            }
            else {
                boolean isFound = false;

                while (!isFound) {
                    // Получаем случайный id_bitrix из БД
                    Statement stmt1 = connFB.createStatement();
                    ResultSet res = stmt1.executeQuery(queryForFB);

                    // Проверяем, есть ли такой пользователь в БД PSQL
                    if (res.next()) {
                        Statement stmt2 = connPSQL.createStatement();
                        ResultSet res2 = stmt2.executeQuery(queryForPSQL + res.getString("ID_BITRIX") + "'");

                        // Если такого пользователя нет, то возвращаем его
                        if (!res2.next()) {
                            isFound = true;
                            customer = new PostAgreement("B2B", res.getString("ID_BITRIX"), res.getString("CORR_ID"));
                        }
                        stmt2.close();
                    }

                    stmt1.close();
                }

                connFB.close();
                connPSQL.close();
            }
        } catch (Exception e) {
            throw new AssertionError("Connection failed:\n" + e.getMessage());
        }

        return customer;
    }

    @Step("Найти существующего пользователя, подходящего для заключения ДС")
    public static PostAgreement findExistingUserInCustomersDb(String condition) {
        PostAgreement customer = new PostAgreement();
        String queryForPSQL = "select * FROM public.customers_customer\n" +
                "where delivery_is_available = '" + condition + "' order by random() limit 1;";

        try {
            Connection connPSQL = DriverManager.getConnection(getDeliveryPostgresUrl(), getDeliveryPostgresUsername(), getDeliveryPostgresPassword());

            if (connPSQL == null) {
                System.err.println("Could not connect to PSQL database");
            }
            else {

                Statement stmt2 = connPSQL.createStatement();
                ResultSet res2 = stmt2.executeQuery(queryForPSQL);

                // Если такой пользователь есть, то возвращаем его
                if (res2.next()) {
                    customer = new PostAgreement("B2B", res2.getString("user_id"), res2.getString("corr_id_holding"));
                }
                stmt2.close();
                connPSQL.close();
            }
        } catch (Exception e) {
            throw new AssertionError("Connection failed:\n" + e.getMessage());
        }

        return customer;
    }

    // Получить строку запроса для поиска пользователя
    private static String getQueryString(String type_of_user) {
        String queryForFB = "";

        switch (type_of_user) {
            case "agreement":
                queryForFB = "SELECT FIRST 1 c.CORR_ID, saf.ID_BITRIX ID FROM CORR c " +
                        "JOIN SITE_APP_FORM saf ON saf.CORR_ID_HOLDING = c.CORR_ID " +
                        "WHERE c.CORR_ISACTIVE = 1 AND CORR_ISTRANSCOMPANY = 0 AND CORR_INN IS NOT NULL " +
                        " AND \"PersonType\" = 0 AND EXISTS ( SELECT 1 FROM CONTRACT c2 WHERE c2.CORR_ID = c.CORR_ID " +
                        " AND c2.CNTT_ID IN (1, 2) ) ORDER BY RAND();";
                break;
            case "offer":
                queryForFB = "SELECT FIRST 1 c.CORR_ID, saf.ID_BITRIX ID FROM CORR c " +
                        "JOIN SITE_APP_FORM saf ON saf.CORR_ID_HOLDING = c.CORR_ID " +
                        "WHERE c.CORR_ISACTIVE = 1 AND CORR_ISTRANSCOMPANY = 0 AND CORR_INN IS NOT NULL " +
                        " AND \"PersonType\" = 0 AND EXISTS ( SELECT 1 FROM CONTRACT c2 WHERE c2.CORR_ID = c.CORR_ID " +
                        " AND c2.CNTT_ID IN (12) AND c2.CNT_ISACTIVE = 1) ORDER BY RAND();";
                break;
            case "new":
                queryForFB = "SELECT FIRST 1 saf.ID_BITRIX, c.CORR_ID FROM CORR c " +
                        "JOIN SITE_APP_FORM saf ON saf.CORR_ID_HOLDING = c.CORR_ID " +
                        "JOIN CONTRACT c3 ON c3.CORR_ID = c.CORR_ID WHERE c.CORR_ISACTIVE = 1 " +
                        " AND CORR_ISTRANSCOMPANY = 0 AND CORR_INN IS NOT NULL AND \"PersonType\" = 0 " +
                        " AND c3.CNTT_ID NOT IN (12) AND c3.CNT_DATE > '2023-10-01' " +
                        "ORDER BY RAND();";
                break;
        }

        return queryForFB;
    }
}