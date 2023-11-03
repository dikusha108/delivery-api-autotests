package model.delivery;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PostDeliveryCheckDayResponse {
    private String day;
    private Boolean day_is_workday;
    private String next_day;

    public PostDeliveryCheckDayResponse(String day, Boolean day_is_workday) {
        this.day = day;
        this.day_is_workday = day_is_workday;

        LocalDate date = LocalDate.parse(day, DateTimeFormatter.ISO_LOCAL_DATE);
        this.next_day = date.plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
