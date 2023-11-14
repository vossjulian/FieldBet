package de.hsos.swa.project.fieldbet;

import static io.restassured.RestAssured.given;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.hsos.swa.project.fieldbet.usermanagement.control.UserCreationDTO;
import de.hsos.swa.project.fieldbet.usermanagement.control.UserFacade;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

@Singleton
public class TestManager {

    // private static final Logger LOG = Logger.getLogger(TestManager.class);

    @ConfigProperty(name = "quarkus.oidc.client-id")
    private String clientId;
    @ConfigProperty(name = "quarkus.oidc.credentials.secret")
    private String clientSecret;

    private UserFacade userFacade;

    private final String tokenUrl = "http://localhost:8180/realms/quarkus/protocol/openid-connect/token";

    @Inject
    public TestManager(UserFacade userFacade) {
        this.userFacade = userFacade;

        // Test users
        this.userFacade.createUser(new UserCreationDTO("user", "user", "Test", "User"));
        this.userFacade.createAdmin(new UserCreationDTO("admin", "admin", "Test", "Admin"));
    }

    public Response testGetRequest(String url, String token, Integer expectedStatus) {
        ValidatableResponse res = given()
                .header("Authorization", "Bearer " + token)
                .when().get(url)
                .then()
                .statusCode(expectedStatus);
        return res.extract().response();
    }

    public Response testPostRequest(String url, String token, Integer expectedStatus, Object data) {
        ValidatableResponse res = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when().body(data).post(url)
                .then().statusCode(expectedStatus);
        return res.extract().response();
    }

    public Response testPatchRequest(String url, String token, Integer expectedStatus, Object data) {
        ValidatableResponse res = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when().body(data).patch(url)
                .then()
                .statusCode(expectedStatus);
        return res.extract().response();
    }

    public Response testDeleteRequest(String url, String token, Integer expectedStatus) {
        ValidatableResponse res = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when().delete(url)
                .then()
                .statusCode(expectedStatus);
        return res.extract().response();
    }

    public String getUserToken() {
        return getToken("user", "user", "password", "openid");
    }

    public String getAdminToken() {
        return getToken("admin", "admin", "password", "openid");
    }

    public String getAliceToken() {
        return getToken("alice", "alice", "password", "openid");
    }

    private String getToken(String username, String password, String grantType, String scope) {
        String idToken = given()
                .auth()
                .preemptive()
                .basic(clientId, clientSecret)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(String.format("username=%s&password=%s&grant_type=%s&scope=%s", username, password, grantType,
                        scope))
                .post(tokenUrl)
                .then().extract().response().jsonPath().getString("id_token");

        return idToken;
    }
}
