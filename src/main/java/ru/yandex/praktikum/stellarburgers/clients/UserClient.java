package ru.yandex.praktikum.stellarburgers.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.stellarburgers.models.User;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserClient extends Client {

    private static final String AUTH_URL = "/api/auth";
    private static final String CREATE_USER_URL = AUTH_URL + "/register";
    private static final String LOGIN_USER_URL = AUTH_URL + "/login";
    private static final String USER_URL = AUTH_URL + "/user";
    private static final String LOGOUT_USER_URL = AUTH_URL + "/logout";
    public String accessToken = "";

    @Step("Create user")
    public Response createUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(CREATE_USER_URL);
    }

    @Step("Login user")
    public Response loginUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(LOGIN_USER_URL);
    }

    @Step("Get user data")
    public ValidatableResponse getUserData(String accessToken) {

        return given()
                .header("Authorization", accessToken)
                .spec(getBaseSpec())
                .when()
                .get(USER_URL)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Step("Editing user with token")
    public Response editUserWithToken(String accessToken, User user) {

        return given()
                .header("Authorization", accessToken)
                .spec(getBaseSpec())
                .when()
                .body(user)
                .patch(USER_URL);
    }

    @Step("Editing user without token")
    public Response editUserWithoutToken(User user) {

        return given()
                .spec(getBaseSpec())
                .when()
                .body(user)
                .patch(USER_URL);
    }

    @Step("Delete user")
    public Response deleteUser() {
        if (this.accessToken.equals("")) {
            return given()
                    .spec(getBaseSpec())
                    .auth().oauth2(accessToken)
                    .delete(USER_URL);
        }
        return null;
    }
}