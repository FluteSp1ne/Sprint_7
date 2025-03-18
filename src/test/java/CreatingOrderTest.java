import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

public class CreatingOrderTest {
    private final static String BASE_URL = "/api/v1/orders";
    private static final String FIRST_NAME = "Alan";
    private static final String LAST_NAME = "Wake";
    private static final String ADDRESS = "OceanView, 0";
    private static final String METRO_STATION = "Kaldera";
    private static final String PHONE = "+7 903 022 10 01";
    private static final int RENT_TIME = 5;
    private static final String DELIVERY_DATE = "2025-09-09";
    private static final String COMMENT = "Where are you, Alice?";

    private String trackId;

    @Before
    @Step("Настройка тестового окружения")
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @Step("Создание заказа без выбора цвета")
    public void creatingOrderWithoutColor() {
        String orderData = createOrderData(new String[]{""});
        trackId = sendOrderRequest(orderData);
    }

    @Test
    @Step("Создание заказа с выбором одного цвета")
    public void creatingOrderWithOneColor() {
        String orderDataBlack = createOrderData(new String[]{"BLACK"});
        trackId = sendOrderRequest(orderDataBlack);

        String orderDataGrey = createOrderData(new String[]{"GREY"});
        trackId = sendOrderRequest(orderDataGrey);
    }

    @Test
    @Step("Создание заказа с выбором нескольких цветов")
    public void creatingOrderWithMultipleColors() {
        String orderDataGreyAndBlack = createOrderData(new String[]{"GREY", "BLACK"});
        trackId = sendOrderRequest(orderDataGreyAndBlack);
    }

    @After
    @Step("Отмена заказа")
    public void cancelOrder() {
            given()
                    .header("Content-type", "application/json")
                    .body("{\"track\": \"" + trackId + "\"}")
                    .when()
                    .put("/api/v1/orders/cancel")
                    .then()
                    .statusCode(200)
                    .body("ok", equalTo(true));
    }

    @Step("Отправка запроса на создание заказа с данными: {orderData}")
    public String sendOrderRequest(String orderData) {
        return given()
                .header("Content-type", "application/json")
                .body(orderData)
                .when()
                .post(BASE_URL)
                .then()
                .statusCode(201)
                .body("$", hasKey("track"))
                .extract().path("track").toString();
    }

    private String createOrderData(String[] colors) {
        String colorArray = String.join("\",\"", colors);
        return String.format("{\"firstName\": \"%s\", \"lastName\": \"%s\", \"address\": \"%s\", \"metroStation\": \"%s\", " +
                        "\"phone\": \"%s\", \"rentTime\": %d, \"deliveryDate\": \"%s\", \"comment\": \"%s\", \"color\": [\"%s\"]}",
                FIRST_NAME, LAST_NAME, ADDRESS, METRO_STATION, PHONE, RENT_TIME, DELIVERY_DATE, COMMENT, colorArray);
    }
}



