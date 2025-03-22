package stepsclass;

import com.google.gson.Gson;
import dataclass.LoginCourier;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class LoginCourierSteps {

    private final static String BASE_URL = "/api/v1/courier/login";

    @Step("Вход курьера с логином и паролем")
    public static ValidatableResponse loginCourier(String login, String password) {

        LoginCourier loginRequest = new LoginCourier(login, password);
        Gson gson = new Gson();

        return given()
                .header("Content-type", "application/json")
                .body(gson.toJson(loginRequest))
                .when()
                .post(BASE_URL)
                .then();
    }

    @Step("Попытка входа курьера без одного из полей с данными '{jsonBody}'")
    public static Response loginWithoutField(String jsonBody) {
        return given()
                .header("Content-type", "application/json")
                .body(jsonBody)
                .when()
                .post(BASE_URL)
                .then()
                .extract().response();
    }

}
