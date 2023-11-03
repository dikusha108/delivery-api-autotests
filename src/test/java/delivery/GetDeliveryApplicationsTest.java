package delivery;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import model.delivery.DeliveryApplication;
import model.errors.BaseResponseError;
import model.errors.ValidationError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.List;

import static data.Errors.getIncorrectTokenError;
import static data.Errors.getValidationError;
import static data.TestParams.*;
import static steps.BaseSteps.checkBody;
import static steps.BaseSteps.checkLists;
import static steps.DeliverySteps.getDeliveryApplications;
import static steps.DeliverySteps.getDeliveryApplicationsFromDb;

@Epic("/delivery")
@Feature("/applications")
public class GetDeliveryApplicationsTest {
    @Test
    @DisplayName("Просмотр заявок на доставку с 4 валидными параметрами")
    public void getDeliveryApplicationsWith4Params() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("system", "B2B");
        queryParamsList.put("user_id", getUid());
        queryParamsList.put("corr_id", getCorrId());
        queryParamsList.put("corr_id_holding", getCorrId());

        // Получаем список заявок на доставку
        List<DeliveryApplication> actual = getDeliveryApplications("correct_token", queryParamsList,200);

        // Получаем список заявок на доставку (ожидаемый результат)
        List<DeliveryApplication> expected = getDeliveryApplicationsFromDb("B2B", getUid(), getCorrId(), getCorrId());

        // Сверяем ожидаемый результат с фактическим
        checkLists(actual, expected);
    }

    @Test
    @DisplayName("Просмотр заявок на доставку с 3 валидными параметрами")
    public void getDeliveryApplicationsWith3Params() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("system", "B2B");
        queryParamsList.put("user_id", getUid());
        queryParamsList.put("corr_id_holding", getCorrId());

        // Получаем список заявок на доставку
        List<DeliveryApplication> actual = getDeliveryApplications("correct_token", queryParamsList,200);

        // Получаем список заявок на доставку (ожидаемый результат)
        List<DeliveryApplication> expected = getDeliveryApplicationsFromDb("B2B", getUid(), null, getCorrId());

        // Сверяем ожидаемый результат с фактическим
        checkLists(actual, expected);
    }

    @Test
    @DisplayName("Просмотр заявок на доставку с 2 валидными параметрами")
    public void getDeliveryApplicationsWith2Params() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("system", "B2B");
        queryParamsList.put("corr_id_holding", getCorrId());

        // Получаем список заявок на доставку
        List<DeliveryApplication> actual = getDeliveryApplications("correct_token", queryParamsList,200);

        // Получаем список заявок на доставку (ожидаемый результат)
        List<DeliveryApplication> expected = getDeliveryApplicationsFromDb("B2B", null, null, getCorrId());

        // Сверяем ожидаемый результат с фактическим
        checkLists(actual, expected);
    }

    @Test
    @DisplayName("Просмотр заявок на доставку с фильтром по system")
    public void getDeliveryApplicationsWithSystem() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("system", "B2B");

        // Получаем список заявок на доставку
        List<DeliveryApplication> actual = getDeliveryApplications("correct_token", queryParamsList,200);

        // Получаем список заявок на доставку (ожидаемый результат)
        List<DeliveryApplication> expected = getDeliveryApplicationsFromDb("B2B", null, null, null);

        // Сверяем ожидаемый результат с фактическим
        checkLists(actual, expected);
    }

    @Test
    @DisplayName("Просмотр заявок на доставку с фильтром по user_id")
    public void getDeliveryApplicationsWithUserId() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("user_id", getUid());

        // Получаем список заявок на доставку
        List<DeliveryApplication> actual = getDeliveryApplications("correct_token", queryParamsList,200);

        // Получаем список заявок на доставку (ожидаемый результат)
        List<DeliveryApplication> expected = getDeliveryApplicationsFromDb(null, getUid(), null, null);

        // Сверяем ожидаемый результат с фактическим
        checkLists(actual, expected);
    }

    @Test
    @DisplayName("Просмотр заявок на доставку с фильтром по corr_id_holding")
    public void getDeliveryApplicationsWithCorrIdHolding() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("corr_id_holding", getCorrId());

        // Получаем список заявок на доставку
        List<DeliveryApplication> actual = getDeliveryApplications("correct_token", queryParamsList,200);

        // Получаем список заявок на доставку (ожидаемый результат)
        List<DeliveryApplication> expected = getDeliveryApplicationsFromDb(null, null, null, getCorrId());

        // Сверяем ожидаемый результат с фактическим
        checkLists(actual, expected);
    }

    @Test
    @DisplayName("Просмотр заявок на доставку с фильтром по corr_id")
    public void getDeliveryApplicationsWithCorrId() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("corr_id", getCorrId());

        // Получаем список заявок на доставку
        List<DeliveryApplication> actual = getDeliveryApplications("correct_token", queryParamsList,200);

        // Получаем список заявок на доставку (ожидаемый результат)
        List<DeliveryApplication> expected = getDeliveryApplicationsFromDb(null, null, getCorrId(), null);

        // Сверяем ожидаемый результат с фактическим
        checkLists(actual, expected);
    }

    @Test
    @DisplayName("Просмотр заявок на доставку с не существующим system")
    public void getDeliveryApplicationsWithUnexistingSystem() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("system", "123");

        // Получаем список заявок на доставку
        ValidationError actual = getDeliveryApplications("correct_token", queryParamsList,422, ValidationError.class);

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("type_error.enum", "value_error"));
    }

    @Test
    @DisplayName("Просмотр заявок на доставку без параметров")
    public void getDeliveryApplicationsWithoutParams() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();

        // Получаем список заявок на доставку
        ValidationError actual = getDeliveryApplications("correct_token", queryParamsList,422, ValidationError.class);

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("value_error"));
    }

    @Test
    @DisplayName("Просмотр заявок на доставку не корректным токеном")
    public void getDeliveryApplicationsWithIncorrectToken() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("corr_id_holding", getCorrId());

        // Получаем список заявок на доставку
        BaseResponseError actual = getDeliveryApplications("incorrect_token", queryParamsList,401, BaseResponseError.class);

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getIncorrectTokenError());
    }

    @ParameterizedTest
    @ValueSource(strings = {"user_id", "corr_id", "corr_id_holding"})
    @DisplayName("Просмотр заявок на доставку с не существующим параметром")
    public void getDeliveryApplicationsWithUnexistingParam(String param) {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put(param, getUnexistingCorrId());

        // Получаем список заявок на доставку
        List<DeliveryApplication> actual = getDeliveryApplications("correct_token", queryParamsList,200);

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, List.of());
    }

    @ParameterizedTest
    @ValueSource(strings = {"user_id", "corr_id", "corr_id_holding"})
    @DisplayName("Просмотр заявок на доставку с не валидным параметром")
    public void getDeliveryApplicationsWithInvalidParam(String param) {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put(param, "qwe");

        // Получаем список заявок на доставку
        ValidationError actual = getDeliveryApplications("correct_token", queryParamsList,422, ValidationError.class);

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("type_error.integer", "value_error"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"user_id", "corr_id", "corr_id_holding"})
    @DisplayName("Просмотр заявок на доставку с пустым параметром")
    public void getDeliveryApplicationsWithEmptyParam(String param) {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put(param, "");

        // Получаем список заявок на доставку
        ValidationError actual = getDeliveryApplications("correct_token", queryParamsList,422, ValidationError.class);

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("type_error.integer", "value_error"));
    }
}