package ru.yandex.praktikum.stellarburgers;

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


public class CreateOrderTest {
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
        responseForToken.then().assertThat().body("success", equalTo(true)).and().statusCode(200);
        accessToken = responseForToken.then().extract().path("accessToken");

    }

    @Test
    @DisplayName("Create order by unauthorized user")
    public void createOderWithoutAuthorizationTest() {
        oderClient = new OrderClient();
        oder = new Order(ingredients);
        response = oderClient.createOrderByUnauthorizedUser(oder);
        response.then().assertThat().statusCode(200).and().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Create order by authorized user")
    public void createOderWithAuthorizationTest() {
        oder = new Order(ingredients);
        oderClient = new OrderClient();
        response = oderClient.createOrderByAuthorizedUser(accessToken, oder);
        response.then().assertThat().body("success", equalTo(true)).and().statusCode(200);

    }

    @Test
    @DisplayName("Create order with wrong hash ingredients")
    public void createOderWithIncorrectIngredient() {

        ingredients.add("Test");
        oder = new Order(ingredients);
        oderClient = new OrderClient();
        response = oderClient.createOrderByAuthorizedUser(accessToken, oder);
        response.then().assertThat().statusCode(500);
    }

    @Test
    @DisplayName("Create order without ingredients")
    public void createOderWithNullIngredient() {

        ingredients.clear();
        oder = new Order(ingredients);
        oderClient = new OrderClient();
        response = oderClient.createOrderByAuthorizedUser(accessToken, oder);
        response.then().assertThat().statusCode(400);
    }

    @After
    public void deleteUser(){
        userClient.deleteUser();
    }

}
