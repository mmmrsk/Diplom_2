package ru.yandex.praktikum.stellarburgers;

import io.qameta.allure.Description;
import ru.yandex.praktikum.stellarburgers.clients.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import ru.yandex.praktikum.stellarburgers.models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UserEditTest {
    public UserClient userClient;
    public User user;
    private String accessToken;

    @Before
    public void setup() {
        userClient = new UserClient();
        user = User.getRandomUser();
        Response response = userClient.createUser(user);
        accessToken = response.then().extract().path("accessToken");
    }
    // Тест 1: Изменение email пользователя с авторизацией
    @Test
    @DisplayName("Update Email for authorized user")
    @Description("Positive test, a successful server response is 200, the response body contains success: true")
    public void updateEmailForAuthorizedUserTest() {
        userClient.getUserData(accessToken);
        user.setEmail(User.getRandomEmail());
        Response response = userClient.editUserWithToken(accessToken, new User(user.getEmail(), user.getName()));
        response.then().assertThat().body("success", equalTo(true)).and().statusCode(200);
    }
    // Тест 2: Изменение имени пользователя с авторизацией
    @Test
    @DisplayName("Update user name for authorized user")
    @Description("Positive test, a successful server response is 200, the response body contains success: true")
    public void updateNameForAuthorizedUserTest() {
        userClient.getUserData(accessToken);
        user.setName(User.getRandomName());
        Response response = userClient.editUserWithToken(accessToken, new User(user.getEmail(), user.getName()));
        response.then().assertThat().body("success", equalTo(true)).and().statusCode(200);
    }
    // Тест 3: Изменение email пользователя без авторизации
    @Test
    @DisplayName("Update Email for unauthorized user")
    @Description("Negative test, the system responds with a 401 code")
    public void updateEmailForUnauthorizedUserTest() {
        userClient.getUserData(accessToken);
        user.setEmail(User.getRandomEmail());
        Response response = userClient.editUserWithoutToken(new User(user.getEmail(), user.getName()));
        response.then().assertThat().body("success", equalTo(false)).and().statusCode(401);
    }
    // Тест 4: Изменение имени пользователя без авторизации
    @Test
    @DisplayName("Update user name for unauthorized user")
    @Description("Negative test, the system responds with a 401 code")
    public void updateNameForUnauthorizedUserTest() {
        userClient.getUserData(accessToken);
        user.setName(User.getRandomName());
        Response response = userClient.editUserWithoutToken(new User(user.getEmail(), user.getName()));
        response.then().assertThat().body("success", equalTo(false)).and().statusCode(401);
    }

    @After
    public void deleteUser() {
        userClient.deleteUser();
    }
}