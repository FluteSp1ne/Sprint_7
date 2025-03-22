package TestsClass;

import StepsClass.BaseTest;
import StepsClass.CourierSteps;
import StepsClass.LoginCourierSteps;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginCourierTest extends BaseTest {

    @Before
    @Step("Создание курьера")
    public void setup() {
        CourierSteps.createCourier("writer", "4321", "alan");
    }

    @Test
    @Step("Вход курьера с существующим логином-паролем")
    public void testLoginCourier() {
        LoginCourierSteps.loginCourier("writer", "4321")
                .statusCode(200)
                .body("$", hasKey("id"));
    }

    @Test
    @Step("Вход курьера без поля {password}")
    public void testLoginCourierWithoutPassword() {
        Response response = LoginCourierSteps.loginWithoutField("{\"login\": \"writer\"}");
        assertEquals(400, response.getStatusCode());
        String message = response.jsonPath().getString("message");
        assertEquals("Недостаточно данных для входа", message);
    }

    @Test
    @Step("Вход курьера без поля {login}")
    public void testLoginCourierWithoutLogin() {
        Response response = LoginCourierSteps.loginWithoutField("{\"password\": \"4321\"}");
        assertEquals(400, response.getStatusCode());
        String message = response.jsonPath().getString("message");
        assertEquals("Недостаточно данных для входа", message);
    }


    @Test
    @Step("Вход курьера с пустым полем {password}")
    public void testLoginCourierWithoutMeaningPassword() {
        LoginCourierSteps.loginCourier("writer", "")
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Step("Вход курьера с пустым полем {login}")
    public void testLoginCourierWithoutMeaningLogin() {
        LoginCourierSteps.loginCourier("", "4321")
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Step("Вход курьера с неправильным паролем")
    public void testLoginCourierWithIncorrectPassword() {
        LoginCourierSteps.loginCourier("writer", "4322")
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Step("Вход курьера с неправильным логином")
    public void testLoginCourierWithIncorrectLogin() {
        LoginCourierSteps.loginCourier("write", "4321")
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    @Step("Удаление курьера")
    public void tearDown() {
            int courierId = CourierSteps.loginAndGetCourierId("writer", "4321");

            if (courierId > 0) {
                Response response = CourierSteps.deleteCourier(courierId);
                assertEquals(200, response.getStatusCode());
                boolean ok = response.jsonPath().getBoolean("ok");
                assertTrue(ok);
            } else {
                Response response = CourierSteps.deleteCourier(courierId);
                assertEquals(404, response.getStatusCode());
            }

    }
}

