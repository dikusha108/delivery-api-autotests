package customers;

import io.qameta.allure.Flaky;
import model.customers.Customer;
import model.customers.PostAgreement;
import model.customers.PostAgreementResponse;
import model.errors.BaseResponseError;
import model.errors.ValidationError;
import org.junit.jupiter.api.BeforeAll;
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
import static steps.DeliverySteps.*;

@Disabled("Тест отключен на проде")
public class PostCustomersAgreementTest {
    @BeforeAll
    public static void setUp() {
        postCustomersCheck(
                "correct_token",
                findNewUserForAgreementInDb("agreement"),
                200,
                Customer.class
        );
    }

    @Test
    @DisplayName("Заключение доп. соглашения с валидными параметрами, новый пользователь")
    public void postCustomersAgreementWithValidParamsAgreementAvaliable() {
        // Формируем тело запроса
        PostAgreement customer = findNewUserForAgreementInDb("agreement");
        
        // Отправляем запрос на заключение ДС
        PostAgreementResponse actualAgreement = postCustomersAgreement(
                "correct_token",
                customer,
                200,
                PostAgreementResponse.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actualAgreement, new PostAgreementResponse(new Customer("B2B", customer.getUserId(), customer.getCorrIdHolding(), "agreement")));

        // Получаем информацию о холдинге (фактический результат)
        Customer actual = postCustomersCheck(
                "correct_token",
                new PostAgreement(
                        "B2B",
                        customer.getUserId(),
                        customer.getCorrIdHolding()
                ),
                200,
                Customer.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, new Customer("B2B", customer.getUserId(), customer.getCorrIdHolding(), "agreement"));
    }

    private static Stream<Arguments> provideParamsForTestsWithValidParams() {
        return Stream.of(
                Arguments.of(
                    "пользователь с офертой",
                    findNewUserForAgreementInDb("offer"),
                        "offer",
                        "Доставка доступна (у холдинга пользователя есть только договор оферты)"
                )
                /*,
                // раскомментирую, когда будет добавлен метод удаления подписания соглашения
                Arguments.of(
                        "пользователь с новым договором",
                        findNewUserForAgreementInDb("new"),
                        "new",
                        "Доставка доступна (у холдинга пользователя есть только новые договора)"
                )

                 */
        );
    }
    @ParameterizedTest(name = "{0}")
    @DisplayName("Заключение доп. соглашения с валидными параметрами")
    @MethodSource("provideParamsForTestsWithValidParams")
    public void postCustomersAgreementWithValidParams(String description, PostAgreement customer, String type_of_user, String detail) {
        // Отправляем запрос на заключение ДС (фактический результат)
        BaseResponseError actualAgreement = postCustomersAgreement(
                "correct_token",
                customer,
                400,
                BaseResponseError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actualAgreement, new BaseResponseError(detail));

        // Получаем информацию о холдинге (фактический результат)
        Customer actual = postCustomersCheck(
                "correct_token",
                new PostAgreement(
                        "B2B",
                        customer.getUserId(),
                        customer.getCorrIdHolding()
                ),
                200,
                Customer.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, new Customer("B2B", customer.getUserId(), customer.getCorrIdHolding(), type_of_user));
    }

    @Test
    @Flaky
    @DisplayName("Заключение доп. соглашения с валидными параметрами, пользователь уже есть в системе (доставка запрещена)")
    public void postCustomersAgreementWithExistingUserWithoutAgreement() {
        // Формируем тело запроса
        String user_id = getValueFromPSQL(
                "SELECT cc.user_id from public.customers_customer cc " +
                        "where cc.delivery_is_available = 'no' order by random() limit 1 ;",
                "user_id",
                String.class
        );
        Integer corr_id_holding = getValueFromPSQL(
                "SELECT cc.corr_id_holding from public.customers_customer cc " +
                        "where cc.user_id = '" + user_id + "';",
                "corr_id_holding",
                Integer.class
        );
        PostAgreement customer = new PostAgreement("B2B", user_id, corr_id_holding.toString());

        // Отправляем запрос на заключение ДС (фактический результат)
        PostAgreementResponse actualAgreement = postCustomersAgreement(
                "correct_token",
                customer,
                200,
                PostAgreementResponse.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actualAgreement, new PostAgreementResponse(new Customer("B2B", customer.getUserId(), customer.getCorrIdHolding(), "agreement")));

        // Получаем информацию о холдинге  (фактический результат)
        Customer actual = postCustomersCheck(
                "correct_token",
                new PostAgreement(
                        "B2B",
                        customer.getUserId(),
                        customer.getCorrIdHolding()
                ),
                200,
                Customer.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, new Customer("B2B", customer.getUserId(), customer.getCorrIdHolding(), "agreement"));
    }

    @Test
    @DisplayName("Заключение доп. соглашения, пользователь уже есть в системе (оферта)")
    public void postCustomersAgreementWithExistingUserWithOffer() {
        // Формируем тело запроса
        String user_id = getValueFromPSQL(
                "SELECT cc.user_id from public.customers_customer cc " +
                        "where cc.delivery_is_available = 'offer' order by random() limit 1;",
                "user_id",
                String.class
        );
        Integer corr_id_holding = getValueFromPSQL(
                "SELECT cc.corr_id_holding from public.customers_customer cc " +
                        "where cc.user_id = '" + user_id + "';",
                "corr_id_holding",
                Integer.class
        );
        PostAgreement customer = new PostAgreement("B2B", user_id, corr_id_holding.toString());

        // Отправляем запрос на заключение ДС (фактический результат)
        BaseResponseError actualAgreement = postCustomersAgreement(
                "correct_token",
                customer,
                400,
                BaseResponseError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actualAgreement, new BaseResponseError("Доставка доступна (у холдинга пользователя есть только договор оферты)"));

        // Получаем информацию о холдинге  (фактический результат)
        Customer actual = postCustomersCheck(
                "correct_token",
                new PostAgreement(
                        "B2B",
                        customer.getUserId(),
                        customer.getCorrIdHolding()
                ),
                200,
                Customer.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, new Customer("B2B", customer.getUserId(), customer.getCorrIdHolding(), "offer"));
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
    @DisplayName("Заключение доп. соглашения с невалидными параметрами")
    @MethodSource("provideParamsForTestsWithInvalidParams")
    public void provideParamsForTestsWithInvalidParams(String description, PostAgreement body, String type) {
        // Отправляем запрос на заключение ДС (фактический результат)
        ValidationError actual = postCustomersAgreement(
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
        // Отправляем запрос на заключение ДС (фактический результат)
        BaseResponseError actual = postCustomersAgreement(
                "incorrect_token",
                new PostAgreement("B2B", getUid().toString(), getCorrId().toString()),
                401,
                BaseResponseError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getIncorrectTokenError());
    }
}
