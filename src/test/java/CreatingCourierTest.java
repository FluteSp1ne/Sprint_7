import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

public class CreatingCourierTest {

    private final static String BASE_URL = "/api/v1/courier";

    @Before
    @Step("Настройка тестового окружения")
    public void setup() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @Step("Создание курьера с логином 'writer'")
    public void testCreateCourier() {
        createCourier("writer", "4321", "alan");
    }

    @Test
    @Step("Создание курьера с дублирующимся логином")
    public void testDuplicateCourierLogin() {
        createCourier("writer", "4321", "alan");

        given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"writer\", \"password\": \"4321\", \"firstname\": \"alan\"}")
                .when()
                .post(BASE_URL)
                .then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @Step("Создание курьера с пропущенными полями")
    public void testMissingFields() {
        checkMissingFields("{\"password\": \"4321\", \"firstName\": \"alan\"}");
        checkMissingFields("{\"login\": \"writer\", \"firstName\": \"alan\"}");
        createCourier("writer", "4321", "alan");
    }

    @After
    @Step("Удаление курьера")
    public void tearDown() {
        int courierId = loginAndGetCourierId("writer", "4321");
        deleteCourier(courierId);
    }

    @Step("Создание курьера с логином '{login}', паролем '{password}' и именем '{firstName}'")
    private void createCourier(String login, String password, String firstName) {
        given()
                .header("Content-type", "application/json")
                .body(String.format("{\"login\": \"%s\", \"password\": \"%s\", \"firstname\": \"%s\"}", login, password, firstName))
                .when()
                .post(BASE_URL)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Step("Проверка недостаточных полей с данными '{jsonBody}'")
    private void checkMissingFields(String jsonBody) {
        given()
                .header("Content-type", "application/json")
                .body(jsonBody)
                .when()
                .post(BASE_URL)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Step("Вход курьера с логином '{login}' и паролем '{password}' и получение его ID")
    private int loginAndGetCourierId(String login, String password) {
        return given()
                .header("Content-type", "application/json")
                .body(String.format("{\"login\": \"%s\", \"password\": \"%s\"}", login, password))
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }

    @Step("Удаление курьера с ID '{courierId}'")
    private void deleteCourier(int courierId) {
        given()
                .header("Content-type", "application/json")
                .body("{\"id\": \"" + courierId + "\"}")
                .when()
                .delete("/api/v1/courier/" + courierId)
                .then()
                .statusCode(200)
                .body("ok", equalTo(true));
    }
}

