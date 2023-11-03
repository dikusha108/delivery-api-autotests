package customers;

import model.customers.GetCustomersHoldingsSearchResponse;
import model.errors.BaseResponseError;
import model.errors.ValidationError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.stream.Stream;

import static data.Errors.getIncorrectTokenError;
import static data.Errors.getValidationError;
import static data.TestParams.getCorrId;
import static steps.BaseSteps.checkBody;
import static steps.BaseSteps.getValueFromPSQL;
import static steps.DeliverySteps.getCustomersHoldingsSearch;
import static steps.DeliverySteps.getCustomersHoldingsSearchFromDb;

public class GetCustomersHoldingsSearchTest {
    private static Stream<Arguments> provideParamsForTests() {
        return Stream.of(
                Arguments.of(
                    "пользователь холдинга имеет ДС",
                    "select corr_id_holding\n" +
                    "FROM public.customers_customer\n" +
                    "where delivery_is_available = 'agreement' order by random() limit 1;",
                    true
                ),
                Arguments.of(
                        "в холдинге нет пользователей с ДС",
                        "SELECT cc.corr_id_holding  \n" +
                        "from public.customers_customer cc \n" +
                        "left join public.customers_agreement ca on ca.customer_id = cc.id\n" +
                        "where cc.delivery_is_available != 'agreement'\n" +
                        "group by cc.corr_id_holding\n" +
                        "having count(cc.user_id) = 1 order by random() limit 1",
                        false
                ),
                Arguments.of(
                        "в холдинге только пользователи с офертой",
                        "SELECT cc.corr_id_holding  \n" +
                        "from public.customers_customer cc \n" +
                        "left join public.customers_agreement ca on ca.customer_id = cc.id\n" +
                        "where cc.delivery_is_available = 'offer'\n" +
                        "group by cc.corr_id_holding\n" +
                        "having count(cc.user_id) = 1 order by random() limit 1",
                        false
                ),
                Arguments.of(
                        "в холдинге только пользователи с новым договором",
                        "SELECT cc.corr_id_holding  \n" +
                        "from public.customers_customer cc \n" +
                        "left join public.customers_agreement ca on ca.customer_id = cc.id\n" +
                        "where cc.delivery_is_available = 'new'\n" +
                        "group by cc.corr_id_holding\n" +
                        "having count(cc.user_id) = 1 order by random() limit 1",
                        true
                )
        );
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Просмотр информации о холдинге с валидными параметрами,")
    @MethodSource("provideParamsForTests")
    public void getCustomersHoldingsSearchUserHasAgreement(String description, String query, Boolean has_agreement) {
        // Получаем случайный corr_id_holding из БД, у которого есть ДС
        Integer corr_id_holding = getValueFromPSQL(query, "corr_id_holding", Integer.class);

        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("corr_id_holding", corr_id_holding);

        // Получаем информацию о холдинге (фактический результат)
        GetCustomersHoldingsSearchResponse actual = getCustomersHoldingsSearch(
                 "correct_token",
                 queryParamsList,
                 200,
                GetCustomersHoldingsSearchResponse.class
         );

        // Получаем информацию о холдинге (ожидаемый результат)
        GetCustomersHoldingsSearchResponse expected = getCustomersHoldingsSearchFromDb(corr_id_holding, has_agreement);

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, expected);
    }

    private static Stream<Arguments> provideParamsForNegativeTests() {
        return Stream.of(
                Arguments.of(
                        "не валидным corr_id_holding",
                        "5102qwe6",
                        "type_error.integer"
                ),
                Arguments.of(
                        "не пустым corr_id_holding",
                        "",
                        "type_error.integer"
                )
        );
    }
    @ParameterizedTest(name = "{0}")
    @DisplayName("Просмотр информации о холдинге с ")
    @MethodSource("provideParamsForNegativeTests")
    public void getCustomersHoldingsSearchWithInvalidCorrIdHolding(String description, String corr_id_holding, String type) {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("corr_id_holding", corr_id_holding);

        // Получаем информацию о холдинге (фактический результат)
        ValidationError actual = getCustomersHoldingsSearch(
                "correct_token",
                queryParamsList,
                422,
                ValidationError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError(type));
    }

    @Test
    @DisplayName("Просмотр информации о холдинге с несуществующим corr_id_holding")
    public void getCustomersHoldingsSearchWithNonexistentCorrIdHolding() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("corr_id_holding", 1);

        // Получаем информацию о холдинге (фактический результат)
        BaseResponseError actual = getCustomersHoldingsSearch(
                "correct_token",
                queryParamsList,
                404,
                BaseResponseError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, new BaseResponseError("Не найдено"));
    }

    @Test
    @DisplayName("Просмотр информации о холдинге с некорректным токеном")
    public void getCustomersHoldingsSearchWithIncorrectToken() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("corr_id_holding", getCorrId());

        // Получаем информацию о холдинге (фактический результат)
        BaseResponseError actual = getCustomersHoldingsSearch(
                "incorrect_token",
                queryParamsList,
                401,
                BaseResponseError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getIncorrectTokenError());
    }
}
