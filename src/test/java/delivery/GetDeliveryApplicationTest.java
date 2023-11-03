package delivery;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import model.delivery.DeliveryApplication;
import model.errors.BaseResponseError;
import model.errors.Detail;
import model.errors.ValidationError;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.HashMap;
import java.util.List;

import static data.Errors.getUnexistingApplicationError;
import static data.Errors.getValidationError;
import static data.TestParams.getCorrId;
import static data.TestParams.getUid;
import static steps.BaseSteps.checkBody;
import static steps.DeliverySteps.*;

@Epic("/delivery")
@Feature("/application")
@Execution(ExecutionMode.SAME_THREAD)
public class GetDeliveryApplicationTest {
    @Test
    @DisplayName("Просмотр заявки на доставку с address_check_id")
    public void getDeliveryApplicationWithDeliveryCheckId() {
        // Получаем список заявок на доставку
        DeliveryApplication expected = getDeliveryApplicationFromDb(" da1.address_check_id > 0");

        // Формируем query параметры
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("address_check_id", expected.getDeliveryCheck());

        // Получаем информацию о заявке на доставку (ожидаемый результат)
        DeliveryApplication actual = getDeliveryApplication(
                "correct_token", queryParams, 200, DeliveryApplication.class);


        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, expected,
                "address_check_id", 
                "invoice_id", 
                "delivery_application_id",
                "point.city",
                "point.city_fias_id_is_valid",
                "point.house",
                "point.multiplace",
                "point.no_weight_limits",
                "point.street",
                "point.streetType",
                "point.transport_company",
                "point.transport_company_is_active");
    }

    @Test
    @DisplayName("Просмотр заявки на доставку с invoice_id")
    public void getDeliveryApplicationWithInvoiceId() {
        // Получаем список заявок на доставку
        List<DeliveryApplication> applicationList = getDeliveryApplicationsFromDb("B2B", getUid(), getCorrId(), getCorrId());
        
        // Формируем query параметры
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("invoice_id", applicationList.get(1).getInvoiceId());

        // Получаем информацию о заявке на доставку (ожидаемый результат)
        DeliveryApplication actual = getDeliveryApplication("correct_token", queryParams, 200, DeliveryApplication.class);

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, applicationList.get(1),
                "address_check_id", 
                "invoice_id", 
                "delivery_application_id",
                "point.city",
                "point.id",
                "point.city_fias_id_is_valid",
                "point.house",
                "point.multiplace",
                "point.no_weight_limits",
                "point.street",
                "point.streetType",
                "point.transport_company",
                "point.transport_company_is_active");
    }

    @Test
    @DisplayName("Просмотр заявки на доставку с delivery_application_id")
    public void getDeliveryApplicationWithDeliveryApplicationId() {
        // Получаем список заявок на доставку
        List<DeliveryApplication> applicationList = getDeliveryApplicationsFromDb("B2B", getUid(), getCorrId(), getCorrId());
        
        // Формируем query параметры
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("delivery_application_id", applicationList.get(2).getId());

        // Получаем информацию о заявке на доставку (ожидаемый результат)
        DeliveryApplication actual = getDeliveryApplication("correct_token", queryParams, 200, DeliveryApplication.class);

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, applicationList.get(2),
                "address_check_id", 
                "invoice_id", 
                "delivery_application_id",
                "point.city",
                "point.id",
                "point.city_fias_id_is_valid",
                "point.house",
                "point.multiplace",
                "point.no_weight_limits",
                "point.street",
                "point.streetType",
                "point.transport_company",
                "point.transport_company_is_active");
    }

    @Test
    @Disabled("Из-за мусорных данных в бд работает не совсем корректно")
    @DisplayName("Просмотр заявки на доставку с двумя параметрами")
    public void getDeliveryApplicationWithTwoParams() {
        // Получаем список заявок на доставку
        List<DeliveryApplication> applicationList = getDeliveryApplicationsFromDb("B2B", getUid(), getCorrId(), getCorrId());
        
        // Формируем query параметры
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("delivery_application_id", applicationList.get(0).getId());
        queryParams.put("invoice_id", applicationList.get(0).getInvoiceId());

        // Получаем информацию о заявке на доставку (ожидаемый результат)
        DeliveryApplication actual = getDeliveryApplication("correct_token", queryParams, 200, DeliveryApplication.class);

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, applicationList.get(0), 
                "address_check_id", 
                "invoice_id", 
                "delivery_application_id",
                "point.city",
                "point.city_fias_id_is_valid",
                "point.house",
                "point.multiplace",
                "point.no_weight_limits",
                "point.street",
                "point.streetType",
                "point.transport_company",
                "point.transport_company_is_active");
    }

    @Test
    @DisplayName("Просмотр заявки на доставку с тремя параметрами")
    public void getDeliveryApplicationWithThreeParams() {
        // Получаем список заявок на доставку
        List<DeliveryApplication> applicationList = getDeliveryApplicationsFromDb("B2B", getUid(), getCorrId(), getCorrId());
        
        // Формируем query параметры
        HashMap<String, Object> queryParams = new HashMap<>();
        //queryParams.put("address_check_id", applicationList.get(0).getDeliveryCheck());
        queryParams.put("delivery_application_id", applicationList.get(0).getId());
        queryParams.put("invoice_id", applicationList.get(0).getInvoiceId());

        // Получаем информацию о заявке на доставку (ожидаемый результат)
        DeliveryApplication actual = getDeliveryApplication("correct_token", queryParams, 200, DeliveryApplication.class);

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, applicationList.get(0), 
                "address_check_id", 
                "invoice_id", 
                "delivery_application_id",
                "point.city",
                "point.id",
                "point.city_fias_id_is_valid",
                "point.house",
                "point.multiplace",
                "point.no_weight_limits",
                "point.street",
                "point.streetType",
                "point.transport_company",
                "point.transport_company_is_active");
    }

    @Test
    @DisplayName("Просмотр заявки на доставку с пустым address_check_id")
    public void getDeliveryApplicationWithEmptyDeliveryCheckId() {
        // Формируем query параметры
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("address_check_id", "");

        // Получаем информацию о заявке на доставку (ожидаемый результат)
        ValidationError actual = getDeliveryApplication("correct_token", queryParams, 422, ValidationError.class);

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, new ValidationError(List.of(
                new Detail("type_error.integer"),
                new Detail("value_error")
        )));
    }

    @Test
    @DisplayName("Просмотр заявки на доставку с пустым delivery_application_id")
    public void getDeliveryApplicationWithEmptyDeliveryApplicationId() {
        // Формируем query параметры
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("delivery_application_id", "");

        // Получаем информацию о заявке на доставку (ожидаемый результат)
        ValidationError actual = getDeliveryApplication("correct_token", queryParams, 422, ValidationError.class);

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, new ValidationError(List.of(
                new Detail("type_error.integer"),
                new Detail("value_error")
        )));
    }

    @Test
    @DisplayName("Просмотр заявки на доставку с пустым invoice_id")
    public void getDeliveryApplicationWithEmptyInvoiceId() {
        // Формируем query параметры
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("invoice_id", "");

        // Получаем информацию о заявке на доставку (ожидаемый результат)
        ValidationError actual = getDeliveryApplication("correct_token", queryParams, 422, ValidationError.class);

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, new ValidationError(List.of(
                new Detail("type_error.integer"),
                new Detail("value_error")
        )));
    }

    @Test
    @DisplayName("Просмотр заявки на доставку с не существующим address_check_id")
    public void getDeliveryApplicationWithUnexistingDeliveryCheckId() {
        // Формируем query параметры
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("address_check_id", "99999");

        // Получаем информацию о заявке на доставку (ожидаемый результат)
        BaseResponseError actual = getDeliveryApplication("correct_token", queryParams, 403, BaseResponseError.class);

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getUnexistingApplicationError());
    }

    @Test
    @DisplayName("Просмотр заявки на доставку с не существующим delivery_application_id")
    public void getDeliveryApplicationWithUnexistingDeliveryApplicationId() {
        // Формируем query параметры
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("delivery_application_id", "99999");

        // Получаем информацию о заявке на доставку (ожидаемый результат)
        BaseResponseError actual = getDeliveryApplication("correct_token", queryParams, 403, BaseResponseError.class);

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getUnexistingApplicationError());
    }

    @Test
    @DisplayName("Просмотр заявки на доставку с не существующим invoice_id")
    public void getDeliveryApplicationWithUnexistingInvoiceId() {
        // Формируем query параметры
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("invoice_id", "99999");

        // Получаем информацию о заявке на доставку (ожидаемый результат)
        BaseResponseError actual = getDeliveryApplication("correct_token", queryParams, 403, BaseResponseError.class);

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getUnexistingApplicationError());
    }

    @Test
    @DisplayName("Просмотр заявки на доставку с не валидным address_check_id")
    public void getDeliveryApplicationWithInvalidDeliveryCheckId() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("system", "B2B");

        // Получаем список заявок на доставку
        List<DeliveryApplication> applicationList = getDeliveryApplicationsFromDb("B2B", getUid(), getCorrId(), getCorrId());
        
        // Формируем query параметры
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("delivery_application_id", applicationList.get(0).getId());
        queryParams.put("invoice_id", applicationList.get(0).getInvoiceId());
        queryParams.put("address_check_id", "qwe");

        // Получаем информацию о заявке на доставку (ожидаемый результат)
        ValidationError actual = getDeliveryApplication("correct_token", queryParams, 422, ValidationError.class);

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("type_error.integer"));
    }

    @Test
    @DisplayName("Просмотр заявки на доставку с не валидным delivery_application_id")
    public void getDeliveryApplicationWithInvalidDeliveryApplicationId() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("system", "B2B");

        // Получаем список заявок на доставку
        List<DeliveryApplication> applicationList = getDeliveryApplicationsFromDb("B2B", getUid(), getCorrId(), getCorrId());
        
        // Формируем query параметры
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("address_check_id", applicationList.get(0).getDeliveryCheck());
        queryParams.put("invoice_id", applicationList.get(0).getInvoiceId());
        queryParams.put("delivery_application_id", "йцу");

        // Получаем информацию о заявке на доставку (ожидаемый результат)
        ValidationError actual = getDeliveryApplication("correct_token", queryParams, 422, ValidationError.class);

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("value_error.number.not_gt", "type_error.integer"));
    }

    @Test
    @DisplayName("Просмотр заявки на доставку с не валидным invoice_id")
    public void getDeliveryApplicationWithInvalidInvoiceId() {
        // Формируем query параметры
        HashMap<String, Object> queryParamsList = new HashMap<>();
        queryParamsList.put("system", "B2B");

        // Получаем список заявок на доставку
        List<DeliveryApplication> applicationList = getDeliveryApplicationsFromDb("B2B", getUid(), getCorrId(), getCorrId());
        
        // Формируем query параметры
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("address_check_id", applicationList.get(0).getDeliveryCheck());
        queryParams.put("delivery_application_id", applicationList.get(0).getId());
        queryParams.put("invoice_id", "йцу");

        // Получаем информацию о заявке на доставку (ожидаемый результат)
        ValidationError actual = getDeliveryApplication("correct_token", queryParams, 422, ValidationError.class);

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("value_error.number.not_gt", "type_error.integer"));
    }

    @Test
    @DisplayName("Просмотр заявки на доставку без параметров")
    public void getDeliveryApplicationWithoutParams() {
        // Формируем query параметры
        HashMap<String, Object> queryParams = new HashMap<>();

        // Получаем информацию о заявке на доставку (ожидаемый результат)
        ValidationError actual = getDeliveryApplication("correct_token", queryParams, 422, ValidationError.class);

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, new ValidationError(List.of(
                new Detail("value_error")
        )));
    }
 }