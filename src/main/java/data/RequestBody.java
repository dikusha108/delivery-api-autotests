package data;

import model.delivery.DeliveryApplicationCreate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static data.TestParams.*;

public class RequestBody {
    // Тело запроса для создания заявки на доставку
    public static DeliveryApplicationCreate getDeliveryApplicationBody(Object delivery_check_id, Object point_id) {
        return new DeliveryApplicationCreate(
                delivery_check_id,
                point_id,
                getPackedInvoiceId(),
                "B2B",
                getUid(),
                getRandomDigitalString(5) + "@mail.ru",
                getCorrId(),
                getCorrId(),
                getContactPerson(),
                getDeliveryAddress(),
                getRandomDigitalString(50),
                getCorrespondentName(),
                getCorrespondentInn(),
                getCorrespondentKpp(),
                "+7999" + getRandomDigitalString(7),
                getRandomDigitalString(300),
                getRandomDigitalString(250),
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                "9:00",
                "16:00"
        );
    }

}