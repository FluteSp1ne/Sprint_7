import io.qameta.allure.Step;
import org.junit.After;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreatingCourierTest extends BaseTest {

    @Test
    @Step("Создание курьера с логином 'writer'")
    public void testCreateCourier() {
        CourierSteps.createCourier("writer", "4321", "alan");
    }

    @Test
    @Step("Создание курьера с дублирующимся логином")
    public void testDuplicateCourierLogin() {
        CourierSteps.createCourier("writer", "4321", "alan");

        given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"writer\", \"password\": \"4321\", \"firstname\": \"alan\"}")
                .when()
                .post(CourierSteps.BASE_URL) // использование BASE_URL из CourierApi
                .then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @Step("Создание курьера с пропущенным полем {login}")
    public void testMissingFields() {
        CourierSteps.checkMissingFields("{\"password\": \"4321\", \"firstName\": \"alan\"}");
    }

    @Test
    @Step("Создание курьера с пропущенным полем {password}")
    public void testMissingFieldPassword() {
        CourierSteps.checkMissingFields("{\"login\": \"writer\", \"firstName\": \"alan\"}");
    }

    @After
    @Step("Удаление курьера")
    public void tearDown() {
        int courierId = CourierSteps.loginAndGetCourierId("writer", "4321");
        CourierSteps.deleteCourier(courierId);
    }
}


