package customers;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import model.customers.Customer;
import model.customers.PostAgreement;
import model.customers.PostAgreementResponse;
import model.errors.BaseResponseError;
import model.errors.ValidationError;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static data.Errors.getIncorrectTokenError;
import static data.Errors.getValidationError;
import static data.TestParams.*;
import static steps.BaseSteps.*;
import static steps.DeliverySteps.*;

@Epic("/customers")
@Feature("/search")
@Execution(ExecutionMode.SAME_THREAD)
@Disabled("Тест отключен на проде")
public class PostCustomersSearchTest {
    private static Stream<Arguments> provideParamsForTestsWithValidParams() {
        return Stream.of(
                Arguments.of(
                        "у пользователя есть ДС, system+user_id+corr_id_holding есть в бд, sync_if_needed = false",
                        findExistingUserInCustomersDb("agreement"),
                        false,
                        "agreement",
                        "false"
                ),
                Arguments.of(
                        "у пользователя нет ДС, system+user_id+corr_id_holding есть в бд, sync_if_needed = false",
                        findExistingUserInCustomersDb("no"),
                        false,
                        "no",
                        "true"
                ),
                Arguments.of(
                        "у пользователя оферта, system+user_id+corr_id_holding есть в бд, sync_if_needed = false",
                        findExistingUserInCustomersDb("offer"),
                        false,
                        "offer",
                        "false"
                ),
                Arguments.of(
                        "у пользователя новый договор, system+user_id+corr_id_holding есть в бд, sync_if_needed = false",
                        findExistingUserInCustomersDb("new"),
                        false,
                        "new",
                        "false"
                ),
                Arguments.of(
                        "у пользователя есть ДС, system+user_id+corr_id_holding есть в бд, sync_if_needed = true",
                        findExistingUserInCustomersDb("agreement"),
                        true,
                        "agreement",
                        "false"
                ),
                Arguments.of(
                        "у пользователя нет ДС, system+user_id+corr_id_holding есть в бд, sync_if_needed = true",
                        findExistingUserInCustomersDb("no"),
                        true,
                        "no",
                        "true"
                ),
                Arguments.of(
                        "у пользователя оферта, system+user_id+corr_id_holding есть в бд, sync_if_needed = true",
                        findExistingUserInCustomersDb("offer"),
                        true,
                        "offer",
                        "false"
                ),
                Arguments.of(
                        "у пользователя новый договор, system+user_id+corr_id_holding есть в бд, sync_if_needed = true",
                        findExistingUserInCustomersDb("new"),
                        true,
                        "new",
                        "false"
                )
        );
    }
    @ParameterizedTest(name = "{0}")
    @DisplayName("с валидными параметрами")
    @MethodSource("provideParamsForTestsWithValidParams")
    public void postCustomersSearchWithValidParams(String description, PostAgreement body, Boolean sync_if_needed, String delivery_is_avaliable, String agreement_is_needed) {
        // Получаем данные о пользователе (фактический результат)
        body.setSyncIfNeeded(sync_if_needed);
        PostAgreementResponse actual = postCustomersSearch("correct_token", body, 200, PostAgreementResponse.class);

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, new PostAgreementResponse(
                new Customer(
                        body.getSystem(),
                        body.getUserId(),
                        body.getCorrIdHolding(),
                        delivery_is_avaliable
                ),
                "OK",
                agreement_is_needed
        ));
    }

    private static Stream<Arguments> provideParamsForTestsWithForeignCorrIdHoldingSyncIfNeededFalse() {
        return Stream.of(
                Arguments.of(
                        "у пользователя есть ДС, system+user_id есть в бд, другой corr_id_holding, sync_if_needed = false",
                        findExistingUserInCustomersDb("agreement"),
                        "agreement"
                ),
                Arguments.of(
                        "у пользователя нет ДС, system+user_id есть в бд, другой corr_id_holding, sync_if_needed = false",
                        findExistingUserInCustomersDb("no"),
                        "no"
                ),
                Arguments.of(
                        "у пользователя оферта, system+user_id есть в бд, другой corr_id_holding, sync_if_needed = false",
                        findExistingUserInCustomersDb("offer"),
                        "offer"
                ),
                Arguments.of(
                        "у пользователя новый договор, system+user_id есть в бд, другой corr_id_holding, sync_if_needed = false",
                        findExistingUserInCustomersDb("new"),
                        "new"
                )
        );
    }
    @ParameterizedTest(name = "{0}")
    @DisplayName("Проверка возможности доставки пользователю с другим corr_id_holding")
    @MethodSource("provideParamsForTestsWithForeignCorrIdHoldingSyncIfNeededFalse")
    public void postCustomersSearchWithForeignCorrIdHoldingSyncIfNeededFalse(String description, PostAgreement body, String delivery_is_avaliable) {
        // Запоминаем корректный corr_id_holding
        String correct_corr_id_holding = body.getCorrIdHolding();

        // Устанавливаем другой corr_id_holding и sync_if_needed = false
        body.setSyncIfNeeded(false);
        body.setCorrIdHolding(getRandomDigitalString(5));

        // Получаем данные о пользователе (фактический результат)
        PostAgreementResponse actual = postCustomersSearch("correct_token", body, 200, PostAgreementResponse.class);

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, new PostAgreementResponse(
                new Customer(
                        body.getSystem(),
                        body.getUserId(),
                        correct_corr_id_holding,
                        delivery_is_avaliable
                ),
                "Changed",
                null
        ));
    }

    private static Stream<Arguments> provideParamsForTestsWithForeignCorrIdHoldingSyncIfNeededTrue() {
        return Stream.of(
                Arguments.of(
                        "у пользователя есть ДС, system+user_id есть в бд, другой corr_id_holding, sync_if_needed = true",
                        findExistingUserInCustomersDb("agreement")
                ),
                Arguments.of(
                        "у пользователя нет ДС, system+user_id есть в бд, другой corr_id_holding, sync_if_needed = true",
                        findExistingUserInCustomersDb("no")
                ),
                Arguments.of(
                        "у пользователя оферта, system+user_id есть в бд, другой corr_id_holding, sync_if_needed = true",
                        findExistingUserInCustomersDb("offer")
                ),
                Arguments.of(
                        "у пользователя новый договор, system+user_id есть в бд, другой corr_id_holding, sync_if_needed = true",
                        findExistingUserInCustomersDb("new")
                )
        );
    }
    @ParameterizedTest(name = "{0}")
    @DisplayName("Проверка возможности доставки пользователю с другим corr_id_holding")
    @MethodSource("provideParamsForTestsWithForeignCorrIdHoldingSyncIfNeededTrue")
    public void postCustomersSearchWithForeignCorrIdHoldingSyncIfNeededTrue(String description, PostAgreement body) {
        // Запоминаем корректный corr_id_holding
        String correct_corr_id_holding = body.getCorrIdHolding();
        String new_corr_id_holding = getRandomDigitalString(5);

        // Устанавливаем другой corr_id_holding и sync_if_needed = true
        body.setSyncIfNeeded(true);
        body.setCorrIdHolding(new_corr_id_holding);

        // Получаем данные о пользователе (фактический результат)
        PostAgreementResponse actual = postCustomersSearch("correct_token", body, 200, PostAgreementResponse.class);

        // Проверяем возможность доставки (для формирования фактического результата)
        Customer customerCheck = postCustomersCheck(
                "correct_token",
                new PostAgreement(body.getSystem(), body.getUserId(), body.getCorrIdHolding()),
                200,
                Customer.class
        );
        String agreement_is_needed = customerCheck.getDeliveryIsAvailable().equals("no") ? "true" : "false";

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, new PostAgreementResponse(
                new Customer(
                        body.getSystem(),
                        body.getUserId(),
                        body.getCorrIdHolding(),
                        customerCheck.getDeliveryIsAvailable()
                ),
                "OK",
                agreement_is_needed
        ));

        // Возвращаем исходный corr_id_holding
        postCustomersCheck("correct_token", new PostAgreement(body.getSystem(), body.getUserId(), correct_corr_id_holding), 200, Customer.class);
    }

    private static Stream<Arguments> provideParamsForTestsWithNewUserSyncIfNeededTrue() {
        return Stream.of(
                Arguments.of(
                        "у пользователя нет ДС, system+user_id+corr_id_holding нет в бд, sync_if_needed = true",
                        findNewUserForAgreementInDb("agreement")
                ),
                Arguments.of(
                        "у пользователя оферта, system+user_id+corr_id_holding нет в бд, sync_if_needed = true",
                        findNewUserForAgreementInDb("offer")
                ),
                Arguments.of(
                        "у пользователя новый договор, system+user_id+corr_id_holding нет в бд, sync_if_needed = true",
                        findNewUserForAgreementInDb("new")
                )
        );
    }
    @ParameterizedTest(name = "{0}")
    @DisplayName("Проверка возможности доставки пользователю")
    @MethodSource("provideParamsForTestsWithNewUserSyncIfNeededTrue")
    public void postCustomersSearchWithWithNewUserSyncIfNeededTrue(String description, PostAgreement body) {
        // Получаем данные о пользователе (фактический результат)
        body.setSyncIfNeeded(true);
        PostAgreementResponse actual = postCustomersSearch("correct_token", body, 200, PostAgreementResponse.class);

        // Проверяем возможность доставки (для формирования фактического результата)
        Customer customerCheck = postCustomersCheck(
                "correct_token",
                new PostAgreement(body.getSystem(), body.getUserId(), body.getCorrIdHolding()),
                200,
                Customer.class
        );
        String agreement_is_needed = customerCheck.getDeliveryIsAvailable().equals("no") ? "true" : "false";

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, new PostAgreementResponse(
                new Customer(
                        body.getSystem(),
                        body.getUserId(),
                        body.getCorrIdHolding(),
                        customerCheck.getDeliveryIsAvailable()
                ),
                "OK",
                agreement_is_needed
        ));
    }

    private static Stream<Arguments> provideParamsForTestsWithNewUserSyncIfNeededFalse() {
        return Stream.of(
                Arguments.of(
                        "у пользователя есть ДС, system+user_id+corr_id_holding нет в бд, sync_if_needed = false",
                        findNewUserForAgreementInDb("agreement")
                ),
                Arguments.of(
                        "у пользователя оферта, system+user_id+corr_id_holding нет в бд, sync_if_needed = false",
                        findNewUserForAgreementInDb("offer")
                ),
                Arguments.of(
                        "у пользователя новый договор, system+user_id+corr_id_holding нет в бд, sync_if_needed = false",
                        findNewUserForAgreementInDb("new")
                )
        );
    }
    @ParameterizedTest(name = "{0}")
    @DisplayName("Проверка возможности доставки пользователю")
    @MethodSource("provideParamsForTestsWithNewUserSyncIfNeededFalse")
    public void postCustomersSearchWithWithNewUserSyncIfNeededFalse(String description, PostAgreement body) {
        // Получаем данные о пользователе (фактический результат)
        body.setSyncIfNeeded(false);
        PostAgreementResponse actual = postCustomersSearch("correct_token", body, 200, PostAgreementResponse.class);

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, new PostAgreementResponse(
                null,
                "NotFound",
                null
        ));
    }

    private static Stream<Arguments> provideParamsForTestsWithInvalidParams() {
        return Stream.of(
                Arguments.of(
                        "с невалидным system",
                        new PostAgreement("sdf", getUid().toString(), getCorrId().toString(), false),
                        "type_error.enum"
                ),
                Arguments.of(
                        "с невалидным corr_id_holding",
                        new PostAgreement("B2B", getUid().toString(), "qwe123", false),
                        "type_error.integer"
                ),
                Arguments.of(
                        "с невалидным sync_if_needed",
                        new PostAgreement("B2B", getUid().toString(), getCorrId().toString(), "qwe"),
                        "type_error.bool"
                ),
                Arguments.of(
                        "с длинным user_id",
                        new PostAgreement("B2B", "1234567890765", getCorrId().toString(), false),
                        "value_error.any_str.max_length"
                ),
                Arguments.of(
                        "без user_id",
                        new PostAgreement("B2B", null, getCorrId().toString(), false),
                        "value_error.missing"
                ),
                Arguments.of(
                        "без corr_id_holding",
                        new PostAgreement("B2B", getUid().toString(), null, false),
                        "value_error.missing"
                ),
                Arguments.of(
                        "без system",
                        new PostAgreement(null, getUid().toString(), getCorrId().toString(), false),
                        "value_error.missing"
                ),
                Arguments.of(
                        "без sync_if_needed",
                        new PostAgreement("B2B", getUid().toString(), getCorrId().toString(), null),
                        "value_error.missing"
                )
        );
    }
    @ParameterizedTest(name = "{0}")
    @DisplayName("Проверка возможности доставки пользователю с невалидными параметрами")
    @MethodSource("provideParamsForTestsWithInvalidParams")
    public void provideParamsForTestsWithInvalidParams(String description, PostAgreement body, String type) {
        // Получаем данные о пользователе (фактический результат)
        ValidationError actual = postCustomersSearch(
                "correct_token",
                body,
                422,
                ValidationError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError(type));
    }

    @Test
    @DisplayName("Проверка возможности доставки пользователю с некорректным токеном")
    public void getCustomersHoldingsSearchWithIncorrectToken() {
        // Получаем данные о пользователе (фактический результат)
        BaseResponseError actual = postCustomersSearch(
                "incorrect_token",
                new PostAgreement("B2B", getUid().toString(), getCorrId().toString(), false ),
                401,
                BaseResponseError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getIncorrectTokenError());
    }
}
