package de.hsos.swa.project.fieldbet;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import de.hsos.swa.project.fieldbet.matchmanagement.control.TeamCreationDTO;
import de.hsos.swa.project.fieldbet.matchmanagement.control.TeamUpdateDTO;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@ApplicationScoped
public class TeamResourceTest {
    // private static final Logger LOG = Logger.getLogger(TeamResourceTest.class);

    private String teamsUrl = "/api/v1/teams";

    @Inject
    TestManager testManager;

    @Test
    public void testGetEndpoint() {
        testManager.testGetRequest(teamsUrl, "", 401);
        testManager.testGetRequest(teamsUrl, testManager.getUserToken(), 403);
        testManager.testGetRequest(teamsUrl, testManager.getAdminToken(), 200);

    }

    @Test
    public void testGetSingleTeamEndpoint() {
        testManager.testGetRequest(teamsUrl + "/" + 1001, "", 401);
        testManager.testGetRequest(teamsUrl + "/" + 1001, testManager.getUserToken(), 403);
        testManager.testGetRequest(teamsUrl + "/" + 1001, testManager.getAdminToken(), 200);

    }

    @Test
    public void testPostEndpoint() {
        TeamCreationDTO creationDTO = new TeamCreationDTO("FC Test", "FCT");
        testManager.testPostRequest(teamsUrl, "", 401, creationDTO);
        testManager.testPostRequest(teamsUrl, testManager.getUserToken(), 403, creationDTO);
        testManager.testPostRequest(teamsUrl, testManager.getAdminToken(), 200, creationDTO);
    }

    @Test
    public void testPatchEndpoint() {
        TeamUpdateDTO updateDTO = new TeamUpdateDTO("Update", "UP1");

        testManager.testPatchRequest(teamsUrl + "/" + 1001, "", 401, updateDTO);
        testManager.testPatchRequest(teamsUrl + "/" + 1001, testManager.getUserToken(), 403, updateDTO);
        testManager.testPatchRequest(teamsUrl + "/" + 1001, testManager.getAdminToken(), 200, updateDTO);
    }
}
