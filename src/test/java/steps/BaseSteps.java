package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static data.TestParams.*;
import static org.assertj.core.api.Assertions.assertThat;

public class BaseSteps {
    @Step("Проверить значение в ответе '{actual}' на корректность")
    public static void checkValue(Object expected, Object actual) {
        assertThat(actual).isEqualTo(expected);
    }
    @Step("Проверить код ответа")
    public static void checkStatusCode(Response response, Integer expected) {
        Integer actual = response.then().extract().statusCode();
        assertThat(actual).isEqualTo(expected);
    }
    @Step("Проверить тело ответа")
    public static void checkBody(Object actual, Object expected, String... ignoringFields) {
        assertThat(actual).usingRecursiveComparison().ignoringFields(ignoringFields).isEqualTo(expected);
    }

    @Step("Проверить тело ответа")
    public static void checkBody(List<?> actualList, List<?> expectedList, String... ignoringFields) {
        assertThat(actualList).usingRecursiveFieldByFieldElementComparatorIgnoringFields(ignoringFields).isEqualTo(expectedList);
    }

    public static String changeDateFormat(String date, String input, String output) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat(input);
        SimpleDateFormat outputFormat = new SimpleDateFormat(output);
        Date d = inputFormat.parse(date);
        String formattedDate = outputFormat.format(d);
        return formattedDate;
    }

    @Step("Получить значение из БД")
    public static <T> T getValueFromFB(String query, String columnLabel, Class<T> typeOf) {
        try {
            Class.forName("org.firebirdsql.jdbc.FBDriver");

            Connection conn = DriverManager.getConnection(getFirebirdUrl(), getFirebirdUsername(), getFirebirdPassword());

            if (conn == null) {
                System.err.println("Could not connect to database");
            } else {
                Statement stmt1 = conn.createStatement();
                ResultSet res = stmt1.executeQuery(query);

                while (res.next()) {
                    return res.getObject(columnLabel, typeOf);
                }

                stmt1.close();
                conn.close();
            }
        } catch (Exception e) {
            throw new AssertionError("Connection failed:\n" + e.getMessage());
        }

        return null;
    }

    @Step("Получить значение из PSQL")
    public static <T> T getValueFromPSQL(String query, String columnLabel, Class<T> typeOf) {
        try {
            Class.forName("org.postgresql.Driver");

            Connection conn = DriverManager.getConnection(getDeliveryPostgresUrl(), getDeliveryPostgresUsername(), getDeliveryPostgresPassword());

            if (conn == null) {
                System.err.println("Could not connect to database");
            } else {
                Statement stmt1 = conn.createStatement();
                ResultSet res = stmt1.executeQuery(query);

                while (res.next()) {
                    return res.getObject(columnLabel, typeOf);
                }

                stmt1.close();
                conn.close();
            }
        } catch (Exception e) {
            throw new AssertionError("Connection failed:\n" + e.getMessage());
        }

        return null;
    }

    @Step("Получить информацию из БД в формате ключ-значение")
    public static <T> HashMap<T, T> getHashMapFromDB(List<T> keys, String query, Class<T> typeOf) {
        HashMap<T, T> invoice_values = new HashMap<>();

        try {
            Class.forName("org.firebirdsql.jdbc.FBDriver");

            Connection conn = DriverManager.getConnection(getFirebirdUrl(), getFirebirdUsername(), getFirebirdPassword());

            if (conn == null) {
                System.err.println("Could not connect to database");
            } else {
                Statement stmt1 = conn.createStatement();
                ResultSet res = stmt1.executeQuery(query);

                while (res.next()) {
                    for (T key: keys) {
                        invoice_values.put(key, res.getObject(key.toString(), typeOf));
                    }
                }
                stmt1.close();
                conn.close();
            }
        } catch (Exception e) {
            throw new AssertionError("Connection failed:\n" + e.getMessage());
        }

        return invoice_values;
    }

    @Step("Проверить, что фактический список элементов совпадает с ожидаемым")
    public static <T> void checkLists(List<T> actualList, List<T> expectedList) {
        boolean result = actualList.isEmpty() && expectedList.isEmpty();
        if (actualList.size() == expectedList.size() && !actualList.isEmpty() ) {
            outerLoop:
            for (T expectedItem : expectedList) {
                for (int i = 0; i < actualList.size(); i++) {
                    if (expectedItem.equals(actualList.get(i))) {
                        result = true;
                        break;
                    } else result = false;

                    if (i == (expectedList.size() - 1) && !result) break outerLoop;
                }
            }
        }


        assertThat(result).isEqualTo(true);
    }
}