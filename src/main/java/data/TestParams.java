package data;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TestParams {
    private static Connection connectToDb() {
        String url = "jdbc:firebirdsql://172.16.8.220:3050/stage?encoding=UTF8";
        String username = "SYSDBA";
        String password = "masterkey";

        try {
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new AssertionError("Connection failed:\n" + e.getMessage());
        }
    }

    private static String getParam(SqlQueryEnum sqe) {
        return getParam(sqe.getParam(), sqe.getQuery());
    }

    private static String getParam(String param, String query) {
        Connection conn = connectToDb();

        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            rs.next();
            return rs.getString(param);
        } catch (Exception e){
            throw new AssertionError(e.getMessage());
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Dotenv dotenv = Dotenv.load();
    private static final String delivery_token = "Bearer " + dotenv.get("DELIVERY_API_TOKEN");
    private static final String delivery_postgres_url = dotenv.get("DELIVERY_POSTGRES_URL");
    private static final String delivery_postgres_username = dotenv.get("DELIVERY_POSTGRES_USERNAME");
    private static final String delivery_postgres_password = dotenv.get("DELIVERY_POSTGRES_PASSWORD");
    private static String firebird_url = dotenv.get("FIREBIRD_URL");
    private static String firebird_username = dotenv.get("FIREBIRD_USERNAME");
    private static String firebird_password = dotenv.get("FIREBIRD_PASSWORD");
    private static final String incorrect_token = "Bearer iKnZYvrhKaNZoZxo8wJx74QBxe0Ytnypsdfsdf";
    private static final List<String> moscowAddresses = List.of(
            "г. Москва, Береговой проезд, 4, к3, стр5",
            "Москва, Дмитровское шоссе, д. 64 Б",
            "ул. Снежная, 28, Москва",
            "Бережковская наб., 20, к1, Москва"
    );
    private static final List<String> outsideMoscowAddresses = List.of(
            "ул. Репина, 47А, Ульяновск, Ульяновская обл.",
            "ул. Валерии Гнаровской, 12, Тюмень",
            "просп. Красноармейский, 111, Барнаул, Алтайский край",
            "Депутатская ул., 17, Братск"
    );
    private static final HashMap<String, String> outsideMoscowAddressesMap = new HashMap<>() {{
        put("ул. Репина, 47А, Ульяновск, Ульяновская обл.", "Приволжский");
        put("ул. Валерии Гнаровской, 12, Тюмень", "Уральский");
        put("просп. Красноармейский, 111, Барнаул, Алтайский край", "Сибирский");
        put("ул. Ким Ю Чена, 44, Хабаровск, Хабаровский край, Россия, 680011", "Дальневосточный");
        put("пр. Михаила Нагибина, 17, Ростов-на-Дону, Ростовская обл., Россия, 344038", "Южный");
        put("ул. Генерала Плиева, 17, Владикавказ, Респ. Северная Осетия-Алания, Россия, 362008", "Северо-Кавказский");
        put("ул. Юрия Гагарина, 1В, Волхов, Ленинградская обл., Россия, 187401", "Северо-Западный");
        put("ул. Батурина, 20, Владимир, Владимирская обл., Россия, 600017", "Центральный");
    }};
    private static final List<String> kaliningradAddresses = List.of(
            "ул. Заводская, 10, Черняховск, Калининградская обл.",
            "ул. Юрия Гагарина, 99, Калининград",
            "ул. Новый вал, 32, Калининград",
            "ул. Житомирская, 22, Калининград"
    );
    private static final List<String> outsideRussiaAddresses = List.of(
            "завулак Буянава 19, Магілёў, Беларусь",
            "вулиця Жовтнева, 112, Чутове, Полтавська область, Украина, 38800",
            "ш. Алаш 20A, Астана",
            "148 Киевская Бишкек"
    );
    private static final List<String> workingDays = List.of(
            "2023-04-10", "2023-04-11", "2023-04-12", "2023-04-13", "2023-04-14"
    );
    private static final List<String> holidays = List.of(
            "2023-01-01", "2023-02-23", "2023-03-08", "2023-05-09", "2023-11-04"
    );
    private static final List<String> weekends = List.of(
            "2023-04-22", "2023-03-11", "2023-04-16", "2023-05-28", "2023-07-09"
    );
    private static final String peccom = "ПЭК ООО";
    private static final String peccom_slug = "pecom";
    private static final String cdek = "СДЭК";
    private static final String cdek_slug = "cdek";
    private static final Integer uid = Integer.parseInt(SqlQueryEnum.uid.getParam());
    private static final Integer corr_id = Integer.parseInt(getParam(SqlQueryEnum.corr_id));
    private static final Integer unexisting_corr_id = 14402123;
    private static final Integer material_id_1 = Integer.parseInt(getParam(SqlQueryEnum.material_id_1));
    private static final Integer packed_invoice_id = Integer.parseInt(
            getParam(
                    SqlQueryEnum.packed_invoice_id.getParam(),
                    String.format(SqlQueryEnum.packed_invoice_id.getQuery(), getCorrId())
            )
    );
    private static final Integer contact_person = Integer.parseInt(
            getParam(SqlQueryEnum.contact_person.getParam(), String.format(SqlQueryEnum.contact_person.getQuery(), getCorrId()))
    );
    private static final Integer delivery_address = Integer.parseInt(
            getParam(
                    SqlQueryEnum.delivery_address.getParam(),
                    String.format(SqlQueryEnum.delivery_address.getQuery(), getCorrId())
            )
    );
    private static final String correspondent_name = getParam(
            SqlQueryEnum.correspondent_name.getParam(), String.format(SqlQueryEnum.correspondent_name.getQuery(), getCorrId())
    );
    private static final String correspondent_inn = getParam(
            SqlQueryEnum.correspondent_inn.getParam(), String.format(SqlQueryEnum.correspondent_inn.getQuery(), getCorrId())
    );
    private static final String correspondent_kpp = getParam(
            SqlQueryEnum.correspondent_kpp.getParam(), String.format(SqlQueryEnum.correspondent_kpp.getQuery(), getCorrId())
    );
    private static final Integer delivery_point_id = 462257;
    private static final String inactive_city = "Горно-Алтайск";
    private static final Integer inactive_district_id = 2;

    public static String getDeliveryToken() { return delivery_token; }
    public static String getDeliveryPostgresUrl() { return delivery_postgres_url; }
    public static String getDeliveryPostgresUsername() { return delivery_postgres_username; }
    public static String getDeliveryPostgresPassword() { return delivery_postgres_password; }
    public static String getFirebirdUrl() { return firebird_url;}

    public static String getFirebirdUsername() {
        return firebird_username;
    }

    public static String getFirebirdPassword() {
        return firebird_password;
    }
    public static String getIncorrectToken() {
        return incorrect_token;
    }
    public static String getInactiveCity() {
        return inactive_city;
    }
    public static Integer getInactiveDistrictId() {
        return inactive_district_id;
    }
    public static List<String> getMoscowAddresses() {
        return moscowAddresses;
    }
    public static List<String> getOutsideMoscowAddresses() {
        return outsideMoscowAddresses;
    }
    public static List<String> getKaliningradAddresses() {
        return kaliningradAddresses;
    }
    public static List<String> getOutsideRussiaAddresses() {
        return outsideRussiaAddresses;
    }
    public static HashMap<String, String> getOutsideMoscowAddressesMap() {
        return outsideMoscowAddressesMap;
    }
    public static List<String> getWorkingDays() {
        return workingDays;
    }
    public static List<String> getHolidays() {
        return holidays;
    }
    public static List<String> getWeekends() {
        return weekends;
    }
    public static Integer getUid() {
        return uid;
    }
    public static Integer getCorrId() {
        return corr_id;
    }
    public static Integer getUnexistingCorrId() {
        return unexisting_corr_id;
    }
    public static Integer getMaterialId1() {
        return material_id_1;
    }
    public static Integer getPackedInvoiceId() {
        return packed_invoice_id;
    }
    public static Integer getContactPerson() {return contact_person;}
    public static Integer getDeliveryAddress() {return delivery_address;}
    public static String getCorrespondentName() {return correspondent_name;}
    public static String getCorrespondentInn() {return correspondent_inn;}
    public static String getCorrespondentKpp() {return correspondent_kpp;}
    public static String getPecom() {return peccom;}
    public static String getPecomSlug() {return peccom_slug;}
    public static String getCdek() {return cdek;}
    public static String getCdekSlug() {return cdek_slug;}
    public static Integer getDeliveryPointId() {return delivery_point_id;}

    public static String getRandomString(List<String> stringList) {
        Random rand = new Random();
        return stringList.get(rand.nextInt(stringList.size()));
    }
    public static String getRandomDigitalString(Integer length) {
        long digital = Math.round(((Math.random() * 0.9) + 0.1) * Math.pow(10, length));
        return Long.toString(digital);
    }
}