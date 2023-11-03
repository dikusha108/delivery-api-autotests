package delivery;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import model.delivery.DeliveryApplication;
import model.delivery.DeliveryApplicationCreate;
import model.delivery.DeliveryPoint;
import model.delivery.PostDeliveryApplicationResponse;
import model.errors.BaseResponseError;
import model.errors.ValidationError;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import static data.Errors.getValidationError;
import static data.RequestBody.getDeliveryApplicationBody;
import static data.TestParams.*;
import static steps.BaseSteps.*;
import static steps.DeliverySteps.*;

@Epic("/delivery")
@Feature("/application")
@Execution(ExecutionMode.SAME_THREAD)
public class PostDeliveryApplicationTest {
    @Test
    @DisplayName("Создание заявки на доставку с валидными параметрами")
    @Disabled("Тест отключен на проде")
    public void postDeliveryApplicationWithValidParams() throws InterruptedException, ParseException {
        // Создаем заявку на отправку (фактический результат)
        PostDeliveryApplicationResponse actual = postDeliveryApplication(
                "correct_token",
                getDeliveryApplicationBody(null, getPointId()),
                200,
                PostDeliveryApplicationResponse.class
        );

        // Т.к. создание заявки отложенное, нужно некоторое время для создания заявки на доставку
        Thread.sleep(10000);

        // Получаем информацию о заявке на доставку (фактический результат)
        DeliveryApplication applicationInfo = getDeliveryApplicationFromDb("da1.id = " + actual.getDeliveryApplicationId());

        // Формируем ождаемый результат
        // Получаем информацию о ПВЗ
        DeliveryPoint point = getDeliveryPointFromDb(getPointId());

        // Получаем информацию о счете и накладной из FireBird
        HashMap<String, String> invoice_values = getHashMapFromDB(
                List.of("bill_date", "bill_number", "invoice_date", "invoice_number", "cargo_places"),
                "SELECT dd.DD_DATE as bill_date, dd.DD_NO as  bill_number, da.DA_DATE as invoice_date, " +
                        "DA_NO as invoice_number, loading_place_qty as cargo_places " +
                        "FROM DOCACC da JOIN DOCDELO dd ON dd.DD_ID = da.DD_ID " +
                        "JOIN l3_store_docacc_transport l3 ON l3.DA_ID = da.DA_ID " +
                        "WHERE da.DA_ID = " + getPackedInvoiceId(),
                String.class
        );

        // Считаем количество грузовых мест как loading_place_qty + 1 (документы)
        int cargo_places = (invoice_values.get("cargo_places") == null) ? 1 : Integer.parseInt(invoice_values.get("cargo_places"));

        // Формируем ожидаемый результат заявки на доставку
        DeliveryApplication expectedApplicationInfo = new DeliveryApplication(
                changeDateFormat(invoice_values.get("bill_date"), "yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd"),
            invoice_values.get("bill_number"),
            point.getAddress(),
            "",
                changeDateFormat(invoice_values.get("invoice_date"), "yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd"),
            invoice_values.get("invoice_number"),
            getPecom(),
                applicationInfo.getTrackNumber(),
                applicationInfo.getTrackUrl(),
                getUid().toString(),
                cargo_places,
                0,
                getPackedInvoiceId(),
                actual.getDeliveryApplicationId()
        );
        expectedApplicationInfo.setPoint(point);

        // Сверяем ожидаемый результат с фактическим
        checkBody(applicationInfo, expectedApplicationInfo, "delivery_date_plan");
    }

    @Test
    @DisplayName("Создание заявки на доставку не существующим delivery_check_id")
    public void postDeliveryApplicationWithUnexistingDeliveryCheckId() {
        // Создаем заявку на отправку (фактический результат)
        BaseResponseError actual = postDeliveryApplication(
                "correct_token",
                getDeliveryApplicationBody(99999, null),
                403,
                BaseResponseError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, new BaseResponseError("Доставка по адресу клиента недоступна на данный момент!"));
    }

    @Test
    @DisplayName("Создание заявки на доставку не существующим point_id")
    public void postDeliveryApplicationWithUnexistingPointId() {
        // Создаем заявку на отправку (фактический результат)
        BaseResponseError actual = postDeliveryApplication(
                "correct_token",
                getDeliveryApplicationBody(null, 99999),
                403,
                BaseResponseError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, new BaseResponseError("Некорректный ID ПВЗ в системе ApiShip!"));
    }

    @Test
    @DisplayName("Создание заявки на доставку не валидным delivery_check_id")
    public void postDeliveryApplicationWithInvalidDeliveryCheckId() {
        // Создаем заявку на отправку (фактический результат)
        ValidationError actual = postDeliveryApplication(
                "correct_token",
                getDeliveryApplicationBody("qwe", null),
                422,
                ValidationError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("type_error.integer", "value_error"));
    }

    @Test
    @DisplayName("Создание заявки на доставку не валидным point_id")
    public void postDeliveryApplicationWithInvalidDeliveryPointId() {
        // Создаем заявку на отправку (фактический результат)
        ValidationError actual = postDeliveryApplication(
                "correct_token",
                getDeliveryApplicationBody(null, "qwe"),
                422,
                ValidationError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("type_error.integer", "value_error"));
    }

    @Test
    @DisplayName("Создание заявки на доставку с не валидным invoice_id")
    public void postDeliveryApplicationWithInvalidInvoiceId() {
        // Формируем тело запроса
        DeliveryApplicationCreate body = getDeliveryApplicationBody(getDeliveryCheckId(), null);
        body.setInvoiceId(getPackedInvoiceId() + "qwe");

        // Создаем заявку на отправку (фактический результат)
        ValidationError actual = postDeliveryApplication(
                "correct_token",
                body,
                422,
                ValidationError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("type_error.integer"));
    }

    @Test
    @DisplayName("Создание заявки на доставку с не валидным system")
    public void postDeliveryApplicationWithInvalidSystem() {
        // Формируем тело запроса
        DeliveryApplicationCreate body = getDeliveryApplicationBody(getDeliveryCheckId(), null);
        body.setSystem("qwe");

        // Создаем заявку на отправку (фактический результат)
        ValidationError actual = postDeliveryApplication(
                "correct_token",
                body,
                422,
                ValidationError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("type_error.enum"));
    }

    @Test
    @DisplayName("Создание заявки на доставку с не валидным user_id")
    public void postDeliveryApplicationWithInvalidUserId() {
        // Формируем тело запроса
        DeliveryApplicationCreate body = getDeliveryApplicationBody(getDeliveryCheckId(), null);
        body.setUserId(getUid() + "qwe");

        // Создаем заявку на отправку (фактический результат)
        ValidationError actual = postDeliveryApplication(
                "correct_token",
                body,
                422,
                ValidationError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("type_error.integer"));
    }

    @Test
    @DisplayName("Создание заявки на доставку с не валидным user_email")
    public void postDeliveryApplicationWithInvalidUserEmail() {
        // Формируем тело запроса
        DeliveryApplicationCreate body = getDeliveryApplicationBody(getDeliveryCheckId(), null);
        body.setUserEmail(getRandomDigitalString(5) + "mail.ru");

        // Создаем заявку на отправку (фактический результат)
        ValidationError actual = postDeliveryApplication(
                "correct_token",
                body,
                422,
                ValidationError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("value_error.emailsyntax"));
    }

    @Test
    @DisplayName("Создание заявки на доставку с не валидным corr_id")
    public void postDeliveryApplicationWithInvalidCorrId() {
        // Формируем тело запроса
        DeliveryApplicationCreate body = getDeliveryApplicationBody(getDeliveryCheckId(), null);
        body.setCorrId(getCorrId() + "qwe");

        // Создаем заявку на отправку (фактический результат)
        ValidationError actual = postDeliveryApplication(
                "correct_token",
                body,
                422,
                ValidationError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("type_error.integer"));
    }

    @Test
    @DisplayName("Создание заявки на доставку с не валидным corr_id_holding")
    public void postDeliveryApplicationWithInvalidCorrIdHolding() {
        // Формируем тело запроса
        DeliveryApplicationCreate body = getDeliveryApplicationBody(getDeliveryCheckId(), null);
        body.setCorrIdHolding(getCorrId() + "qwe");

        // Создаем заявку на отправку (фактический результат)
        ValidationError actual = postDeliveryApplication(
                "correct_token",
                body,
                422,
                ValidationError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("type_error.integer"));
    }

    @Test
    @DisplayName("Создание заявки на доставку с не валидным contact_person_id")
    public void postDeliveryApplicationWithInvalidContactPersonId() {
        // Формируем тело запроса
        DeliveryApplicationCreate body = getDeliveryApplicationBody(getDeliveryCheckId(), null);
        body.setContactPersonId(getContactPerson() + "qwe");

        // Создаем заявку на отправку (фактический результат)
        ValidationError actual = postDeliveryApplication(
                "correct_token",
                body,
                422,
                ValidationError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("type_error.integer"));
    }

    @Test
    @DisplayName("Создание заявки на доставку с не валидным delivery_address_id")
    public void postDeliveryApplicationWithInvalidDeliveryAddressId() {
        // Формируем тело запроса
        DeliveryApplicationCreate body = getDeliveryApplicationBody(getDeliveryCheckId(), null);
        body.setDeliveryAddressId(getDeliveryAddress() + "qwe");

        // Создаем заявку на отправку (фактический результат)
        ValidationError actual = postDeliveryApplication(
                "correct_token",
                body,
                422,
                ValidationError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("type_error.integer"));
    }

    @Test
    @DisplayName("Создание заявки на доставку с user_email 255+ символов")
    public void postDeliveryApplicationWithUserEmailMore255Symbols() {
        // Формируем тело запроса
        DeliveryApplicationCreate body = getDeliveryApplicationBody(getDeliveryCheckId(), null);
        body.setUserEmail("qryiv7qho6ib0bxlu7ieuk6h3llstiofvea7x3ngdn5ym7yntkbsi9u46o92n8xs5a2nvaz3xzvc8xha02uni32dl" +
                "06sr6fz9ensp7jglxv94lfmskms19uihzpcuhny8qj6tmdwf3pd7cxv0rbx8mpg9lixjok2imu9igyd1aah8mw6gvk" +
                "cai4utf754pznz1vk20ij20lbysw6isrbkwepi7jz1ays7bi64a4wnd5tzlufotn@amil.com");

        // Создаем заявку на отправку (фактический результат)
        ValidationError actual = postDeliveryApplication(
                "correct_token",
                body,
                422,
                ValidationError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("value_error.emailsyntax"));
    }

    @Test
    @DisplayName("Создание заявки на доставку с receiver_fio длиной 50+ символов")
    public void postDeliveryApplicationWithReceiverFioMore50Symbols() {
        // Формируем тело запроса
        DeliveryApplicationCreate body = getDeliveryApplicationBody(getDeliveryCheckId(), null);
        body.setReceiverFio("Lorem ipsum dolor sit amet, consectetuer adipiscins");

        // Создаем заявку на отправку (фактический результат)
        ValidationError actual = postDeliveryApplication(
                "correct_token",
                body,
                422,
                ValidationError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("value_error.any_str.max_length"));
    }

    @Test
    @DisplayName("Создание заявки на доставку с receiver_organization_name длиной 60+ символов")
    public void postDeliveryApplicationWithReceiverOrganisationNameMore50Symbols() {
        // Формируем тело запроса
        DeliveryApplicationCreate body = getDeliveryApplicationBody(getDeliveryCheckId(), null);
        body.setReceiverOrganizationName("k30YGCROnAccLeFbAKoK4h8cPCvcCE5WtNM44BZbT4Yl92dmmEgJk4FfE1Vox");

        // Создаем заявку на отправку (фактический результат)
        ValidationError actual = postDeliveryApplication(
                "correct_token",
                body,
                422,
                ValidationError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("value_error.any_str.max_length"));
    }

    @Test
    @DisplayName("Создание заявки на доставку с receiver_organization_kpp длиной 32+ символов")
    public void postDeliveryApplicationWithReceiverOrganisationKPPMore32Symbols() {
        // Формируем тело запроса
        DeliveryApplicationCreate body = getDeliveryApplicationBody(getDeliveryCheckId(), null);
        body.setReceiverOrganizationKpp("weitrhulpvgwbjrhabhbffgegtttupugca");

        // Создаем заявку на отправку (фактический результат)
        ValidationError actual = postDeliveryApplication(
                "correct_token",
                body,
                422,
                ValidationError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("value_error.any_str.max_length"));
    }

    @Test
    @DisplayName("Создание заявки на доставку с receiver_organization_inn длиной 32+ символов")
    public void postDeliveryApplicationWithReceiverOrganisationINNMore32Symbols() {
        // Формируем тело запроса
        DeliveryApplicationCreate body = getDeliveryApplicationBody(getDeliveryCheckId(), null);
        body.setReceiverOrganizationInn("weitrhulpvgwbjrhabhbffgegtttupugca");

        // Создаем заявку на отправку (фактический результат)
        ValidationError actual = postDeliveryApplication(
                "correct_token",
                body,
                422,
                ValidationError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("value_error.any_str.max_length"));
    }

    @Test
    @DisplayName("Создание заявки на доставку с не валидным receiver_phone")
    public void postDeliveryApplicationWithInvalidReceiverPhone() {
        // Формируем тело запроса
        DeliveryApplicationCreate body = getDeliveryApplicationBody(getDeliveryCheckId(), null);
        body.setReceiverPhone(getRandomDigitalString(26)+ "qwe");

        // Создаем заявку на отправку (фактический результат)
        ValidationError actual = postDeliveryApplication(
                "correct_token",
                body,
                422,
                ValidationError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("value_error.any_str.max_length"));
    }

    @Test
    @DisplayName("Создание заявки на доставку с receiver_phone длиной 11+ символов")
    public void postDeliveryApplicationWithReceiverPhoneMore26Symbols() {
        // Формируем тело запроса
        DeliveryApplicationCreate body = getDeliveryApplicationBody(getDeliveryCheckId(), null);
        body.setReceiverPhone("476578987657890079851367432");

        // Создаем заявку на отправку (фактический результат)
        ValidationError actual = postDeliveryApplication(
                "correct_token",
                body,
                422,
                ValidationError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("value_error.any_str.max_length"));
    }

    @Test
    @DisplayName("Создание заявки на доставку с address_comment длиной 300+ символов")
    public void postDeliveryApplicationWithAddressCommentMore300Symbols() {
        // Формируем тело запроса
        DeliveryApplicationCreate body = getDeliveryApplicationBody(getDeliveryCheckId(), null);
        body.setAddressComment("The quick, brown fox jumps over a lazy dog. DJs flock by when MTV ax quiz prog. Junk " +
                "MTV quiz graced by fox whelps. Bawds jog, flick quartz, vex nymphs. Waltz, bad nymph, for " +
                "quick jigs vex! Fox nymphs grab quick-jived waltz. Brick quiz whangs jumpy veldt fox. " +
                "Bright vixens jump; dozy fowl quack. Quiw");

        // Создаем заявку на отправку (фактический результат)
        ValidationError actual = postDeliveryApplication(
                "correct_token",
                body,
                422,
                ValidationError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("value_error.any_str.max_length"));
    }

    @Test
    @DisplayName("Создание заявки на доставку с cargo_comment длиной 250+ символов")
    public void postDeliveryApplicationWithCargoCommentMore250Symbols() {
        // Формируем тело запроса
        DeliveryApplicationCreate body = getDeliveryApplicationBody(getDeliveryCheckId(), null);
        body.setCargoComment("The quick, brown fox jumps over a lazy dog. DJs flock by when MTV ax quiz prog. Junk " +
                "MTV quiz graced by fox whelps. Bawds jog, flick quartz, vex nymphs. Waltz, bad nymph, for " +
                "quick jigs vex! Fox nymphs grab quick-jived waltz. Brick quiz whangs jumpy vqw");

        // Создаем заявку на отправку (фактический результат)
        ValidationError actual = postDeliveryApplication(
                "correct_token",
                body,
                422,
                ValidationError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("value_error.any_str.max_length"));
    }

    @Test
    @DisplayName("Создание заявки на доставку с не валидным delivery_date_desired")
    public void postDeliveryApplicationWithInvalidDeliveryDateDwsired() {
        // Формируем тело запроса
        DeliveryApplicationCreate body = getDeliveryApplicationBody(getDeliveryCheckId(), null);
        body.setDeliveryDateDesired("qwe");

        // Создаем заявку на отправку (фактический результат)
        ValidationError actual = postDeliveryApplication(
                "correct_token",
                body,
                422,
                ValidationError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("value_error.date"));
    }

    @Test
    @DisplayName("Создание заявки на доставку с не валидным delivery_time_from")
    public void postDeliveryApplicationWithInvalidDeliveryTimeFrom() {
        // Формируем тело запроса
        DeliveryApplicationCreate body = getDeliveryApplicationBody(getDeliveryCheckId(), null);
        body.setDeliveryTimeFrom("9qwe");

        // Создаем заявку на отправку (фактический результат)
        ValidationError actual = postDeliveryApplication(
                "correct_token",
                body,
                422,
                ValidationError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("value_error"));
    }

    @Test
    @DisplayName("Создание заявки на доставку с не валидным delivery_time_to")
    public void postDeliveryApplicationWithInvalidDeliveryTimeTo() {
        // Формируем тело запроса
        DeliveryApplicationCreate body = getDeliveryApplicationBody(getDeliveryCheckId(), null);
        body.setDeliveryTimeTo("16qwe");

        // Создаем заявку на отправку (фактический результат)
        ValidationError actual = postDeliveryApplication(
                "correct_token",
                body,
                422,
                ValidationError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, getValidationError("value_error"));
    }

    @Test
    @DisplayName("Создание заявки на доставку с delivery_time_to меньше delivery_time_from")
    public void postDeliveryApplicationWithDeliveryTimeToLessDeliveryTimeFrom() {
        // Формируем тело запроса
        DeliveryApplicationCreate body = getDeliveryApplicationBody(null, getPointId());
        body.setDeliveryTimeFrom("14:00");
        body.setDeliveryTimeTo("09:00");

        // Создаем заявку на отправку (фактический результат)
        BaseResponseError actual = postDeliveryApplication(
                "correct_token",
                body,
                403,
                BaseResponseError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, new BaseResponseError("Некорректный диапазон времени доставки!"));
    }

    @Test
    @Disabled
    @DisplayName("Создание заявки на доставку с повторным создание заявки на одну накладную")
    public void postDeliveryApplicationWithSameInvoiceTwice() throws InterruptedException {
        Integer invoice_id = getPackedInvoiceId();
        // Формируем тело запроса
        DeliveryApplicationCreate body = new DeliveryApplicationCreate(
                getDeliveryCheckId(),
                null,
                invoice_id,
                "B2B",
                getUid(),
                getRandomDigitalString(5) + "@mail.ru",
                getCorrId(),
                getCorrId(),
                getContactPerson(),
                getDeliveryAddress(),
                "Фамилия Имя Отчество",
                getCorrespondentName(),
                getCorrespondentInn(),
                getCorrespondentKpp(),
                getRandomDigitalString(26),
                getRandomDigitalString(10),
                getRandomDigitalString(10),
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                "09:00",
                "12:00"
        );

        // Создаем заявку на отправку
        PostDeliveryApplicationResponse firstApplication = postDeliveryApplication(
                "correct_token",
                body,
                200,
                PostDeliveryApplicationResponse.class
        );

        // Необходимо некоторое время для создания заявки на доставку
        Thread.sleep(10000);

        // Создаем заявку на отправку (фактический результат)
        BaseResponseError actual = postDeliveryApplication(
                "correct_token",
                body,
                403,
                BaseResponseError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, new BaseResponseError("По данной накладной уже есть отправление Заявка на доставку (ID: "
                + firstApplication.getDeliveryApplicationId() + ")"));
    }

    @Test
    @DisplayName("Создание заявки на доставку c point_id, недоступным для доставки")
    @Disabled("Такой проверки в методе нет, т.к. это не дат возможность тестировать доставку на стейдже")
    public void postDeliveryApplicationWithUnavailablePointId() {
        Integer point_id = getDeliveryPointsFromDb(getCdek(), false, null, null).get(0).getId();
        // Создаем заявку на отправку (фактический результат)
        BaseResponseError actual = postDeliveryApplication(
                "correct_token",
                getDeliveryApplicationBody(null, point_id),
                403,
                BaseResponseError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, new BaseResponseError("Некорректный ПВЗ в системе ApiShip!"));
    }

    @Test
    @DisplayName("Создание заявки на доставку c point_id из неактивного субъекта федерации")
    @Disabled("Такой проверки в методе нет, т.к. это не дат возможность тестировать доставку на стейдже")
    public void postDeliveryApplicationWithPointIdFromInactiveSubject() {
        Integer point_id = getValueFromPSQL("select apiship_id " +
                "from delivery2_deliverypoint dd " +
                "where dd.transport_company_id = 2 and city = \'" + getInactiveCity() + "\'",
                "apiship_id",
                Integer.class);

        // Создаем заявку на отправку (фактический результат)
        BaseResponseError actual = postDeliveryApplication(
                "correct_token",
                getDeliveryApplicationBody(null, point_id),
                403,
                BaseResponseError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, new BaseResponseError("Некорректный ПВЗ в системе ApiShip!"));
    }

    @Test
    @DisplayName("Создание заявки на доставку c point_id из неактивного феерального округа")
    @Disabled("Такой проверки в методе нет, т.к. это не дат возможность тестировать доставку на стейдже")
    public void postDeliveryApplicationWithPointIdFromInactiveDistrict() {
        Integer point_id = getValueFromPSQL("select apiship_id " +
                        "from delivery2_deliverypoint dd " +
                        "where dd.transport_company_id = 2 and federal_district_id = " + getInactiveDistrictId(),
                "apiship_id",
                Integer.class);

        // Создаем заявку на отправку (фактический результат)
        BaseResponseError actual = postDeliveryApplication(
                "correct_token",
                getDeliveryApplicationBody(null, point_id),
                403,
                BaseResponseError.class
        );

        // Сверяем ожидаемый результат с фактическим
        checkBody(actual, new BaseResponseError("Некорректный ПВЗ в системе ApiShip!"));
    }
}