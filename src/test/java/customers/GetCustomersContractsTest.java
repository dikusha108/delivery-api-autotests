package customers;

import model.customers.Contract;
import model.errors.BaseResponseError;
import model.errors.ValidationError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static data.Errors.getIncorrectTokenError;
import static data.Errors.getValidationError;
import static data.TestParams.getCorrId;
import static steps.BaseSteps.*;
import static steps.DeliverySteps.getCustomersContracts;
import static steps.DeliverySteps.getCustomersContractsFromDb;

public class GetCustomersContractsTest {
    private static Stream<Arguments> provideParamsForTests() {
        return Stream.of(
                Arguments.of(
                    "холдинг имеет договоры, требующие подписание ДС",
                    "SELECT FIRST 1 c.CORR_ID FROM CORR c \n" +
                            "JOIN SITE_APP_FORM saf ON saf.CORR_ID_HOLDING = c.CORR_ID \n" +
                            "JOIN CONTRACT c2 ON c2.CORR_ID = c.CORR_ID \n" +
                            "WHERE c.CORR_ISACTIVE = 1 AND CORR_ISTRANSCOMPANY = 0 AND \n" +
                            "CORR_INN IS NOT NULL and \"PersonType\" = 0 AND c2.CNTT_ID = 1\n" +
                            "ORDER BY RAND();"
                ),
                Arguments.of(
                        "холдинг не имеет договоры, требующие подписание ДС",
                        "SELECT FIRST 1 c.CORR_ID FROM CORR c " +
                        "JOIN SITE_APP_FORM saf ON saf.CORR_ID_HOLDING = c.CORR_ID " +
                        "WHERE c.CORR_ISACTIVE = 1 AND CORR_ISTRANSCOMPANY = 0 AND CORR_INN IS NOT NULL " +
                        " AND \"PersonType\" = 0 AND NOT EXISTS (" +
                        " SELECT 1 FROM CONTRACT c2 WHERE c2.CORR_ID = c.CORR_ID AND c2.CNTT_ID IN (1, 2) " +
                        ") ORDER BY RAND()"
                )
        );
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Получение списка договоров, требующих подписания ДС с валидными параметрами,")
    @MethodSource("provideParamsForTests")
    public void getCustomersContractsWithValidParams(String description, String query) {
        // Получаем случайный corr_id_holding из БД, у которого есть ДС
        Integer corr_id_holding = getValueFromFB(query, "corr_id", Integer.class);

        // Получаем список договоров (фактический результат)
        List<Contract> actual = getCustomersContracts("correct_token", corr_id_holding, 200);

        // Получаем список договоров (ожидаемый результат)
        List<Contract> expected = getCustomersContractsFromDb(corr_id_holding);

        // Сверяем ожидаемый результат с фактическим
        checkLists(actual, expected);
    }

    @Test
    @DisplayName("Получение списка договоров, требующих подписания ДС с не валидным corr_id_holding")
    public void getCustomersContractsWithInvalidCorrIdHolding() {
        // Получаем список договоров (фактический результат)
        ValidationError actual = getCustomersContracts(
                "correct_token",
                "5102qwe6",
                422,
                ValidationError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("type_error.integer"));
    }

    @Test
    @DisplayName("Получение списка договоров, требующих подписания ДС с несуществующим corr_id_holding")
    public void getCustomersHoldingsSearchWithNonexistentCorrIdHolding() {
        // Получаем список договоров (фактический результат)
        List<Contract> actual = getCustomersContracts("correct_token",  1,  200);

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, List.of());
    }

    @Test
    @DisplayName("Получение списка договоров, требующих подписания ДС с некорректным токеном")
    public void getCustomersHoldingsSearchWithIncorrectToken() {
        // Получаем список договоров (фактический результат)
        BaseResponseError actual = getCustomersContracts(
                "incorrect_token",
                getCorrId(),
                401,
                BaseResponseError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getIncorrectTokenError());
    }
}
