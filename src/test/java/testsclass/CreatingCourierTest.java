package testsclass;

import dataclass.CreateCourier;
import stepsclass.BaseTest;
import stepsclass.CourierSteps;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreatingCourierTest extends BaseTest {
    private boolean isCourierCreated = false;

    @Test
    @Step("Создание курьера с логином 'writer'")
    public void testCreateCourier() {
        CreateCourier courier = new CreateCourier("writer", "4321", "alan");
        Response response = CourierSteps.createCourier(courier);
        assertEquals(201, response.getStatusCode());
        boolean ok = response.jsonPath().getBoolean("ok");
        assertTrue(ok);
        isCourierCreated = true;
    }

    @Test
    @Step("Создание курьера с дублирующимся логином")
    public void testDuplicateCourierLogin() {
        CreateCourier courier = new CreateCourier("writer", "4321", "alan");
        CourierSteps.createCourier(courier);
        isCourierCreated = true;

        Response response = CourierSteps.createCourier(courier);
        assertEquals(409, response.getStatusCode());
        String message = response.jsonPath().getString("message");
        assertEquals("Этот логин уже используется", message);
    }

    @Test
    @Step("Создание курьера с пропущенным полем {login}")
    public void testMissingFieldLogin() {
        Response response = CourierSteps.checkMissingFields("", "4321", "alan");
        assertEquals(400, response.getStatusCode());
        String message = response.jsonPath().getString("message");
        assertEquals("Недостаточно данных для создания учетной записи", message);
    }

    @Test
    @Step("Создание курьера с пропущенным полем {password}")
    public void testMissingFieldPassword() {
        Response response = CourierSteps.checkMissingFields("writer", "", "alan");
        assertEquals(400, response.getStatusCode());
        String message = response.jsonPath().getString("message");
        assertEquals("Недостаточно данных для создания учетной записи", message);
    }

    @After
    @Step("Удаление курьера")
    public void tearDown() {
        if (isCourierCreated) {
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
}


