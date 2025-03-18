import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasKey;

public class LoginCourierTest {

    private final static String BASE_URL = "/api/v1/courier/login";

    @Before
    @Step("Настройка тестового окружения и создание курьера")
    public void setup() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        createCourier("writer", "4321", "alan");
    }

    @Test
    @Step("Вход курьера с существующим логином-паролем")
    public void testLoginCourier() {
        loginCourier("writer", "4321")
                .statusCode(200)
                .body("$", hasKey("id"));
    }

    @Test
    @Step("Вход курьера без полей пароля/логина")
    public void testLoginCourierWithoutFields() {
        loginWithoutField("{\"login\": \"writer\"}");
        loginWithoutField("{\"password\": \"4321\"}");
    }

    @Test
    @Step("Вход курьера с пустыми полями логина/пароля")
    public void testLoginCourierWithoutMeaning() {
        loginCourierWithEmptyFields("{\"login\": \"writer\", \"password\": \"\"}");
        loginCourierWithEmptyFields("{\"login\": \"\",\"password\": \"4321\"}");
    }

    @Test
    @Step("Вход курьера с неправильным паролем/логином")
    public void testLoginCourierWithIncorrectLoginPassword() {
        loginCourier("writer", "4322")
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));

        loginCourier("write", "4321")
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
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
                .post("/api/v1/courier")
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Step("Вход курьера с логином и паролем")
    private ValidatableResponse loginCourier(String login, String password) {
        return given()
                .header("Content-type", "application/json")
                .body(String.format("{\"login\": \"%s\", \"password\": \"%s\"}", login, password))
                .when()
                .post(BASE_URL)
                .then();
    }

    @Step("Попытка входа курьера без одного из полей с данными '{jsonBody}'")
    private void loginWithoutField(String jsonBody) {
        given()
                .header("Content-type", "application/json")
                .body(jsonBody)
                .when()
                .post(BASE_URL)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Step("Вход курьера с пустыми полями с данными '{jsonBody}'")
    private void loginCourierWithEmptyFields(String jsonBody) {
        given()
                .header("Content-type", "application/json")
                .body(jsonBody)
                .when()
                .post(BASE_URL)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Step("Вход курьера с логином '{login}' и паролем '{password}' и получение его ID")
    private int loginAndGetCourierId(String login, String password) {
        return loginCourier(login, password)
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

