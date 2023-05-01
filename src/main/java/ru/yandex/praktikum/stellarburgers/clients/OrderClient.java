package ru.yandex.praktikum.stellarburgers.clients;

import io.qameta.allure.Step;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import ru.yandex.praktikum.stellarburgers.models.Order;

public class OrderClient extends Client {
    private static final String ORDERS_URL = "/api/orders/";

    @Step("Create order by Unauthorized User")
    public Response createOrderByUnauthorizedUser(Order oder) {
        return given()
                .spec(getBaseSpec())
                .when()
                .body(oder)
                .post(ORDERS_URL);
    }

    @Step("Create order by Authorized User")
    public Response createOrderByAuthorizedUser(String accessToken, Order oder) {
        return given()
                .header("Authorization", accessToken)
                .spec(getBaseSpec())
                .when()
                .body(oder)
                .post(ORDERS_URL);
    }

    @Step("Get orders for Authorized User")
    public Response getOrdersForAuthorizedUser(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .spec(getBaseSpec())
                .get(ORDERS_URL);
    }

    @Step("Get orders for Unauthorized User")
    public Response getOrdersForUnauthorizedUser() {
        return given()
                .spec(getBaseSpec())
                .get(ORDERS_URL);
    }
}