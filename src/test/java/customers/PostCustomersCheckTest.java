package customers;

import model.customers.Customer;
import model.customers.PostAgreement;
import model.errors.BaseResponseError;
import model.errors.ValidationError;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static data.Errors.getIncorrectTokenError;
import static data.Errors.getValidationError;
import static data.TestParams.getCorrId;
import static data.TestParams.getUid;
import static steps.BaseSteps.*;
import static steps.DeliverySteps.postCustomersCheck;

public class PostCustomersCheckTest {
    private static Stream<Arguments> provideParamsForTestsWithValidParams() {
        return Stream.of(
                Arguments.of(
                    "с user_id, у которого заключено доп. соглашение",
                    getValueFromPSQL(
                            "SELECT cc.user_id from public.customers_customer cc " +
                                    "where cc.delivery_is_available = 'agreement' order by random() limit 1;",
                            "user_id",
                            String.class
                    ),
                    "agreement"
                ),
                Arguments.of(
                        "с user_id, у которого заключен новый договор",
                        getValueFromFB(
                                "SELECT FIRST 1 saf.ID_BITRIX FROM CORR c " +
                                        "JOIN SITE_APP_FORM saf ON saf.CORR_ID_HOLDING = c.CORR_ID " +
                                        "JOIN CONTRACT c3 ON c3.CORR_ID = c.CORR_ID WHERE c.CORR_ISACTIVE = 1 " +
                                        " AND CORR_ISTRANSCOMPANY = 0 AND CORR_INN IS NOT NULL AND \"PersonType\" = 0 " +
                                        " AND c3.CNTT_ID NOT IN (12) AND c3.CNT_DATE > '2023-10-01' " +
                                        "ORDER BY ID_BITRIX desc",
                                "ID_BITRIX",
                                String.class
                        ),
                        "new"
                ),
                Arguments.of(
                        "с user_id, у которого заключен договор оферты",
                        getValueFromFB(
                                "SELECT FIRST 1 saf.ID_BITRIX FROM CORR c " +
                                        "JOIN SITE_APP_FORM saf ON saf.CORR_ID_HOLDING = c.CORR_ID " +
                                        "JOIN CONTRACT c3 ON c3.CORR_ID = c.CORR_ID WHERE c.CORR_ISACTIVE = 1 " +
                                        " AND CORR_ISTRANSCOMPANY = 0 AND CORR_INN IS NOT NULL AND \"PersonType\" = 0 " +
                                        " AND c3.CNTT_ID IN (12) AND c3.CNT_ISACTIVE = 1 " +
                                        "ORDER BY RAND()",
                                "ID_BITRIX",
                                String.class
                        ),
                        "offer"
                ),
                Arguments.of(
                        "с user_id не подходящим под условия",
                        getValueFromFB(
                                "select first 1 ID_BITRIX from CONTRACT " +
                                        "join SITE_APP_FORM on SITE_APP_FORM.CORR_ID_HOLDING = CONTRACT.CORR_ID " +
                                        "where CNTT_ID =1 and CNT_ISACTIVE = 1 and APP_FORM_STATUS = 3 AND CONTRACT.CNT_DATEFROM < '01.09.2023'",
                                "ID_BITRIX",
                                String.class
                        ),
                        "no"
                )
        );
    }
    @ParameterizedTest(name = "{0}")
    @DisplayName("Проверка возможности доставки пользователю с валидными параметрами")
    @MethodSource("provideParamsForTestsWithValidParams")
    public void postCustomersCheckWithValidParams(String description, String user_id, String delivery_is_available) {
        // Получаем corr_id_holding из БД
        String corr_id_holding = getValueFromFB(
                "select CORR_ID_HOLDING from SITE_APP_FORM where ID_BITRIX = " + user_id,
                "CORR_ID_HOLDING",
                String.class
        );
        if (corr_id_holding == null) corr_id_holding = getCorrId().toString();

        // Проверяем возможность доставки (фактический результат)
        Customer actual = postCustomersCheck(
                "correct_token",
                new PostAgreement(
                        "B2B",
                        user_id,
                        corr_id_holding
                ),
                200,
                Customer.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, new Customer("B2B", user_id, corr_id_holding, delivery_is_available));
    }

    private static Stream<Arguments> provideParamsForTestsWithForeignParams() {
        return Stream.of(
                Arguments.of(
                        "с user_id, который есть в системе и чужим corr_id_holding, user_id без ДС",
                        getValueFromPSQL(
                                "SELECT cc.user_id from public.customers_customer cc " +
                                        "where cc.delivery_is_available = 'offer' order by random() limit 1;",
                                "user_id",
                                String.class
                        ),
                        "no"
                ),
                Arguments.of(
                        "с user_id, который есть в системе и чужим corr_id_holding, user_id с ДП",
                        getValueFromPSQL(
                                "SELECT cc.user_id from public.customers_customer cc " +
                                        "where cc.delivery_is_available = 'agreement' order by random() limit 1;",
                                "user_id",
                                String.class
                        ),
                        "agreement"
                )
        );
    }
    @ParameterizedTest(name = "{0}")
    @Disabled("Тест отключен на проде")
    @DisplayName("Проверка возможности доставки пользователю с чужими параметрами")
    @MethodSource("provideParamsForTestsWithForeignParams")
    public void postCustomersCheckWithForeignParams(String description, String user_id, String delivery_is_available) {
        // Получаем corr_id_holding из БД
        Integer corr_id_holding = getValueFromFB(
                "select CORR_ID_HOLDING from SITE_APP_FORM where ID_BITRIX = " + user_id,
                "CORR_ID_HOLDING",
                Integer.class
        );

        if (corr_id_holding == null) corr_id_holding = getCorrId();

        // Проверяем возможность доставки (фактический результат)
        Customer actual = postCustomersCheck(
                "correct_token",
                new PostAgreement(
                        "B2B",
                        user_id,
                        Integer.valueOf(corr_id_holding + 1).toString()
                ),
                200,
                Customer.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, new Customer("B2B", user_id, Integer.valueOf(corr_id_holding + 1).toString(), delivery_is_available));
    }

    private static Stream<Arguments> provideParamsForTestsWithInvalidParams() {
        return Stream.of(
                Arguments.of(
                        "с невалидным system",
                        new PostAgreement("sdf", getUid().toString(), getCorrId().toString()),
                        "type_error.enum"
                ),
                Arguments.of(
                        "с невалидным corr_id_holding",
                        new PostAgreement("B2B", getUid().toString(), "qwe123"),
                        "type_error.integer"
                ),
                Arguments.of(
                        "с длинным user_id",
                        new PostAgreement("B2B", "1234567890765", getCorrId().toString()),
                        "value_error.any_str.max_length"
                ),
                Arguments.of(
                        "без user_id",
                        new PostAgreement("B2B", null, getCorrId().toString()),
                        "value_error.missing"
                ),
                Arguments.of(
                        "без corr_id_holding",
                        new PostAgreement("B2B", getUid().toString(), null),
                        "value_error.missing"
                ),
                Arguments.of(
                        "без system",
                        new PostAgreement(null, getUid().toString(), getCorrId().toString()),
                        "value_error.missing"
                )
        );
    }
    @ParameterizedTest(name = "{0}")
    @DisplayName("Проверка возможности доставки пользователю с невалидными параметрами")
    @MethodSource("provideParamsForTestsWithInvalidParams")
    public void provideParamsForTestsWithInvalidParams(String description, PostAgreement body, String type) {
        // Проверяем возможность доставки (фактический результат)
        ValidationError actual = postCustomersCheck(
                "correct_token",
                body,
                422,
                ValidationError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError(type));
    }

    private static Stream<Arguments> provideParamsForTestsWithNonexistentParams() {
        return Stream.of(
                Arguments.of(
                        "с не существующим в РС user_id",
                        "qwe123",
                        getCorrId().toString(),
                        "no"
                ),
                Arguments.of(
                        "с не существующим в РС corr_id_holding",
                        getUid().toString(),
                        "153001233",
                        "agreement"
                )
        );
    }
    @ParameterizedTest(name = "{0}")
    @DisplayName("Проверка возможности доставки пользователю с не существующими параметрами")
    @Disabled("Тест отключен на проде")
    @MethodSource("provideParamsForTestsWithNonexistentParams")
    public void provideParamsForTestsWithNonexistentParams(String description, String user_id, String corr_id_holding, String delivery_is_available) {
        // Проверяем возможность доставки (фактический результат)
        Customer actual = postCustomersCheck(
                "correct_token",
                new PostAgreement(
                        "B2B",
                        user_id,
                        corr_id_holding
                ),
                200,
                Customer.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, new Customer("B2B", user_id, corr_id_holding, delivery_is_available));
    }

    @Test
    @DisplayName("Проверка возможности доставки пользователю с некорректным токеном")
    public void getCustomersHoldingsSearchWithIncorrectToken() {
        // Проверяем возможность доставки (фактический результат)
        BaseResponseError actual = postCustomersCheck(
                "incorrect_token",
                new PostAgreement("B2B", getUid().toString(), getCorrId().toString()),
                401,
                BaseResponseError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getIncorrectTokenError());
    }
}
