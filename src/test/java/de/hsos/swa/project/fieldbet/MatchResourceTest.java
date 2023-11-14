package de.hsos.swa.project.fieldbet;

import java.time.LocalDateTime;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.hsos.swa.project.fieldbet.matchmanagement.control.MatchCreationDTO;
import de.hsos.swa.project.fieldbet.matchmanagement.control.MatchUpdateDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;

@QuarkusTest
@ApplicationScoped
@TestMethodOrder(OrderAnnotation.class)
public class MatchResourceTest {
    private String matchesUrl = "/api/v1/matches";

    private String createdMatchId;

    @Inject
    TestManager testManager;

    @Test
    public void testGetAllEndpoint() {
        testManager.testGetRequest(matchesUrl, "", 200);
    }

    @Test
    public void testGetSingleEndpoint() {
        testManager.testGetRequest(matchesUrl + "/2001", "", 200);
    }

    @Test
    @Order(1)
    public void testPostEndpoint() {
        MatchCreationDTO creationDTO = new MatchCreationDTO(1012L, 1002L, LocalDateTime.parse("2030-02-12T10:15:30"));
        testManager.testPostRequest(matchesUrl, "", 401, creationDTO);
        testManager.testPostRequest(matchesUrl, testManager.getUserToken(), 403, creationDTO);
        Response res = testManager.testPostRequest(matchesUrl, testManager.getAdminToken(), 200, creationDTO);
        createdMatchId = res.jsonPath().getString("id");
    }

    @Test
    @Order(2)
    public void testPatchEndpoint() {
        MatchUpdateDTO updateDTO = new MatchUpdateDTO(1, 0, LocalDateTime.parse("2030-03-12T10:15:30"), true);
        testManager.testPatchRequest(matchesUrl + "/" + createdMatchId, "", 401, updateDTO);
        testManager.testPatchRequest(matchesUrl + "/" + createdMatchId, testManager.getUserToken(), 403, updateDTO);
        testManager.testPatchRequest(matchesUrl + "/" + createdMatchId, testManager.getAdminToken(), 200, updateDTO);
    }

}
