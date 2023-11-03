package delivery;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import model.delivery.DeliveryPoint;
import model.errors.BaseResponseError;
import model.errors.ValidationError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.HashMap;
import java.util.List;

import static data.Errors.getIncorrectTokenError;
import static data.Errors.getValidationError;
import static data.TestParams.*;
import static steps.BaseSteps.checkBody;
import static steps.DeliverySteps.getDeliveryPoints;
import static steps.DeliverySteps.getDeliveryPointsFromDb;

@Epic("/delivery")
@Feature("/points")
@Execution(ExecutionMode.SAME_THREAD)
public class GetDeliveryPointsTest {
    @Test
    @DisplayName("Просмотр списка ПВЗ с 4 валидными параметрами")
    public void getDeliveryPointsWith4Params() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("transport_company", getPecomSlug());
        queryParamsList.put("available_for_delivery", true);
        queryParamsList.put("no_weight_limits", true);
        queryParamsList.put("multiplace", false);

        // Получаем список заявок на доставку
        List<DeliveryPoint> actual = getDeliveryPoints("correct_token", queryParamsList, 200);

        // Получаем список заявок на доставку (ожидаемый результат)
        List<DeliveryPoint> expected = getDeliveryPointsFromDb(getPecom(), true, true, false);

        // Сверяем ожидаемый результат с фактическим (список позиций в счете)
        checkBody(actual, expected);
    }

    @Test
    @DisplayName("Просмотр списка ПВЗ с 3 валидными параметрами")
    public void getDeliveryPointsWith3Params() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("transport_company", getPecomSlug());
        queryParamsList.put("no_weight_limits", true);
        queryParamsList.put("multiplace", false);

        // Получаем список заявок на доставку
        List<DeliveryPoint> actual = getDeliveryPoints("correct_token", queryParamsList, 200);

        // Получаем список заявок на доставку (ожидаемый результат)
        List<DeliveryPoint> expected = getDeliveryPointsFromDb(getPecom(), null, true, false);

        // Сверяем ожидаемый результат с фактическим (список позиций в счете)
        checkBody(actual, expected);
    }

    @Test
    @DisplayName("Просмотр списка ПВЗ с 2 валидными параметрами")
    public void getDeliveryPointsWith2Params() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("transport_company", getCdekSlug());
        queryParamsList.put("available_for_delivery", true);

        // Получаем список заявок на доставку
        List<DeliveryPoint> actual = getDeliveryPoints("correct_token", queryParamsList, 200);

        // Получаем список заявок на доставку (ожидаемый результат)
        List<DeliveryPoint> expected = getDeliveryPointsFromDb(getCdek(), true, null, null);

        // Сверяем ожидаемый результат с фактическим (список позиций в счете)
        checkBody(actual, expected);
    }

    @Test
    @DisplayName("Просмотр списка ПВЗ c transport_company = cdek")
    public void getDeliveryPointsWithTransportCompanyIsCdek() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("transport_company", getCdekSlug());

        // Получаем список заявок на доставку
        List<DeliveryPoint> actual = getDeliveryPoints("correct_token", queryParamsList, 200);

        // Получаем список заявок на доставку (ожидаемый результат)
        List<DeliveryPoint> expected = getDeliveryPointsFromDb(getCdek(), null, null, null);

        // Сверяем ожидаемый результат с фактическим (список позиций в счете)
        checkBody(actual, expected);
    }

    @Test
    @DisplayName("Просмотр списка ПВЗ c transport_company = peccom")
    public void getDeliveryPointsWithTransportCompanyIsPeccoom() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("transport_company", getPecomSlug());

        // Получаем список заявок на доставку
        List<DeliveryPoint> actual = getDeliveryPoints("correct_token", queryParamsList, 200);

        // Получаем список заявок на доставку (ожидаемый результат)
        List<DeliveryPoint> expected = getDeliveryPointsFromDb(getPecom(), null, null, null);

        // Сверяем ожидаемый результат с фактическим (список позиций в счете)
        checkBody(actual, expected);
    }

    @Test
    @DisplayName("Просмотр списка ПВЗ c не существующим transport_company")
    public void getDeliveryPointsWithNonexistentTransportCompany() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("transport_company", "qwe");

        // Получаем список заявок на доставку
        ValidationError actual = getDeliveryPoints("correct_token", queryParamsList, 422, ValidationError.class);

        // Сверяем ожидаемый результат с фактическим (список позиций в счете)
        checkBody(actual, getValidationError("type_error.enum") );
    }

    @Test
    @DisplayName("Просмотр списка ПВЗ c available_for_delivery = true")
    public void getDeliveryPointsWithAvailableForDeliveryIsTrue() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("available_for_delivery", true);

        // Получаем список заявок на доставку
        List<DeliveryPoint> actual = getDeliveryPoints("correct_token", queryParamsList, 200);

        // Получаем список заявок на доставку (ожидаемый результат)
        List<DeliveryPoint> expected = getDeliveryPointsFromDb(null, true, null, null);

        // Сверяем ожидаемый результат с фактическим (список позиций в счете)
        checkBody(actual, expected);
    }

    @Test
    @DisplayName("Просмотр списка ПВЗ c available_for_delivery = false")
    public void getDeliveryPointsWithAvailableForDeliveryIsFalse() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("available_for_delivery", false);

        // Получаем список заявок на доставку
        List<DeliveryPoint> actual = getDeliveryPoints("correct_token", queryParamsList, 200);

        // Получаем список заявок на доставку (ожидаемый результат)
        List<DeliveryPoint> expected = getDeliveryPointsFromDb(null, false, null, null);

        // Сверяем ожидаемый результат с фактическим (список позиций в счете)
        checkBody(actual, expected);
    }

    @Test
    @DisplayName("Просмотр списка ПВЗ c не валидным available_for_delivery")
    public void getDeliveryPointsWithInvalidForDelivery() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("available_for_delivery", "qwe");

        // Получаем список заявок на доставку
        ValidationError actual = getDeliveryPoints("correct_token", queryParamsList, 422, ValidationError.class);

        // Сверяем ожидаемый результат с фактическим (список позиций в счете)
        checkBody(actual, getValidationError("type_error.bool"));
    }

    @Test
    @DisplayName("Просмотр списка ПВЗ c no_weight_limits = true")
    public void getDeliveryPointsWithNoWeightLimitsIsTrue() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("no_weight_limits", true);

        // Получаем список заявок на доставку
        List<DeliveryPoint> actual = getDeliveryPoints("correct_token", queryParamsList, 200);

        // Получаем список заявок на доставку (ожидаемый результат)
        List<DeliveryPoint> expected = getDeliveryPointsFromDb(null, null, true, null);

        // Сверяем ожидаемый результат с фактическим (список позиций в счете)
        checkBody(actual, expected);
    }

    @Test
    @DisplayName("Просмотр списка ПВЗ c no_weight_limits = false")
    public void getDeliveryPointsWithNoWeightLimitsIsIsFalse() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("no_weight_limits", false);

        // Получаем список заявок на доставку
        List<DeliveryPoint> actual = getDeliveryPoints("correct_token", queryParamsList, 200);

        // Получаем список заявок на доставку (ожидаемый результат)
        List<DeliveryPoint> expected = getDeliveryPointsFromDb(null, null, false, null);

        // Сверяем ожидаемый результат с фактическим (список позиций в счете)
        checkBody(actual, expected);
    }

    @Test
    @DisplayName("Просмотр списка ПВЗ c не валидным no_weight_limits")
    public void getDeliveryPointsWithInvalidNoWeightLimits() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("no_weight_limits", "qwe");

        // Получаем список заявок на доставку
        ValidationError actual = getDeliveryPoints("correct_token", queryParamsList, 422, ValidationError.class);

        // Сверяем ожидаемый результат с фактическим (список позиций в счете)
        checkBody(actual, getValidationError("type_error.bool"));
    }

    @Test
    @DisplayName("Просмотр списка ПВЗ c multiplace = true")
    public void getDeliveryPointsWithMultiplaceIsTrue() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("multiplace", true);

        // Получаем список заявок на доставку
        List<DeliveryPoint> actual = getDeliveryPoints("correct_token", queryParamsList, 200);

        // Получаем список заявок на доставку (ожидаемый результат)
        List<DeliveryPoint> expected = getDeliveryPointsFromDb(null, null, null, true);

        // Сверяем ожидаемый результат с фактическим (список позиций в счете)
        checkBody(actual, expected);
    }

    @Test
    @DisplayName("Просмотр списка ПВЗ c multiplace = false")
    public void getDeliveryPointsWithMultiplaceFalse() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("multiplace", false);

        // Получаем список заявок на доставку
        List<DeliveryPoint> actual = getDeliveryPoints("correct_token", queryParamsList, 200);

        // Получаем список заявок на доставку (ожидаемый результат)
        List<DeliveryPoint> expected = getDeliveryPointsFromDb(null, null, null, false);

        // Сверяем ожидаемый результат с фактическим (список позиций в счете)
        checkBody(actual, expected);
    }

    @Test
    @DisplayName("Просмотр списка ПВЗ c не валидным multiplace")
    public void getDeliveryPointsWithInvalidMultiplace() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("multiplace", "qwe");

        // Получаем список заявок на доставку
        ValidationError actual = getDeliveryPoints("correct_token", queryParamsList, 422, ValidationError.class);

        // Сверяем ожидаемый результат с фактическим (список позиций в счете)
        checkBody(actual, getValidationError("type_error.bool"));
    }

    @Test
    @DisplayName("Просмотр списка ПВЗ с не корректным токеном")
    public void getDeliveryPointsWithIncorrectToken() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("transport_company", getPecomSlug());
        queryParamsList.put("available_for_delivery", true);
        queryParamsList.put("no_weight_limits", true);
        queryParamsList.put("multiplace", false);

        // Получаем список заявок на доставку
        BaseResponseError actual = getDeliveryPoints("incorrect_token", queryParamsList, 401, BaseResponseError.class);

        // Сверяем ожидаемый результат с фактическим (список позиций в счете)
        checkBody(actual, getIncorrectTokenError());
    }
}
