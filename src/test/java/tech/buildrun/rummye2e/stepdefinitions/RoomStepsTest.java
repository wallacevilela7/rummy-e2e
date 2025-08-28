package tech.buildrun.rummye2e.stepdefinitions;


import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import tech.buildrun.rummye2e.config.RestConfig;
import tech.buildrun.rummye2e.config.ScenarioContext;
import tech.buildrun.rummye2e.dto.RoomDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RoomStepsTest {

    private ScenarioContext scenarioContext;
    private final RestConfig restConfig;

    public RoomStepsTest(ScenarioContext scenarioContext,
                         RestConfig restConfig) {
        this.scenarioContext = scenarioContext;
        this.restConfig = restConfig;
    }


    @When("i list all the rooms")
    public void iListAllTheRooms() {
        Response response = restConfig.givenBackend()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/rooms");

        scenarioContext.put("response", response);
    }

    @Then("i detect that the room {string} exists")
    public void iDetectThatTheRoomExists(String name) {
        var response = scenarioContext.get("response", Response.class);

        var rooms = response.then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<List<RoomDto>>() {
                });

        assertTrue(rooms.stream().anyMatch(room -> room.name().equalsIgnoreCase(name)));
    }
}