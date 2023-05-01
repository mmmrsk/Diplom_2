package ru.yandex.praktikum.stellarburgers;

import io.qameta.allure.Description;
import ru.yandex.praktikum.stellarburgers.clients.IngredientClient;
import ru.yandex.praktikum.stellarburgers.clients.OrderClient;
import ru.yandex.praktikum.stellarburgers.clients.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import ru.yandex.praktikum.stellarburgers.models.Ingredient;
import ru.yandex.praktikum.stellarburgers.models.Order;
import ru.yandex.praktikum.stellarburgers.models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class GetOrderTest {
    public User user;
    public UserClient userClient;
    public OrderClient oderClient;
    public Order oder;
    public Ingredient allIngredient;
    public IngredientClient ingredientClient;
    private String accessToken;
    public List<String> ingredients;
    Response response;

    @Before
    public void setup() {
        ingredientClient = new IngredientClient();
        allIngredient = ingredientClient.getIngredient();
        ingredients = new ArrayList<>();
        ingredients.add(allIngredient.data.get(0).get_id());
        ingredients.add(allIngredient.data.get(1).get_id());
        ingredients.add(allIngredient.data.get(2).get_id());

        user = User.getRandomUser();
        userClient = new UserClient();
        userClient.createUser(user);
        Response responseForToken = userClient.loginUser(user);
        accessToken = responseForToken.then().extract().path("accessToken");
        oder = new Order(ingredients);
        oderClient = new OrderClient();
        oderClient.createOrderByAuthorizedUser(accessToken, oder);
    }
    // Тест 1: получение заказов для пользователя с авторизацией
    @Test
    @DisplayName("Getting order for authorized user")
    @Description("Positive test, a successful server response is 200, the response body contains success: true")
    public void getOderForUserWithAuthorizationTest() {
        response = oderClient.getOrdersForAuthorizedUser(accessToken);
        response.then().assertThat().body("success", equalTo(true)).and().statusCode(200);
    }
    // Тест 2: получение заказов для пользователя без авторизации
    @Test
    @DisplayName("Getting order for unauthorized user")
    @Description("Negative test, the system responds with a 401 code")
    public void getOderForUserWithoutAuthorizationTest() {

        response = oderClient.getOrdersForUnauthorizedUser();
        response.then().assertThat().body("success", equalTo(false)).and().statusCode(401);
    }

    @After
    public void deleteUser(){
        userClient.deleteUser();
    }
}
