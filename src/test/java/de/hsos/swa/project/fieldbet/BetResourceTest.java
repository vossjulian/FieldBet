package de.hsos.swa.project.fieldbet;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.hsos.swa.project.fieldbet.betmanagement.control.BetCreationDTO;
import de.hsos.swa.project.fieldbet.betmanagement.control.BetUpdateDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;

@QuarkusTest
@ApplicationScoped
@TestMethodOrder(OrderAnnotation.class)
public class BetResourceTest {
    private String betsUrl = "/api/v1/bets";

    private String createdBetId;

    @Inject
    TestManager testManager;

    @Test
    @Order(1)
    public void testPostEndpoint() {
        BetCreationDTO creationDTO = new BetCreationDTO(2005L, 2, 1);
        testManager.testPostRequest(betsUrl, "", 401, creationDTO);
        Response res = testManager.testPostRequest(betsUrl, testManager.getUserToken(), 200, creationDTO);
        createdBetId = res.jsonPath().getString("id");
    }

    @Test
    @Order(2)
    public void testPatchEndpoint() {
        BetUpdateDTO updateDTO = new BetUpdateDTO(5, 3);
        testManager.testPatchRequest(betsUrl + "/" + createdBetId, "", 401, updateDTO);
        testManager.testPatchRequest(betsUrl + "/" + createdBetId, testManager.getUserToken(), 200, updateDTO);
    }

    @Test
    @Order(3)
    public void testGetOwnEndpoint() {
        testManager.testGetRequest(betsUrl + "/me", "", 401);
        testManager.testGetRequest(betsUrl + "/me", testManager.getUserToken(), 200);
    }

    @Test
    @Order(4)
    public void testGetAllEndpoint() {
        testManager.testGetRequest(betsUrl, "", 401);
        testManager.testGetRequest(betsUrl, testManager.getUserToken(), 403);
        testManager.testGetRequest(betsUrl, testManager.getAdminToken(), 200);
    }

    @Test
    @Order(5)
    public void testDeleteEndpoint() {
        testManager.testDeleteRequest(betsUrl + "/" + createdBetId, "", 401);
        testManager.testDeleteRequest(betsUrl + "/" + createdBetId, testManager.getUserToken(), 200);

    }

}
