package de.hsos.swa.project.fieldbet;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.junit.jupiter.api.Test;

import de.hsos.swa.project.fieldbet.usermanagement.control.ProfileCreationDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;

@QuarkusTest
@ApplicationScoped
public class ProfileResourceTest {

    // private static final Logger LOG =
    // Logger.getLogger(ProfileResourceTest.class);

    private String profileUrl = "/api/v1/profiles";

    private String createdProfileId;

    @Inject
    TestManager testManager;

    @Inject
    JsonWebToken jwt;

    @Test
    public void testGetEndpoint() {
        testManager.testGetRequest(profileUrl, "", 401);
        testManager.testGetRequest(profileUrl, testManager.getUserToken(), 200);
    }

    @Test
    public void testPostEndpoint() {
        ProfileCreationDTO creationDTO = new ProfileCreationDTO("test", "test");

        testManager.testPostRequest(profileUrl, "", 401, creationDTO);
        Response res = testManager.testPostRequest(profileUrl, testManager.getAliceToken(), 200, creationDTO);

        createdProfileId = res.jsonPath().getString("id");
    }

    @Test
    public void testGetSingleProfileEndpoint() {
        testManager.testGetRequest(profileUrl + "/" + createdProfileId, "", 401);
        testManager.testGetRequest(profileUrl + "/" + createdProfileId, testManager.getUserToken(), 200);
    }

    @Test
    public void testGetMeEndpoint() {
        testManager.testGetRequest(profileUrl + "/me", "", 401);
        testManager.testGetRequest(profileUrl + "/me", testManager.getUserToken(), 200);
    }
}
