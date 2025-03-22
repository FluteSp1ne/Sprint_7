package TestsClass;

import StepsClass.BaseTest;
import StepsClass.ListOrderSteps;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Test;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class ListOrderTest extends BaseTest {

    @Test
    @Step("Получение списка заказов")
    public void testGetListOrder() {
        Response response = ListOrderSteps.sendGetOrderRequest();
        assertEquals(200, response.getStatusCode());
        List<Object> orders = response.jsonPath().getList("orders");
        assertThat(orders, notNullValue());
    }
}

